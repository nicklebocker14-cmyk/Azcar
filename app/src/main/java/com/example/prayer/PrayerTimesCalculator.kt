package com.example.prayer

import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.*

data class PrayerTime(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val timeMillis: Long,
    val formattedTime12: String,
    val formattedTime24: String,
    val isNext: Boolean = false
)

data class PrayerTimesResult(
    val fajr: PrayerTime,
    val sunrise: PrayerTime,
    val dhuhr: PrayerTime,
    val asr: PrayerTime,
    val maghrib: PrayerTime,
    val isha: PrayerTime,
    val nextPrayer: PrayerTime,
    val timeRemainingMillis: Long,
    val calculationMethodName: String,
    val juristicName: String,
    val dateText: String,
    val hijriDateText: String
)

enum class CalculationMethod(
    val id: String,
    val displayNameAr: String,
    val displayNameEn: String,
    val fajrAngle: Double,
    val ishaAngle: Double?, // null if fixed minutes after maghrib
    val ishaMinutesAfterMaghrib: Int?
) {
    UMM_AL_QURA("umm_al_qura", "أم القرى (مكة المكرمة)", "Umm Al-Qura (Makkah)", 18.5, null, 90),
    MUSLIM_WORLD_LEAGUE("mwl", "رابطة العالم الإسلامي", "Muslim World League", 18.0, 17.0, null),
    EGYPTIAN("egyptian", "الهيئة العامة المصرية للمساحة", "Egyptian General Authority", 19.5, 17.5, null),
    ISNA("isna", "الجمعية الإسلامية لأمريكا الشمالية (ISNA)", "Islamic Society of North America", 15.0, 15.0, null),
    KARACHI("karachi", "جامعة العلوم الإسلامية بكراتشي", "University of Islamic Sciences, Karachi", 18.0, 18.0, null);

    companion object {
        fun fromId(id: String): CalculationMethod {
            return entries.find { it.id == id } ?: UMM_AL_QURA
        }
    }
}

enum class AsrJuristic(
    val id: String,
    val displayNameAr: String,
    val displayNameEn: String,
    val shadowFactor: Double
) {
    STANDARD("standard", "الجمهور (الشافعي، المالكي، الحنبلي)", "Standard (Shafi'i, Maliki, Hanbali)", 1.0),
    HANAFI("hanafi", "الحنفي", "Hanafi", 2.0);

    companion object {
        fun fromId(id: String): AsrJuristic {
            return entries.find { it.id == id } ?: STANDARD
        }
    }
}

object PrayerTimesCalculator {

    fun calculatePrayerTimes(
        latitude: Double,
        longitude: Double,
        calendar: Calendar = Calendar.getInstance(),
        method: CalculationMethod = CalculationMethod.UMM_AL_QURA,
        asrJuristic: AsrJuristic = AsrJuristic.STANDARD,
        adjustmentsMinutes: Map<String, Int> = emptyMap()
    ): PrayerTimesResult {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val timeZoneOffset = calendar.timeZone.getOffset(calendar.timeInMillis) / 3600000.0

        val julianDate = julianDate(year, month, day)
        val d = julianDate - 2451545.0

        // Sun's mean anomaly & ecliptic longitude
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(deg2rad(g)) + 0.020 * sin(deg2rad(2 * g)))

        // Obliquity of ecliptic
        val e = 23.439 - 0.00000036 * d

        // Declination (delta) & Right Ascension (alpha)
        val delta = rad2deg(asin(sin(deg2rad(e)) * sin(deg2rad(l))))
        var alpha = rad2deg(atan2(cos(deg2rad(e)) * sin(deg2rad(l)), cos(deg2rad(l)))) / 15.0
        alpha = fixHour(alpha)

        // Equation of Time in minutes
        val equationOfTime = (q / 15.0) - alpha

        // Solar Noon (Dhuhr) in hours
        val noon = fixHour(12.0 + timeZoneOffset - (longitude / 15.0) - equationOfTime)
        val dhuhrHours = noon

        // Sunrise and Sunset (Sun altitude = -0.8333 degrees for refraction)
        val sunAlt = -0.8333
        val sunriseHourAngle = hourAngle(sunAlt, latitude, delta)
        val sunriseHours = noon - sunriseHourAngle
        val sunsetHours = noon + sunriseHourAngle

        // Fajr
        val fajrHourAngle = hourAngle(-method.fajrAngle, latitude, delta)
        val fajrHours = noon - fajrHourAngle

        // Asr (shadow factor = 1 for Standard, 2 for Hanafi)
        val asrAlt = -rad2deg(atan(1.0 / (asrJuristic.shadowFactor + tan(deg2rad(abs(latitude - delta))))))
        val asrHourAngle = hourAngle(-asrAlt, latitude, delta)
        val asrHours = noon + asrHourAngle

        // Maghrib = Sunset
        val maghribHours = sunsetHours

        // Isha
        val ishaHours = if (method.ishaMinutesAfterMaghrib != null) {
            maghribHours + (method.ishaMinutesAfterMaghrib / 60.0)
        } else {
            val ishaHourAngle = hourAngle(-(method.ishaAngle ?: 17.0), latitude, delta)
            noon + ishaHourAngle
        }

        // Apply manual adjustments (minutes converted to fraction of hour)
        val adjFajr = adjustmentsMinutes["fajr"] ?: 0
        val adjDhuhr = adjustmentsMinutes["dhuhr"] ?: 0
        val adjAsr = adjustmentsMinutes["asr"] ?: 0
        val adjMaghrib = adjustmentsMinutes["maghrib"] ?: 0
        val adjIsha = adjustmentsMinutes["isha"] ?: 0

