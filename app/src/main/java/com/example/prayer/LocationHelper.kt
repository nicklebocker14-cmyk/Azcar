package com.example.prayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class CityPreset(
    val nameAr: String,
    val nameEn: String,
    val countryAr: String,
    val countryEn: String,
    val latitude: Double,
    val longitude: Double
)

object LocationPresets {
    val presets = listOf(
        CityPreset("مكة المكرمة", "Makkah", "المملكة العربية السعودية", "Saudi Arabia", 21.4225, 39.8262),
        CityPreset("المدينة المنورة", "Madinah", "المملكة العربية السعودية", "Saudi Arabia", 24.4672, 39.6111),
        CityPreset("الرياض", "Riyadh", "المملكة العربية السعودية", "Saudi Arabia", 24.7136, 46.6753),
        CityPreset("جدة", "Jeddah", "المملكة العربية السعودية", "Saudi Arabia", 21.5433, 39.1728),
        CityPreset("القاهرة", "Cairo", "مصر", "Egypt", 30.0444, 31.2357),
        CityPreset("الإسكندرية", "Alexandria", "مصر", "Egypt", 31.2001, 29.9187),
        CityPreset("دبي", "Dubai", "الإمارات العربية المتحدة", "UAE", 25.2048, 55.2708),
        CityPreset("أبوظبي", "Abu Dhabi", "الإمارات العربية المتحدة", "UAE", 24.4539, 54.3773),
        CityPreset("عَمّان", "Amman", "الأردن", "Jordan", 31.9539, 35.9106),
        CityPreset("القدس الشريف", "Jerusalem", "فلسطين", "Palestine", 31.7683, 35.2137),
        CityPreset("دمشق", "Damascus", "سوريا", "Syria", 33.5138, 36.2765),
        CityPreset("بيروت", "Beirut", "لبنان", "Lebanon", 33.8938, 35.5018),
        CityPreset("بغداد", "Baghdad", "العراق", "Iraq", 33.3152, 44.3661),
        CityPreset("الكويت", "Kuwait City", "الكويت", "Kuwait", 29.3759, 47.9774),
        CityPreset("الدوحة", "Doha", "قطر", "Qatar", 25.2854, 51.5310),
        CityPreset("مسقط", "Muscat", "عُمان", "Oman", 23.5880, 58.3829),
        CityPreset("المنامة", "Manama", "البحرين", "Bahrain", 26.2285, 50.5860),
        CityPreset("الرباط", "Rabat", "المغرب", "Morocco", 34.0209, -6.8416),
        CityPreset("الدار البيضاء", "Casablanca", "المغرب", "Morocco", 33.5731, -7.5898),
        CityPreset("تونس", "Tunis", "تونس", "Tunisia", 36.8065, 10.1815),
        CityPreset("الجزائر", "Algiers", "الجزائر", "Algeria", 36.7538, 3.0588),
        CityPreset("إسطنبول", "Istanbul", "تركيا", "Turkey", 41.0082, 28.9784),
        CityPreset("لندن", "London", "المملكة المتحدة", "United Kingdom", 51.5074, -0.1278),
        CityPreset("جاكرتا", "Jakarta", "إندونيسيا", "Indonesia", -6.2088, 106.8456),
        CityPreset("كوالالمبور", "Kuala Lumpur", "ماليزيا", "Malaysia", 3.1390, 101.6869)
    )
}

class LocationHelper(private val context: Context) {

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentCoordinates(): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) return@withContext null

        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            var resultLoc: Location? = null

            // Try fast current location first
            val task = fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            try {
                // Blocking await with timeout
                val loc = com.google.android.gms.tasks.Tasks.await(task, 4, java.util.concurrent.TimeUnit.SECONDS)
                if (loc != null) resultLoc = loc
            } catch (e: Exception) {
                // fallback to last location
            }

            if (resultLoc == null) {
                val lastLocTask = fusedClient.lastLocation
                val loc = com.google.android.gms.tasks.Tasks.await(lastLocTask, 2, java.util.concurrent.TimeUnit.SECONDS)
                if (loc != null) resultLoc = loc
            }

            // If still null, try system LocationManager
            if (resultLoc == null) {
                val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                val gpsLoc = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val netLoc = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                resultLoc = gpsLoc ?: netLoc
            }

            resultLoc?.let { Pair(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPlaceName(lat: Double, lon: Double, isArabic: Boolean): Pair<String, String> =
        withContext(Dispatchers.IO) {
            try {
                if (Geocoder.isPresent()) {
                    val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
                    val geocoder = Geocoder(context, locale)
                    @Suppress("DEPRECATION")
                    val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        val city = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: ""
                        val country = addr.countryName ?: ""
                        if (city.isNotEmpty() || country.isNotEmpty()) {
                            return@withContext Pair(city.ifEmpty { "الموقع الحالي" }, country)
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore geocoding failure
            }

            // Find closest preset city
            val closest = LocationPresets.presets.minByOrNull {
                val dLat = it.latitude - lat
                val dLon = it.longitude - lon
                dLat * dLat + dLon * dLon
            }
            if (closest != null) {
                Pair(
                    if (isArabic) closest.nameAr else closest.nameEn,
                    if (isArabic) closest.countryAr else closest.countryEn
                )
            } else {
                Pair("مكة المكرمة", "المملكة العربية السعودية")
            }
        }
}
