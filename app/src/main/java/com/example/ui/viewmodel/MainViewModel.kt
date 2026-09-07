package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.AppUserPreferences
import com.example.data.local.UserPreferencesManager
import com.example.data.model.BuiltInAdhkarData
import com.example.data.model.BuiltInDhikr
import com.example.data.model.CustomDhikr
import com.example.data.model.DhikrCategory
import com.example.data.model.FavoriteItem
import com.example.data.model.TasbeehRecord
import com.example.data.repository.DhikrRepository
import com.example.prayer.AsrJuristic
import com.example.prayer.CalculationMethod
import com.example.prayer.LocationHelper
import com.example.prayer.PrayerTimesCalculator
import com.example.prayer.PrayerTimesResult
import com.example.qibla.CompassSensorManager
import com.example.qibla.QiblaCalculator
import com.example.ui.components.ArabicTextNormalizer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object Adhkar : ScreenDestination()
    data class CategoryDetail(val categoryId: String) : ScreenDestination()
    data class DhikrPlayer(val dhikrKey: String) : ScreenDestination()
    object Tasbeeh : ScreenDestination()
    object Qibla : ScreenDestination()
    object Favorites : ScreenDestination()
    object CustomAdhkar : ScreenDestination()
    object Settings : ScreenDestination()
    object Search : ScreenDestination()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = DhikrRepository(db.customDhikrDao(), db.favoriteDao(), db.tasbeehDao())
    private val userPrefsManager = UserPreferencesManager(application)
    private val locationHelper = LocationHelper(application)
    private val compassSensorManager = CompassSensorManager(application)

    // User preferences
    val preferences: StateFlow<AppUserPreferences> = userPrefsManager.userPreferences

    // Navigation Stack
    private val _currentDestination = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentDestination: StateFlow<ScreenDestination> = _currentDestination.asStateFlow()

    private val _navigationStack = mutableListOf<ScreenDestination>()

    // Prayer Times & Countdown
    private val _prayerResult = MutableStateFlow<PrayerTimesResult?>(null)
    val prayerResult: StateFlow<PrayerTimesResult?> = _prayerResult.asStateFlow()

    // Location
    private val _isLocating = MutableStateFlow(false)
    val isLocating: StateFlow<Boolean> = _isLocating.asStateFlow()

    // Compass
    val compassState = compassSensorManager.compassState

    // Custom Adhkar & Favorites from DB
    val customAdhkar: StateFlow<List<CustomDhikr>> = repository.allCustomAdhkar
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteItems: StateFlow<List<FavoriteItem>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasbeehHistory: StateFlow<List<TasbeehRecord>> = repository.recentTasbeehRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Dhikr Player State
    private val _playerDhikr = MutableStateFlow<BuiltInDhikr?>(null)
    val playerDhikr: StateFlow<BuiltInDhikr?> = _playerDhikr.asStateFlow()

    private val _playerCurrentCount = MutableStateFlow(0)
    val playerCurrentCount: StateFlow<Int> = _playerCurrentCount.asStateFlow()

    private val _isPlayerCompleted = MutableStateFlow(false)
    val isPlayerCompleted: StateFlow<Boolean> = _isPlayerCompleted.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Tasbeeh State
    private val _tasbeehCount = MutableStateFlow(preferences.value.tasbeehLastCount)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val _tasbeehTarget = MutableStateFlow(preferences.value.tasbeehTarget)
    val tasbeehTarget: StateFlow<Int> = _tasbeehTarget.asStateFlow()

    private val _tasbeehPhrase = MutableStateFlow(preferences.value.tasbeehPhrase)
    val tasbeehPhrase: StateFlow<String> = _tasbeehPhrase.asStateFlow()

    // Qibla Info
    val qiblaBearing: StateFlow<Double> = preferences.map {
        QiblaCalculator.calculateQiblaBearing(it.lastLatitude, it.lastLongitude)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val distanceToMakkahKm: StateFlow<Double> = preferences.map {
        QiblaCalculator.calculateDistanceToMakkahKm(it.lastLatitude, it.lastLongitude)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Daily Dhikr
    val dailyDhikr: StateFlow<BuiltInDhikr> = MutableStateFlow(getTodaysDhikr()).asStateFlow()

    init {
        // Start prayer calculation & countdown ticker
        recalculatePrayerTimes()
        startCountdownTicker()

        // Sync initial tasbeeh state
        _tasbeehCount.value = preferences.value.tasbeehLastCount
        _tasbeehTarget.value = preferences.value.tasbeehTarget
        _tasbeehPhrase.value = preferences.value.tasbeehPhrase

        // Try updating location if permission is already granted
        if (locationHelper.hasLocationPermission()) {
            refreshLocation()
        }
    }

    private fun getTodaysDhikr(): BuiltInDhikr {
        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val list = BuiltInAdhkarData.adhkarList
        return list[dayOfYear % list.size]
    }

    // Navigation methods
    fun navigateTo(destination: ScreenDestination) {
        if (_currentDestination.value != destination) {
            _navigationStack.add(_currentDestination.value)
            _currentDestination.value = destination
            if (destination is ScreenDestination.Qibla) {
                compassSensorManager.startListening()
            } else {
                compassSensorManager.stopListening()
            }
        }
    }

    fun navigateBack(): Boolean {
        return if (_navigationStack.isNotEmpty()) {
            val prev = _navigationStack.removeAt(_navigationStack.size - 1)
            _currentDestination.value = prev
            if (prev is ScreenDestination.Qibla) {
                compassSensorManager.startListening()
            } else {
                compassSensorManager.stopListening()
            }
            true
        } else {
            if (_currentDestination.value != ScreenDestination.Home) {
                _currentDestination.value = ScreenDestination.Home
                compassSensorManager.stopListening()
                true
            } else {
                false
            }
        }
    }

    // Prayer Times & Countdown
    fun recalculatePrayerTimes() {
        val pref = preferences.value
        val method = CalculationMethod.fromId(pref.prayerMethod)
        val juristic = AsrJuristic.fromId(pref.asrJuristic)
        val adjustments = mapOf(
            "fajr" to pref.manualFajrAdj,
            "dhuhr" to pref.manualDhuhrAdj,
            "asr" to pref.manualAsrAdj,
            "maghrib" to pref.manualMaghribAdj,
            "isha" to pref.manualIshaAdj
        )

        val result = PrayerTimesCalculator.calculatePrayerTimes(
            latitude = pref.lastLatitude,
            longitude = pref.lastLongitude,
            method = method,
            asrJuristic = juristic,
            adjustmentsMinutes = adjustments
        )
        _prayerResult.value = result
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                recalculatePrayerTimes()
            }
        }
    }

    // Location
    fun refreshLocation() {
        viewModelScope.launch {
            _isLocating.value = true
            val coords = locationHelper.getCurrentCoordinates()
            if (coords != null) {
                val isAr = preferences.value.language == "ar"
                val (city, country) = locationHelper.getPlaceName(coords.first, coords.second, isAr)
                userPrefsManager.updateLocation(coords.first, coords.second, city, country)
                recalculatePrayerTimes()
            }
            _isLocating.value = false
        }
    }

    fun setManualCity(nameAr: String, nameEn: String, countryAr: String, countryEn: String, lat: Double, lon: Double) {
        val isAr = preferences.value.language == "ar"
        userPrefsManager.updateLocation(
            lat,
            lon,
            if (isAr) nameAr else nameEn,
            if (isAr) countryAr else countryEn
        )
        recalculatePrayerTimes()
    }

    // Player controls
    fun openDhikr(dhikr: BuiltInDhikr) {
        _playerDhikr.value = dhikr
        _playerCurrentCount.value = 0
        _isPlayerCompleted.value = false
        navigateTo(ScreenDestination.DhikrPlayer(dhikr.id))
    }

    fun openDhikrByKey(key: String) {
        val found = BuiltInAdhkarData.adhkarList.find { it.id == key }
        if (found != null) {
            openDhikr(found)
        } else if (key.startsWith("custom_")) {
            val customId = key.removePrefix("custom_").toLongOrNull()
            val custom = customAdhkar.value.find { it.id == customId }
            if (custom != null) {
                val converted = BuiltInDhikr(
                    id = key,
                    title = custom.title,
                    titleEn = custom.title,
                    content = custom.content,
                    count = custom.targetCount,
                    categoryId = "custom",
                    categoryNameAr = custom.category,
                    categoryNameEn = "Custom",
                    virtue = "ذكر خاص بك محفوظ محليًا",
                    source = ""
                )
                openDhikr(converted)
            }
        }
    }

    fun incrementPlayerCount() {
        val current = _playerCurrentCount.value
        val target = _playerDhikr.value?.count ?: 1
        if (current < target) {
            val next = current + 1
            _playerCurrentCount.value = next
            performHaptic()
            if (next >= target) {
                _isPlayerCompleted.value = true
                performSuccessHaptic()
            }
        }
    }

    fun resetPlayerCount() {
        _playerCurrentCount.value = 0
        _isPlayerCompleted.value = false
    }

    fun nextDhikrInPlayer() {
        val current = _playerDhikr.value ?: return
        val list = BuiltInAdhkarData.adhkarList.filter { it.categoryId == current.categoryId }
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1 && currentIndex + 1 < list.size) {
            openDhikr(list[currentIndex + 1])
        }
    }

    // Tasbeeh
    fun incrementTasbeeh() {
        val next = _tasbeehCount.value + 1
        _tasbeehCount.value = next
        performHaptic()

        val target = _tasbeehTarget.value
        if (target > 0 && next % target == 0) {
            performSuccessHaptic()
            viewModelScope.launch {
                repository.recordTasbeehSession(_tasbeehPhrase.value, next, target)
            }
        }
        userPrefsManager.updateTasbeehProgress(next, target, _tasbeehPhrase.value)
    }

    fun resetTasbeeh() {
        val count = _tasbeehCount.value
        val target = _tasbeehTarget.value
        val phrase = _tasbeehPhrase.value
        if (count > 0) {
            viewModelScope.launch {
                repository.recordTasbeehSession(phrase, count, target)
            }
        }
        _tasbeehCount.value = 0
        userPrefsManager.updateTasbeehProgress(0, target, phrase)
    }

    fun setTasbeehTarget(target: Int) {
        _tasbeehTarget.value = target
        userPrefsManager.updateTasbeehProgress(_tasbeehCount.value, target, _tasbeehPhrase.value)
    }

    fun setTasbeehPhrase(phrase: String) {
        _tasbeehPhrase.value = phrase
        userPrefsManager.updateTasbeehProgress(_tasbeehCount.value, _tasbeehTarget.value, phrase)
    }

    fun clearTasbeehHistory() {
        viewModelScope.launch {
            repository.clearTasbeehHistory()
        }
    }

    // Favorites
    fun toggleFavorite(dhikr: BuiltInDhikr) {
        viewModelScope.launch {
            repository.toggleFavorite(
                key = dhikr.id,
                title = dhikr.title,
                content = dhikr.content,
                category = dhikr.categoryNameAr,
                targetCount = dhikr.count,
                source = dhikr.source
            )
        }
    }

    fun removeFavoriteByKey(key: String) {
        viewModelScope.launch {
            repository.removeFavorite(key)
        }
    }

    fun isFavorite(key: String): Boolean {
        return favoriteItems.value.any { it.dhikrKey == key }
    }

    // Custom Adhkar
    fun addCustomDhikr(title: String, content: String, targetCount: Int, category: String, icon: String) {
        viewModelScope.launch {
            repository.insertCustomDhikr(
                CustomDhikr(
                    title = title.trim(),
                    content = content.trim(),
                    targetCount = targetCount,
                    category = category.ifEmpty { "أذكار مخصصة" },
                    iconName = icon
                )
            )
        }
    }

    fun updateCustomDhikr(dhikr: CustomDhikr) {
        viewModelScope.launch {
            repository.updateCustomDhikr(dhikr)
        }
    }

    fun deleteCustomDhikr(dhikr: CustomDhikr) {
        viewModelScope.launch {
            repository.deleteCustomDhikr(dhikr)
        }
    }

    // Search
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchResults(query: String): List<BuiltInDhikr> {
        if (query.isBlank()) return emptyList()
        val builtInMatches = BuiltInAdhkarData.adhkarList.filter {
            ArabicTextNormalizer.containsQuery(it.title, query) ||
                    ArabicTextNormalizer.containsQuery(it.content, query) ||
                    ArabicTextNormalizer.containsQuery(it.categoryNameAr, query) ||
                    it.titleEn.contains(query, ignoreCase = true)
        }
        val customMatches = customAdhkar.value.filter {
            ArabicTextNormalizer.containsQuery(it.title, query) ||
                    ArabicTextNormalizer.containsQuery(it.content, query) ||
                    ArabicTextNormalizer.containsQuery(it.category, query)
        }.map {
            BuiltInDhikr(
                id = "custom_${it.id}",
                title = it.title,
                titleEn = it.title,
                content = it.content,
                count = it.targetCount,
                categoryId = "custom",
                categoryNameAr = it.category,
                categoryNameEn = "Custom",
                virtue = "ذكر خاص بك",
                source = ""
            )
        }
        return builtInMatches + customMatches
    }

    // Preferences Setters
    fun setThemeMode(mode: String) = userPrefsManager.setThemeMode(mode)
    fun setAccentColor(color: String) = userPrefsManager.setAccentColor(color)
    fun setFontScale(scale: String) = userPrefsManager.setFontScale(scale)
    fun setCardStyle(style: String) = userPrefsManager.setCardStyle(style)
    fun setLanguage(lang: String) = userPrefsManager.setLanguage(lang)
    fun setPrayerMethod(method: String) {
        userPrefsManager.setPrayerMethod(method)
        recalculatePrayerTimes()
    }
    fun setAsrJuristic(juristic: String) {
        userPrefsManager.setAsrJuristic(juristic)
        recalculatePrayerTimes()
    }
    fun setManualAdjustment(prayer: String, minutes: Int) {
        userPrefsManager.setManualAdjustment(prayer, minutes)
        recalculatePrayerTimes()
    }
    fun setNotificationToggle(key: String, enabled: Boolean) =
        userPrefsManager.setNotificationToggle(key, enabled)

    // Haptics
    private fun performHaptic() {
        if (!preferences.value.tasbeehVibration) return
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun performSuccessHaptic() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 80, 50), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun onCleared() {
        super.onCleared()
        compassSensorManager.stopListening()
    }
}
