package com.example

import com.example.data.model.BuiltInAdhkarData
import com.example.prayer.CalculationMethod
import com.example.prayer.PrayerTimesCalculator
import com.example.qibla.QiblaCalculator
import com.example.ui.components.ArabicTextNormalizer
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DhikrAppLogicTest {

    @Test
    fun testPrayerTimesCalculationChronology() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Riyadh"))
        calendar.set(2026, Calendar.SEPTEMBER, 6, 12, 0, 0)

        val result = PrayerTimesCalculator.calculatePrayerTimes(
            latitude = 21.4225, // Makkah
            longitude = 39.8262,
            calendar = calendar,
            method = CalculationMethod.UMM_AL_QURA
        )

        val fajr = result.fajr
        val sunrise = result.sunrise
        val dhuhr = result.dhuhr
        val asr = result.asr
        val maghrib = result.maghrib
        val isha = result.isha

        assertTrue("Fajr should precede Sunrise", fajr.timeMillis < sunrise.timeMillis)
        assertTrue("Sunrise should precede Dhuhr", sunrise.timeMillis < dhuhr.timeMillis)
        assertTrue("Dhuhr should precede Asr", dhuhr.timeMillis < asr.timeMillis)
        assertTrue("Asr should precede Maghrib", asr.timeMillis < maghrib.timeMillis)
        assertTrue("Maghrib should precede Isha", maghrib.timeMillis < isha.timeMillis)
    }

    @Test
    fun testQiblaBearingAndDistance() {
        // Riyadh coordinates: 24.7136° N, 46.6753° E
        val bearing = QiblaCalculator.calculateQiblaBearing(24.7136, 46.6753)
        val distance = QiblaCalculator.calculateDistanceToMakkahKm(24.7136, 46.6753)

        // From Riyadh, Qibla is South-West (~240° - 250°)
        assertTrue("Riyadh Qibla bearing should be around 245°", bearing in 235.0..255.0)
        // Distance to Makkah is ~850 km
        assertTrue("Riyadh to Makkah distance should be ~800-900 km", distance in 750.0..950.0)
    }

    @Test
    fun testArabicTextNormalization() {
        val diacritics = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ"
        val normalized = ArabicTextNormalizer.normalize(diacritics)
        assertEquals("سبحان الله وبحمده", normalized)

        val alefs = "أذكار الإِفطار معاً"
        val normalizedAlef = ArabicTextNormalizer.normalize(alefs)
        assertTrue(normalizedAlef.contains("اذكار"))
        assertTrue(normalizedAlef.contains("الافطار"))
    }

    @Test
    fun testBuiltInAdhkarCategories() {
        val categories = BuiltInAdhkarData.categories
        assertTrue("Should have at least 10 categories", categories.size >= 10)

        val allAdhkar = BuiltInAdhkarData.adhkarList
        assertTrue("Should contain a comprehensive set of adhkar", allAdhkar.size >= 30)

        // Verify each dhikr has required content
        allAdhkar.forEach { dhikr ->
            assertTrue("Dhikr ${dhikr.id} must have non-empty content", dhikr.content.isNotBlank())
            assertTrue("Dhikr ${dhikr.id} must have positive count", dhikr.count >= 1)
        }
    }
}
