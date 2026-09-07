package com.example.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

data class CompassState(
    val azimuth: Float = 0f, // 0..360
    val accuracy: Int = SensorManager.SENSOR_STATUS_ACCURACY_HIGH,
    val isSensorAvailable: Boolean = true
)

class CompassSensorManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val _compassState = MutableStateFlow(
        CompassState(isSensorAvailable = rotationSensor != null || (accelerometer != null && magnetometer != null))
    )
    val compassState: StateFlow<CompassState> = _compassState.asStateFlow()

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var lastAzimuth = 0f

    fun startListening() {
        if (sensorManager == null) return

        if (rotationSensor != null) {
            sensorManager.registerListener(
                this,
                rotationSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            accelerometer?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            magnetometer?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                val azimuthInDeg = (Math.toDegrees(orientationAngles[0].toDouble()) + 360.0) % 360.0
                updateSmoothedAzimuth(azimuthInDeg.toFloat(), event.accuracy)
            }
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, gravity, 0, 3)
                computeOrientationFromAccelMag(event.accuracy)
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                computeOrientationFromAccelMag(event.accuracy)
            }
        }
    }

    private fun computeOrientationFromAccelMag(accuracy: Int) {
        val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val azimuthInDeg = (Math.toDegrees(orientationAngles[0].toDouble()) + 360.0) % 360.0
            updateSmoothedAzimuth(azimuthInDeg.toFloat(), accuracy)
        }
    }

    private fun updateSmoothedAzimuth(targetAzimuth: Float, accuracy: Int) {
        // Smooth rotation to avoid 359 -> 0 jumping artifacts
        var diff = targetAzimuth - lastAzimuth
        while (diff < -180f) diff += 360f
        while (diff > 180f) diff -= 360f

        val smoothed = (lastAzimuth + diff * 0.25f + 360f) % 360f
        lastAzimuth = smoothed

        _compassState.value = CompassState(
            azimuth = smoothed,
            accuracy = accuracy,
            isSensorAvailable = true
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        _compassState.value = _compassState.value.copy(accuracy = accuracy)
    }
}
