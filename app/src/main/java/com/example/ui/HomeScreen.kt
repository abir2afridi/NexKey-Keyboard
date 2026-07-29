package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences

@Composable
fun HomeScreen(
    appTheme: String,
    onToggleTheme: () -> Unit,
    onNavigateToSetup: () -> Unit,
    onNavigateToSandbox: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToClipboard: () -> Unit = {},
    onNavigateToTextCorrection: () -> Unit = {},
    onNavigateToMoreLanguages: () -> Unit = {},
    onNavigateToGifQuality: () -> Unit = {},
    onNavigateToTypingAnalysis: () -> Unit = {}
) {
    val context = LocalContext.current
    val isEnabled = checkIsKeyboardEnabled(context)
    val isSelected = checkIsKeyboardSelected(context)
    val isActive = isEnabled && isSelected

    val prefs = remember { UserPreferences(context) }
    val totalWords by prefs.totalWords.collectAsState(initial = 0)
    val totalChars by prefs.totalChars.collectAsState(initial = 0)
    val timeSaved = (totalChars / 5) * 0.5

    // Infinite breathing pulsing animation for Active status dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fixed Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.icon_header),
                        contentDescription = "NexKey",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "NexKey",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "Vibrant. Fast. Original.",
                            fontSize = 11.sp,
                            color = Color(0xFF1B5E20).copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = when (appTheme) {
                        "DARK" -> Icons.Default.DarkMode
                        "LIGHT" -> Icons.Default.LightMode
                        else -> Icons.Default.BrightnessAuto
                    },
                    contentDescription = "Toggle theme",
                    tint = Color(0xFF1B5E20),
                    modifier = Modifier.size(20.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isActive) Color(0xFF1B5E20) else Color(0xFFFF9800),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .alpha(if (isActive) pulseAlpha else 1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isActive) "ACTIVE" else "SETUP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Primary Action
            Card(
                onClick = { if (isActive) onNavigateToSandbox() else onNavigateToSetup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                if (isActive) listOf(Color(0xFF1B5E20), Color(0xFF1B5E20).copy(alpha = 0.8f))
                                else listOf(Color(0xFFFF9800), Color(0xFFE65100))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isActive) Icons.Default.PlayArrow else Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isActive) "Typing Sandbox" else "Complete Setup",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Typing Summary Cards
            Text(
                text = "TYPING SUMMARY",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            val avgRpm = if (timeSaved > 0) ((totalWords.toFloat() / (timeSaved / 60f))).toInt() else 0

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(totalWords.toString(), "Words", Color(0xFFC8E6C9), Color(0xFF1B5E20), Icons.Default.TextFields, Modifier.weight(1f))
                StatCard("$avgRpm", "Avg RPM", Color(0xFFFFF3E0), Color(0xFFF57C00), Icons.Default.Speed, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(totalChars.toString(), "Characters", Color(0xFFE3F2FD), Color(0xFF1565C0), Icons.Default.Keyboard, Modifier.weight(1f))
                StatCard("${timeSaved}s", "Time Saved", Color(0xFFF3E5F5), Color(0xFF7B1FA2), Icons.Default.Timer, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // More Details Button
            Surface(
                onClick = onNavigateToTypingAnalysis,
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1B5E20),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("More Details", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NexKey Pro • Stable v1.0.0",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20).copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, bgColor: Color, iconTint: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = modifier) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(bgColor), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


