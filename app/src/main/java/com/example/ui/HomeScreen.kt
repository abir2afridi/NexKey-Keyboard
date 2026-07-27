package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ScenicHeader()

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(200.dp)) // Higher offset for scenic header visibility

                // Floating Status Card (Exact matching Ridmik style)
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E1)), // Cream background
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NexKey Keyboard",
                                color = Color(0xFF202124),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                        .background(if (isActive) Color(0xFF4CAF50) else Color(0xFFFBC02D))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isActive) "Active" else "Inactive",
                                    color = Color(0xFF5F6368),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        if (!isActive) {
                            Text(
                                text = "NexKey Keyboard is not active!",
                                color = Color(0xFFD93025), // Precision red
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Press the button below to activate the keyboard",
                                color = Color(0xFF5F6368),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        } else {
                            Text(
                                text = "NexKey Keyboard is active!",
                                color = Color(0xFF1E8E3E), // Precision green
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Your keyboard is ready to use",
                                color = Color(0xFF5F6368),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { if (!isActive) onNavigateToSetup() else onNavigateToSandbox() },
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF202124)), // Dark grey button
                            shape = RoundedCornerShape(26.dp)
                        ) {
                            Text(
                                text = if (!isActive) "Activate" else "Test Now",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Circular Menu Grid (Matching proportions)
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RidmikMenuIcon(icon = Icons.Default.Storefront, label = "Shop", onClick = {}, modifier = Modifier.weight(1f))
                        RidmikMenuIcon(icon = Icons.Default.Palette, label = "Theme", onClick = onNavigateToThemes, modifier = Modifier.weight(1f))
                        RidmikMenuIcon(icon = Icons.Default.Tune, label = "Customize", onClick = {}, modifier = Modifier.weight(1f))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RidmikMenuIcon(icon = Icons.Default.Settings, label = "Settings", onClick = onNavigateToSettings, modifier = Modifier.weight(1f))
                        RidmikMenuIcon(icon = Icons.Default.ContentPaste, label = "Clipboard", onClick = {}, modifier = Modifier.weight(1f))
                        RidmikMenuIcon(icon = Icons.Default.BarChart, label = "Stats", onClick = onNavigateToStats, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Gradient Feature Banners (Matching gradients from screenshot)
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GradientFeatureBanner(
                        title = "AI Assist",
                        subtitle = "Make your writing smarter now",
                        icon = Icons.Default.AutoAwesome,
                        gradient = listOf(Color(0xFFE8EAF6), Color(0xFFEDE7F6)), // Light Purple gradient
                        onClick = {}
                    )
                    GradientFeatureBanner(
                        title = "Learn to Type",
                        subtitle = "Learn Bangla with Phonetic & more",
                        icon = Icons.Default.Edit,
                        gradient = listOf(Color(0xFFF1F3F4), Color(0xFFF8F9FA)), // Light Grey gradient
                        onClick = onNavigateToHelp
                    )
                    GradientFeatureBanner(
                        title = "Create Theme",
                        subtitle = "Create your own theme",
                        icon = Icons.Default.Style,
                        gradient = listOf(Color(0xFFE0F2F1), Color(0xFFE8F5E9)), // Mint gradient
                        onClick = {}
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Branding Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2E7D32)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Keyboard, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "NexKey Keyboard", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "1.0.0 (Release)", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
