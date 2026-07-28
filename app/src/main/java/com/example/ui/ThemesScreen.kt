package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemesScreen(
    onBack: () -> Unit
) {
    val themes = remember {
        listOf(
            KeyboardTheme.DarkNeon,
            KeyboardTheme.LightMinimal,
            KeyboardTheme.AmoledBlack,
            KeyboardTheme.EmeraldGreen
        )
    }
    
    var selectedTheme by remember { mutableStateOf(KeyboardTheme.DarkNeon) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keyboard Themes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF202124),
                    navigationIconContentColor = Color(0xFF202124)
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Select a preset for your keyboard",
                color = Color(0xFF5F6368),
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(themes) { theme ->
                    ThemePreviewCard(
                        theme = theme,
                        isSelected = selectedTheme.preset == theme.preset,
                        onClick = { selectedTheme = theme }
                    )
                }
            }
        }
    }
}

@Composable
fun ThemePreviewCard(
    theme: KeyboardTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF8F9FA))
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF2E7D32) else Color(0xFFF1F3F4),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        // Mini Keyboard Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(theme.backgroundColor)
                .padding(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(if (row == 2) 6 else 8) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(theme.keyBackgroundColor)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = theme.preset.name.replace("_", " ").lowercase().capitalize(),
            color = Color(0xFF202124),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp)
        )
    }
}

// Extension to avoid compilation error if capitalize() is deprecated in some Kotlin versions
private fun String.capitalize() = this.replaceFirstChar { it.uppercase() }
