package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.data.UserPreferences
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
                title = { Text(stringResource(R.string.theme_creator_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
                        Text(stringResource(R.string.save), color = Color.White, fontWeight = FontWeight.Bold)
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
        ) {
            // Live Preview — fixed at top, does NOT scroll
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.live_preview), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp)
            ThemeDetailedPreview(currentTheme)

            // Scrollable color pickers below
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Customization Controls
                Text(text = stringResource(R.string.design_elements), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp)
                ColorPickerSection(title = stringResource(R.string.color_bg), selectedColor = bgColor, options = colorOptions) { bgColor = it }
                ColorPickerSection(title = stringResource(R.string.color_primary_keys), selectedColor = keyBgColor, options = colorOptions) { keyBgColor = it }
                ColorPickerSection(title = stringResource(R.string.color_key_text), selectedColor = keyTextColor, options = colorOptions) { keyTextColor = it }
                ColorPickerSection(title = stringResource(R.string.color_special_keys), selectedColor = keySpecialColor, options = colorOptions) { keySpecialColor = it }
                ColorPickerSection(title = stringResource(R.string.color_special_text), selectedColor = keySpecialTextColor, options = colorOptions) { keySpecialTextColor = it }
                ColorPickerSection(title = stringResource(R.string.color_accent), selectedColor = accentColor, options = colorOptions) { accentColor = it }
                ColorPickerSection(title = stringResource(R.string.color_suggestion_bg), selectedColor = suggestionBgColor, options = colorOptions) { suggestionBgColor = it }
                ColorPickerSection(title = stringResource(R.string.color_suggestion_text), selectedColor = suggestionTextColor, options = colorOptions) { suggestionTextColor = it }
                ColorPickerSection(title = stringResource(R.string.color_popup_bg), selectedColor = popupBgColor, options = colorOptions) { popupBgColor = it }
                ColorPickerSection(title = stringResource(R.string.color_popup_text), selectedColor = popupTextColor, options = colorOptions) { popupTextColor = it }
                ColorPickerSection(title = stringResource(R.string.color_hints), selectedColor = keyHintColor, options = colorOptions) { keyHintColor = it }

                Spacer(modifier = Modifier.height(8.dp))

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
                    Text(stringResource(R.string.reset_default), color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun ThemeDetailedPreview(theme: KeyboardTheme) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = theme.accentColor, spotColor = theme.accentColor),
        shape = RoundedCornerShape(24.dp),
        color = theme.backgroundColor
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Simulated Smart Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .background(theme.suggestionBgColor.copy(alpha = 0.9f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = theme.accentColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, theme.accentColor.copy(alpha = 0.3f))
                    ) {
                        Text("EN", color = theme.accentColor, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Icon(Icons.Default.Palette, null, tint = theme.keySpecialTextColor, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.Mic, null, tint = theme.keySpecialTextColor.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                }

                // Mini Speed Meter (Calculator style)
                Surface(
                    modifier = Modifier.width(50.dp).height(24.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, theme.accentColor.copy(alpha = 0.4f))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text("88.8", color = theme.accentColor.copy(alpha = 0.05f), fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, modifier = Modifier.align(Alignment.Center))
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                            Text("1.2", color = theme.accentColor, fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                            Text("LIVE", color = theme.accentColor.copy(alpha = 0.8f), fontSize = 5.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Icon(Icons.Default.Settings, null, tint = theme.keySpecialTextColor, modifier = Modifier.size(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Suggestion Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(theme.suggestionBgColor)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Keyboard Rows (QWERTY)
                val row1 = listOf("Q" to "1", "W" to "2", "E" to "3", "R" to "4", "T" to "5", "Y" to "6", "U" to "7", "I" to "8", "O" to "9", "P" to "0")
                val row2 = listOf("A" to "@", "S" to "#", "D" to "$", "F" to "%", "G" to "&", "H" to "-", "J" to "+", "K" to "(", "L" to ")")
                val row3 = listOf("Z" to "!", "X" to "\"", "C" to "'", "V" to "?", "B" to "/", "N" to ";", "M" to ":")

                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    row1.forEach { (char, hint) ->
                        DetailedMiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor, char, hint)
                    }
                }
                Row(modifier = Modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    row2.forEach { (char, hint) ->
                        DetailedMiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor, char, hint)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "", icon = Icons.Default.KeyboardArrowUp, weight = 1.3f)
                    row3.forEach { (char, hint) ->
                        DetailedMiniKey(theme.keyBackgroundColor, theme.keyTextColor, theme.keyHintColor, char, hint)
                    }
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "", icon = Icons.AutoMirrored.Filled.Backspace, weight = 1.3f)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "?12", weight = 1.2f)
                    DetailedMiniKey(theme.keySpecialColor, theme.keySpecialTextColor, theme.keyHintColor, "☺", weight = 1.1f)
                    Box(
                        modifier = Modifier
                            .weight(4f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(theme.accentColor, theme.accentColor.copy(alpha = 0.8f))
                                )
                            )
                            .shadow(2.dp, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("English", color = Color.White.copy(alpha = 0.9f), fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                    DetailedMiniKey(theme.keySpecialColor, theme.accentColor, theme.keyHintColor, "", icon = Icons.Default.Check, weight = 1.3f)
                }
            }
        }
        
        // Simulated Popup Overlay (G key)
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 20.dp),
                shape = RoundedCornerShape(6.dp),
                color = theme.popupBackgroundColor,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, theme.accentColor.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "G",
                    color = theme.popupTextColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
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
    icon: ImageVector? = null,
    weight: Float = 1f
) {
    Surface(
        modifier = Modifier
            .weight(weight)
            .height(34.dp),
        shape = RoundedCornerShape(4.dp),
        color = bgColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(14.dp).align(Alignment.Center)
                )
            } else {
                Text(
                    text = label, 
                    color = textColor, 
                    fontSize = 10.sp, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
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
    var showPicker by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.custom_color),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
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
            item {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.custom_color),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
    if (showPicker) {
        ColorPickerDialog(
            title = title,
            initialColor = selectedColor,
            onDismiss = { showPicker = false },
            onConfirm = { onColorSelected(it) }
        )
    }
}

@Composable
private fun ColorPickerDialog(
    title: String,
    initialColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (Color) -> Unit
) {
    val initialHsv = remember(initialColor) { initialColor.toHsvArr() }
    var hue by remember(initialColor) { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember(initialColor) { mutableFloatStateOf(initialHsv[1]) }
    var value by remember(initialColor) { mutableFloatStateOf(initialHsv[2]) }
    val alpha = initialColor.alpha
    val temp = hsvColor(hue, saturation, value, alpha)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                            .background(temp)
                    )
                    Text(
                        text = String.format("#%06X", 0xFFFFFF and temp.toArgb()),
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                ColorSliderRow(
                    label = stringResource(R.string.hue),
                    value = hue,
                    onValueChange = { hue = it },
                    gradient = hueGradientColors
                )
                ColorSliderRow(
                    label = stringResource(R.string.saturation),
                    value = saturation,
                    onValueChange = { saturation = it },
                    gradient = listOf(hsvColor(hue, 0f, value, alpha), hsvColor(hue, 1f, value, alpha))
                )
                ColorSliderRow(
                    label = stringResource(R.string.value),
                    value = value,
                    onValueChange = { value = it },
                    gradient = listOf(hsvColor(hue, saturation, 0f, alpha), hsvColor(hue, saturation, 1f, alpha))
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(temp); onDismiss() }) {
                Text(stringResource(R.string.done), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
private fun ColorSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    gradient: List<Color>
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        GradientSlider(value = value, onValueChange = onValueChange, gradient = gradient)
    }
}

@Composable
private fun GradientSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    gradient: List<Color>
) {
    val density = LocalDensity.current
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        val trackHeight = 12.dp
        val thumbSize = 24.dp
        val usableWidth = maxWidth - thumbSize
        val usableWidthPx = with(density) { usableWidth.toPx() }
        val thumbHalfPx = with(density) { (thumbSize / 2).toPx() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(Brush.horizontalGradient(gradient))
        )
        Box(
            modifier = Modifier
                .size(thumbSize)
                .align(Alignment.CenterStart)
                .offset(x = usableWidth * value)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .shadow(2.dp, CircleShape)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        currentOnValueChange(((down.position.x - thumbHalfPx) / usableWidthPx).coerceIn(0f, 1f))
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (change.changedToUpIgnoreConsumed()) break
                            change.consume()
                            currentOnValueChange(((change.position.x - thumbHalfPx) / usableWidthPx).coerceIn(0f, 1f))
                        }
                    }
                }
        )
    }
}

private val hueGradientColors = (0..360 step 10).map { hsvColor(it.toFloat(), 1f, 1f) }

private fun colorToHex(color: Color): String {
    return String.format("#%08X", color.toArgb())
}

private fun Color.toHsvArr(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsv)
    return hsv
}

private fun hsvColor(hue: Float, saturation: Float, value: Float, alpha: Float = 1f): Color {
    return Color(android.graphics.Color.HSVToColor((alpha * 255f).roundToInt(), floatArrayOf(hue, saturation, value)))
}

private fun hexToColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color.Black
    }
}