        val fajrMillis = hoursToEpochMillis(calendar, fajrHours + adjFajr / 60.0)
        val sunriseMillis = hoursToEpochMillis(calendar, sunriseHours)
        val dhuhrMillis = hoursToEpochMillis(calendar, dhuhrHours + adjDhuhr / 60.0)
        val asrMillis = hoursToEpochMillis(calendar, asrHours + adjAsr / 60.0)
        val maghribMillis = hoursToEpochMillis(calendar, maghribHours + adjMaghrib / 60.0)
        val ishaMillis = hoursToEpochMillis(calendar, ishaHours + adjIsha / 60.0)

        val now = System.currentTimeMillis()

        // Determine next prayer
        val prayerList = listOf(
            PrayerTime("fajr", "الفجر", "Fajr", fajrMillis, formatTime12(fajrMillis), formatTime24(fajrMillis)),
            PrayerTime("sunrise", "الشروق", "Sunrise", sunriseMillis, formatTime12(sunriseMillis), formatTime24(sunriseMillis)),
            PrayerTime("dhuhr", "الظهر", "Dhuhr", dhuhrMillis, formatTime12(dhuhrMillis), formatTime24(dhuhrMillis)),
            PrayerTime("asr", "العصر", "Asr", asrMillis, formatTime12(asrMillis), formatTime24(asrMillis)),
            PrayerTime("maghrib", "المغرب", "Maghrib", maghribMillis, formatTime12(maghribMillis), formatTime24(maghribMillis)),
            PrayerTime("isha", "العشاء", "Isha", ishaMillis, formatTime12(ishaMillis), formatTime24(ishaMillis))
        )

        // Find next prayer (excluding Sunrise if prayer passed)
        val futurePrayers = prayerList.filter { it.id != "sunrise" && it.timeMillis > now }
        val (next, remainingMillis) = if (futurePrayers.isNotEmpty()) {
            val nextP = futurePrayers.first()
            nextP to (nextP.timeMillis - now)
        } else {
            // Next is tomorrow's Fajr
            val tomorrowFajrMillis = fajrMillis + 24 * 3600 * 1000
            val nextP = prayerList.first().copy(timeMillis = tomorrowFajrMillis)
            nextP to (tomorrowFajrMillis - now)
        }

        val updatedPrayerList = prayerList.map {
            if (it.id == next.id) it.copy(isNext = true) else it
        }

        val gregorianDateText = String.format(
            Locale.getDefault(),
            "%d/%02d/%02d",
            year, month, day
        )

        val hijriEstimate = estimateHijriDate(year, month, day)

        return PrayerTimesResult(
            fajr = updatedPrayerList[0],
            sunrise = updatedPrayerList[1],
            dhuhr = updatedPrayerList[2],
            asr = updatedPrayerList[3],
            maghrib = updatedPrayerList[4],
            isha = updatedPrayerList[5],
            nextPrayer = next,
            timeRemainingMillis = remainingMillis,
            calculationMethodName = method.displayNameAr,
            juristicName = asrJuristic.displayNameAr,
            dateText = gregorianDateText,
            hijriDateText = hijriEstimate
        )
    }

    private fun hourAngle(altitude: Double, latitude: Double, delta: Double): Double {
        val cosHA = (sin(deg2rad(altitude)) - sin(deg2rad(latitude)) * sin(deg2rad(delta))) /
                (cos(deg2rad(latitude)) * cos(deg2rad(delta)))
        val clampedCos = cosHA.coerceIn(-1.0, 1.0)
        return rad2deg(acos(clampedCos)) / 15.0
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun hoursToEpochMillis(baseCal: Calendar, hoursDecimal: Double): Long {
        var hrs = fixHour(hoursDecimal)
        val h = hrs.toInt()
        val remainderMins = (hrs - h) * 60.0
        val m = remainderMins.toInt()
        val s = ((remainderMins - m) * 60.0).toInt()

        val cal = baseCal.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        cal.set(Calendar.SECOND, s)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun formatTime12(millis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        val hour24 = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val isPm = hour24 >= 12
        val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12
        val suffixAr = if (isPm) "م" else "ص"
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minute, suffixAr)
    }

    private fun formatTime24(millis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    private fun estimateHijriDate(year: Int, month: Int, day: Int): String {
        // High-precision arithmetic Kuwati/Umm-al-Qura Hijri algorithm approximation
        val jd = julianDate(year, month, day)
        val l = (jd - 1948440 + 10632).toLong()
        val n = ((l - 1) / 10631).toInt()
        val lPrime = l - 10631 * n + 354
        val j = (((10985 - lPrime) / 5316).toInt()) * (((50 * lPrime) / 17719).toInt()) +
                ((lPrime / 5670).toInt()) * (((43 * lPrime) / 15238).toInt())
        val l2 = lPrime - (((30 - j) / 15).toInt()) * (((17719 * j) / 50).toInt()) -
                ((j / 16).toInt()) * (((15238 * j) / 43).toInt()) + 29
        val m = ((24 * l2) / 709).toInt()
        val d = (l2 - ((709 * m) / 24).toInt()).toInt()
        val y = (30 * n + j - 30).toInt()

        val hijriMonths = listOf(
            "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
            "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
            "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
        )
        val monthName = if (m in 1..12) hijriMonths[m - 1] else "رمضان"
        return "$d $monthName $y هـ"
    }

    private fun fixAngle(angle: Double): Double {
        var b = angle - 360.0 * floor(angle / 360.0)
        if (b < 0) b += 360.0
        return b
    }

    private fun fixHour(hour: Double): Double {
        var b = hour - 24.0 * floor(hour / 24.0)
        if (b < 0) b += 24.0
        return b
    }

    private fun deg2rad(d: Double): Double = d * (PI / 180.0)
    private fun rad2deg(r: Double): Double = r * (180.0 / PI)
}
