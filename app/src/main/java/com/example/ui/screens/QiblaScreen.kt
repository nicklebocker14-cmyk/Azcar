package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val compassState by viewModel.compassState.collectAsState()
    val qiblaBearing by viewModel.qiblaBearing.collectAsState()
    val distanceKm by viewModel.distanceToMakkahKm.collectAsState()
    val prefs by viewModel.preferences.collectAsState()
    val isArabic = prefs.language == "ar"

    var showCalibrationDialog by remember { mutableStateOf(false) }

    // Calculate difference between compass azimuth and Qibla bearing
    val azimuth = compassState.azimuth
    var diff = (qiblaBearing.toFloat() - azimuth + 360f) % 360f
    if (diff > 180f) diff -= 360f

    val isAligned = abs(diff) <= 3.5f

    val animatedRotation by animateFloatAsState(
        targetValue = -azimuth,
        label = "compass_rotation"
    )

    val dialBorderColor by animateColorAsState(
        targetValue = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "dial_color"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "اتجاه القبلة" else "Qibla Direction",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showCalibrationDialog = true },
                        modifier = Modifier.testTag("qibla_info_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = if (isArabic) "تعليمات المعايرة" else "Calibration Guide"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Status / Alignment Banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = if (isAligned) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAligned) Icons.Default.CheckCircle else Icons.Default.Navigation,
                            contentDescription = null,
                            tint = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAligned) {
                                if (isArabic) "أنت باتجاه القبلة تمامًا 🕋" else "Facing Qibla directly 🕋"
                            } else {
                                if (isArabic) "حرّك هاتفك حتى يتطابق السهم مع الكعبة" else "Rotate phone until needle aligns with Kaaba"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isAligned) FontWeight.Bold else FontWeight.Medium,
                            color = if (isAligned) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Compass Visualizer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .border(width = 3.dp, color = dialBorderColor, shape = CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .testTag("qibla_compass_dial")
            ) {
                // Rotating dial with cardinal points
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedRotation)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.width / 2f - 18.dp.toPx()

                        // Draw tick marks
                        for (i in 0 until 360 step 15) {
                            val angleRad = Math.toRadians(i.toDouble() - 90.0)
                            val isMajor = i % 90 == 0
                            val tickLen = if (isMajor) 14.dp.toPx() else 7.dp.toPx()
                            val startX = center.x + (radius - tickLen) * cos(angleRad).toFloat()
                            val startY = center.y + (radius - tickLen) * sin(angleRad).toFloat()
                            val endX = center.x + radius * cos(angleRad).toFloat()
                            val endY = center.y + radius * sin(angleRad).toFloat()

                            drawLine(
                                color = if (isMajor) Color.Gray else Color.LightGray,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = if (isMajor) 3f else 1.5f
                            )
                        }
                    }

                    // North Marker (ش)
                    Text(
                        text = if (isArabic) "ش (N)" else "N",
                        color = Color.Red,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 18.dp)
                    )

                    // East Marker (ق)
                    Text(
                        text = if (isArabic) "ق" else "E",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 18.dp)
                    )

                    // South Marker (ج)
                    Text(
                        text = if (isArabic) "ج" else "S",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 18.dp)
                    )

                    // West Marker (غ)
                    Text(
                        text = if (isArabic) "غ" else "W",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 18.dp)
                    )

                    // Kaaba Pointer Needle inside rotating dial
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(qiblaBearing.toFloat())
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 34.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "🕋", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }

                // Fixed Center Crosshair & Alignment Ring
                Surface(
                    shape = CircleShape,
                    color = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(24.dp)
                ) {}
            }

            // Bottom Information Cards
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isArabic) "زاوية القبلة" else "Qibla Angle",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${qiblaBearing.roundToInt()}°",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    VerticalDivider(modifier = Modifier.height(36.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isArabic) "المسافة إلى مكة" else "Distance to Makkah",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "${distanceKm.roundToInt()} كم" else "${distanceKm.roundToInt()} km",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    // Calibration Instructions Dialog
    if (showCalibrationDialog) {
        AlertDialog(
            onDismissRequest = { showCalibrationDialog = false },
            title = {
                Text(
                    text = if (isArabic) "إرشادات معايرة البوصلة" else "Compass Calibration",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isArabic)
                            "1. ضع الهاتف في وضع أفقي مستوٍ بعيدًا عن أي أجهزة إلكترونية أو أسطح معدنية."
                        else
                            "1. Keep the phone flat and away from electronic or metal objects."
                    )
                    Text(
                        text = if (isArabic)
                            "2. حرّك هاتفك في الهواء بحركة تشبه رقم 8 بالإنجليزية (∞) لمدة ثانيتين لمعايرة حساسات الهاتف."
                        else
                            "2. Wave your phone in a figure-8 motion (∞) to calibrate the compass sensor."
                    )
                    Text(
                        text = if (isArabic)
                            "3. تأكد من تفعيل خدمة الموقع (GPS) لحساب زاوية القبلة الدقيقة من موقعك الحالي."
                        else
                            "3. Make sure location service (GPS) is active for accurate true bearing."
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showCalibrationDialog = false }) {
                    Text(if (isArabic) "فهمت" else "Got it")
                }
            }
        )
    }
}
