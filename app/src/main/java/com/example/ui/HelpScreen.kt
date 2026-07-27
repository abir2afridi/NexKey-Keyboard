package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Guide") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ridmik-Class Phonetic Cheat Sheet",
                color = Color(0xFF00E5FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1C28)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PhoneticRow("ami", "আমি", "I / Me")
                    PhoneticRow("bangla", "বাংলা", "Bengali")
                    PhoneticRow("amar", "আমার", "My / Mine")
                    PhoneticRow("shonar", "সোনার", "Golden")
                    PhoneticRow("kormo", "কর্ম", "Work")
                    PhoneticRow("kS", "ক্ষ", "Juktakkhor")
                    PhoneticRow("rri", "ঋ", "Vowel")
                    PhoneticRow("NG", "ঙ", "Consonant")
                    PhoneticRow("..", "।", "Dari")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tips",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            TipCard(
                title = "Conjuncts (যুক্তাক্ষর)",
                description = "Type two letters and NexKey will automatically join them if valid. E.g. 'kS' for 'ক্ষ'."
            )

            TipCard(
                title = "Switching Modes",
                description = "Tap the globe icon or 'ABC/?123' key on the keyboard to switch between Bangla and English."
            )

            TipCard(
                title = "Voice Typing",
                description = "Tap the microphone icon on the toolbar to speak instead of typing."
            )
        }
    }
}

@Composable
fun TipCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1C28)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

// I need to import or move PhoneticRow from MainActivity. 
// I'll move it to Components.kt for better reuse.
