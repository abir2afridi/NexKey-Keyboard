package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetupStepCard(
    stepNumber: String,
    title: String,
    description: String,
    isCompleted: Boolean,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (isCompleted) Color(0xFFF1F8E9) else Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(20.dp),
        border = if (isCompleted) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) Color(0xFF4CAF50) else Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber,
                            color = if (isCompleted) Color.White else Color(0xFF757575),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = title,
                        color = Color(0xFF202124),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = Color(0xFF5F6368),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFF202124),
                    contentColor = if (isCompleted) Color(0xFF2E7D32) else Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = buttonText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun PhoneticRow(latin: String, bangla: String, meaning: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = latin, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(text = "➜", color = Color(0xFF9E9E9E), fontSize = 12.sp)
        Text(text = bangla, color = Color(0xFF202124), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "($meaning)", color = Color(0xFF757575), fontSize = 12.sp)
    }
}

@Composable
fun FeatureBadge(
    icon: ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Color(0xFF202124), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = desc, color = Color(0xFF5F6368), fontSize = 12.sp)
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F3F4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF5F6368),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color(0xFF202124),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                subtitle?.let {
                    Text(
                        text = it,
                        color = Color(0xFF5F6368),
                        fontSize = 13.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFBDC1C6),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun RidmikMenuIcon(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F3F4))
                .shadow(elevation = 1.dp, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF202124),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            color = Color(0xFF3C4043),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun GradientFeatureBanner(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF202124),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Color(0xFF202124),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = Color(0xFF202124).copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF202124).copy(alpha = 0.2f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SettingSliderItem(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, color = Color(0xFF202124), fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(text = value.toInt().toString(), color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF2E7D32),
                activeTrackColor = Color(0xFF2E7D32),
                inactiveTrackColor = Color(0xFFF1F3F4)
            )
        )
    }
}

@Composable
fun SettingSwitchItem(title: String, subtitle: String?, icon: ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F3F4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF5F6368), modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color(0xFF202124), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                subtitle?.let { Text(text = it, color = Color(0xFF5F6368), fontSize = 13.sp) }
            }
        }
        Switch(
            checked = checked, 
            onCheckedChange = onCheckedChange, 
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White, 
                checkedTrackColor = Color(0xFF4CAF50), 
                uncheckedThumbColor = Color.White, 
                uncheckedTrackColor = Color(0xFFBDC1C6)
            )
        )
    }
}

@Composable
fun ScenicHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF003D33), Color(0xFF004D40), Color(0xFF2E7D32))
                )
            )
    ) {
        // Sun with Glow
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .align(Alignment.TopCenter)
                .size(160.dp)
                .background(
                    Brush.radialGradient(
                        0f to Color(0xFFFFEE58).copy(alpha = 0.4f),
                        0.5f to Color(0xFFFFEE58).copy(alpha = 0.1f),
                        1f to Color.Transparent
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFEE58), Color(0xFFFBC02D))
                        )
                    )
            )
        }

        // Layered Mountains (High Fidelity)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Clouds (Subtle texture)
            drawCircle(Color.White.copy(alpha = 0.05f), radius = 60f, center = androidx.compose.ui.geometry.Offset(width * 0.2f, height * 0.2f))
            drawCircle(Color.White.copy(alpha = 0.05f), radius = 80f, center = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.22f))
            drawCircle(Color.White.copy(alpha = 0.05f), radius = 50f, center = androidx.compose.ui.geometry.Offset(width * 0.8f, height * 0.15f))

            // Far Mountain Range
            val path1 = Path().apply {
                moveTo(0f, height * 0.7f)
                lineTo(width * 0.15f, height * 0.55f)
                lineTo(width * 0.3f, height * 0.65f)
                lineTo(width * 0.5f, height * 0.48f)
                lineTo(width * 0.75f, height * 0.62f)
                lineTo(width * 0.9f, height * 0.52f)
                lineTo(width, height * 0.75f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path1, Color(0xFFE53935))

            // Near Mountain Range
            val path2 = Path().apply {
                moveTo(0f, height * 0.85f)
                lineTo(width * 0.25f, height * 0.65f)
                lineTo(width * 0.45f, height * 0.82f)
                lineTo(width * 0.65f, height * 0.58f)
                lineTo(width * 0.85f, height * 0.78f)
                lineTo(width, height * 0.7f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path2, Color(0xFFC62828))
        }
    }
}
