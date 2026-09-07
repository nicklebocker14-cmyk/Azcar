package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomBar
import com.example.ui.screens.*
import com.example.ui.theme.DhikrTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun DhikrApp(
    viewModel: MainViewModel = viewModel()
) {
    val prefs by viewModel.preferences.collectAsState()
    val currentDestination by viewModel.currentDestination.collectAsState()
    val isArabic = prefs.language == "ar"

    // Intercept back button when not on Home
    BackHandler(enabled = currentDestination !is ScreenDestination.Home) {
        viewModel.navigateBack()
    }

    val showBottomBar = when (currentDestination) {
        is ScreenDestination.Home,
        is ScreenDestination.Adhkar,
        is ScreenDestination.Tasbeeh,
        is ScreenDestination.Qibla,
        is ScreenDestination.Settings -> true
        else -> false
    }

    DhikrTheme(
        themeMode = prefs.themeMode,
        accentColor = prefs.accentColor,
        cardStyle = prefs.cardStyle,
        language = prefs.language
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    AppBottomBar(
                        currentDestination = currentDestination,
                        isArabic = isArabic,
                        onNavigate = { dest -> viewModel.navigateTo(dest) }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { targetScreen ->
                    when (targetScreen) {
                        is ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                        is ScreenDestination.Adhkar -> AdhkarCategoriesScreen(viewModel = viewModel)
                        is ScreenDestination.CategoryDetail -> CategoryDetailScreen(
                            categoryId = targetScreen.categoryId,
                            viewModel = viewModel
                        )
                        is ScreenDestination.DhikrPlayer -> DhikrPlayerScreen(viewModel = viewModel)
                        is ScreenDestination.Tasbeeh -> TasbeehScreen(viewModel = viewModel)
                        is ScreenDestination.Qibla -> QiblaScreen(viewModel = viewModel)
                        is ScreenDestination.Favorites -> FavoritesScreen(viewModel = viewModel)
                        is ScreenDestination.CustomAdhkar -> CustomAdhkarScreen(viewModel = viewModel)
                        is ScreenDestination.Settings -> SettingsScreen(viewModel = viewModel)
                        is ScreenDestination.Search -> SearchScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
