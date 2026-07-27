package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NexKey Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F1017),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0F1017)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "General",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "Languages",
                subtitle = "Bangla Phonetic, Jatiyo, English",
                icon = Icons.Default.Language,
                onClick = {}
            )

            HorizontalDivider(color = Color(0xFF2D314E), thickness = 0.5.dp)

            SettingItem(
                title = "Keyboard Preferences",
                subtitle = "Vibration, Sound, Key heights",
                icon = Icons.Default.Settings,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Intelligence",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "AI Assist",
                subtitle = "Rewrite, Fix Grammar, Professional tone",
                icon = Icons.Default.AutoAwesome,
                onClick = {}
            )

            HorizontalDivider(color = Color(0xFF2D314E), thickness = 0.5.dp)

            SettingItem(
                title = "Predictive Text",
                subtitle = "Learning from your typing style",
                icon = Icons.Default.QueryStats,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Privacy & Safety",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "Incognito Mode",
                subtitle = "Do not learn words or save history",
                icon = Icons.Default.Security,
                onClick = {}
            )

            HorizontalDivider(color = Color(0xFF2D314E), thickness = 0.5.dp)

            SettingItem(
                title = "Clipboard Settings",
                subtitle = "Pinned items and history cleanup",
                icon = Icons.Default.ContentPaste,
                onClick = {}
            )
        }
    }
}
