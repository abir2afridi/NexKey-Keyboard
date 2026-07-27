package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backspace
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
    isIncognito: Boolean = false,
    isPasswordField: Boolean = false,
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
    onAiAction: (String) -> Unit,
    onCursorMove: (Int) -> Unit = {},
    onIncognitoToggle: (() -> Unit)? = null
) {
    var longPressKey by remember { mutableStateOf<KeyModel?>(null) }

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
                SmartToolbar(
                    currentMode = mode,
                    theme = theme,
                    isIncognito = isIncognito,
                    onModeChange = onModeChange,
                    onVoiceClick = onVoiceClick,
                    onThemeToggle = onThemeToggle,
                    onOpenSettings = onOpenSettings,
                    onIncognitoToggle = onIncognitoToggle
                )

                when (mode) {
                    KeyboardMode.EMOJI -> EmojiPanel(
                        theme = theme,
                        onEmojiClick = { emoji -> onKeyTap(emoji) }
                    )
                    KeyboardMode.CLIPBOARD -> ClipboardPanel(
                        theme = theme,
                        onClipClick = { clip -> onKeyTap(clip) }
                    )
                    KeyboardMode.AI_ASSIST -> AiAssistPanel(
                        theme = theme,
                        onAiAction = onAiAction
                    )
                    else -> CandidateStrip(
                        composingText = composingText,
                        suggestions = suggestions,
                        theme = theme,
                        onSuggestionSelect = onSuggestionSelect
                    )
                }

                if (mode != KeyboardMode.EMOJI && mode != KeyboardMode.CLIPBOARD && mode != KeyboardMode.AI_ASSIST) {
                    KeyboardKeysGrid(
                        mode = mode,
                        shiftState = shiftState,
                        theme = theme,
                        actionLabel = actionLabel,
                        showNumberRow = showNumberRow,
                        onKeyTap = onKeyTap,
                        onBackspaceTap = onBackspaceTap,
                        onSpaceTap = onSpaceTap,
                        onEnterTap = onEnterTap,
                        onShiftTap = onShiftTap,
                        onModeChange = onModeChange,
                        onCursorMove = onCursorMove,
                        onLongPress = { longPressKey = it }
                    )
                }
            }

            // Long Press Popup Overlay
            longPressKey?.let { key ->
                if (key.popupCandidates.isNotEmpty()) {
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

@Composable
fun SmartToolbar(
    currentMode: KeyboardMode,
    theme: KeyboardTheme,
    isIncognito: Boolean = false,
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
                        KeyboardMode.BANGLA_PHONETIC, KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                        KeyboardMode.ARABIC -> "عربي"
                        else -> "EN"
                    },
                    active = currentMode == KeyboardMode.BANGLA_PHONETIC || currentMode == KeyboardMode.BANGLA_JATIYO,
                    theme = theme
                ) {
                    if (currentMode == KeyboardMode.BANGLA_PHONETIC) {
                        onModeChange(KeyboardMode.BANGLA_JATIYO)
                    } else if (currentMode == KeyboardMode.BANGLA_JATIYO) {
                        onModeChange(KeyboardMode.ENGLISH)
                    } else {
                        onModeChange(KeyboardMode.BANGLA_PHONETIC)
                    }
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
            item {
                ToolbarIcon(
                    icon = Icons.Default.AutoAwesome,
                    contentDescription = "AI",
                    active = currentMode == KeyboardMode.AI_ASSIST,
                    theme = theme
                ) {
                    onModeChange(if (currentMode == KeyboardMode.AI_ASSIST) KeyboardMode.ENGLISH else KeyboardMode.AI_ASSIST)
                }
            }
            item {
                ToolbarIcon(icon = Icons.Default.Mic, contentDescription = "Voice", active = false, theme = theme, onClick = onVoiceClick)
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

@Composable
fun KeyboardKeysGrid(
    mode: KeyboardMode,
    shiftState: ShiftState,
    theme: KeyboardTheme,
    actionLabel: String,
    showNumberRow: Boolean = false,
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
    val dragThreshold = 40f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (showNumberRow && mode != KeyboardMode.SYMBOLS && mode != KeyboardMode.NUMBERS) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                KeyboardLayouts.NumbersRow.forEach { keyModel ->
                    KeyItem(
                        keyModel = keyModel,
                        shiftState = shiftState,
                        theme = theme,
                        modifier = Modifier.weight(1f),
                        onTap = { onKeyTap(it) },
                        onLongPress = { onLongPress(keyModel) }
                    )
                }
            }
        }

        val rows = when (mode) {
            KeyboardMode.BANGLA_JATIYO -> listOf(KeyboardLayouts.BanglaJatiyoRow1, KeyboardLayouts.BanglaJatiyoRow2, KeyboardLayouts.BanglaJatiyoRow3)
            KeyboardMode.ARABIC -> listOf(KeyboardLayouts.ArabicRow1, KeyboardLayouts.ArabicRow2, KeyboardLayouts.ArabicRow3)
            KeyboardMode.SYMBOLS -> listOf(KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2, KeyboardLayouts.SymbolsRow3)
            KeyboardMode.NUMBERS -> listOf(KeyboardLayouts.NumbersRow, KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2)
            else -> listOf(KeyboardLayouts.EnglishRow1, KeyboardLayouts.EnglishRow2, KeyboardLayouts.EnglishRow3)
        }

        rows.take(2).forEach { rowKeys ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                rowKeys.forEach { keyModel ->
                    KeyItem(
                        keyModel = keyModel,
                        shiftState = shiftState,
                        theme = theme,
                        modifier = Modifier.weight(1f),
                        onTap = { onKeyTap(it) },
                        onLongPress = { onLongPress(keyModel) }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(modifier = Modifier.weight(1.3f), theme = theme, isSpecial = shiftState != ShiftState.OFF, onClick = onShiftTap) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null, tint = if (shiftState == ShiftState.CAPS_LOCK) theme.accentColor else theme.keyTextColor)
            }
            rows.getOrNull(2)?.forEach { keyModel ->
                KeyItem(
                    keyModel = keyModel,
                    shiftState = shiftState,
                    theme = theme,
                    modifier = Modifier.weight(1f),
                    onTap = { onKeyTap(it) },
                    onLongPress = { onLongPress(keyModel) }
                )
            }
            KeyButton(modifier = Modifier.weight(1.3f), theme = theme, isSpecial = true, onClick = onBackspaceTap) {
                Icon(imageVector = Icons.Default.Backspace, contentDescription = null, tint = theme.keySpecialTextColor)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(modifier = Modifier.weight(1.2f), theme = theme, isSpecial = true, onClick = { onModeChange(if (mode == KeyboardMode.SYMBOLS) KeyboardMode.ENGLISH else KeyboardMode.SYMBOLS) }) {
                Text(text = if (mode == KeyboardMode.SYMBOLS) "ABC" else "?123", color = theme.keySpecialTextColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            KeyButton(modifier = Modifier.weight(1.2f), theme = theme, isSpecial = true, onClick = {
                when (mode) {
                    KeyboardMode.BANGLA_PHONETIC -> onModeChange(KeyboardMode.BANGLA_JATIYO)
                    KeyboardMode.BANGLA_JATIYO -> onModeChange(KeyboardMode.ENGLISH)
                    else -> onModeChange(KeyboardMode.BANGLA_PHONETIC)
                }
            }) {
                Text(text = when (mode) { KeyboardMode.BANGLA_PHONETIC -> "Phonetic"; KeyboardMode.BANGLA_JATIYO -> "জাতীয়"; KeyboardMode.ARABIC -> "عربي"; else -> "EN" }, color = theme.accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            KeyButton(
                modifier = Modifier.weight(4f).pointerInput(Unit) {
                    detectDragGestures(onDragStart = { totalDragX = 0f }, onDrag = { change, dragAmount ->
                        change.consume(); totalDragX += dragAmount.x
                        if (abs(totalDragX) > dragThreshold) { onCursorMove(if (totalDragX > 0) 1 else -1); totalDragX = 0f }
                    })
                },
                theme = theme, isSpecial = false, onClick = onSpaceTap
            ) {
                Text(text = when (mode) { KeyboardMode.BANGLA_PHONETIC -> "বাংলা"; KeyboardMode.BANGLA_JATIYO -> "জাতীয়"; else -> "English" }, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 13.sp)
            }
            KeyButton(modifier = Modifier.weight(1f), theme = theme, isSpecial = false, onClick = { onKeyTap(if (mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.BANGLA_JATIYO) "।" else ".") }) {
                Text(text = if (mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.BANGLA_JATIYO) "।" else ".", color = theme.keyTextColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            KeyButton(modifier = Modifier.weight(1.5f), theme = theme, isSpecial = true, onClick = onEnterTap) {
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
        onClick = { onTap(charToOutput) },
        onLongClick = onLongPress
    ) {
        Text(text = charToOutput, color = if (keyModel.isSpecial) theme.keySpecialTextColor else theme.keyTextColor, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyButton(modifier: Modifier = Modifier, theme: KeyboardTheme, isSpecial: Boolean, contentDescription: String = "Key", onClick: () -> Unit, onLongClick: (() -> Unit)? = null, content: @Composable () -> Unit) {
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

@Composable
fun EmojiPanel(theme: KeyboardTheme, onEmojiClick: (String) -> Unit) {
    val emojis = remember { listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠", "😡", "🤬", "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "👍", "👎", "👏", "🙌", "👐", "🤲", "🤝", "🙏", "✌️", "🤟") }
    Column(modifier = Modifier.fillMaxWidth().height(210.dp).background(theme.backgroundColor).padding(8.dp)) {
        Text(text = "Frequently Used Emojis", color = theme.keySpecialTextColor, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(8), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(emojis) { emoji ->
                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(theme.keyBackgroundColor).clickable(role = Role.Button, onClick = { onEmojiClick(emoji) }), contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ClipboardPanel(theme: KeyboardTheme, onClipClick: (String) -> Unit) {
    val clips by ClipboardManager.clips.collectAsState()
    Column(modifier = Modifier.fillMaxWidth().height(210.dp).background(theme.backgroundColor).padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Clipboard History", color = theme.accentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            TextButton(onClick = { ClipboardManager.clearAllUnpinned() }) { Text("Clear Unpinned", color = theme.keySpecialTextColor, fontSize = 11.sp) }
        }
        if (clips.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No copied clips yet", color = theme.keyTextColor.copy(alpha = 0.5f)) }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(clips) { clip ->
                    Box(modifier = Modifier.width(160.dp).height(130.dp).clip(RoundedCornerShape(10.dp)).background(theme.keyBackgroundColor).border(width = 1.dp, color = if (clip.isPinned) theme.accentColor else Color.Transparent, shape = RoundedCornerShape(10.dp)).clickable(role = Role.Button, onClick = { onClipClick(clip.text) }).padding(8.dp)) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Icon(imageVector = Icons.Default.PushPin, contentDescription = null, tint = if (clip.isPinned) theme.accentColor else theme.keyTextColor.copy(alpha = 0.4f), modifier = Modifier.size(16.dp).clickable { ClipboardManager.togglePin(clip.id) })
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = theme.keyTextColor.copy(alpha = 0.4f), modifier = Modifier.size(16.dp).clickable { ClipboardManager.deleteClip(clip.id) })
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = clip.text, color = theme.keyTextColor, fontSize = 12.sp, maxLines = 4)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiAssistPanel(theme: KeyboardTheme, onAiAction: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().height(210.dp).background(theme.backgroundColor).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = theme.accentColor, modifier = Modifier.size(18.dp))
            Text(text = "NexKey Smart AI Helper", color = theme.accentColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AiActionButton("Rewrite & Polish", theme) { onAiAction("Rewrite text to be clear and elegant.") }
            AiActionButton("Fix Grammar", theme) { onAiAction("Fix spelling and grammar errors.") }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AiActionButton("Translate EN ⇄ BN", theme) { onAiAction("Translate to Bangla/English.") }
            AiActionButton("Professional Tone", theme) { onAiAction("Make tone polite and professional.") }
        }
    }
}

@Composable
fun AiActionButton(title: String, theme: KeyboardTheme, onClick: () -> Unit) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(theme.keyBackgroundColor).clickable(role = Role.Button, onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp), contentAlignment = Alignment.Center) {
        Text(text = title, color = theme.keyTextColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
