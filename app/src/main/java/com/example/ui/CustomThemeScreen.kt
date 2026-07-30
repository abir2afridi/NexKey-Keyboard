package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomThemeScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

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
    val customKeyHintColor by prefs.customKeyHintColor.collectAsState(initial = "#66F1F3FB")

    // State for the editor (initialized with saved values)
    var bgColor by remember(customBg) { mutableStateOf(hexToColor(customBg)) }
    var keyBgColor by remember(customKeyBg) { mutableStateOf(hexToColor(customKeyBg)) }
    var keyTextColor by remember(customKeyText) { mutableStateOf(hexToColor(customKeyText)) }
    var keySpecialColor by remember(customKeySpec) { mutableStateOf(hexToColor(customKeySpec)) }
    var keySpecialTextColor by remember(customKeySpecText) { mutableStateOf(hexToColor(customKeySpecText)) }
    var accentColor by remember(customAccent) { mutableStateOf(hexToColor(customAccent)) }
    var suggestionBgColor by remember(customSugBg) { mutableStateOf(hexToColor(customSugBg)) }
    var suggestionTextColor by remember(customSugText) { mutableStateOf(hexToColor(customSugText)) }
    var popupBgColor by remember(customPopBg) { mutableStateOf(hexToColor(customPopBg)) }
    var popupTextColor by remember(customPopText) { mutableStateOf(hexToColor(customPopText)) }
    var keyHintColor by remember(customKeyHintColor) { mutableStateOf(hexToColor(customKeyHintColor)) }

    LaunchedEffect(Unit) {
        // Load existing custom theme if any
        // For simplicity, we just use defaults for now in this turn, 
        // but in real app we would collectLatest from prefs.
    }

    val colorOptions = listOf(
        Color(0xFF12131C), Color(0xFF000000), Color(0xFF1A1A1A), 
        Color(0xFF263238), Color(0xFF0A1F1C), Color(0xFF1E2136),
        Color(0xFF00E5FF), Color(0xFFBB86FC), Color(0xFF00E676),
        Color(0xFFFF5252), Color(0xFFFFD740), Color(0xFFFFFFFF),
        Color(0xFF7B1FA2), Color(0xFF1565C0), Color(0xFF2E7D32),
        Color(0xFFE65100), Color(0xFFC62828), Color(0xFF5D4037)
    )

    val currentTheme = KeyboardTheme(
        preset = ThemePreset.CUSTOM,
        backgroundColor = bgColor,
        keyBackgroundColor = keyBgColor,
        keyTextColor = keyTextColor,
        keySpecialColor = keySpecialColor,
        keySpecialTextColor = keySpecialTextColor,
        accentColor = accentColor,
        suggestionBgColor = suggestionBgColor,
        suggestionTextColor = suggestionTextColor,
        popupBackgroundColor = popupBgColor,
        popupTextColor = popupTextColor,
        keyHintColor = keyHintColor
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Creator Pro", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            scope.launch {
                                prefs.updateCustomTheme(
                                    bgColor = colorToHex(bgColor),
                                    keyBgColor = colorToHex(keyBgColor),
                                    keySpecialColor = colorToHex(keySpecialColor),
                                    keyTextColor = colorToHex(keyTextColor),
                                    keySpecialTextColor = colorToHex(keySpecialTextColor),
                                    accentColor = colorToHex(accentColor),
                                    suggestionBgColor = colorToHex(suggestionBgColor),
                                    suggestionTextColor = colorToHex(suggestionTextColor),
                                    popupBgColor = colorToHex(popupBgColor),
                                    popupTextColor = colorToHex(popupTextColor),
                                    keyHintColor = colorToHex(keyHintColor)
                                )
                                prefs.setTheme(ThemePreset.CUSTOM)
                                onBack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // High Fidelity Preview
            Text(text = "LIVE PREVIEW", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp)
            
            ThemeDetailedPreview(currentTheme)

            // Customization Controls
            Text(text = "DESIGN ELEMENTS", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp)

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                ColorPickerSection(title = "Background", selectedColor = bgColor, options = colorOptions) { bgColor = it }
                ColorPickerSection(title = "Primary Keys", selectedColor = keyBgColor, options = colorOptions) { keyBgColor = it }
                ColorPickerSection(title = "Key Text", selectedColor = keyTextColor, options = colorOptions) { keyTextColor = it }
                ColorPickerSection(title = "Special Keys (Shift/Enter)", selectedColor = keySpecialColor, options = colorOptions) { keySpecialColor = it }
                ColorPickerSection(title = "Special Text/Icons", selectedColor = keySpecialTextColor, options = colorOptions) { keySpecialTextColor = it }
                ColorPickerSection(title = "Accent (Active State)", selectedColor = accentColor, options = colorOptions) { accentColor = it }
                ColorPickerSection(title = "Suggestion Strip Bg", selectedColor = suggestionBgColor, options = colorOptions) { suggestionBgColor = it }
                ColorPickerSection(title = "Suggestion Text", selectedColor = suggestionTextColor, options = colorOptions) { suggestionTextColor = it }
                ColorPickerSection(title = "Key Popup Bg", selectedColor = popupBgColor, options = colorOptions) { popupBgColor = it }
                ColorPickerSection(title = "Key Popup Text", selectedColor = popupTextColor, options = colorOptions) { popupTextColor = it }
                ColorPickerSection(title = "Sub-character Hints", selectedColor = keyHintColor, options = colorOptions) { keyHintColor = it }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = { 
                    bgColor = Color(0xFF12131C)
                    keyBgColor = Color(0xFF1E2136)
                    keyTextColor = Color(0xFFF1F3FB)
                    keySpecialColor = Color(0xFF2A2E4B)
                    keySpecialTextColor = Color(0xFF80D8FF)
                    accentColor = Color(0xFF00E5FF)
                    suggestionBgColor = Color(0xFF1A1C29)
                    suggestionTextColor = Color(0xFFF1F3FB)
                    popupBgColor = Color(0xFF2A2E4B)
                    popupTextColor = Color(0xFF00E5FF)
                    keyHintColor = Color(0xFFF1F3FB).copy(alpha = 0.4f)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset to Default Dark Neon", color = MaterialTheme.colorScheme.error)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ThemeDetailedPreview(theme: KeyboardTheme) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = theme.backgroundColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Suggestion Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(theme.suggestionBgColor)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { i ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = theme.keyBackgroundColor.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = if (i == 0) "Nex" else if (i == 1) "Key" else "Keyboard",
                                color = theme.suggestionTextColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Keyboard Rows
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(10) { i ->
                        DetailedMiniKey(
                            bgColor = theme.keyBackgroundColor,
                            textColor = theme.keyTextColor,
                            hintColor = theme.keyHintColor,
                            label = if (i == 0) "Q" else if (i == 1) "W" else "E",
                            hint = if (i == 0) "1" else if (i == 1) "2" else if (i == 2) "3" else null
                        )
                    }
                }
                Row(modifier = Modifier.padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(9) { i ->
                        DetailedMiniKey(
                            bgColor = theme.keyBackgroundColor,
                            textColor = theme.keyTextColor,
                            hintColor = theme.keyHintColor,
                            label = "A",
                            hint = if (i == 0) "@" else null
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "↑", weight = 1.3f)
                    repeat(7) { DetailedMiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor, "Z") }
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "⌫", weight = 1.3f)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "?12", weight = 1.2f)
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "☺", weight = 1.1f)
                    Box(
                        modifier = Modifier
                            .weight(4f)
                            .height(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(theme.accentColor)
                    )
                    DetailedMiniKey(theme.keySpecialColor, theme.accentColor, theme.keyHintColor, "↵", weight = 1.3f)
                }
            }

            // Simulated Popup Overlay
            Box(
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = theme.popupBackgroundColor,
                    shadowElevation = 6.dp,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, theme.accentColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "G",
                        color = theme.popupTextColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.DetailedMiniKey(
    bgColor: Color,
    textColor: Color,
    hintColor: Color,
    label: String,
    hint: String? = null,
    weight: Float = 1f
) {
    Surface(
        modifier = Modifier
            .weight(weight)
            .height(32.dp),
        shape = RoundedCornerShape(4.dp),
        color = bgColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = label, 
                color = textColor, 
                fontSize = 9.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            
            if (hint != null) {
                Text(
                    text = hint,
                    color = hintColor,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 1.dp, end = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ColorPickerSection(
    title: String,
    selectedColor: Color,
    options: List<Color>,
    onColorSelected: (Color) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(options) { color ->
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 2.dp,
                            color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedColor == color) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (color == Color.White) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun colorToHex(color: Color): String {
    return String.format("#%08X", color.toArgb())
}

private fun hexToColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color.Black
    }
}
