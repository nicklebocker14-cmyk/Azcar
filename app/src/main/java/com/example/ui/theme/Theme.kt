package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun getThemeColorScheme(accentColor: String, isDark: Boolean): ColorScheme {
    return when (accentColor) {
        "gold" -> if (isDark) {
            darkColorScheme(
                primary = GoldAccent,
                onPrimary = Color.Black,
                primaryContainer = GoldPrimary,
                onPrimaryContainer = Color.White,
                secondary = GoldSecondary,
                onSecondary = Color.Black,
                tertiary = GoldTertiary,
                background = GoldBackgroundDark,
                surface = GoldSurfaceDark,
                onBackground = Color(0xFFECE6DD),
                onSurface = Color(0xFFECE6DD),
                surfaceVariant = Color(0xFF2C271E),
                onSurfaceVariant = Color(0xFFCDC3B5)
            )
        } else {
            lightColorScheme(
                primary = GoldPrimary,
                onPrimary = Color.White,
                primaryContainer = Color(0xFFF7EEDD),
                onPrimaryContainer = GoldPrimary,
                secondary = GoldSecondary,
                tertiary = GoldTertiary,
                background = GoldBackgroundLight,
                surface = GoldSurfaceLight,
                onBackground = Color(0xFF25201A),
                onSurface = Color(0xFF25201A),
                surfaceVariant = Color(0xFFEFE8DA),
                onSurfaceVariant = Color(0xFF5A5245)
            )
        }
        "teal" -> if (isDark) {
            darkColorScheme(
                primary = Color(0xFF4DB6AC),
                onPrimary = Color.Black,
                primaryContainer = TealPrimary,
                onPrimaryContainer = Color.White,
                secondary = TealSecondary,
                tertiary = TealTertiary,
                background = TealBackgroundDark,
                surface = TealSurfaceDark,
                onBackground = Color(0xFFE0F2F1),
                onSurface = Color(0xFFE0F2F1),
                surfaceVariant = Color(0xFF1E3535),
                onSurfaceVariant = Color(0xFFB2DFDB)
            )
        } else {
            lightColorScheme(
                primary = TealPrimary,
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD4EBEB),
                onPrimaryContainer = TealPrimary,
                secondary = TealSecondary,
                tertiary = TealTertiary,
                background = TealBackgroundLight,
                surface = TealSurfaceLight,
                onBackground = Color(0xFF112222),
                onSurface = Color(0xFF112222),
                surfaceVariant = Color(0xFFE0ECEC),
                onSurfaceVariant = Color(0xFF4A6262)
            )
        }
        "blue" -> if (isDark) {
            darkColorScheme(
                primary = Color(0xFF93C5FD),
                onPrimary = Color.Black,
                primaryContainer = BluePrimary,
                onPrimaryContainer = Color.White,
                secondary = BlueSecondary,
                tertiary = BlueTertiary,
                background = BlueBackgroundDark,
                surface = BlueSurfaceDark,
                onBackground = Color(0xFFE5EDFB),
                onSurface = Color(0xFFE5EDFB),
                surfaceVariant = Color(0xFF1F2B48),
                onSurfaceVariant = Color(0xFFB8C7E0)
            )
        } else {
            lightColorScheme(
                primary = BluePrimary,
                onPrimary = Color.White,
                primaryContainer = Color(0xFFDBEAFE),
                onPrimaryContainer = BluePrimary,
                secondary = BlueSecondary,
                tertiary = BlueTertiary,
                background = BlueBackgroundLight,
                surface = BlueSurfaceLight,
                onBackground = Color(0xFF141D2D),
                onSurface = Color(0xFF141D2D),
                surfaceVariant = Color(0xFFE2E8F4),
                onSurfaceVariant = Color(0xFF4B5565)
            )
        }
        "rose" -> if (isDark) {
            darkColorScheme(
                primary = Color(0xFFF48FB1),
                onPrimary = Color.Black,
                primaryContainer = RosePrimary,
                onPrimaryContainer = Color.White,
                secondary = RoseSecondary,
                tertiary = RoseTertiary,
                background = RoseBackgroundDark,
                surface = RoseSurfaceDark,
                onBackground = Color(0xFFFBE4E9),
                onSurface = Color(0xFFFBE4E9),
                surfaceVariant = Color(0xFF332026),
                onSurfaceVariant = Color(0xFFE0B8C2)
            )
        } else {
            lightColorScheme(
                primary = RosePrimary,
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFCE7EC),
                onPrimaryContainer = RosePrimary,
                secondary = RoseSecondary,
                tertiary = RoseTertiary,
                background = RoseBackgroundLight,
                surface = RoseSurfaceLight,
                onBackground = Color(0xFF281419),
                onSurface = Color(0xFF281419),
                surfaceVariant = Color(0xFFF3DFE3),
                onSurfaceVariant = Color(0xFF634A51)
            )
        }
        else -> { // "emerald" / Clean Minimalism default
            if (isDark) {
                darkColorScheme(
                    primary = CleanMinimalPrimaryLight,
                    onPrimary = Color.Black,
                    primaryContainer = CleanMinimalPrimaryDark,
                    onPrimaryContainer = Color(0xFFD6E8D2),
                    secondary = Color(0xFF5E8255),
                    onSecondary = Color.White,
                    tertiary = GoldAccent,
                    background = CleanMinimalBackgroundDark,
                    surface = CleanMinimalSurfaceDark,
                    onBackground = Color(0xFFECEFEA),
                    onSurface = Color(0xFFECEFEA),
                    surfaceVariant = CleanMinimalSurfaceVariantDark,
                    onSurfaceVariant = Color(0xFFB5C4B4),
                    outline = Color(0xFF2E382E),
                    outlineVariant = Color(0xFF212821)
                )
            } else {
                lightColorScheme(
                    primary = CleanMinimalPrimary,
                    onPrimary = Color.White,
                    primaryContainer = CleanMinimalPrimaryContainer,
                    onPrimaryContainer = CleanMinimalPrimaryDark,
                    secondary = CleanMinimalPrimaryDark,
                    onSecondary = Color.White,
                    tertiary = Color(0xFFC48B28),
                    background = CleanMinimalBackgroundLight,
                    surface = CleanMinimalSurfaceLight,
                    onBackground = CleanMinimalOnSurface,
                    onSurface = CleanMinimalOnSurface,
                    surfaceVariant = CleanMinimalSurfaceVariantLight,
                    onSurfaceVariant = CleanMinimalSlate500,
                    outline = CleanMinimalSlate200,
                    outlineVariant = CleanMinimalSlate100
                )
            }
        }
    }
}

fun getAppShapes(cardStyle: String): Shapes {
    return when (cardStyle) {
        "compact" -> Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp)
        )
        "simple" -> Shapes(
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(18.dp),
            large = RoundedCornerShape(24.dp)
        )
        else -> Shapes( // "rounded" Clean Minimalism
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(24.dp),
            large = RoundedCornerShape(32.dp)
        )
    }
}

@Composable
fun DhikrTheme(
    themeMode: String = "system",
    accentColor: String = "emerald",
    cardStyle: String = "rounded",
    language: String = "ar",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = getThemeColorScheme(accentColor, isDark)
    val shapes = getAppShapes(cardStyle)
    val layoutDirection = if (language == "en") LayoutDirection.Ltr else LayoutDirection.Rtl

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = shapes,
            content = content
        )
    }
}
