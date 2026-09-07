package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prayer.AsrJuristic
import com.example.prayer.CalculationMethod
import com.example.prayer.LocationPresets
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val prefs by viewModel.preferences.collectAsState()
    val isArabic = prefs.language == "ar"

    var showPrayerMethodDialog by remember { mutableStateOf(false) }
    var showAsrJuristicDialog by remember { mutableStateOf(false) }
    var showAdjustmentsDialog by remember { mutableStateOf(false) }
    var showCityPickerLocation by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "الإعدادات" else "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp)
        ) {
            // 1. Appearance Section (المظهر)
            item {
                SectionHeader(title = if (isArabic) "المظهر والتخصيص" else "Appearance & Customization")

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Theme Mode (Light / Dark / System)
                        Text(
                            text = if (isArabic) "نمط الواجهة" else "Theme Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val modes = listOf(
                                Triple("system", if (isArabic) "تلقائي" else "System", Icons.Default.BrightnessAuto),
                                Triple("light", if (isArabic) "نهاري" else "Light", Icons.Default.LightMode),
                                Triple("dark", if (isArabic) "ليلي" else "Dark", Icons.Default.DarkMode)
                            )
                            modes.forEach { (modeKey, modeLabel, icon) ->
                                val isSelected = prefs.themeMode == modeKey
                                OutlinedButton(
                                    onClick = { viewModel.setThemeMode(modeKey) },
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = modeLabel, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Accent Color Picker
                        Text(
                            text = if (isArabic) "اللون الرئيسي" else "Accent Color",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            val colorOptions = listOf(
                                Triple("emerald", EmeraldPrimary, if (isArabic) "زمردي" else "Emerald"),
                                Triple("gold", GoldPrimary, if (isArabic) "ذهبي" else "Amber"),
                                Triple("teal", TealPrimary, if (isArabic) "فيروزي" else "Teal"),
                                Triple("blue", BluePrimary, if (isArabic) "كحلي" else "Blue"),
                                Triple("rose", RosePrimary, if (isArabic) "وردي" else "Rose")
                            )

                            colorOptions.forEach { (colorKey, colorVal, name) ->
                                val isSelected = prefs.accentColor == colorKey
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(colorVal)
                                            .border(
                                                width = if (isSelected) 3.dp else 0.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable { viewModel.setAccentColor(colorKey) }
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = name, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Font Size Scale
                        Text(
                            text = if (isArabic) "حجم خط الأذكار" else "Dhikr Font Size",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Pair("small", if (isArabic) "صغير" else "Small"),
                                Pair("medium", if (isArabic) "متوسط" else "Medium"),
                                Pair("large", if (isArabic) "كبير" else "Large")
                            ).forEach { (sizeKey, sizeLabel) ->
                                val isSelected = prefs.fontScale == sizeKey
                                OutlinedButton(
                                    onClick = { viewModel.setFontScale(sizeKey) },
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = sizeLabel, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Card Style
                        Text(
                            text = if (isArabic) "شكل البطاقات" else "Card Shape",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Pair("rounded", if (isArabic) "مستدير" else "Rounded"),
                                Pair("simple", if (isArabic) "بسيط" else "Simple"),
                                Pair("compact", if (isArabic) "مدمج" else "Compact")
                            ).forEach { (styleKey, styleLabel) ->
                                val isSelected = prefs.cardStyle == styleKey
                                OutlinedButton(
                                    onClick = { viewModel.setCardStyle(styleKey) },
                                    shape = MaterialTheme.shapes.small,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = styleLabel, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Language Section (اللغة)
            item {
                SectionHeader(title = if (isArabic) "اللغة / Language" else "Language")

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "واجهة التطبيق" else "App Language",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = isArabic,
                                onClick = { viewModel.setLanguage("ar") },
                                label = { Text("العربية (RTL)") }
                            )
                            FilterChip(
                                selected = !isArabic,
                                onClick = { viewModel.setLanguage("en") },
                                label = { Text("English (LTR)") }
                            )
                        }
                    }
                }
            }

            // 3. Prayer Times Settings (مواقيت الصلاة)
            item {
                SectionHeader(title = if (isArabic) "مواقيت الصلاة والحساب" else "Prayer Times & Calculation")

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Location Picker
                        SettingClickableRow(
                            title = if (isArabic) "تحديد المدينة والموقع" else "City & Location",
                            subtitle = "${prefs.lastCityName}, ${prefs.lastCountryName}",
                            icon = Icons.Default.Place,
                            onClick = { showCityPickerLocation = true }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        // Calculation Method
                        val method = CalculationMethod.fromId(prefs.prayerMethod)
                        SettingClickableRow(
                            title = if (isArabic) "طريقة الحساب الفلكي" else "Calculation Method",
                            subtitle = if (isArabic) method.displayNameAr else method.displayNameEn,
                            icon = Icons.Default.Calculate,
                            onClick = { showPrayerMethodDialog = true }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        // Asr Juristic
                        val juristic = AsrJuristic.fromId(prefs.asrJuristic)
                        SettingClickableRow(
                            title = if (isArabic) "مذهب صلاة العصر" else "Asr Juristic Method",
                            subtitle = if (isArabic) juristic.displayNameAr else juristic.displayNameEn,
                            icon = Icons.Default.Schedule,
                            onClick = { showAsrJuristicDialog = true }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        // Manual Minute Adjustments
                        SettingClickableRow(
                            title = if (isArabic) "ضبط الدقائق يدويًا" else "Manual Adjustments (Minutes)",
                            subtitle = if (isArabic) "تعديل تقديم أو تأخير الدقائق لكل صلاة" else "Offset prayer minutes (+/-)",
                            icon = Icons.Default.Tune,
                            onClick = { showAdjustmentsDialog = true }
                        )
                    }
                }
            }

            // 4. Notifications Section (الإشعارات)
            item {
                SectionHeader(title = if (isArabic) "التنبيهات والإشعارات" else "Notifications & Reminders")

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingSwitchRow(
                            title = if (isArabic) "تذكير أذكار الصباح" else "Morning Adhkar Reminder",
                            subtitle = if (isArabic) "تنبيه يومي عند شروق الشمس" else "Daily alert at sunrise",
                            checked = prefs.notifyMorningAdhkar,
                            onCheckedChange = { viewModel.setNotificationToggle("morning", it) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        SettingSwitchRow(
                            title = if (isArabic) "تذكير أذكار المساء" else "Evening Adhkar Reminder",
                            subtitle = if (isArabic) "تنبيه يومي قبل صلاة المغرب" else "Daily alert before Maghrib",
                            checked = prefs.notifyEveningAdhkar,
                            onCheckedChange = { viewModel.setNotificationToggle("evening", it) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        SettingSwitchRow(
                            title = if (isArabic) "أذان وتنبيه الصلوات الخمس" else "Prayer Times Notifications",
                            subtitle = if (isArabic) "تنبيه صوتي عند حلول وقت الصلاة" else "Sound and alert at prayer times",
                            checked = prefs.notifyFajr,
                            onCheckedChange = {
                                viewModel.setNotificationToggle("fajr", it)
                                viewModel.setNotificationToggle("dhuhr", it)
                                viewModel.setNotificationToggle("asr", it)
                                viewModel.setNotificationToggle("maghrib", it)
                                viewModel.setNotificationToggle("isha", it)
                            }
                        )
                    }
                }
            }

            // 5. About Section
            item {
                SectionHeader(title = if (isArabic) "عن التطبيق" else "About")

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SettingClickableRow(
                            title = if (isArabic) "تطبيق ذِكر" else "Dhikr App",
                            subtitle = if (isArabic) "الإصدار 1.0 (تطبيق إسلامي حديث ومتكامل)" else "Version 1.0 (Modern Islamic App)",
                            icon = Icons.Default.Info,
                            onClick = { showAboutDialog = true }
                        )
                    }
                }
            }
        }
    }

    // Calculation Method Dialog
    if (showPrayerMethodDialog) {
        AlertDialog(
            onDismissRequest = { showPrayerMethodDialog = false },
            title = {
                Text(
                    text = if (isArabic) "طريقة حساب مواقيت الصلاة" else "Calculation Method",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalculationMethod.entries.forEach { method ->
                        val isSelected = prefs.prayerMethod == method.id
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setPrayerMethod(method.id)
                                    showPrayerMethodDialog = false
                                }
                        ) {
                            Text(
                                text = if (isArabic) method.displayNameAr else method.displayNameEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrayerMethodDialog = false }) {
                    Text(if (isArabic) "إغلاق" else "Close")
                }
            }
        )
    }

    // Asr Juristic Dialog
    if (showAsrJuristicDialog) {
        AlertDialog(
            onDismissRequest = { showAsrJuristicDialog = false },
            title = {
                Text(
                    text = if (isArabic) "مذهب حساب صلاة العصر" else "Asr Juristic Method",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsrJuristic.entries.forEach { juristic ->
                        val isSelected = prefs.asrJuristic == juristic.id
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAsrJuristic(juristic.id)
                                    showAsrJuristicDialog = false
                                }
                        ) {
                            Text(
                                text = if (isArabic) juristic.displayNameAr else juristic.displayNameEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAsrJuristicDialog = false }) {
                    Text(if (isArabic) "إغلاق" else "Close")
                }
            }
        )
    }

    // Manual Adjustments Dialog
    if (showAdjustmentsDialog) {
        AlertDialog(
            onDismissRequest = { showAdjustmentsDialog = false },
            title = {
                Text(
                    text = if (isArabic) "ضبط الدقائق اليدوي" else "Manual Adjustments",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val prayerAdjustments = listOf(
                        Triple("fajr", if (isArabic) "الفجر" else "Fajr", prefs.manualFajrAdj),
                        Triple("dhuhr", if (isArabic) "الظهر" else "Dhuhr", prefs.manualDhuhrAdj),
                        Triple("asr", if (isArabic) "العصر" else "Asr", prefs.manualAsrAdj),
                        Triple("maghrib", if (isArabic) "المغرب" else "Maghrib", prefs.manualMaghribAdj),
                        Triple("isha", if (isArabic) "العشاء" else "Isha", prefs.manualIshaAdj)
                    )

                    prayerAdjustments.forEach { (key, name, adj) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = name, fontWeight = FontWeight.SemiBold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.setManualAdjustment(key, adj - 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Remove, contentDescription = "-")
                                }
                                Text(
                                    text = if (adj >= 0) "+$adj" else "$adj",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.setManualAdjustment(key, adj + 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "+")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAdjustmentsDialog = false }) {
                    Text(if (isArabic) "تم" else "Done")
                }
            }
        )
    }

    // City Picker Location Dialog
    if (showCityPickerLocation) {
        AlertDialog(
            onDismissRequest = { showCityPickerLocation = false },
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
                            shape = MaterialTheme.shapes.small,
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
                                    showCityPickerLocation = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
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
                    showCityPickerLocation = false
                }) {
                    Text(if (isArabic) "تحديد تلقائي (GPS)" else "Use GPS")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCityPickerLocation = false }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📿", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (isArabic) "عن تطبيق ذِكر" else "About Dhikr", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isArabic)
                            "تطبيق إسلامي حديث ومتكامل يجمع بين الأذكار اليومية الموثقة من الكتاب والسنة، والمسبحة الإلكترونية، ومواقيت الصلاة الدقيقة المحسوبة فلكيًا بناءً على موقعك الجغرافي، مع اتجاه القبلة وتخصيص المظهر بالكامل."
                        else
                            "A modern, comprehensive Islamic app featuring verified daily adhkar from Quran and Sunnah, digital tasbeeh, astronomically calculated prayer times based on your location, Qibla compass, and full theme customization."
                    )
                    Text(
                        text = if (isArabic) "﴿أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ﴾" else "\"Verily in the remembrance of Allah do hearts find rest.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(if (isArabic) "تم" else "OK")
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingClickableRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
