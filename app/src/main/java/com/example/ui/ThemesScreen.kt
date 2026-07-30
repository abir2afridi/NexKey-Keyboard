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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemesScreen(
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { com.example.data.UserPreferences(context) }
    val scope = rememberCoroutineScope()
    val savedThemePresetName by prefs.theme.collectAsState(initial = ThemePreset.DARK_NEON.name)

    // Custom Theme State
    val customBg by prefs.customBgColor.collectAsState(initial = "#FF12131C")
    val customKeyBg by prefs.customKeyBgColor.collectAsState(initial = "#FF1E2136")
    val customKeySpec by prefs.customKeySpecialColor.collectAsState(initial = "#FF2A2E4B")
    val customKeyText by prefs.customKeyTextColor.collectAsState(initial = "#FFF1F3FB")
    val customKeySpecText by prefs.customKeySpecialTextColor.collectAsState(initial = "#FF80D8FF")
    val customAccent by prefs.customAccentColor.collectAsState(initial = "#FF00E5FF")
    val customSugBg by prefs.customSuggestionBgColor.collectAsState(initial = "#FF1A1C29")
    val customSugText by prefs.customSuggestionTextColor.collectAsState(initial = "#FFF1F3FB")
    val customPopBg by prefs.customPopupBgColor.collectAsState(initial = "#FF2A2E4B")
    val customPopText by prefs.customPopupTextColor.collectAsState(initial = "#FF00E5FF")
    val customKeyHint by prefs.customKeyHintColor.collectAsState(initial = "#66F1F3FB")

    val customTheme = remember(customBg, customKeyBg, customKeySpec, customKeyText, customKeySpecText, customAccent, customSugBg, customSugText, customPopBg, customPopText, customKeyHint) {
        KeyboardTheme(
            preset = ThemePreset.CUSTOM,
            backgroundColor = Color(android.graphics.Color.parseColor(customBg)),
            keyBackgroundColor = Color(android.graphics.Color.parseColor(customKeyBg)),
            keySpecialColor = Color(android.graphics.Color.parseColor(customKeySpec)),
            keyTextColor = Color(android.graphics.Color.parseColor(customKeyText)),
            keySpecialTextColor = Color(android.graphics.Color.parseColor(customKeySpecText)),
            accentColor = Color(android.graphics.Color.parseColor(customAccent)),
            suggestionBgColor = Color(android.graphics.Color.parseColor(customSugBg)),
            suggestionTextColor = Color(android.graphics.Color.parseColor(customSugText)),
            popupBackgroundColor = Color(android.graphics.Color.parseColor(customPopBg)),
            popupTextColor = Color(android.graphics.Color.parseColor(customPopText)),
            keyHintColor = Color(android.graphics.Color.parseColor(customKeyHint))
        )
    }

    val themes = remember(customTheme) { listOf(customTheme) + KeyboardTheme.allThemes() }
    
    val selectedTheme = remember(savedThemePresetName) {
        try {
            KeyboardTheme.fromPreset(ThemePreset.valueOf(savedThemePresetName))
        } catch (e: Exception) {
            KeyboardTheme.DarkNeon
        }
    }

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
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Select a preset for your keyboard",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(themes) { theme ->
                    ThemePreviewCard(
                        theme = theme,
                        isSelected = selectedTheme.preset == theme.preset,
                        onClick = {
                            scope.launch {
                                prefs.setTheme(theme.preset)
                            }
                        }
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
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        // Mini Keyboard Preview Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(theme.backgroundColor)
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Simulated Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(theme.suggestionBgColor.copy(alpha = 0.5f)),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(6.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(theme.suggestionTextColor.copy(alpha = 0.4f))
                    )
                }
            }

            // Keyboard Rows
            // Row 1
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(10) {
                    MiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor)
                }
            }
            // Row 2
            Row(
                modifier = Modifier.padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                repeat(9) {
                    MiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor)
                }
            }
            // Row 3
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                MiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, weight = 1.5f) // Shift
                repeat(7) {
                    MiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor)
                }
                MiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, weight = 1.5f) // Backspace
            }
            // Row 4
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                MiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, weight = 1.2f) // Mode
                MiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, weight = 1.2f) // Emoji
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(theme.accentColor)
                )
                MiniKey(theme.keySpecialColor, theme.accentColor, theme.keyHintColor, weight = 1.6f) // Enter
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = theme.preset.name.replace("_", " ").lowercase().capitalize(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun RowScope.MiniKey(
    bgColor: Color,
    textColor: Color,
    hintColor: Color,
    weight: Float = 1f
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(16.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        // Main text dot
        Box(
            modifier = Modifier
                .size(2.dp)
                .clip(RoundedCornerShape(0.5.dp))
                .background(textColor.copy(alpha = 0.6f))
        )
        
        // Hint text dot
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 1.5.dp, end = 2.dp)
                .size(1.2.dp)
                .clip(RoundedCornerShape(0.3.dp))
                .background(hintColor.copy(alpha = 0.8f))
        )
    }
}

// Extension to avoid compilation error if capitalize() is deprecated in some Kotlin versions
private fun String.capitalize() = this.replaceFirstChar { it.uppercase() }
