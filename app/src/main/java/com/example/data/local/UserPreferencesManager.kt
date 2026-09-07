package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppUserPreferences(
    val themeMode: String = "system", // "system", "light", "dark"
    val accentColor: String = "emerald", // "emerald", "gold", "teal", "blue", "rose"
    val fontScale: String = "medium", // "small", "medium", "large"
    val cardStyle: String = "rounded", // "rounded", "simple", "compact"
    val language: String = "ar", // "ar", "en"
    val prayerMethod: String = "umm_al_qura", // "umm_al_qura", "mwl", "egyptian", "isna", "karachi"
    val asrJuristic: String = "standard", // "standard" (Shafi/Maliki/Hanbali), "hanafi"
    val manualFajrAdj: Int = 0,
    val manualDhuhrAdj: Int = 0,
    val manualAsrAdj: Int = 0,
    val manualMaghribAdj: Int = 0,
    val manualIshaAdj: Int = 0,
    val notifyFajr: Boolean = true,
    val notifyDhuhr: Boolean = true,
    val notifyAsr: Boolean = true,
    val notifyMaghrib: Boolean = true,
    val notifyIsha: Boolean = true,
    val notifyMorningAdhkar: Boolean = true,
    val notifyEveningAdhkar: Boolean = true,
    val notifyDailyDhikr: Boolean = true,
    val lastLatitude: Double = 21.4225, // Default Makkah if no GPS
    val lastLongitude: Double = 39.8262,
    val lastCityName: String = "مكة المكرمة",
    val lastCountryName: String = "المملكة العربية السعودية",
    val hasCustomLocation: Boolean = false,
    val tasbeehLastCount: Int = 0,
    val tasbeehTarget: Int = 33,
    val tasbeehPhrase: String = "سبحان الله",
    val tasbeehVibration: Boolean = true,
    val tasbeehSound: Boolean = false
)

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dhikr_user_prefs", Context.MODE_PRIVATE)

    private val _userPreferences = MutableStateFlow(loadPreferences())
    val userPreferences: StateFlow<AppUserPreferences> = _userPreferences.asStateFlow()

    private fun loadPreferences(): AppUserPreferences {
        return AppUserPreferences(
            themeMode = prefs.getString("theme_mode", "system") ?: "system",
            accentColor = prefs.getString("accent_color", "emerald") ?: "emerald",
            fontScale = prefs.getString("font_scale", "medium") ?: "medium",
            cardStyle = prefs.getString("card_style", "rounded") ?: "rounded",
            language = prefs.getString("language", "ar") ?: "ar",
            prayerMethod = prefs.getString("prayer_method", "umm_al_qura") ?: "umm_al_qura",
            asrJuristic = prefs.getString("asr_juristic", "standard") ?: "standard",
            manualFajrAdj = prefs.getInt("manual_fajr_adj", 0),
            manualDhuhrAdj = prefs.getInt("manual_dhuhr_adj", 0),
            manualAsrAdj = prefs.getInt("manual_asr_adj", 0),
            manualMaghribAdj = prefs.getInt("manual_maghrib_adj", 0),
            manualIshaAdj = prefs.getInt("manual_isha_adj", 0),
            notifyFajr = prefs.getBoolean("notify_fajr", true),
            notifyDhuhr = prefs.getBoolean("notify_dhuhr", true),
            notifyAsr = prefs.getBoolean("notify_asr", true),
            notifyMaghrib = prefs.getBoolean("notify_maghrib", true),
            notifyIsha = prefs.getBoolean("notify_isha", true),
            notifyMorningAdhkar = prefs.getBoolean("notify_morning_adhkar", true),
            notifyEveningAdhkar = prefs.getBoolean("notify_evening_adhkar", true),
            notifyDailyDhikr = prefs.getBoolean("notify_daily_dhikr", true),
            lastLatitude = java.lang.Double.longBitsToDouble(
                prefs.getLong("last_latitude", java.lang.Double.doubleToLongBits(21.4225))
            ),
            lastLongitude = java.lang.Double.longBitsToDouble(
                prefs.getLong("last_longitude", java.lang.Double.doubleToLongBits(39.8262))
            ),
            lastCityName = prefs.getString("last_city_name", "مكة المكرمة") ?: "مكة المكرمة",
            lastCountryName = prefs.getString("last_country_name", "المملكة العربية السعودية")
                ?: "المملكة العربية السعودية",
            hasCustomLocation = prefs.getBoolean("has_custom_location", false),
            tasbeehLastCount = prefs.getInt("tasbeeh_last_count", 0),
            tasbeehTarget = prefs.getInt("tasbeeh_target", 33),
            tasbeehPhrase = prefs.getString("tasbeeh_phrase", "سبحان الله") ?: "سبحان الله",
            tasbeehVibration = prefs.getBoolean("tasbeeh_vibration", true),
            tasbeehSound = prefs.getBoolean("tasbeeh_sound", false)
        )
    }

    private fun saveAndEmit(updater: AppUserPreferences.() -> AppUserPreferences) {
        val updated = _userPreferences.value.updater()
        _userPreferences.value = updated
        prefs.edit().apply {
            putString("theme_mode", updated.themeMode)
            putString("accent_color", updated.accentColor)
            putString("font_scale", updated.fontScale)
            putString("card_style", updated.cardStyle)
            putString("language", updated.language)
            putString("prayer_method", updated.prayerMethod)
            putString("asr_juristic", updated.asrJuristic)
            putInt("manual_fajr_adj", updated.manualFajrAdj)
            putInt("manual_dhuhr_adj", updated.manualDhuhrAdj)
            putInt("manual_asr_adj", updated.manualAsrAdj)
            putInt("manual_maghrib_adj", updated.manualMaghribAdj)
            putInt("manual_isha_adj", updated.manualIshaAdj)
            putBoolean("notify_fajr", updated.notifyFajr)
            putBoolean("notify_dhuhr", updated.notifyDhuhr)
            putBoolean("notify_asr", updated.notifyAsr)
            putBoolean("notify_maghrib", updated.notifyMaghrib)
            putBoolean("notify_isha", updated.notifyIsha)
            putBoolean("notify_morning_adhkar", updated.notifyMorningAdhkar)
            putBoolean("notify_evening_adhkar", updated.notifyEveningAdhkar)
            putBoolean("notify_daily_dhikr", updated.notifyDailyDhikr)
            putLong("last_latitude", java.lang.Double.doubleToLongBits(updated.lastLatitude))
            putLong("last_longitude", java.lang.Double.doubleToLongBits(updated.lastLongitude))
            putString("last_city_name", updated.lastCityName)
            putString("last_country_name", updated.lastCountryName)
            putBoolean("has_custom_location", updated.hasCustomLocation)
            putInt("tasbeeh_last_count", updated.tasbeehLastCount)
            putInt("tasbeeh_target", updated.tasbeehTarget)
            putString("tasbeeh_phrase", updated.tasbeehPhrase)
            putBoolean("tasbeeh_vibration", updated.tasbeehVibration)
            putBoolean("tasbeeh_sound", updated.tasbeehSound)
            apply()
        }
    }

    fun setThemeMode(mode: String) = saveAndEmit { copy(themeMode = mode) }
    fun setAccentColor(color: String) = saveAndEmit { copy(accentColor = color) }
    fun setFontScale(scale: String) = saveAndEmit { copy(fontScale = scale) }
    fun setCardStyle(style: String) = saveAndEmit { copy(cardStyle = style) }
    fun setLanguage(lang: String) = saveAndEmit { copy(language = lang) }
    fun setPrayerMethod(method: String) = saveAndEmit { copy(prayerMethod = method) }
    fun setAsrJuristic(juristic: String) = saveAndEmit { copy(asrJuristic = juristic) }
    fun setManualAdjustment(prayer: String, minutes: Int) = saveAndEmit {
        when (prayer) {
            "fajr" -> copy(manualFajrAdj = minutes)
            "dhuhr" -> copy(manualDhuhrAdj = minutes)
            "asr" -> copy(manualAsrAdj = minutes)
            "maghrib" -> copy(manualMaghribAdj = minutes)
            "isha" -> copy(manualIshaAdj = minutes)
            else -> this
        }
    }

    fun setNotificationToggle(key: String, enabled: Boolean) = saveAndEmit {
        when (key) {
            "fajr" -> copy(notifyFajr = enabled)
            "dhuhr" -> copy(notifyDhuhr = enabled)
            "asr" -> copy(notifyAsr = enabled)
            "maghrib" -> copy(notifyMaghrib = enabled)
            "isha" -> copy(notifyIsha = enabled)
            "morning" -> copy(notifyMorningAdhkar = enabled)
            "evening" -> copy(notifyEveningAdhkar = enabled)
            "daily" -> copy(notifyDailyDhikr = enabled)
            else -> this
        }
    }

    fun updateLocation(lat: Double, lon: Double, city: String, country: String) = saveAndEmit {
        copy(
            lastLatitude = lat,
            lastLongitude = lon,
            lastCityName = city,
            lastCountryName = country,
            hasCustomLocation = true
        )
    }

    fun updateTasbeehProgress(count: Int, target: Int, phrase: String) = saveAndEmit {
        copy(
            tasbeehLastCount = count,
            tasbeehTarget = target,
            tasbeehPhrase = phrase
        )
    }

    fun toggleTasbeehVibration(enabled: Boolean) = saveAndEmit { copy(tasbeehVibration = enabled) }
    fun toggleTasbeehSound(enabled: Boolean) = saveAndEmit { copy(tasbeehSound = enabled) }
}
