package com.example.qibla

import kotlin.math.*

object QiblaCalculator {
    const val MAKKAH_LATITUDE = 21.422487
    const val MAKKAH_LONGITUDE = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Calculates the true bearing to the Kaaba from given latitude and longitude.
     * Output is in degrees from True North (0..360).
     */
    fun calculateQiblaBearing(lat: Double, lon: Double): Double {
        val phi1 = Math.toRadians(lat)
        val phi2 = Math.toRadians(MAKKAH_LATITUDE)
        val deltaLambda = Math.toRadians(MAKKAH_LONGITUDE - lon)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)

        var bearing = Math.toDegrees(atan2(y, x))
        bearing = (bearing + 360.0) % 360.0
        return bearing
    }

    /**
     * Calculates distance to Kaaba in kilometers.
     */
    fun calculateDistanceToMakkahKm(lat: Double, lon: Double): Double {
        val phi1 = Math.toRadians(lat)
        val phi2 = Math.toRadians(MAKKAH_LATITUDE)
        val deltaPhi = Math.toRadians(MAKKAH_LATITUDE - lat)
        val deltaLambda = Math.toRadians(MAKKAH_LONGITUDE - lon)

        val a = sin(deltaPhi / 2.0).pow(2.0) +
                cos(phi1) * cos(phi2) * sin(deltaLambda / 2.0).pow(2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        return EARTH_RADIUS_KM * c
    }
}
