package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuiltInDhikr
import com.example.data.model.CustomDhikr
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAdhkarScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val customList by viewModel.customAdhkar.collectAsState()
    val prefs by viewModel.preferences.collectAsState()
    val isArabic = prefs.language == "ar"

    var showAddDialog by remember { mutableStateOf(false) }
    var dhikrToEdit by remember { mutableStateOf<CustomDhikr?>(null) }
    var dhikrToDelete by remember { mutableStateOf<CustomDhikr?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "أذكاري المخصصة" else "Custom Adhkar",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("custom_adhkar_back_btn")
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_custom_dhikr_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (isArabic) "إضافة ذكر" else "Add Dhikr"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (customList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "✨",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (isArabic) "لم تقم بإضافة أذكار مخصصة بعد" else "No custom adhkar added yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isArabic)
                            "اضغط على زر الإضافة (+) بالأسفل لإضافة أدعيتك وأذكارك الخاصة وتحديد عدد تكرارها"
                        else
                            "Tap the (+) button below to add your own personal duas and prayers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
            ) {
                items(customList, key = { it.id }) { item ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_dhikr_item_${item.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = if (isArabic) "${item.targetCount} تكرار" else "${item.targetCount}x",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = { dhikrToEdit = item },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = if (isArabic) "تعديل" else "Edit",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { dhikrToDelete = item },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = if (isArabic) "حذف" else "Delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = item.content,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 26.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    val converted = BuiltInDhikr(
                                        id = "custom_${item.id}",
                                        title = item.title,
                                        titleEn = item.title,
                                        content = item.content,
                                        count = item.targetCount,
                                        categoryId = "custom",
                                        categoryNameAr = item.category,
                                        categoryNameEn = "Custom",
                                        virtue = "ذكر خاص بك",
                                        source = ""
                                    )
                                    viewModel.openDhikr(converted)
                                },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isArabic) "ابدأ الذكر" else "Start Dhikr")
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Dialog
    if (showAddDialog || dhikrToEdit != null) {
        val editing = dhikrToEdit
        var title by remember { mutableStateOf(editing?.title ?: "") }
        var content by remember { mutableStateOf(editing?.content ?: "") }
        var countText by remember { mutableStateOf((editing?.targetCount ?: 33).toString()) }
        var category by remember { mutableStateOf(editing?.category ?: if (isArabic) "أذكار مخصصة" else "Custom") }

        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                dhikrToEdit = null
            },
            title = {
                Text(
                    text = if (editing != null) {
                        if (isArabic) "تعديل الذكر" else "Edit Dhikr"
                    } else {
                        if (isArabic) "إضافة ذكر جديد" else "Add New Dhikr"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(if (isArabic) "عنوان الذكر" else "Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_title_input")
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text(if (isArabic) "نص الذكر أو الدعاء" else "Dhikr / Dua Text") },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_content_input")
                    )

                    OutlinedTextField(
                        value = countText,
                        onValueChange = { countText = it.filter { ch -> ch.isDigit() } },
                        label = { Text(if (isArabic) "عدد التكرار (الهدف)" else "Target Count") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("custom_dhikr_count_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val count = countText.toIntOrNull()?.coerceAtLeast(1) ?: 1
                        if (editing != null) {
                            viewModel.updateCustomDhikr(
                                editing.copy(
                                    title = title,
                                    content = content,
                                    targetCount = count,
                                    category = category
                                )
                            )
                        } else {
                            viewModel.addCustomDhikr(
                                title = title.ifEmpty { if (isArabic) "ذكر مخصص" else "Custom Dhikr" },
                                content = content,
                                targetCount = count,
                                category = category,
                                icon = "✨"
                            )
                        }
                        showAddDialog = false
                        dhikrToEdit = null
                    },
                    enabled = content.isNotBlank(),
                    modifier = Modifier.testTag("save_custom_dhikr_btn")
                ) {
                    Text(if (isArabic) "حفظ" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    dhikrToEdit = null
                }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (dhikrToDelete != null) {
        val item = dhikrToDelete!!
        AlertDialog(
            onDismissRequest = { dhikrToDelete = null },
            title = {
                Text(
                    text = if (isArabic) "تأكيد الحذف" else "Confirm Delete",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isArabic)
                        "هل أنت متأكد من حذف \"${item.title}\"؟"
                    else
                        "Are you sure you want to delete \"${item.title}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCustomDhikr(item)
                        dhikrToDelete = null
                    }
                ) {
                    Text(
                        text = if (isArabic) "حذف" else "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { dhikrToDelete = null }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }
}
