package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun AppSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")
    val scope = rememberCoroutineScope()

    val themeOptions = listOf("SYSTEM", "LIGHT", "DARK")
    val themeLabels = listOf("System Default", "Light", "Dark")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Settings", fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "App Theme",
                color = Color(0xFF2E7D32),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column {
                    themeOptions.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = appTheme == option,
                                onClick = {
                                    scope.launch { prefs.setAppTheme(option) }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2E7D32))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = themeLabels[index],
                                fontSize = 16.sp,
                                color = Color(0xFF202124)
                            )
                        }
                        if (index < themeOptions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFF1F3F4)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Other",
                color = Color(0xFF2E7D32),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
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
                        onClick = { /* navigate */ }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF1F3F4)
                    )
                    SettingItem(
                        title = "About",
                        subtitle = "About NexKey Keyboard",
                        icon = Icons.Default.Info,
                        onClick = { /* navigate */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
