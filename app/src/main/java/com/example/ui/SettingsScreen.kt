package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToTyping: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    onNavigateToLanguageKeys: () -> Unit = {},
    onNavigateToLayout: () -> Unit = {},
    onNavigateToSize: () -> Unit = {},
    onNavigateToNavigation: () -> Unit = {},
    onNavigateToPaste: () -> Unit = {},
    onNavigateToAdvancedGroup: () -> Unit = {},
    onNavigateToTextCorrection: () -> Unit = {},
    onNavigateToMoreLanguages: () -> Unit = {},
    onNavigateToGifQuality: () -> Unit = {}
) {
    val settingsItems = remember {
        listOf(
            SettingCategory("Typing", "Auto-cap, double-space, tab", Icons.Default.TextFormat, onNavigateToTyping),
            SettingCategory("Feedback", "Vibrations, sound, popups", Icons.Default.Vibration, onNavigateToFeedback),
            SettingCategory("Language & Keys", "Voice, emoji, globe keys", Icons.Default.Language, onNavigateToLanguageKeys),
            SettingCategory("Layout", "Number row, split, resize", Icons.Default.Keyboard, onNavigateToLayout),
            SettingCategory("Size", "Height and width adjustments", Icons.Default.AspectRatio, onNavigateToSize),
            SettingCategory("Navigation", "Cursor and volume control", Icons.Default.SwapHoriz, onNavigateToNavigation),
            SettingCategory("Paste & Clipboard", "Hold key to paste, clipboard settings", Icons.Default.ContentPaste, onNavigateToPaste),
            SettingCategory("Advanced", "Delays, cursor, behaviour", Icons.Default.Tune, onNavigateToAdvancedGroup),
            SettingCategory("Text correction", "Suggestions and dictionaries", Icons.Default.Spellcheck, onNavigateToTextCorrection),
            SettingCategory("More Languages", "English, Bangla, Avro, Arabic...", Icons.Default.Language, onNavigateToMoreLanguages),
            SettingCategory("Gif Quality", "Manage data usage for Gifs", Icons.Default.Gif, onNavigateToGifQuality)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keyboard Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Text(
                    text = "Keyboard Customization", 
                    color = MaterialTheme.colorScheme.primary, 
                    fontSize = 11.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }

            items(settingsItems.size) { index ->
                val item = settingsItems[index]
                SettingGridItem(
                    title = item.title,
                    subtitle = item.description,
                    icon = item.icon,
                    onClick = item.onClick
                )
            }
            
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private data class SettingCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
