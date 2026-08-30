package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

data class AccentColorOption(val name: String, val hex: String)

private val accentColors = listOf(
    AccentColorOption("GREEN", "#FF2E7D32"),
    AccentColorOption("BLUE", "#FF1976D2"),
    AccentColorOption("PURPLE", "#FF7B1FA2"),
    AccentColorOption("TEAL", "#FF00796B"),
    AccentColorOption("ORANGE", "#FFF57C00"),
    AccentColorOption("PINK", "#FFC2185B"),
    AccentColorOption("RED", "#FFD32F2F"),
    AccentColorOption("INDIGO", "#FF303F9F")
)

@Composable
private fun accentColorLabel(code: String): String = stringResource(
    when (code) {
        "GREEN" -> R.string.accent_green
        "BLUE" -> R.string.accent_blue
        "PURPLE" -> R.string.accent_purple
        "TEAL" -> R.string.accent_teal
        "ORANGE" -> R.string.accent_orange
        "PINK" -> R.string.accent_pink
        "RED" -> R.string.accent_red
        else -> R.string.accent_indigo
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    onBack: () -> Unit,
    onNavigateToAppLanguage: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToSystemLogs: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences.getInstance(context) }
    val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")
    val navigationStyle by prefs.navigationStyle.collectAsState(initial = "STANDARD")
    val accentColorHex by prefs.accentColor.collectAsState(initial = "#FF2E7D32")
    val scope = rememberCoroutineScope()

    val themeOptions = listOf("SYSTEM", "LIGHT", "DARK")
    val themeLabels = listOf(stringResource(R.string.app_settings_theme_system), stringResource(R.string.app_settings_theme_light), stringResource(R.string.app_settings_theme_dark))

    val navOptions = listOf("STANDARD", "FLOATING")
    val navLabels = listOf(stringResource(R.string.app_settings_nav_standard), stringResource(R.string.app_settings_nav_floating))

    androidx.compose.runtime.key(appTheme, accentColorHex) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    ThemeToggleButton(
                        appTheme = appTheme,
                        onToggleTheme = {
                            scope.launch {
                                prefs.setAppTheme(if (appTheme == "DARK") "LIGHT" else "DARK")
                            }
                        }
                    )
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_settings_look_feel),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column {
                    // App Theme
                    themeOptions.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { scope.launch { prefs.setAppTheme(option) } }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = appTheme == option,
                                onClick = { scope.launch { prefs.setAppTheme(option) } },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = themeLabels[index],
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (index < themeOptions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    // Accent Color
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                        Text(
                            text = stringResource(R.string.app_settings_accent_color),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            accentColors.forEach { colorOption ->
                                val isSelected = accentColorHex == colorOption.hex
                                val colorVal = try {
                                    Color(android.graphics.Color.parseColor(colorOption.hex))
                                } catch (_: Exception) { Color(0xFF2E7D32) }
                                Column(
                                    modifier = Modifier
                                        .clickable { scope.launch { prefs.setAccentColor(colorOption.hex) } },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(colorVal)
                                            .then(
                                                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                                else Modifier.border(2.dp, Color.Transparent, CircleShape)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = accentColorLabel(colorOption.name),
                                        fontSize = 10.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_settings_navigation),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column {
                    navOptions.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { scope.launch { prefs.setNavigationStyle(option) } }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = navigationStyle == option,
                                onClick = { scope.launch { prefs.setNavigationStyle(option) } },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = navLabels[index],
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (index < navOptions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_settings_other),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column {
                    SettingItem(
                        title = stringResource(R.string.app_settings_app_language),
                        subtitle = stringResource(R.string.app_settings_app_language_sub),
                        icon = Icons.Default.Translate,
                        onClick = onNavigateToAppLanguage
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    SettingItem(
                        title = stringResource(R.string.logs_settings_title),
                        subtitle = stringResource(R.string.logs_settings_sub),
                        icon = Icons.Default.BugReport,
                        onClick = onNavigateToSystemLogs
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    SettingItem(
                        title = stringResource(R.string.app_settings_about),
                        subtitle = stringResource(R.string.app_settings_about_sub),
                        icon = Icons.Default.Info,
                        onClick = onNavigateToAbout
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
    }
}
