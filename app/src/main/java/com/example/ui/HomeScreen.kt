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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToThemes: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToSetup: () -> Unit,
    onNavigateToSandbox: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    val context = LocalContext.current
    val isEnabled = checkIsKeyboardEnabled(context)
    val isSelected = checkIsKeyboardSelected(context)
    val isActive = isEnabled && isSelected

    val prefs = remember { UserPreferences(context) }
    val totalWords by prefs.totalWords.collectAsState(initial = 0)
    val totalChars by prefs.totalChars.collectAsState(initial = 0)
    val timeSaved = (totalChars / 5) * 0.5 // Estimated seconds saved

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Brand & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NexKey",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF202124)
                )
                
                Surface(
                    shape = CircleShape,
                    color = if (isActive) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isActive) Color(0xFF2E7D32) else Color(0xFFFB8C00))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isActive) "Active" else "Inactive",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) Color(0xFF2E7D32) else Color(0xFFFB8C00)
                        )
                    }
                }
            }

            Text(
                text = "Minimal, fast, original.",
                fontSize = 16.sp,
                color = Color(0xFF5F6368),
                modifier = Modifier.padding(top = 4.dp)
            )

            MinimalDivider()

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                MinimalStatItem(
                    value = totalWords.toString(),
                    label = "Words typed",
                    modifier = Modifier.weight(1f)
                )
                MinimalStatItem(
                    value = "${timeSaved.toInt()}s",
                    label = "Time saved",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Primary Action
            Button(
                onClick = { if (isActive) onNavigateToSandbox() else onNavigateToSetup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF202124),
                    contentColor = Color.White
                ),
                elevation = null
            ) {
                Text(
                    text = if (isActive) "Try Sandbox" else "Finish Setup",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Tools Section
            Text(
                text = "Tools",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9AA0A6),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MinimalGridItem(icon = Icons.Default.Palette, label = "Themes", onClick = onNavigateToThemes, modifier = Modifier.weight(1f))
                    MinimalGridItem(icon = Icons.Default.Settings, label = "Settings", onClick = onNavigateToSettings, modifier = Modifier.weight(1f))
                    MinimalGridItem(icon = Icons.Default.BarChart, label = "Analytics", onClick = onNavigateToStats, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    MinimalGridItem(icon = Icons.Default.AutoStories, label = "Tutorial", onClick = onNavigateToHelp, modifier = Modifier.weight(1f))
                    MinimalGridItem(icon = Icons.Default.SmartToy, label = "AI Assist", onClick = {}, modifier = Modifier.weight(1f))
                    MinimalGridItem(icon = Icons.Default.History, label = "History", onClick = {}, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NexKey Pro v1.0.0",
                    fontSize = 12.sp,
                    color = Color(0xFFBDC1C6)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
