package com.example.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clipboard.ClipItem
import com.example.clipboard.ClipboardManager
import com.example.theme.KeyboardTheme
import kotlin.math.abs

@Composable
fun KeyboardComposeView(
    mode: KeyboardMode,
    shiftState: ShiftState,
    theme: KeyboardTheme,
    composingText: String,
    suggestions: List<String>,
    actionLabel: String,
    showNumberRow: Boolean = false,
    hideLongPressHints: Boolean = false,
    keyboardHeightPortrait: Int = 100,
    keyboardHeightLandscape: Int = 100,
    oneHandedWidth: Int = 100,
    oneHandedWidthLandscape: Int = 40,
    isIncognito: Boolean = false,
    isPasswordField: Boolean = false,
    showVoiceKey: Boolean = true,
    showEmojiKey: Boolean = true,
    showGlobeKey: Boolean = true,
    moveCursorSpaceEnabled: Boolean = true,
    popupOnKeypressEnabled: Boolean = true,
    largeNumberRowEnabled: Boolean = false,
    longPressDelayMs: Long = 300L,
    spaceCursorSpeed: Int = 150,
    spaceCursorDelay: Int = 1000,
    splitKeyboardEnabled: Boolean = false,
    popupDismissDelay: String = "Default",
    physicalKbEmojiEnabled: Boolean = true,
    holdPasteEnabled: Boolean = false,
    holdPasteTriggerKey: String = "v",
    holdPasteDuration: Int = 400,
    alwaysShowSuggestions: Boolean = false,
    autoHideToolbar: Boolean = false,
    backspaceRepeatDelayMs: Long = 400L,
    backspaceRepeatSpeedMs: Long = 50L,
    onKeyTap: (String) -> Unit,
    onBackspaceTap: () -> Unit,
    onSpaceTap: () -> Unit,
    onEnterTap: () -> Unit,
    onShiftTap: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit,
    onSuggestionSelect: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onThemeToggle: () -> Unit,
    onOpenSettings: () -> Unit,
    onCursorMove: (Int) -> Unit = {},
    onIncognitoToggle: (() -> Unit)? = null,
    onHoldPaste: (() -> Unit)? = null
) {
    val popupAutoDismissMs = when (popupDismissDelay) {
        "Short" -> 1500L
        "Long" -> 3000L
        else -> Long.MAX_VALUE
    }
    val config = LocalConfiguration.current
    val hasPhysicalKeyboard = config.keyboard == android.content.res.Configuration.KEYBOARD_QWERTY
    val effectiveShowEmojiKey = showEmojiKey || (physicalKbEmojiEnabled && hasPhysicalKeyboard)
    var longPressKey by remember { mutableStateOf<KeyModel?>(null) }
    var isToolbarVisible by remember { mutableStateOf(true) }
    val isTyping = composingText.isNotEmpty() || suggestions.isNotEmpty()
    LaunchedEffect(isTyping) {
        if (autoHideToolbar && isTyping) isToolbarVisible = false
    }
    val orientation = config.orientation
    val effectiveHeight = if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) keyboardHeightLandscape else keyboardHeightPortrait
    val effectiveOneHanded = if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) oneHandedWidthLandscape else oneHandedWidth

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = theme.backgroundColor
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                if (autoHideToolbar && !isToolbarVisible) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(42.dp).background(theme.suggestionBgColor).padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.TextButton(onClick = { isToolbarVisible = true }) {
                            Text("⟨⟨", color = theme.keyTextColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    SmartToolbar(
                        currentMode = mode,
                        theme = theme,
                        isIncognito = isIncognito,
                        showVoiceKey = showVoiceKey,
                        showEmojiKey = effectiveShowEmojiKey,
                        showGlobeKey = showGlobeKey,
                        onModeChange = onModeChange,
                        onVoiceClick = onVoiceClick,
                        onThemeToggle = onThemeToggle,
                        onOpenSettings = onOpenSettings,
                        onIncognitoToggle = onIncognitoToggle
                    )
                }

                when (mode) {
                    KeyboardMode.EMOJI -> EmojiPanel(
                        theme = theme,
                        onEmojiClick = { emoji -> onKeyTap(emoji) }
                    )
                    KeyboardMode.CLIPBOARD -> ClipboardPanel(
                        theme = theme,
                        onClipClick = { clip -> onKeyTap(clip) }
                    )
                    else -> {
                        if (alwaysShowSuggestions || suggestions.isNotEmpty() || composingText.isNotEmpty()) {
                            CandidateStrip(
                                composingText = composingText,
                                suggestions = suggestions,
                                theme = theme,
                                onSuggestionSelect = onSuggestionSelect
                            )
                        }
                    }
                }

                if (mode != KeyboardMode.EMOJI && mode != KeyboardMode.CLIPBOARD) {
                    val adjustedTheme = theme.copy(
                        keyHeightDp = (theme.keyHeightDp * (effectiveHeight / 100f)).toInt()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ((100 - effectiveOneHanded) / 2f).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KeyboardKeysGrid(
                            mode = mode,
                            shiftState = shiftState,
                            theme = adjustedTheme,
                            actionLabel = actionLabel,
                            showNumberRow = showNumberRow,
                            hideLongPressHints = hideLongPressHints,
                            moveCursorSpaceEnabled = moveCursorSpaceEnabled,
                            largeNumberRowEnabled = largeNumberRowEnabled,
                            longPressDelayMs = longPressDelayMs,
                            spaceCursorSpeed = spaceCursorSpeed,
                            spaceCursorDelay = spaceCursorDelay,
                            splitKeyboardEnabled = splitKeyboardEnabled,
                            backspaceRepeatDelayMs = backspaceRepeatDelayMs,
                            backspaceRepeatSpeedMs = backspaceRepeatSpeedMs,
                            onKeyTap = onKeyTap,
                            onBackspaceTap = onBackspaceTap,
                            onSpaceTap = onSpaceTap,
                            onEnterTap = onEnterTap,
                            onShiftTap = onShiftTap,
                            onModeChange = onModeChange,
                            onCursorMove = onCursorMove,
                            onLongPress = { key ->
                                if (holdPasteEnabled && key.code == holdPasteTriggerKey) {
                                    onHoldPaste?.invoke()
                                } else {
                                    longPressKey = key
                                }
                            }
                        )
                    }
                }
            }

            if (popupOnKeypressEnabled) {
                longPressKey?.let { key ->
                    if (key.popupCandidates.isNotEmpty()) {
                        if (popupAutoDismissMs < Long.MAX_VALUE) {
                            LaunchedEffect(key) {
                                kotlinx.coroutines.delay(popupAutoDismissMs)
                                longPressKey = null
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable { longPressKey = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = theme.keySpecialColor),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    key.popupCandidates.forEach { candidate ->
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(theme.keyBackgroundColor)
                                                .clickable {
                                                    onKeyTap(candidate)
                                                    longPressKey = null
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = candidate, color = theme.keyTextColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SmartToolbar(
    currentMode: KeyboardMode,
    theme: KeyboardTheme,
    isIncognito: Boolean = false,
    showVoiceKey: Boolean = true,
    showEmojiKey: Boolean = true,
    showGlobeKey: Boolean = true,
    onModeChange: (KeyboardMode) -> Unit,
    onVoiceClick: () -> Unit,
    onThemeToggle: () -> Unit,
    onOpenSettings: () -> Unit,
    onIncognitoToggle: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(theme.suggestionBgColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                ToolbarBadge(
                    label = when (currentMode) {
                        KeyboardMode.BANGLA_PHONETIC -> "বাংলা"
                        KeyboardMode.BANGLA_JATIYO -> "জাতীয়"
                        KeyboardMode.AVRO -> "Avro"
                        KeyboardMode.ARABIC -> "عربي"
                        else -> "EN"
                    },
                    active = currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.BANGLA_JATIYO || currentMode == KeyboardMode.AVRO,
                    theme = theme
                ) {
                    onModeChange(
                        when (currentMode) {
                            KeyboardMode.ENGLISH -> KeyboardMode.BANGLA_PHONETIC
                            KeyboardMode.BANGLA_PHONETIC -> KeyboardMode.BANGLA_JATIYO
                            KeyboardMode.BANGLA_JATIYO -> KeyboardMode.AVRO
                            KeyboardMode.AVRO -> KeyboardMode.ARABIC
                            else -> KeyboardMode.ENGLISH
                        }
                    )
                }
            }
            item {
                ToolbarIcon(
                    icon = Icons.Default.ContentCopy,
                    contentDescription = "Clipboard",
                    active = currentMode == KeyboardMode.CLIPBOARD,
                    theme = theme
                ) {
                    onModeChange(if (currentMode == KeyboardMode.CLIPBOARD) KeyboardMode.ENGLISH else KeyboardMode.CLIPBOARD)
                }
            }
            if (showEmojiKey) {
                item {
                    ToolbarIcon(
                        icon = Icons.Default.SentimentSatisfiedAlt,
                        contentDescription = "Emoji",
                        active = currentMode == KeyboardMode.EMOJI,
                        theme = theme
                    ) {
                        onModeChange(if (currentMode == KeyboardMode.EMOJI) KeyboardMode.ENGLISH else KeyboardMode.EMOJI)
                    }
                }
            }
            if (showVoiceKey) {
                item {
                    ToolbarIcon(icon = Icons.Default.Mic, contentDescription = "Voice", active = false, theme = theme, onClick = onVoiceClick)
                }
            }
            item {
                ToolbarIcon(icon = Icons.Default.Palette, contentDescription = "Theme", active = false, theme = theme, onClick = onThemeToggle)
            }
            if (onIncognitoToggle != null) {
                item {
                    ToolbarIcon(
                        icon = Icons.Default.Security,
                        contentDescription = "Incognito",
                        active = isIncognito,
                        theme = theme,
                        onClick = onIncognitoToggle
                    )
                }
            }
        }

        IconButton(onClick = onOpenSettings) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = theme.keySpecialTextColor)
        }
    }
}

@Composable
fun ToolbarIcon(icon: ImageVector, contentDescription: String, active: Boolean, theme: KeyboardTheme, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) theme.accentColor.copy(alpha = 0.25f) else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription; role = Role.Button },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = if (active) theme.accentColor else theme.keyTextColor.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ToolbarBadge(label: String, active: Boolean, theme: KeyboardTheme, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) theme.accentColor else theme.keySpecialColor)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label; role = Role.Button }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = if (active) Color.Black else theme.keyTextColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun CandidateStrip(composingText: String, suggestions: List<String>, theme: KeyboardTheme, onSuggestionSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(theme.suggestionBgColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (composingText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(theme.accentColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "✎ $composingText", color = theme.accentColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (suggestions.isEmpty() && composingText.isEmpty()) {
            Text(
                text = "Suggestions",
                color = theme.keyTextColor.copy(alpha = 0.4f),
                fontSize = 13.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                items(suggestions) { candidate ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.keyBackgroundColor)
                            .clickable(role = Role.Button, onClick = { onSuggestionSelect(candidate) })
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = candidate, color = theme.keyTextColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardKeysGrid(
    mode: KeyboardMode,
    shiftState: ShiftState,
    theme: KeyboardTheme,
    actionLabel: String,
    showNumberRow: Boolean = false,
    hideLongPressHints: Boolean = false,
    moveCursorSpaceEnabled: Boolean = true,
    largeNumberRowEnabled: Boolean = false,
    longPressDelayMs: Long = 300L,
    spaceCursorSpeed: Int = 150,
    spaceCursorDelay: Int = 1000,
    splitKeyboardEnabled: Boolean = false,
    backspaceRepeatDelayMs: Long = 400L,
    backspaceRepeatSpeedMs: Long = 50L,
    onKeyTap: (String) -> Unit,
    onBackspaceTap: () -> Unit,
    onSpaceTap: () -> Unit,
    onEnterTap: () -> Unit,
    onShiftTap: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit,
    onCursorMove: (Int) -> Unit = {},
    onLongPress: (KeyModel) -> Unit = {}
) {
    var totalDragX by remember { mutableStateOf(0f) }
    val dragThreshold = ((200f - spaceCursorSpeed * 0.35f) + spaceCursorDelay * 0.02f).coerceIn(15f, 180f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (showNumberRow && mode != KeyboardMode.SYMBOLS && mode != KeyboardMode.NUMBERS) {
            val numRowHeight = if (largeNumberRowEnabled) 48.dp else 36.dp
            Row(modifier = Modifier.fillMaxWidth().height(numRowHeight), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                KeyboardLayouts.NumbersRow.forEach { keyModel ->
                    KeyItem(
                        keyModel = keyModel,
                        shiftState = shiftState,
                        theme = theme.copy(keyHeightDp = if (largeNumberRowEnabled) 48 else 36),
                        modifier = Modifier.weight(1f),
                        hideHints = hideLongPressHints,
                        longPressDelayMs = longPressDelayMs,
                        onTap = { onKeyTap(it) },
                        onLongPress = { onLongPress(keyModel) }
                    )
                }
            }
        }

        val rows = when (mode) {
            KeyboardMode.BANGLA_PHONETIC, KeyboardMode.AVRO -> listOf(BanglaLayout.PhoneticRow1, BanglaLayout.PhoneticRow2, BanglaLayout.PhoneticRow3)
            KeyboardMode.BANGLA_JATIYO -> listOf(BanglaLayout.JatiyoRow1, BanglaLayout.JatiyoRow2, BanglaLayout.JatiyoRow3)
            KeyboardMode.ARABIC -> listOf(ArabicLayout.Row1, ArabicLayout.Row2, ArabicLayout.Row3)
            KeyboardMode.SYMBOLS -> listOf(KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2, KeyboardLayouts.SymbolsRow3)
            KeyboardMode.NUMBERS -> listOf(KeyboardLayouts.NumbersRow, KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2)
            else -> listOf(EnglishLayout.Row1, EnglishLayout.Row2, EnglishLayout.Row3)
        }

        rows.take(2).forEach { rowKeys ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                rowKeys.forEach { keyModel ->
                    KeyItem(
                        keyModel = keyModel,
                        shiftState = shiftState,
                        theme = theme,
                        modifier = Modifier.weight(1f),
                        hideHints = hideLongPressHints,
                        longPressDelayMs = longPressDelayMs,
                        onTap = { onKeyTap(it) },
                        onLongPress = { onLongPress(keyModel) }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(modifier = Modifier.weight(1.3f), theme = theme, isSpecial = shiftState != ShiftState.OFF, longPressDelayMs = longPressDelayMs, onClick = onShiftTap) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null, tint = if (shiftState == ShiftState.CAPS_LOCK) theme.accentColor else theme.keyTextColor)
            }
            rows.getOrNull(2)?.forEach { keyModel ->
                KeyItem(
                    keyModel = keyModel,
                    shiftState = shiftState,
                    theme = theme,
                    modifier = Modifier.weight(1f),
                    hideHints = hideLongPressHints,
                    longPressDelayMs = longPressDelayMs,
                    onTap = { onKeyTap(it) },
                    onLongPress = { onLongPress(keyModel) }
                )
            }
            Box(
                modifier = Modifier.weight(1.3f)
                    .height(theme.keyHeightDp.dp)
                    .clip(RoundedCornerShape(theme.keyRadiusDp.dp))
                    .background(theme.keySpecialColor)
                    .pointerInput(backspaceRepeatDelayMs, backspaceRepeatSpeedMs) {
                        while (true) {
                            awaitPointerEventScope { awaitFirstDown(requireUnconsumed = false) }
                            var didRepeat = false
                            coroutineScope {
                                val repeatJob = launch {
                                    delay(backspaceRepeatDelayMs)
                                    didRepeat = true
                                    while (isActive) {
                                        onBackspaceTap()
                                        delay(backspaceRepeatSpeedMs)
                                    }
                                }
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.changes.all { !it.pressed }) break
                                    }
                                }
                                repeatJob.cancel()
                            }
                            if (!didRepeat) onBackspaceTap()
                        }
                    }
                    .semantics { contentDescription = "Delete"; role = Role.Button },
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Backspace, contentDescription = null, tint = theme.keySpecialTextColor)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(modifier = Modifier.weight(1.2f), theme = theme, isSpecial = true, longPressDelayMs = longPressDelayMs, onClick = { onModeChange(if (mode == KeyboardMode.SYMBOLS) KeyboardMode.ENGLISH else KeyboardMode.SYMBOLS) }) {
                Text(text = if (mode == KeyboardMode.SYMBOLS) "ABC" else "?123", color = theme.keySpecialTextColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            KeyButton(modifier = Modifier.weight(1f), theme = theme, isSpecial = false, longPressDelayMs = longPressDelayMs, onClick = { onKeyTap(",") }) {
                Text(text = ",", color = theme.keyTextColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            KeyButton(
                modifier = Modifier.weight(4f).then(
                    if (moveCursorSpaceEnabled) Modifier.pointerInput(Unit) {
                        detectDragGestures(onDragStart = { totalDragX = 0f }, onDrag = { change, dragAmount ->
                            change.consume(); totalDragX += dragAmount.x
                            if (abs(totalDragX) > dragThreshold) { onCursorMove(if (totalDragX > 0) 1 else -1); totalDragX = 0f }
                        })
                    } else Modifier
                ),
                theme = theme, isSpecial = false, longPressDelayMs = longPressDelayMs, onClick = onSpaceTap
            ) {
                Text(text = when (mode) { KeyboardMode.BANGLA_PHONETIC -> "বাংলা"; KeyboardMode.BANGLA_JATIYO -> "জাতীয়"; KeyboardMode.ARABIC -> "عربي"; else -> "English" }, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 13.sp)
            }
            KeyButton(modifier = Modifier.weight(1f), theme = theme, isSpecial = false, longPressDelayMs = longPressDelayMs, onClick = { onKeyTap(if (mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.BANGLA_JATIYO || mode == KeyboardMode.AVRO) "।" else ".") }) {
                Text(text = if (mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.BANGLA_JATIYO || mode == KeyboardMode.AVRO) "।" else ".", color = theme.keyTextColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            KeyButton(modifier = Modifier.weight(1.5f), theme = theme, isSpecial = true, longPressDelayMs = longPressDelayMs, onClick = onEnterTap) {
                Text(text = actionLabel, color = theme.accentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun KeyItem(
    keyModel: KeyModel,
    shiftState: ShiftState,
    theme: KeyboardTheme,
    modifier: Modifier = Modifier,
    hideHints: Boolean = false,
    longPressDelayMs: Long = 300L,
    onTap: (String) -> Unit,
    onLongPress: (() -> Unit)? = null
) {
    val charToOutput = when (shiftState) {
        ShiftState.SHIFT, ShiftState.CAPS_LOCK -> keyModel.label.uppercase()
        ShiftState.OFF -> keyModel.label
    }
    KeyButton(
        modifier = modifier,
        theme = theme,
        isSpecial = keyModel.isSpecial,
        longPressDelayMs = longPressDelayMs,
        onClick = { onTap(charToOutput) },
        onLongClick = onLongPress
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = charToOutput,
                color = if (keyModel.isSpecial) theme.keySpecialTextColor else theme.keyTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            if (!hideHints && keyModel.popupCandidates.isNotEmpty()) {
                Text(
                    text = keyModel.popupCandidates.first(),
                    color = (if (keyModel.isSpecial) theme.keySpecialTextColor else theme.keyTextColor).copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    theme: KeyboardTheme,
    isSpecial: Boolean,
    contentDescription: String = "Key",
    longPressDelayMs: Long = 300L,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(theme.keyHeightDp.dp)
            .clip(RoundedCornerShape(theme.keyRadiusDp.dp))
            .background(if (isSpecial) theme.keySpecialColor else theme.keyBackgroundColor)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick, role = Role.Button)
            .semantics { this.contentDescription = contentDescription; role = Role.Button },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
