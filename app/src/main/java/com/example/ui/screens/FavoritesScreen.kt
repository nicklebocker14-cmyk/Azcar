package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuiltInAdhkarData
import com.example.data.model.BuiltInDhikr
import com.example.ui.components.DhikrCard
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favoriteItems.collectAsState()
    val prefs by viewModel.preferences.collectAsState()
    val isArabic = prefs.language == "ar"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "الأذكار المفضلة" else "Favorite Adhkar",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("favorites_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isArabic) "رجوع" else "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isArabic) "قائمة المفضلة فارغة" else "No favorites added yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isArabic)
                            "اضغط على رمز النجمة ⭐ بجانب أي ذكر لحفظه هنا والوصول إليه بسرعة"
                        else
                            "Tap the star ⭐ icon on any dhikr to save it here for quick access",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp)
            ) {
                items(favorites, key = { it.dhikrKey }) { item ->
                    val foundBuiltIn = BuiltInAdhkarData.adhkarList.find { it.id == item.dhikrKey }
                    val displayDhikr = foundBuiltIn ?: BuiltInDhikr(
                        id = item.dhikrKey,
                        title = item.title,
                        titleEn = item.title,
                        content = item.content,
                        count = item.targetCount,
                        categoryId = "favorite",
                        categoryNameAr = item.category,
                        categoryNameEn = "Favorite",
                        virtue = "",
                        source = item.source
                    )

                    DhikrCard(
                        dhikr = displayDhikr,
                        isFavorite = true,
                        isArabic = isArabic,
                        onStartClick = { viewModel.openDhikr(displayDhikr) },
                        onToggleFavorite = { viewModel.removeFavoriteByKey(item.dhikrKey) }
                    )
                }
            }
        }
    }
}
