package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuiltInDhikr
import com.example.prayer.LocationPresets
import com.example.prayer.PrayerTime
import com.example.prayer.PrayerTimesResult
import com.example.ui.theme.CleanMinimalPrimary
import com.example.ui.theme.CleanMinimalPrimaryDark
import com.example.ui.theme.CleanMinimalSlate100
import com.example.ui.theme.CleanMinimalSlate200
import com.example.ui.theme.CleanMinimalSlate400
import com.example.ui.theme.CleanMinimalSlate500
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val prayerResult by viewModel.prayerResult.collectAsState()
    val dailyDhikr by viewModel.dailyDhikr.collectAsState()
    val isLocating by viewModel.isLocating.collectAsState()
    val isArabic = prefs.language == "ar"

    var showLocationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isArabic) "ذِكر" else "Dhikr",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { showLocationDialog = true }
                                    .padding(top = 2.dp)
                            ) {
                                // Pulsing green status dot
                                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                val pulseAlpha by infiniteTransition.animateFloat(
                                    initialValue = 0.4f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1000),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "pulseAlpha"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .alpha(pulseAlpha)
                                        .background(CleanMinimalPrimary, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${prefs.lastCityName}، ${prefs.lastCountryName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = CleanMinimalPrimary
                                )
                            }
                        }

                        // Hijri & Gregorian date in header
                        prayerResult?.let { result ->
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = result.hijriDateText,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = CleanMinimalSlate500
                                )
                                Text(
                                    text = result.dateText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Search) },
                        modifier = Modifier.testTag("home_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = if (isArabic) "بحث" else "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Favorites) },
                        modifier = Modifier.testTag("home_favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = if (isArabic) "المفضلة" else "Favorites",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            // 1. Next Prayer Countdown Hero Card (Clean Minimalism Gradient)
            item {
                prayerResult?.let { result ->
                    NextPrayerHeroCard(
                        result = result,
                        isArabic = isArabic
                    )
                }
            }

            // 2. 5-Column Prayer Times Grid (Clean Minimalism Card Pills)
            item {
                prayerResult?.let { result ->
                    PrayerTimesMinimalGrid(
                        result = result,
                        isArabic = isArabic
                    )
                }
            }

            // 3. Daily Dhikr Card (Clean Minimalism Tinted Container)
            item {
                DailyDhikrCard(
                    dhikr = dailyDhikr,
                    isArabic = isArabic,
                    onStartClick = { viewModel.openDhikr(dailyDhikr) }
                )
            }

            // 4. Morning & Evening Adhkar Grid Cards
            item {
                MorningEveningDhikrRow(
                    isArabic = isArabic,
                    onMorningClick = { viewModel.navigateTo(ScreenDestination.CategoryDetail("morning")) },
                    onEveningClick = { viewModel.navigateTo(ScreenDestination.CategoryDetail("evening")) }
                )
            }

            // 5. Quick Shortcuts Section
            item {
                QuickShortcutsRow(
                    isArabic = isArabic,
                    onShortcutClick = { destination -> viewModel.navigateTo(destination) }
                )
            }
        }
    }

    // Location Picker Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = {
                Text(
                    text = if (isArabic) "اختر المدينة" else "Select City",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(LocationPresets.presets) { city ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setManualCity(
                                        city.nameAr,
                                        city.nameEn,
                                        city.countryAr,
                                        city.countryEn,
                                        city.latitude,
                                        city.longitude
                                    )
                                    showLocationDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isArabic) city.nameAr else city.nameEn,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isArabic) city.countryAr else city.countryEn,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.refreshLocation()
                    showLocationDialog = false
                }) {
                    Text(if (isArabic) "تحديد تلقائي (GPS)" else "Use GPS")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun NextPrayerHeroCard(
    result: PrayerTimesResult,
    isArabic: Boolean
) {
    val next = result.nextPrayer
    val remainingSecs = (result.timeRemainingMillis / 1000).coerceAtLeast(0)
    val hours = remainingSecs / 3600
    val minutes = (remainingSecs % 3600) / 60
    val seconds = remainingSecs % 60

    val countdownText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("next_prayer_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            CleanMinimalPrimary,
                            CleanMinimalPrimaryDark
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Decorative background watermark
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.08f),
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-12).dp, y = 12.dp)
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "الصلاة القادمة" else "Next Prayer",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isArabic) next.nameAr else next.nameEn,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Translucent Adhan time badge pill
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.20f)
                    ) {
                        Text(
                            text = next.formattedTime12,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "-$countdownText",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 34.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isArabic) "متبقي" else "Remaining",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PrayerTimesMinimalGrid(
    result: PrayerTimesResult,
    isArabic: Boolean
) {
    val prayers = listOf(
        result.fajr,
        result.dhuhr,
        result.asr,
        result.maghrib,
        result.isha
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        prayers.forEach { prayer ->
            val isNext = prayer.isNext
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (isNext) CleanMinimalPrimary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    width = if (isNext) 1.5.dp else 1.dp,
                    color = if (isNext) CleanMinimalPrimary else CleanMinimalSlate100
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("prayer_item_${prayer.id}")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 2.dp)
                ) {
                    Text(
                        text = if (isArabic) prayer.nameAr else prayer.nameEn,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                        color = if (isNext) CleanMinimalPrimary else CleanMinimalSlate400
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prayer.formattedTime24,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        fontWeight = FontWeight.Bold,
                        color = if (isNext) CleanMinimalPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyDhikrCard(
    dhikr: BuiltInDhikr,
    isArabic: Boolean,
    onStartClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = CleanMinimalSlate100.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_dhikr_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isArabic) "ذِكْر اليوم" else "DAILY DHIKR",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    fontWeight = FontWeight.Bold,
                    color = CleanMinimalPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "«${dhikr.content}»",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    color = Color(0xFF334155),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, CleanMinimalSlate200),
                    modifier = Modifier
                        .clickable(onClick = onStartClick)
                        .testTag("start_daily_dhikr_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isArabic) "ابدأ الذكر" else "Start Dhikr",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.Bold,
                            color = CleanMinimalPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Decorative circular indicator icon container
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.75f },
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 4.dp,
                        color = CleanMinimalPrimary,
                        trackColor = CleanMinimalPrimary.copy(alpha = 0.15f)
                    )
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = CleanMinimalPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MorningEveningDhikrRow(
    isArabic: Boolean,
    onMorningClick: () -> Unit,
    onEveningClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Morning Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, CleanMinimalSlate100),
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onMorningClick)
                .testTag("card_morning_adhkar")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFFBEB),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🌅", fontSize = 20.sp)
                    }
                }
                Column {
                    Text(
                        text = if (isArabic) "أذكار الصباح" else "Morning Dhikr",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) "٢٤ ذِكر" else "24 Dhikr",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = CleanMinimalSlate400
                    )
                }
            }
        }

        // Evening Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, CleanMinimalSlate100),
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onEveningClick)
                .testTag("card_evening_adhkar")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFEEF2FF),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🌙", fontSize = 20.sp)
                    }
                }
                Column {
                    Text(
                        text = if (isArabic) "أذكار المساء" else "Evening Dhikr",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) "١٨ ذِكر" else "18 Dhikr",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = CleanMinimalSlate400
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickShortcutsRow(
    isArabic: Boolean,
    onShortcutClick: (ScreenDestination) -> Unit
) {
    data class ShortcutData(
        val titleAr: String,
        val titleEn: String,
        val icon: ImageVector,
        val destination: ScreenDestination,
        val testTag: String
    )

    val shortcuts = listOf(
        ShortcutData("التسبيح", "Tasbeeh", Icons.Default.TouchApp, ScreenDestination.Tasbeeh, "shortcut_tasbeeh"),
        ShortcutData("القبلة", "Qibla", Icons.Default.CompassCalibration, ScreenDestination.Qibla, "shortcut_qibla"),
        ShortcutData("أدعية مأثورة", "Duas", Icons.Default.AutoStories, ScreenDestination.CategoryDetail("quranic_duas"), "shortcut_duas"),
        ShortcutData("أذكاري", "Custom", Icons.Default.BookmarkBorder, ScreenDestination.CustomAdhkar, "shortcut_custom")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isArabic) "الوصول السريع" else "Quick Shortcuts",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            shortcuts.forEach { shortcut ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onShortcutClick(shortcut.destination) }
                        .testTag(shortcut.testTag)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CleanMinimalSlate100,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = shortcut.icon,
                                contentDescription = if (isArabic) shortcut.titleAr else shortcut.titleEn,
                                tint = CleanMinimalPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isArabic) shortcut.titleAr else shortcut.titleEn,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
