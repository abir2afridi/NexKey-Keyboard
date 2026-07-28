package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToPreferences: () -> Unit,
    onNavigateToAppearance: () -> Unit,
    onNavigateToTextCorrection: () -> Unit,
    onNavigateToMoreLanguages: () -> Unit,
    onNavigateToAdvanced: () -> Unit,
    onNavigateToGifQuality: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToAppLanguage: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Keyboard Settings",
                color = Color(0xFF2E7D32),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
            )

            SettingItem(
                title = "Preferences",
                subtitle = "Feedback, clipboard, and gestures",
                icon = Icons.Default.Tune,
                onClick = onNavigateToPreferences
            )

            SettingItem(
                title = "Appearance & Layouts",
                subtitle = "Themes, height, and resizing",
                icon = Icons.Default.Palette,
                onClick = onNavigateToAppearance
            )

            SettingItem(
                title = "Text correction",
                subtitle = "Suggestions and dictionaries",
                icon = Icons.Default.Spellcheck,
                onClick = onNavigateToTextCorrection
            )

            SettingItem(
                title = "More Languages",
                subtitle = "Arabic, Chakma, Syloti...",
                icon = Icons.Default.Language,
                onClick = onNavigateToMoreLanguages
            )

            SettingItem(
                title = "Advanced",
                subtitle = "Delays and typing engine",
                icon = Icons.Default.SettingsInputComponent,
                onClick = onNavigateToAdvanced
            )

            SettingItem(
                title = "Gif Quality",
                subtitle = "Manage data usage for Gifs",
                icon = Icons.Default.Gif,
                onClick = onNavigateToGifQuality
            )
            
            SettingItem(
                title = "Release notes",
                subtitle = "What's new in NexKey",
                icon = Icons.Default.Description,
                onClick = { /* Navigate to Release Notes */ }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F3F4))

            Text(
                text = "Application",
                color = Color(0xFF2E7D32),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
            )

            SettingItem(
                title = "App Language",
                subtitle = "Change between Bangla and English",
                icon = Icons.Default.Translate,
                onClick = onNavigateToAppLanguage
            )

            SettingItem(
                title = "About",
                subtitle = "About NexKey Keyboard",
                icon = Icons.Default.Info,
                onClick = onNavigateToAbout
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


