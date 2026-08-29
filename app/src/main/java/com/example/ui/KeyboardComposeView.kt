package com.example.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.clipboard.ClipItem
import com.example.clipboard.ClipboardManager
import com.example.ime.SpeedMeterPhase
import com.example.theme.InfoBoxFrame
import com.example.theme.InfoBoxFramePreset
import com.example.theme.KeyboardTheme
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import java.util.Locale
import kotlin.math.abs
import org.json.JSONArray
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
    spacebarLanguageSwitchEnabled: Boolean = false,
    enableArabic: Boolean = true,
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
    meterEnabled: Boolean = true,
    meterPosition: String = "right",
    meterDisplayMode: String = "speed",
    meterInterval: String = "5s",
    liveElapsedSec: Int = 0,
    meterPhase: SpeedMeterPhase = SpeedMeterPhase.WAITING,
    meterResultLines: List<String> = emptyList(),
    lastPressedWord: String = "",
    infoBoxFrame: String = "CLASSIC",
    infoBoxTextColor: String = "#00FF41",
    infoBoxInfoColor: String = "#00FF41",
    infoBoxCustomTextColor: String = "#FFFFFF",
    infoBoxCustomTexts: String = "[]",
    infoBoxCustomMode: String = "off",
    infoBoxCustomSec: Int = 5,
    infoBoxSwipeTimeoutSec: Int = 10,
    infoBoxEnabled: Boolean = true,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
    infoBoxFont: String = "DEFAULT",
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
    onBackspaceWord: () -> Unit = {},
    onSpaceTap: () -> Unit,
    onEnterTap: () -> Unit,
    onShiftTap: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit,
    onSymbolToggle: () -> Unit = {},
    onSuggestionSelect: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onThemeToggle: () -> Unit,
    onOpenSettings: () -> Unit,
    onCursorMove: (Int) -> Unit = {},
    onHoldPaste: (() -> Unit)? = null
) {
    val popupAutoDismissMs = when (popupDismissDelay) {
        "Short" -> 1500L
        "Long" -> 3000L
        else -> Long.MAX_VALUE
    }
    val config = LocalConfiguration.current
    val haptic = LocalHapticFeedback.current
    val hasPhysicalKeyboard = config.keyboard == android.content.res.Configuration.KEYBOARD_QWERTY
    val effectiveShowEmojiKey = showEmojiKey || (physicalKbEmojiEnabled && hasPhysicalKeyboard)
    // TAP POPUP state (letter-preview bubble on key press):
    // - tapPopupChar: character currently shown in the bubble (null = no bubble)
    // - tapPopupSeq: increments on every key tap; restarts the dismiss timer
    // - tappedKeyCoords: the LayoutCoordinates of the key that was tapped — the bubble is
    //   anchored to THIS key (not to any other key) so it always appears over the right key.
    //   Stored as LayoutCoordinates so boundsInRoot() is re-read fresh when the bubble places.
    // - keysBoxOrigin/keysBoxSize: bounds of the keyboard grid Box, used to convert the
    //   key's root coordinates into grid-local coordinates and to clamp the bubble inside.
    var longPressKey by remember { mutableStateOf<KeyModel?>(null) }
    var tapPopupChar by remember { mutableStateOf<String?>(null) }
    var tapPopupSeq by remember { mutableIntStateOf(0) }
    var tappedKeyCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var keysBoxOrigin by remember { mutableStateOf(Offset.Zero) }
    var keysBoxSize by remember { mutableStateOf(IntSize.Zero) }
    // UNIFIED HEADER state: keyboard opens with the toolbar. While typing the toolbar is
    // swapped for the suggestion strip and does NOT come back on its own — only the strip's
    // toggle button brings it back. If the keyboard stays idle for toolbarAutoShowDelay
    // seconds (no typing), the toolbar auto-appears again. One header at a time.
    var isToolbarHeaderVisible by remember { mutableStateOf(true) }
    // IMPORTANT: "typing" here means the user is actively composing text. It MUST NOT be
    // driven by the suggestions list — the IME keeps next-word predictions non-empty even
    // when idle, so a suggestions-based check would keep isTyping true forever and the
    // toolbar would never auto-show.
    val isUserTyping = composingText.isNotEmpty()
    // The spacebar swipe/long-press gesture handler lives for the whole keyboard session
    // (its pointerInput key never changes), so it captures the language-switch callback
    // ONCE. Without this, every later swipe would keep using the ORIGINAL mode and the
    // language would appear "stuck" (swipe left once, then nothing; reverse swipe needed).
    // rememberUpdatedState keeps the captured callback reading the CURRENT mode.
    val currentModeState by rememberUpdatedState(mode)
    // Language-switch popup: shows the new language above the spacebar (like Gboard)
    // whenever the spacebar swipe/long-press switches languages. Auto-hides after ~1s.
    var languageSwitchPopupLabel by remember { mutableStateOf<String?>(null) }
    var lastSwipeDirection by remember { mutableIntStateOf(1) } // 1: Next (from Right), -1: Prev (from Left)
    
    // We keep a persistent copy of the label for the sliding animation in the popup
    // so it doesn't reset when languageSwitchPopupLabel briefly becomes null or new.
    var persistentPopupLabel by remember { mutableStateOf("") }
    LaunchedEffect(languageSwitchPopupLabel) {
        languageSwitchPopupLabel?.let { persistentPopupLabel = it }
        if (languageSwitchPopupLabel != null) {
            delay(1000)
            languageSwitchPopupLabel = null
        }
    }
    LaunchedEffect(enableArabic) {
        // If Arabic is turned off while the Arabic layout is on screen, jump back
        // to Bangla so a disabled language never stays visible on the keyboard.
        if (!enableArabic && mode == KeyboardMode.ARABIC) onModeChange(KeyboardMode.BANGLA_JATIYO)
    }
    // Pinned state: when the user taps the strip's toggle button WHILE typing, the
    // toolbar must stay up even though composing is active (otherwise the auto-hide
    // effect immediately reverts the toggle and the toolbar icons feel dead while
    // typing). Pinning only lasts until a NEW typing burst starts (idle -> typing);
    // a burst that starts while already typing is NOT new (the user may have pinned
    // the toolbar mid-word), so the toolbar stays.
    var toolbarPinned by remember { mutableStateOf(false) }
    var prevUserTyping by remember { mutableStateOf(false) }
    LaunchedEffect(isUserTyping, isToolbarHeaderVisible, unifiedHeader, toolbarAutoShowDelay, toolbarPinned) {
        if (!unifiedHeader) return@LaunchedEffect
        if (isUserTyping) {
            // Actively composing -> hide the toolbar (unless pinned), the strip takes over.
            if (!prevUserTyping) toolbarPinned = false
            prevUserTyping = true
            if (!toolbarPinned) isToolbarHeaderVisible = false
        } else {
            prevUserTyping = false
            if (!isToolbarHeaderVisible) {
                // Not composing: if the toolbar is currently hidden, bring it back after
                // toolbarAutoShowDelay seconds of idle (checked again before showing so a
                // keystroke right before the timeout cancels the auto-show).
                delay(toolbarAutoShowDelay * 1000L)
                if (composingText.isEmpty()) isToolbarHeaderVisible = true
            }
        }
    }
    val orientation = config.orientation
    val effectiveHeight = if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) keyboardHeightLandscape else keyboardHeightPortrait
    val effectiveOneHanded = if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) oneHandedWidthLandscape else oneHandedWidth

    val infoFrame = remember(infoBoxFrame) {
        try { InfoBoxFrame.fromPreset(InfoBoxFramePreset.valueOf(infoBoxFrame)) } catch (_: Exception) { InfoBoxFrame.Classic }
    }
    val infoTextColor = remember(infoBoxTextColor, infoFrame) {
        try { Color(android.graphics.Color.parseColor(infoBoxTextColor)) } catch (_: Exception) { infoFrame.defaultTextColor }
    }
    val infoInfoColor = remember(infoBoxInfoColor, infoFrame) {
        try { Color(android.graphics.Color.parseColor(infoBoxInfoColor)) } catch (_: Exception) { infoFrame.defaultTextColor }
    }
    val infoCustomTextColor = remember(infoBoxCustomTextColor) {
        try { Color(android.graphics.Color.parseColor(infoBoxCustomTextColor)) } catch (_: Exception) { Color.White }
    }
    val infoCustomTexts = remember(infoBoxCustomTexts) {
        try {
            JSONArray(infoBoxCustomTexts).let { arr -> List(arr.length()) { arr.getString(it) } }
                .filter { it.isNotBlank() }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // Info Box display state is hoisted here (instead of inside InfoBox) so the swipe-info
    // sequence survives header swaps (unified header) and only one sequence runs at a time.
    var infoBoxDisplayText by remember { mutableStateOf("") }
    var infoBoxCustomPhase by remember { mutableStateOf(false) }
    var infoBoxKeyPressActive by remember { mutableStateOf(false) }
    val infoBoxMainTextColor = if (infoBoxKeyPressActive) infoTextColor else infoInfoColor

    // Swipe-info + custom-text sequence. Deliberately NOT keyed on lastPressedWord:
    // per-keypress word updates must never restart a running RESULT sequence.
    LaunchedEffect(meterPhase, meterResultLines, infoCustomTexts, infoBoxCustomMode, infoBoxCustomSec, infoBoxSwipeTimeoutSec) {
        if (meterPhase != SpeedMeterPhase.RESULT) return@LaunchedEffect
        infoBoxDisplayText = ""
        infoBoxKeyPressActive = false

        val timeoutMillis = infoBoxSwipeTimeoutSec.coerceAtLeast(1) * 1000L
        val start = System.currentTimeMillis()
        val hasCustoms = infoCustomTexts.isNotEmpty() && infoBoxCustomMode != "off"
        val perText = infoBoxCustomSec.coerceAtLeast(1) * 1000L

        if (hasCustoms) {
            if (infoBoxCustomMode == "always") {
                while (true) {
                    // Default speed info first (info text color)
                    infoBoxCustomPhase = false
                    for (i in meterResultLines.indices) {
                        infoBoxDisplayText = meterResultLines[i]
                        delay(1600)
                    }
                    // Then custom texts (custom text color)
                    infoBoxCustomPhase = true
                    for (t in infoCustomTexts) {
                        infoBoxDisplayText = t
                        delay(perText)
                    }
                }
            } else {
                // Timed: default info first, then custom texts once, then hold
                infoBoxCustomPhase = false
                for (i in meterResultLines.indices) {
                    infoBoxDisplayText = meterResultLines[i]
                    delay(1600)
                }
                infoBoxCustomPhase = true
                for (t in infoCustomTexts) {
                    infoBoxDisplayText = t
                    delay(perText)
                }
                infoBoxCustomPhase = false
                val held = System.currentTimeMillis() - start
                if (held < timeoutMillis) delay(timeoutMillis - held)
                infoBoxDisplayText = ""
            }
            return@LaunchedEffect
        }

        // No customs: play the swipe info sequence, one line per 1.6s fade cycle.
        for (i in meterResultLines.indices) {
            infoBoxDisplayText = meterResultLines[i]
            if (i == meterResultLines.lastIndex) {
                val held = System.currentTimeMillis() - start
                if (held < timeoutMillis) delay(timeoutMillis - held)
                infoBoxDisplayText = ""
                return@LaunchedEffect
            }
            delay(1600)
        }
    }

    // Live pressed word (shown per key press while typing) and WAITING clearing.
    LaunchedEffect(meterPhase, lastPressedWord) {
        when (meterPhase) {
            SpeedMeterPhase.LIVE -> {
                infoBoxCustomPhase = false
                infoBoxKeyPressActive = true
                infoBoxDisplayText = lastPressedWord
            }
            SpeedMeterPhase.WAITING -> {
                infoBoxCustomPhase = false
                infoBoxKeyPressActive = false
                infoBoxDisplayText = ""
            }
            else -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = theme.backgroundColor
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                            meterEnabled = meterEnabled,
                            meterPosition = meterPosition,
                            meterPhase = meterPhase,
                            infoFrame = infoFrame,
                            infoTextColor = infoBoxMainTextColor,
                            infoCustomTextColor = infoCustomTextColor,
                            infoBoxText = infoBoxDisplayText,
                            infoCustomActive = infoBoxCustomPhase,
                            infoBoxEnabled = infoBoxEnabled,
                            meterDisplayMode = meterDisplayMode,
                            meterInterval = meterInterval,
                            liveElapsedSec = liveElapsedSec,
                            meterTheme = meterTheme,
                            meterFont = meterFont,
                            infoBoxFont = infoBoxFont,
                            enableArabic = enableArabic,
                            onModeChange = onModeChange,
                            onVoiceClick = onVoiceClick,
                            onThemeToggle = onThemeToggle,
                            onOpenSettings = onOpenSettings
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
                            showMeter = unifiedHeader && meterEnabled,
                            meterPosition = meterPosition,
                            meterPhase = meterPhase,
                            infoFrame = infoFrame,
                            infoTextColor = infoBoxMainTextColor,
                            infoCustomTextColor = infoCustomTextColor,
                            infoBoxText = infoBoxDisplayText,
                            infoCustomActive = infoBoxCustomPhase,
                            infoBoxEnabled = infoBoxEnabled,
                            meterDisplayMode = meterDisplayMode,
                            meterInterval = meterInterval,
                            liveElapsedSec = liveElapsedSec,
                            liveCps = if (isSpeedActive) liveCps else 0f,
                            meterTheme = meterTheme,
                            meterFont = meterFont,
                            infoBoxFont = infoBoxFont,
                            onShowToolbar = { toolbarPinned = true; isToolbarHeaderVisible = true }
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
                        // Language quick-switcher: hold-then-swipe ONLY on the spacebar
                        // cycles through enabled languages. Hold firmly (~380ms, like
                        // Gboard/others apk) then swipe left/right; both directions work
                        // (left = next, right = previous). Holding without swiping does
                        // NOT change language; quick flick without proper hold is ignored.
                        // Popup shows above spacebar.
                        val cycleLanguage: (Int) -> Unit = { direction ->
                            // NOTE: Arabic is only offered when the user enables it in
                            // Settings -> More Languages -> Arabic. Keep this conditional —
                            // without it, disabling Arabic would still let the spacebar
                            // cycle into the Arabic layout.
                            val enabledModes = mutableListOf(
                                KeyboardMode.ENGLISH,
                                KeyboardMode.BANGLA_JATIYO,
                                KeyboardMode.AVRO
                            )
                            if (enableArabic) enabledModes.add(KeyboardMode.ARABIC)
                            val currentIndex = enabledModes.indexOf(currentModeState).coerceAtLeast(0)
                            val nextMode = enabledModes[
                                ((currentIndex + direction) % enabledModes.size + enabledModes.size) % enabledModes.size
                            ]
                            
                            // Capture direction for animation:
                            // direction 1 = Next (Enter from Right), direction -1 = Prev (Enter from Left)
                            lastSwipeDirection = direction
                            
                            // Smooth haptic + visual feedback for language switch
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onModeChange(nextMode)
                            languageSwitchPopupLabel = when (nextMode) {
                                KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                                KeyboardMode.AVRO -> "Avro"
                                KeyboardMode.ARABIC -> "عربي"
                                else -> "English"
                            }
                        }
                        KeyboardKeysGrid(
                            mode = mode,
                            lastTextMode = lastTextMode,
                            shiftState = shiftState,
                            theme = adjustedTheme,
                            actionLabel = actionLabel,
                            showNumberRow = showNumberRow,
                            hideLongPressHints = hideLongPressHints,
                            moveCursorSpaceEnabled = moveCursorSpaceEnabled,
                            spacebarLanguageSwitchEnabled = spacebarLanguageSwitchEnabled,
                            largeNumberRowEnabled = largeNumberRowEnabled,
                            longPressDelayMs = longPressDelayMs,
                            spaceCursorSpeed = spaceCursorSpeed,
                            spaceCursorDelay = spaceCursorDelay,
                            splitKeyboardEnabled = splitKeyboardEnabled,
                            backspaceRepeatDelayMs = backspaceRepeatDelayMs,
                            backspaceRepeatSpeedMs = backspaceRepeatSpeedMs,
                            lastSwipeDirection = lastSwipeDirection,
                            // Tap popup trigger. onKeyTap fires for the letter itself; we ALSO
                            // bump tapPopupSeq + set tapPopupChar (toggle on) so the bubble
                            // shows. Real input still goes through onKeyTap(char).
                            onKeyTap = { char ->
                                if (popupOnKeypressEnabled) {
                                    tapPopupSeq++
                                    tapPopupChar = char
                                }
                                onKeyTap(char)
                            },
                            // Captures the LayoutCoordinates of the ACTUAL key tapped. The key
                            // reports its own coords at tap time, so the bubble is anchored to
                            // the right key even when several keys recompose/layout.
                            onKeyTapWithCoords = { _, coords ->
                                tappedKeyCoords = coords
                            },
                            onBackspaceTap = onBackspaceTap,
                            onBackspaceWord = onBackspaceWord,
                            onSpaceTap = onSpaceTap,
                            onEnterTap = onEnterTap,
                            onShiftTap = onShiftTap,
                            onModeChange = onModeChange,
                            onCursorMove = onCursorMove,
                            onLongPress = { key ->
                                tapPopupChar = null
                                if (holdPasteEnabled && key.code == holdPasteTriggerKey) {
                                    onHoldPaste?.invoke()
                                } else {
                                    longPressKey = key
                                }
                            },
                            onSpacebarLongPress = {
                                // Hold-alone no longer changes language — only hold+swipe does.
                                // Kept as no-op to satisfy the parameter; swipe handles cycling.
                            },
                            onSpacebarSwipe = { direction -> cycleLanguage(direction) },
                            onSpacebarHold = {
                                // Hold alone no longer shows language — only swipe shows new language
                            },
                            onSymbolToggle = onSymbolToggle
                        )

                        // Language-switch popup — shows the current language above the
                        // spacebar when the language changes via spacebar swipe/long-press
                        // (like Gboard).
                        if (spacebarLanguageSwitchEnabled) {
                            Box(
                                modifier = Modifier.matchParentSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = languageSwitchPopupLabel != null,
                                    enter = fadeIn() + androidx.compose.animation.scaleIn(initialScale = 0.8f),
                                    exit = fadeOut() + androidx.compose.animation.scaleOut(targetScale = 0.8f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = (adjustedTheme.keyHeightDp + 12).dp)
                                            .shadow(12.dp, RoundedCornerShape(16.dp))
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(theme.popupBackgroundColor)
                                            .padding(horizontal = 20.dp, vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        androidx.compose.animation.AnimatedContent(
                                            targetState = persistentPopupLabel,
                                            transitionSpec = {
                                                val direction = lastSwipeDirection
                                                (androidx.compose.animation.slideInHorizontally { width -> direction * width } + fadeIn()).togetherWith(
                                                    androidx.compose.animation.slideOutHorizontally { width -> -direction * width } + fadeOut())
                                                    .using(androidx.compose.animation.SizeTransform(clip = false))
                                            },
                                            label = "popupLabelAnimation"
                                        ) { labelText ->
                                            Text(
                                                text = labelText,
                                                color = theme.popupTextColor,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAP POPUP (Popup on keypress) — bubble shown above the tapped key.
            // Lifecycle: shows instantly on tap, auto-dismisses after a short delay.
            // Duration is short (130–260ms) so it feels snappy like Gboard/SwiftKey;
            // the popupDismissDelay setting only affects the SEPARATE long-press popup below.
            if (popupOnKeypressEnabled && tapPopupSeq > 0) {
                tapPopupChar?.let { char ->
                    val tapPopupDismissMs = when (popupDismissDelay) {
                        "Short" -> 130L
                        "Long" -> 260L
                        else -> 160L
                    }
                    // Restart the dismiss timer on every new tap (key = tapPopupSeq),
                    // cancel the previous pending hide (if any).
                    LaunchedEffect(tapPopupSeq) {
                        delay(tapPopupDismissMs)
                        tapPopupChar = null
                    }
                    // Wrapper Box: matchParentSize fills the keyboard grid Box. It records
                    // its own root-origin so we can convert key coordinates (measured in
                    // root space) into grid-local offsets for the bubble below.
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .onGloballyPositioned { keysBoxOrigin = it.boundsInRoot().topLeft; keysBoxSize = it.size },
                        contentAlignment = Alignment.TopStart
                    ) {
                        // The bubble itself. Positioned via .offset {}:
                        //   x = key center, clamped to stay inside the keyboard width.
                        //   y = just above the key (bottom of bubble 2dp above key top);
                        //       for top-row keys with no room above, it clamps to the
                        //       keyboard top and barely overlaps the key (like Gboard).
                        Box(
                            modifier = Modifier
                                .offset {
                                    val anchor = tappedKeyCoords?.boundsInRoot()?.topLeft ?: Offset.Zero
                                    val keyW = (tappedKeyCoords?.boundsInRoot()?.size?.width ?: 0).toFloat()
                                    val popupW = 56.dp.toPx()
                                    val popupH = 56.dp.toPx()
                                    val safeMaxX = (keysBoxSize.width - popupW - 4.dp.toPx()).coerceAtLeast(4.dp.toPx())
                                    val x = (anchor.x - keysBoxOrigin.x + keyW / 2f - popupW / 2f)
                                        .coerceIn(4.dp.toPx(), safeMaxX)
                                    val y = (anchor.y - keysBoxOrigin.y - popupH - 2.dp.toPx())
                                        .coerceAtLeast(4.dp.toPx())
                                    IntOffset(x.toInt(), y.toInt())
                                }
                                .size(56.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.popupBackgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                color = theme.popupTextColor,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
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
    enableArabic: Boolean = true,
    liveCps: Float = 0f,
    maxBurstCps: Float = 0f,
    isSpeedActive: Boolean = false,
    meterEnabled: Boolean = true,
    meterPosition: String = "right",
    meterPhase: SpeedMeterPhase = SpeedMeterPhase.WAITING,
    infoFrame: InfoBoxFrame = InfoBoxFrame.Classic,
    infoTextColor: Color = Color(0xFF00FF41),
    infoCustomTextColor: Color = Color.White,
    infoBoxText: String = "",
    infoCustomActive: Boolean = false,
    infoBoxEnabled: Boolean = true,
    meterDisplayMode: String = "speed",
    meterInterval: String = "5s",
    liveElapsedSec: Int = 0,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
    infoBoxFont: String = "DEFAULT",
    onModeChange: (KeyboardMode) -> Unit,
    onVoiceClick: () -> Unit,
    onThemeToggle: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(theme.suggestionBgColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Speed meter + info box (left position)
        if (meterEnabled && meterPosition == "left") {
            MeterHeaderPair(
                cps = if (isSpeedActive) liveCps else maxBurstCps,
                isLive = isSpeedActive,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
        }

        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showGlobeKey) {
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
                        // Same rule as the spacebar cycle: skip Arabic entirely when the
                        // user has disabled it in Settings -> More Languages.
                        val cycleModes = mutableListOf(
                            KeyboardMode.ENGLISH,
                            KeyboardMode.BANGLA_JATIYO,
                            KeyboardMode.AVRO
                        )
                        if (enableArabic) cycleModes.add(KeyboardMode.ARABIC)
                        val currentIdx = cycleModes.indexOf(currentMode).coerceAtLeast(0)
                        val nextMode = cycleModes[(currentIdx + 1) % cycleModes.size]
                        onModeChange(nextMode)
                    }
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
        }

        // Speed meter + info box (middle: centered between tool icons and the settings button)
        if (meterEnabled && meterPosition == "middle") {
            Spacer(modifier = Modifier.weight(1f))
            MeterHeaderPair(
                cps = if (isSpeedActive) liveCps else maxBurstCps,
                isLive = isSpeedActive,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Speed meter + info box (right position, default)
        if (meterEnabled && meterPosition != "left" && meterPosition != "middle") {
            MeterHeaderPair(
                cps = if (isSpeedActive) liveCps else maxBurstCps,
                isLive = isSpeedActive,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
        }

        AnimatedTapIcon(
            icon = Icons.Default.Settings,
            contentDescription = stringResource(R.string.kb_settings),
            tint = theme.keySpecialTextColor,
            modifier = Modifier.size(40.dp)
        ) {
            onOpenSettings()
        }
    }
}

@Composable
fun DigitalSpeedMeter(
    cps: Float,
    isLive: Boolean,
    phase: SpeedMeterPhase = SpeedMeterPhase.LIVE,
    meterDisplayMode: String = "speed",
    meterInterval: String = "5s",
    liveElapsedSec: Int = 0,
    meterTheme: MeterTheme,
    fontStyle: String = "DIGITAL"
) {
    val textStyle = remember(fontStyle, meterTheme) {
        when (fontStyle) {
            "LCD" -> androidx.compose.ui.text.TextStyle(
                fontFamily = com.example.theme.meterFontFamily("LCD"),
                letterSpacing = 2.sp,
                fontFeatureSettings = "tnum"
            )
            "SEGMENT" -> androidx.compose.ui.text.TextStyle(
                fontFamily = com.example.theme.meterFontFamily("SEGMENT"),
                letterSpacing = 1.5.sp,
                fontFeatureSettings = "tnum"
            )
            "MODERN" -> androidx.compose.ui.text.TextStyle(
                fontFamily = com.example.theme.meterFontFamily("MODERN"),
                letterSpacing = 1.sp,
                fontFeatureSettings = "tnum"
            )
            else -> androidx.compose.ui.text.TextStyle( // DIGITAL
                fontFamily = com.example.theme.meterFontFamily("DIGITAL"),
                letterSpacing = meterTheme.letterSpacing,
                fontFeatureSettings = "tnum"
            )
        }
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .widthIn(min = 40.dp, max = 56.dp)
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
                    fontSize = 11.sp,
                    style = textStyle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                if (phase == SpeedMeterPhase.LIVE || phase == SpeedMeterPhase.RESULT) {
                    val isCounter = phase == SpeedMeterPhase.LIVE && meterDisplayMode == "counter"
                    Text(
                        text = if (isCounter) "$liveElapsedSec" else String.format(Locale.US, "%.1f", cps),
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
                        text = when {
                            isCounter -> stringResource(R.string.meter_unit_sec)
                            isLive -> stringResource(R.string.kb_live)
                            else -> stringResource(R.string.kb_peak)
                        },
                        color = meterTheme.labelColor,
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                } else {
                    Text(
                        text = "00",
                        color = meterTheme.textColor.copy(alpha = 0.18f),
                        fontSize = 12.sp,
                        style = textStyle
                    )
                }
            }
        }
    }
    }
}

@Composable
fun MeterHeaderPair(
    cps: Float,
    isLive: Boolean,
    meterPhase: SpeedMeterPhase,
    meterTheme: MeterTheme,
    meterFont: String,
    infoFrame: InfoBoxFrame,
    infoTextColor: Color,
    infoCustomTextColor: Color,
    infoBoxText: String,
    infoCustomActive: Boolean,
    infoBoxEnabled: Boolean,
    meterDisplayMode: String,
    meterInterval: String,
    liveElapsedSec: Int,
    infoBoxFont: String = "DEFAULT"
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.widthIn(max = 180.dp)) {
        DigitalSpeedMeter(
            cps = cps,
            isLive = isLive,
            phase = meterPhase,
            meterDisplayMode = meterDisplayMode,
            meterInterval = meterInterval,
            liveElapsedSec = liveElapsedSec,
            meterTheme = meterTheme,
            fontStyle = meterFont
        )
        if (infoBoxEnabled) {
            InfoBox(
                text = infoBoxText,
                customActive = infoCustomActive,
                frame = infoFrame,
                textColor = infoTextColor,
                customTextColor = infoCustomTextColor,
                fontStyle = infoBoxFont
            )
        }
    }
}

@Composable
fun InfoBox(
    text: String,
    frame: InfoBoxFrame,
    textColor: Color,
    customActive: Boolean = false,
    customTextColor: Color = Color.White,
    fontStyle: String = "DEFAULT"
) {
    val displayColor = if (customActive) customTextColor else textColor
    val fontFamily = com.example.theme.meterFontFamily(fontStyle)

    Surface(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .widthIn(min = 44.dp, max = 100.dp)
            .height(30.dp),
        color = frame.backgroundColor.copy(alpha = frame.backgroundAlpha),
        shape = RoundedCornerShape(frame.cornerRadius),
        border = androidx.compose.foundation.BorderStroke(frame.borderWidth, frame.borderColor),
        shadowElevation = frame.glowRadius.dp,
        tonalElevation = 4.dp
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = { (fadeIn(tween(300)) togetherWith fadeOut(tween(300))) },
            label = "infoBoxText"
        ) { target ->
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (target.isNotEmpty()) {
                    Text(
                        text = target,
                        color = displayColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        fontFamily = fontFamily,
                        letterSpacing = 0.3.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedTapIcon(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Button, onClick = {
                onClick()
                scope.launch { scale.playIconPop() }
            })
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .semantics { this.contentDescription = contentDescription ?: ""; role = Role.Button },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

suspend fun Animatable<Float, AnimationVector1D>.playIconPop() {
    snapTo(1f)
    animateTo(0.82f, tween(durationMillis = 60))
    animateTo(1f, tween(durationMillis = 90))
}

@Composable
fun ToolbarIcon(icon: ImageVector, contentDescription: String, active: Boolean, theme: KeyboardTheme, onClick: () -> Unit) {
    AnimatedTapIcon(
        icon = icon,
        contentDescription = contentDescription,
        tint = if (active) theme.accentColor else theme.keyTextColor.copy(alpha = 0.8f),
        modifier = Modifier
            .size(34.dp)
            .background(if (active) theme.accentColor.copy(alpha = 0.25f) else Color.Transparent),
        onClick = onClick
    )
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
    showMeter: Boolean = false,
    meterPosition: String = "right",
    meterPhase: SpeedMeterPhase = SpeedMeterPhase.WAITING,
    infoFrame: InfoBoxFrame = InfoBoxFrame.Classic,
    infoTextColor: Color = Color(0xFF00FF41),
    infoCustomTextColor: Color = Color.White,
    infoBoxText: String = "",
    infoCustomActive: Boolean = false,
    infoBoxEnabled: Boolean = true,
    meterDisplayMode: String = "speed",
    meterInterval: String = "5s",
    liveElapsedSec: Int = 0,
    liveCps: Float = 0f,
    meterTheme: MeterTheme = MeterTheme.Calculator,
    meterFont: String = "DIGITAL",
    infoBoxFont: String = "DEFAULT",
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
        // Keep the live meter always visible in the header (unified header while typing).
        if (showMeter && meterPosition == "left") {
            MeterHeaderPair(
                cps = if (meterPhase == SpeedMeterPhase.LIVE) liveCps else 0f,
                isLive = meterPhase == SpeedMeterPhase.LIVE,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

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

        // Speed meter + info box (middle: centered after the suggestions row)
        if (showMeter && meterPosition == "middle") {
            Spacer(modifier = Modifier.weight(1f))
            MeterHeaderPair(
                cps = if (meterPhase == SpeedMeterPhase.LIVE) liveCps else 0f,
                isLive = meterPhase == SpeedMeterPhase.LIVE,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Speed meter + info box (right position, default)
        if (showMeter && meterPosition != "left" && meterPosition != "middle") {
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            MeterHeaderPair(
                cps = if (meterPhase == SpeedMeterPhase.LIVE) liveCps else 0f,
                isLive = meterPhase == SpeedMeterPhase.LIVE,
                meterPhase = meterPhase,
                meterTheme = meterTheme,
                meterFont = meterFont,
                infoBoxFont = infoBoxFont,
                infoFrame = infoFrame,
                infoTextColor = infoTextColor,
                infoCustomTextColor = infoCustomTextColor,
                infoBoxText = infoBoxText,
                infoCustomActive = infoCustomActive,
                infoBoxEnabled = infoBoxEnabled,
                meterDisplayMode = meterDisplayMode,
                meterInterval = meterInterval,
                liveElapsedSec = liveElapsedSec
            )
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
    spacebarLanguageSwitchEnabled: Boolean = false,
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
    lastSwipeDirection: Int = 1,
    onKeyTap: (String) -> Unit,
    onBackspaceTap: () -> Unit,
    onBackspaceWord: () -> Unit = {},
    onSpaceTap: () -> Unit,
    onEnterTap: () -> Unit,
    onShiftTap: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit,
    onSymbolToggle: () -> Unit = {},
    onCursorMove: (Int) -> Unit = {},
    onLongPress: (KeyModel) -> Unit = {},
    onSpacebarLongPress: () -> Unit = {},
    onSpacebarSwipe: (Int) -> Unit = {},
    onSpacebarHold: () -> Unit = {},
    onKeyTapWithCoords: (String, LayoutCoordinates) -> Unit = { _, _ -> }
) {
    val hapticKeys = LocalHapticFeedback.current
    var totalDragX by remember { mutableStateOf(0f) }
    val dragThreshold = ((200f - spaceCursorSpeed * 0.35f) + spaceCursorDelay * 0.02f).coerceIn(15f, 180f)
    val iconAnimationScope = rememberCoroutineScope()
    val shiftIconScale = remember { Animatable(1f) }
    val backspaceIconScale = remember { Animatable(1f) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp, vertical = 2.dp)
                .padding(bottom = 6.dp),
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
                        onLongPress = { onLongPress(keyModel) },
                        onTapWithCoords = { char, coords -> onKeyTapWithCoords(char, coords) }
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
                        onLongPress = { onLongPress(keyModel) },
                        onTapWithCoords = { char, coords -> onKeyTapWithCoords(char, coords) }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(modifier = Modifier.weight(1.3f), theme = theme, isSpecial = shiftState != ShiftState.OFF, longPressDelayMs = longPressDelayMs, onClick = {
                iconAnimationScope.launch { shiftIconScale.playIconPop() }
                onShiftTap()
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = if (shiftState == ShiftState.CAPS_LOCK) theme.accentColor else theme.keyTextColor,
                    modifier = Modifier.graphicsLayer {
                        scaleX = shiftIconScale.value
                        scaleY = shiftIconScale.value
                    }
                )
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
                    onLongPress = { onLongPress(keyModel) },
                    onTapWithCoords = { char, coords -> onKeyTapWithCoords(char, coords) }
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
                                    // Gboard-style repeat: after the hold-delay, keys repeat
                                    // CHARACTER by CHARACTER at backspaceRepeatSpeedMs so the
                                    // speed setting is clearly visible. Holding much longer
                                    // (1500 ms past the delay) switches to word-by-word so a
                                    // long sentence can still be cleared fast.
                                    val wordPhaseStart = System.currentTimeMillis() + 1500L
                                    while (isActive) {
                                        iconAnimationScope.launch { backspaceIconScale.playIconPop() }
                                        if (System.currentTimeMillis() >= wordPhaseStart) {
                                            onBackspaceWord()
                                        } else {
                                            onBackspaceTap()
                                        }
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
                            if (!didRepeat) {
                                iconAnimationScope.launch { backspaceIconScale.playIconPop() }
                                onBackspaceTap()
                            }
                        }
                    }
                    .semantics { contentDescription = deleteLabel; role = Role.Button },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = null,
                    tint = theme.keySpecialTextColor,
                    modifier = Modifier.graphicsLayer {
                        scaleX = backspaceIconScale.value
                        scaleY = backspaceIconScale.value
                    }
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            KeyButton(
                modifier = Modifier.weight(1.2f),
                theme = theme,
                isSpecial = true,
                longPressDelayMs = longPressDelayMs,
                onClick = {
                    hapticKeys.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSymbolToggle()
                }
            ) {
                // Show return language so user knows where ?123 will go back to
                val buttonText = if (mode == KeyboardMode.SYMBOLS || mode == KeyboardMode.NUMBERS) {
                    when (effectiveTextMode) {
                        KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                        KeyboardMode.AVRO -> "Avro"
                        KeyboardMode.ARABIC -> "عربي"
                        else -> "ABC"
                    }
                } else {
                    "?123"
                }
                androidx.compose.animation.AnimatedContent(
                    targetState = buttonText,
                    transitionSpec = { fadeIn(tween(120)) togetherWith fadeOut(tween(120)) },
                    label = "symbolToggle"
                ) { text ->
                    Text(text = text, color = theme.keySpecialTextColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            KeyButton(modifier = Modifier.weight(1f), theme = theme, isSpecial = false, longPressDelayMs = longPressDelayMs, onClick = { onKeyTap(",") }) {
                Text(text = ",", color = theme.keyTextColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            val spacebarLabel = when (mode) {
                KeyboardMode.BANGLA_JATIYO -> "বাংলা"
                KeyboardMode.BANGLA_PHONETIC -> "Phonetic"
                KeyboardMode.AVRO -> "Avro"
                KeyboardMode.ARABIC -> "عربي"
                else -> "English"
            }
            if (spacebarLanguageSwitchEnabled) {
                // Custom spacebar with FULL gesture ownership — a single pointer handler
                // drives tap / hold-then-swipe ONLY. Hold firmly (~380ms, like
                // Gboard/others apk) then swipe to change language; hold alone does
                // NOT change language (prevents accidental switches). Quick flick
                // without proper hold is ignored. Uses plain event-loop tracking,
                // not combinedClickable.
                var spacebarPressed by remember { mutableStateOf(false) }
                val spacebarScale by animateFloatAsState(
                    targetValue = if (spacebarPressed) 0.92f else 1f,
                    animationSpec = if (spacebarPressed) tween(60) else tween(90)
                )
                val spaceContentDescription = stringResource(R.string.kb_space)
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .height(theme.keyHeightDp.dp)
                        .clip(RoundedCornerShape(theme.keyRadiusDp.dp))
                        .background(if (spacebarPressed) theme.keyBackgroundColor.copy(alpha = 0.8f) else theme.keyBackgroundColor)
                        .pointerInput(longPressDelayMs) {
                            // Hold-then-swipe ONLY: language changes ONLY when you hold
                            // the spacebar firmly (~380ms, like Gboard/others apk) and
                            // THEN swipe. Holding without swiping does NOT change
                            // language (just inserts space on release). Quick flick
                            // without proper hold is ignored to prevent accidental
                            // switches — matches professional keyboards.
                            val swipeThreshold = 24.dp.toPx()
                            val holdGateMs = longPressDelayMs.coerceIn(380L, 500L)
                            while (true) {
                                val down = awaitPointerEventScope { awaitFirstDown(requireUnconsumed = false) }
                                spacebarPressed = true
                                coroutineScope {
                                    var handled = false
                                    var hasHeld = false
                                    val holdGateJob = launch {
                                        delay(holdGateMs)
                                        hasHeld = true
                                        // Hold registered subtly — tick only, no language popup on hold alone
                                        try { hapticKeys.performHapticFeedback(HapticFeedbackType.LongPress) } catch (_: Exception) {}
                                    }
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                            if (!change.pressed) {
                                                if (!handled) onSpaceTap()
                                                break
                                            }
                                            val totalX = change.position.x - down.position.x
                                            // Only allow swipe AFTER the hold gate — hold alone does nothing.
                                            if (!handled && hasHeld && abs(totalX) > swipeThreshold) {
                                                handled = true
                                                holdGateJob.cancel()
                                                onSpacebarSwipe(if (totalX < 0) 1 else -1)
                                            }
                                        }
                                    }
                                    holdGateJob.cancel()
                                }
                                spacebarPressed = false
                            }
                        }
                        .graphicsLayer {
                            scaleX = spacebarScale
                            scaleY = spacebarScale
                        }
                        .semantics { this.contentDescription = spaceContentDescription; role = Role.Button },
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedContent(
                        targetState = spacebarLabel,
                        transitionSpec = {
                            val direction = lastSwipeDirection
                            if (targetState != initialState) {
                                (androidx.compose.animation.slideInHorizontally { width -> direction * width } + fadeIn()).togetherWith(
                                    androidx.compose.animation.slideOutHorizontally { width -> -direction * width } + fadeOut())
                            } else {
                                fadeIn() togetherWith fadeOut()
                            }.using(androidx.compose.animation.SizeTransform(clip = false))
                        },
                        label = "spacebarLabel"
                    ) { label ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⟨", color = theme.keyTextColor.copy(alpha = 0.35f), fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = label, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "⟩", color = theme.keyTextColor.copy(alpha = 0.35f), fontSize = 14.sp)
                        }
                    }
                }
            } else {
                KeyButton(
                    modifier = Modifier.weight(4f).then(
                        if (moveCursorSpaceEnabled) Modifier.pointerInput(Unit) {
                            detectDragGestures(onDragStart = { totalDragX = 0f }, onDrag = { change, dragAmount ->
                                change.consume(); totalDragX += dragAmount.x
                                if (abs(totalDragX) > dragThreshold) { onCursorMove(if (totalDragX > 0) 1 else -1); totalDragX = 0f }
                            })
                        } else Modifier
                    ),
                    theme = theme, isSpecial = false, longPressDelayMs = longPressDelayMs,
                    onClick = onSpaceTap
                ) {
                    Text(text = spacebarLabel, color = theme.keyTextColor.copy(alpha = 0.6f), fontSize = 13.sp)
                }
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
    onLongPress: (() -> Unit)? = null,
    onTapWithCoords: (String, LayoutCoordinates) -> Unit = { _, _ -> }
) {
    val charToOutput = when (shiftState) {
        ShiftState.SHIFT, ShiftState.CAPS_LOCK -> keyModel.label.uppercase()
        ShiftState.OFF -> keyModel.label
    }
    var selfCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    KeyButton(
        modifier = modifier,
        theme = theme,
        isSpecial = keyModel.isSpecial,
        longPressDelayMs = longPressDelayMs,
        onClick = {
            onTap(charToOutput)
            selfCoords?.let { onTapWithCoords(charToOutput, it) }
        },
        onLongClick = onLongPress,
        onLayout = { selfCoords = it }
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
    onLayout: ((LayoutCoordinates) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // Snappy press feedback (no bounce): a long spring tail after EVERY key press is
    // what makes rapid typing feel laggy/floaty. Quick tweens like Gboard instead.
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = if (isPressed) tween(60) else tween(90)
    )
    val bgColor = if (isSpecial) theme.keySpecialColor else theme.keyBackgroundColor
    val pressedBg = if (isSpecial) theme.keySpecialColor.copy(alpha = 0.7f) else theme.keyBackgroundColor.copy(alpha = 0.8f)

    Box(
        modifier = modifier
            .height(theme.keyHeightDp.dp)
            .clip(RoundedCornerShape(theme.keyRadiusDp.dp))
            .background(if (isPressed) pressedBg else bgColor)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .semantics { this.contentDescription = contentDescription; role = Role.Button }
            .let { base ->
                if (onLayout != null) base.onGloballyPositioned { onLayout(it) } else base
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
