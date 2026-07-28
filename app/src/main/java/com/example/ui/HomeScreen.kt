package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences

@Composable
fun HomeScreen(
    onNavigateToThemes: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToSetup: () -> Unit,
    onNavigateToSandbox: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToPreferences: () -> Unit = {},
    onNavigateToAppearance: () -> Unit = {},
    onNavigateToTextCorrection: () -> Unit = {},
    onNavigateToMoreLanguages: () -> Unit = {},
    onNavigateToAdvanced: () -> Unit = {},
    onNavigateToGifQuality: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToAppLanguage: () -> Unit = {},
    onNavigateToAppSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val isEnabled = checkIsKeyboardEnabled(context)
    val isSelected = checkIsKeyboardSelected(context)
    val isActive = isEnabled && isSelected

    val prefs = remember { UserPreferences(context) }
    val totalWords by prefs.totalWords.collectAsState(initial = 0)
    val totalChars by prefs.totalChars.collectAsState(initial = 0)
    val timeSaved = (totalChars / 5) * 0.5

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF1F8E9), Color.White, Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // Brand & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NexKey",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "Vibrant. Fast. Original.",
                        fontSize = 13.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isActive) Color(0xFF2E7D32) else Color(0xFFFF9800),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color.White)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8F5E9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    MinimalStatItem(
                        value = totalWords.toString(),
                        label = "Words typed",
                        valueColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                    MinimalStatItem(
                        value = "${timeSaved.toInt()}s",
                        label = "Time saved",
                        valueColor = Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Action
            Button(
                onClick = { if (isActive) onNavigateToSandbox() else onNavigateToSetup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) Color(0xFF2E7D32) else Color(0xFF1B5E20),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Default.PlayArrow else Icons.Default.FlashOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isActive) "Typing Sandbox" else "Complete Setup",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Discover Tools
            Text(
                text = "DISCOVER TOOLS",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorfulGridItem(icon = Icons.Default.Palette, label = "Themes", color = Color(0xFF7B1FA2), onClick = onNavigateToThemes, modifier = Modifier.weight(1f))
                    ColorfulGridItem(icon = Icons.Default.BarChart, label = "Stats", color = Color(0xFF388E3C), onClick = onNavigateToStats, modifier = Modifier.weight(1f))
                    ColorfulGridItem(icon = Icons.Default.AutoStories, label = "Tutorial", color = Color(0xFFF57C00), onClick = onNavigateToHelp, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorfulGridItem(icon = Icons.Default.Storefront, label = "Store", color = Color(0xFF0097A7), onClick = {}, modifier = Modifier.weight(1f))
                    ColorfulGridItem(icon = Icons.Default.Settings, label = "App Settings", color = Color(0xFF1976D2), onClick = onNavigateToAppSettings, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Keyboard Settings
            Text(
                text = "KEYBOARD SETTINGS",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column {
                    SettingItem(
                        title = "Preferences",
                        subtitle = "Feedback, clipboard, and gestures",
                        icon = Icons.Default.Tune,
                        onClick = onNavigateToPreferences
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "Appearance & Layouts",
                        subtitle = "Themes, height, and resizing",
                        icon = Icons.Default.Palette,
                        onClick = onNavigateToAppearance
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "Text correction",
                        subtitle = "Suggestions and dictionaries",
                        icon = Icons.Default.Spellcheck,
                        onClick = onNavigateToTextCorrection
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "More Languages",
                        subtitle = "Arabic, Chakma, Syloti...",
                        icon = Icons.Default.Language,
                        onClick = onNavigateToMoreLanguages
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "Advanced",
                        subtitle = "Delays and typing engine",
                        icon = Icons.Default.SettingsInputComponent,
                        onClick = onNavigateToAdvanced
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "Gif Quality",
                        subtitle = "Manage data usage for Gifs",
                        icon = Icons.Default.Gif,
                        onClick = onNavigateToGifQuality
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Application Section
            Text(
                text = "APPLICATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column {
                    SettingItem(
                        title = "App Language",
                        subtitle = "Change between Bangla and English",
                        icon = Icons.Default.Translate,
                        onClick = onNavigateToAppLanguage
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                    SettingItem(
                        title = "About",
                        subtitle = "About NexKey Keyboard",
                        icon = Icons.Default.Info,
                        onClick = onNavigateToAbout
                    )
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
                    color = Color(0xFF4CAF50).copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
