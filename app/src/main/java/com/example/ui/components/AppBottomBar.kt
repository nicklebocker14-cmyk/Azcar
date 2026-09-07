package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CleanMinimalPrimary
import com.example.ui.theme.CleanMinimalSlate100
import com.example.ui.theme.CleanMinimalSlate400
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AppBottomBar(
    currentDestination: ScreenDestination,
    isArabic: Boolean,
    onNavigate: (ScreenDestination) -> Unit
) {
    val items = listOf(
        NavigationItemData(
            destination = ScreenDestination.Home,
            labelAr = "الرئيسية",
            labelEn = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavigationItemData(
            destination = ScreenDestination.Adhkar,
            labelAr = "الأذكار",
            labelEn = "Adhkar",
            selectedIcon = Icons.Filled.MenuBook,
            unselectedIcon = Icons.Outlined.MenuBook,
            testTag = "nav_adhkar"
        ),
        NavigationItemData(
            destination = ScreenDestination.Tasbeeh,
            labelAr = "التسبيح",
            labelEn = "Tasbeeh",
            selectedIcon = Icons.Filled.TouchApp,
            unselectedIcon = Icons.Outlined.TouchApp,
            testTag = "nav_tasbeeh"
        ),
        NavigationItemData(
            destination = ScreenDestination.Qibla,
            labelAr = "القبلة",
            labelEn = "Qibla",
            selectedIcon = Icons.Filled.CompassCalibration,
            unselectedIcon = Icons.Outlined.CompassCalibration,
            testTag = "nav_qibla"
        ),
        NavigationItemData(
            destination = ScreenDestination.Settings,
            labelAr = "الإعدادات",
            labelEn = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            testTag = "nav_settings"
        )
    )

    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    NavigationBar(
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        items.forEach { item ->
            val isSelected = when (item.destination) {
                is ScreenDestination.Home -> currentDestination is ScreenDestination.Home
                is ScreenDestination.Adhkar -> currentDestination is ScreenDestination.Adhkar ||
                        currentDestination is ScreenDestination.CategoryDetail ||
                        currentDestination is ScreenDestination.DhikrPlayer
                is ScreenDestination.Tasbeeh -> currentDestination is ScreenDestination.Tasbeeh
                is ScreenDestination.Qibla -> currentDestination is ScreenDestination.Qibla
                is ScreenDestination.Settings -> currentDestination is ScreenDestination.Settings
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = if (isArabic) item.labelAr else item.labelEn
                    )
                },
                label = {
                    Text(
                        text = if (isArabic) item.labelAr else item.labelEn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = CleanMinimalSlate400,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = CleanMinimalSlate400
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

private data class NavigationItemData(
    val destination: ScreenDestination,
    val labelAr: String,
    val labelEn: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
