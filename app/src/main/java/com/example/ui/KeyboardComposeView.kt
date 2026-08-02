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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.clipboard.ClipItem
import com.example.clipboard.ClipboardManager
import com.example.theme.KeyboardTheme
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import java.util.Locale
import kotlin.math.abs
@Composable
fun KeyboardComposeView(
    mode: KeyboardMode,
    lastTextMode: KeyboardMode = KeyboardMode.BANGLA_JATIYO,
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
    unifiedHeader: Boolean = false,
    toolbarAutoShowDelay: Int = 10,
    headerAnimation: HeaderAnimation = HeaderAnimation.FADE,
    backspaceRepeatDelayMs: Long = 400L,
    backspaceRepeatSpeedMs: Long = 50L,
    liveCps: Float = 0f,
    maxBurstCps: Float = 0f,
    isSpeedActive: Boolean = false,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
    recentEmojis: MutableList<String> = remember { mutableStateListOf() },
    onRecentEmojisChanged: (List<String>) -> Unit = {},
    emojiSearchActive: Boolean = false,
    emojiSearchQuery: String = "",
    emojiSearchVisibleRows: Int = 2,
    emojiSearchHorizontal: Boolean = true,
    onEmojiSearchToggle: () -> Unit = {},
    onEmojiSearchQueryChange: (String) -> Unit = {},
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
    // UNIFIED HEADER state: keyboard opens with the toolbar. While typing the toolbar is
    // swapped for the suggestion strip and does NOT come back on its own — only the strip's
    // toggle button brings it back. If the keyboard stays idle for toolbarAutoShowDelay
    // seconds (no typing), the toolbar auto-appears again. One header at a time.
    var isToolbarHeaderVisible by remember { mutableStateOf(true) }
    val isTyping = composingText.isNotEmpty() || suggestions.isNotEmpty()
    LaunchedEffect(isTyping, unifiedHeader, toolbarAutoShowDelay) {
        if (!unifiedHeader) return@LaunchedEffect
        if (isTyping) {
            isToolbarHeaderVisible = false
        } else {
            delay(toolbarAutoShowDelay * 1000L)
            isToolbarHeaderVisible = true
        }
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
                    .padding(bottom = 8.dp)
            ) {
                val isToolbarCollapsed = unifiedHeader && !isToolbarHeaderVisible
                val showSuggestionBar = alwaysShowSuggestions && mode != KeyboardMode.EMOJI && mode != KeyboardMode.CLIPBOARD
                // UNIFIED HEADER: exactly ONE header is drawn at a time.
                // - Keyboard opens            -> toolbar header
                // - Typing starts             -> suggestion strip replaces the toolbar
                // - Strip toggle button       -> toolbar back (suggestions hidden)
                // - Typing starts again       -> toolbar auto-hides, suggestions return
                // The swap between the two headers is animated with the user-selected
                // HeaderAnimation style (see HeaderAnimation.kt).
                AnimatedHeaderSwitcher(
                    showToolbar = !(isToolbarCollapsed && showSuggestionBar),
                    animation = headerAnimation,
                    toolbar = {
                        SmartToolbar(
                            currentMode = mode,
                            theme = theme,
                            isIncognito = isIncognito,
                            showVoiceKey = showVoiceKey,
                            showEmojiKey = effectiveShowEmojiKey,
                            showGlobeKey = showGlobeKey,
                            liveCps = liveCps,
                            maxBurstCps = maxBurstCps,
                            isSpeedActive = isSpeedActive,
                            meterTheme = meterTheme,
                            meterFont = meterFont,
                            onModeChange = onModeChange,
                            onVoiceClick = onVoiceClick,
                            onThemeToggle = onThemeToggle,
                            onOpenSettings = onOpenSettings,
                            onIncognitoToggle = onIncognitoToggle
                        )
                    },
                    suggestions = {
                        // The suggestion strip IS the header while typing; its trailing button
                        // brings the full toolbar back.
                        CandidateStrip(
                            composingText = composingText,
                            suggestions = suggestions,
                            theme = theme,
                            onSuggestionSelect = onSuggestionSelect,
                            onShowToolbar = { isToolbarHeaderVisible = true }
                        )
                    }
                )

                when (mode) {
                    KeyboardMode.EMOJI -> {
                        if (emojiSearchActive) {
                            EmojiSearchBar(
                                theme = theme,
                                searchQuery = emojiSearchQuery,
                                onQueryChange = onEmojiSearchQueryChange,
                                onClose = onEmojiSearchToggle,
                                recentEmojis = recentEmojis,
                                onEmojiClick = { emoji -> onKeyTap(emoji) },
                                visibleRows = emojiSearchVisibleRows,
                                horizontal = emojiSearchHorizontal
                            )
                        } else {
                            EmojiPanel(
                                theme = theme,
                                onEmojiClick = { emoji -> onKeyTap(emoji) },
                                onBackspace = onBackspaceTap,
                                recentEmojis = recentEmojis,
                                onRecentEmojisChanged = onRecentEmojisChanged,
                                onSearchToggle = onEmojiSearchToggle
                            )
                        }
                    }
                    KeyboardMode.CLIPBOARD -> ClipboardPanel(
                        theme = theme,
                        onClipClick = { clip -> onKeyTap(clip) }
                    )
                    else -> {
                        // Suggestion bar is shown ONLY when "Always show suggestions" is on.
                        // When the toggle is off, no suggestion bar appears on the keyboard —
                        // not even while typing (the toggle is the master switch).
                        // With the unified header on, suggestions are already drawn as the
                        // header (above), so no separate strip is rendered here.
                        if (showSuggestionBar && !unifiedHeader) {
                            CandidateStrip(
                                composingText = composingText,
                                suggestions = suggestions,
                                theme = theme,
                                onSuggestionSelect = onSuggestionSelect
                            )
                        }
                    }
                }

                if ((mode != KeyboardMode.EMOJI || emojiSearchActive) && mode != KeyboardMode.CLIPBOARD) {
                    val isCompact = emojiSearchActive
                    val adjustedTheme = theme.copy(
                        keyHeightDp = if (isCompact) (theme.keyHeightDp * 0.65f).toInt().coerceAtLeast(28) else (theme.keyHeightDp * (effectiveHeight / 100f)).toInt()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ((100 - effectiveOneHanded) / 2f).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KeyboardKeysGrid(
                            mode = mode,
                            lastTextMode = lastTextMode,
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
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable { longPressKey = null },
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = theme.popupBackgroundColor),
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
                                            Text(text = candidate, color = theme.popupTextColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
    liveCps: Float = 0f,
    maxBurstCps: Float = 0f,
    isSpeedActive: Boolean = false,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
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
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                ToolbarBadge(
                    label = when (currentMode) {
                        KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                        KeyboardMode.AVRO -> "Avro"
                        KeyboardMode.ARABIC -> "عربي"
                        else -> "EN"
                    },
                    active = currentMode == KeyboardMode.BANGLA_JATIYO || currentMode == KeyboardMode.AVRO,
                    theme = theme
                ) {
                    onModeChange(
                        when (currentMode) {
                            KeyboardMode.ENGLISH -> KeyboardMode.BANGLA_JATIYO
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
                    contentDescription = stringResource(R.string.kb_clipboard),
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
                        contentDescription = stringResource(R.string.kb_emoji),
                        active = currentMode == KeyboardMode.EMOJI,
                        theme = theme
                    ) {
                        onModeChange(if (currentMode == KeyboardMode.EMOJI) KeyboardMode.ENGLISH else KeyboardMode.EMOJI)
                    }
                }
            }
            if (showVoiceKey) {
                item {
                    ToolbarIcon(icon = Icons.Default.Mic, contentDescription = stringResource(R.string.kb_voice), active = false, theme = theme, onClick = onVoiceClick)
                }
            }
            item {
                ToolbarIcon(icon = Icons.Default.Palette, contentDescription = stringResource(R.string.kb_theme), active = false, theme = theme, onClick = onThemeToggle)
            }
            if (onIncognitoToggle != null) {
                item {
                    ToolbarIcon(
                        icon = Icons.Default.Security,
                        contentDescription = stringResource(R.string.kb_incognito),
                        active = isIncognito,
                        theme = theme,
                        onClick = onIncognitoToggle
                    )
                }
            }
        }

        // Digital Speed Meter
        DigitalSpeedMeter(
            cps = if (isSpeedActive) liveCps else maxBurstCps,
            isLive = isSpeedActive,
            meterTheme = meterTheme,
            fontStyle = meterFont
        )

        IconButton(onClick = onOpenSettings) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.kb_settings), tint = theme.keySpecialTextColor)
        }
    }
}

