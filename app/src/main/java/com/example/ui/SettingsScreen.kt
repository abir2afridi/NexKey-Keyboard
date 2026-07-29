package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Keyboard Settings", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp))

            Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp) {
                Column {
                    SettingItem("Typing", "Auto-cap, double-space, tab", Icons.Default.TextFormat, onClick = onNavigateToTyping)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Feedback", "Vibrations, sound, popups", Icons.Default.Vibration, onClick = onNavigateToFeedback)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Language & Keys", "Voice, emoji, globe keys", Icons.Default.Language, onClick = onNavigateToLanguageKeys)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Layout", "Number row, split, resize", Icons.Default.Keyboard, onClick = onNavigateToLayout)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Size", "Height and width adjustments", Icons.Default.AspectRatio, onClick = onNavigateToSize)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Navigation", "Cursor and volume control", Icons.Default.SwapHoriz, onClick = onNavigateToNavigation)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Paste & Clipboard", "Hold key to paste, clipboard settings", Icons.Default.ContentPaste, onClick = onNavigateToPaste)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Advanced", "Delays, cursor, behaviour", Icons.Default.Tune, onClick = onNavigateToAdvancedGroup)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Text correction", "Suggestions and dictionaries", Icons.Default.Spellcheck, onClick = onNavigateToTextCorrection)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("More Languages", "Arabic, Chakma, Syloti...", Icons.Default.Language, onClick = onNavigateToMoreLanguages)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SettingItem("Gif Quality", "Manage data usage for Gifs", Icons.Default.Gif, onClick = onNavigateToGifQuality)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