@Composable
fun DigitalSpeedMeter(
    cps: Float,
    isLive: Boolean,
    meterTheme: MeterTheme,
    fontStyle: String = "DIGITAL"
) {
    val textStyle = remember(fontStyle, meterTheme) {
        when (fontStyle) {
            "LCD" -> androidx.compose.ui.text.TextStyle(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                letterSpacing = 2.sp,
                fontFeatureSettings = "tnum"
            )
            "SEGMENT" -> androidx.compose.ui.text.TextStyle(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                fontFeatureSettings = "tnum"
            )
            "MODERN" -> androidx.compose.ui.text.TextStyle(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontFeatureSettings = "tnum"
            )
            else -> androidx.compose.ui.text.TextStyle( // DIGITAL
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                letterSpacing = meterTheme.letterSpacing,
                fontFeatureSettings = "tnum"
            )
        }
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .width(58.dp)
            .height(30.dp),
        color = meterTheme.backgroundColor.copy(alpha = meterTheme.backgroundAlpha),
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(meterTheme.borderWidth, meterTheme.borderColor),
        tonalElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background "88.8" shadow effect for LCD/Seven-segment
            if (meterTheme.showLcdShadow || fontStyle == "LCD" || fontStyle == "SEGMENT") {
                Text(
                    text = "88.8",
                    color = meterTheme.textColor.copy(alpha = 0.05f),
                    fontSize = 12.sp,
                    style = textStyle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f", cps),
                    color = meterTheme.textColor,
                    fontSize = 12.sp,
                    style = textStyle.merge(
                        androidx.compose.ui.text.TextStyle(
                            shadow = if (meterTheme.glowRadius > 0f) {
                                androidx.compose.ui.graphics.Shadow(
                                    color = meterTheme.textColor.copy(alpha = 0.8f),
                                    blurRadius = meterTheme.glowRadius
                                )
                            } else null
                        )
                    )
                )
                Text(
                    text = if (isLive) stringResource(R.string.kb_live) else stringResource(R.string.kb_peak),
                    color = meterTheme.labelColor,
                    fontSize = 6.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
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
fun CandidateStrip(
    composingText: String,
    suggestions: List<String>,
    theme: KeyboardTheme,
    onSuggestionSelect: (String) -> Unit,
    // UNIFIED HEADER: when set, a trailing toggle button is drawn that hides the suggestion
    // strip and shows the full toolbar (tap-to-swap, one header at a time).
    onShowToolbar: (() -> Unit)? = null
) {
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
                text = stringResource(R.string.kb_suggestions),
                color = theme.keyTextColor.copy(alpha = 0.4f),
                fontSize = 13.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        } else {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(suggestions) { candidate ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.keyBackgroundColor)
                            .clickable(role = Role.Button, onClick = { onSuggestionSelect(candidate) })
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = candidate, color = theme.suggestionTextColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (onShowToolbar != null) {
            Spacer(modifier = Modifier.width(4.dp))
            ToolbarIcon(
                icon = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.kb_show_toolbar),
                active = false,
                theme = theme,
                onClick = onShowToolbar
            )
        }
    }
}

@Composable
fun KeyboardKeysGrid(
    mode: KeyboardMode,
    lastTextMode: KeyboardMode = KeyboardMode.BANGLA_JATIYO,
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
    liveCps: Float = 0f,
    maxBurstCps: Float = 0f,
    isSpeedActive: Boolean = false,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
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
            val activeNumberRow = if (mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.BANGLA_JATIYO || mode == KeyboardMode.AVRO) {
                KeyboardLayouts.BanglaNumbersRow
            } else {
                KeyboardLayouts.NumbersRow
            }
            Row(modifier = Modifier.fillMaxWidth().height(numRowHeight), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                activeNumberRow.forEach { keyModel ->
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

        val effectiveTextMode = if (mode == KeyboardMode.SYMBOLS || mode == KeyboardMode.NUMBERS || mode == KeyboardMode.EMOJI || mode == KeyboardMode.CLIPBOARD) lastTextMode else mode

        val rows = when (mode) {
            KeyboardMode.BANGLA_PHONETIC -> listOf(BanglaLayout.PhoneticRow1, BanglaLayout.PhoneticRow2, BanglaLayout.PhoneticRow3)
            KeyboardMode.AVRO -> listOf(BanglaLayout.AvroRow1, BanglaLayout.AvroRow2, BanglaLayout.AvroRow3)
            KeyboardMode.BANGLA_JATIYO -> if (shiftState != ShiftState.OFF) {
                listOf(BanglaLayout.JatiyoShiftRow1, BanglaLayout.JatiyoShiftRow2, BanglaLayout.JatiyoShiftRow3)
            } else {
                listOf(BanglaLayout.JatiyoRow1, BanglaLayout.JatiyoRow2, BanglaLayout.JatiyoRow3)
            }
            KeyboardMode.ARABIC -> listOf(ArabicLayout.Row1, ArabicLayout.Row2, ArabicLayout.Row3)
            KeyboardMode.SYMBOLS -> if (effectiveTextMode == KeyboardMode.BANGLA_JATIYO || effectiveTextMode == KeyboardMode.AVRO || effectiveTextMode == KeyboardMode.BANGLA_PHONETIC) {
                listOf(KeyboardLayouts.BanglaNumbersRow, KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2)
            } else {
                listOf(KeyboardLayouts.NumbersRow, KeyboardLayouts.SymbolsRow1, KeyboardLayouts.SymbolsRow2)
            }
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
            val deleteLabel = stringResource(R.string.kb_delete)
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
                    .semantics { contentDescription = deleteLabel; role = Role.Button },
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Backspace, contentDescription = null, tint = theme.keySpecialTextColor)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(
                modifier = Modifier.weight(1.2f),
                theme = theme,
                isSpecial = true,
                longPressDelayMs = longPressDelayMs,
                onClick = { onModeChange(if (mode == KeyboardMode.SYMBOLS) effectiveTextMode else KeyboardMode.SYMBOLS) }
            ) {
                val buttonText = if (mode == KeyboardMode.SYMBOLS) {
                    when (effectiveTextMode) {
                        KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                        KeyboardMode.AVRO -> "Avro"
                        KeyboardMode.ARABIC -> "عربي"
                        else -> "ABC"
                    }
                } else {
                    "?123"
                }
                Text(text = buttonText, color = theme.keySpecialTextColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
                Text(text = when (mode) { KeyboardMode.BANGLA_JATIYO -> "বাংলা"; KeyboardMode.BANGLA_PHONETIC -> "Phonetic"; KeyboardMode.AVRO -> "Avro"; KeyboardMode.ARABIC -> "عربي"; else -> "English" }, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 13.sp)
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = charToOutput,
                color = if (keyModel.isSpecial) theme.keySpecialTextColor else theme.keyTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            if (!hideHints && keyModel.popupCandidates.isNotEmpty()) {
                Text(
                    text = keyModel.popupCandidates.first(),
                    color = theme.keyHintColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 3.dp)
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
