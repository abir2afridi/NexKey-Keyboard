package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clipboard.ClipboardManager
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val autoCap by prefs.autoCapitalize.collectAsState(initial = true)
    val doubleSpacePeriod by prefs.smartPunctuation.collectAsState(initial = true)
    val doubleSpaceTab by prefs.doubleSpaceTab.collectAsState(initial = false)

    SettingsSubScaffold(title = "Typing", onBack = onBack) {
        SettingSwitchItem("Auto-capitalization", "Capitalize the first word of each sentence", Icons.Default.TextFormat, autoCap) { scope.launch { prefs.setAutoCapitalize(it) } }
        SettingSwitchItem("Double-space period", "Double tap on spacebar inserts a period followed by a space", Icons.Default.SpaceBar, doubleSpacePeriod) { scope.launch { prefs.setSmartPunctuation(it) } }
        SettingSwitchItem("Double-space tab", "Double tap on spacebar inserts a tab", Icons.AutoMirrored.Filled.KeyboardTab, doubleSpaceTab) { scope.launch { prefs.setDoubleSpaceTab(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val haptics by prefs.haptics.collectAsState(initial = true)
    val sound by prefs.sound.collectAsState(initial = true)
    val popupOnKeypress by prefs.popupOnKeypress.collectAsState(initial = true)
    val hapticIntensity by prefs.hapticIntensity.collectAsState(initial = 50)
    val soundVol by prefs.soundVolume.collectAsState(initial = 50)

    SettingsSubScaffold(title = "Feedback", onBack = onBack) {
        SettingSwitchItem("Vibrate on keypress", null, Icons.Default.Vibration, haptics) { scope.launch { prefs.setHaptics(it) } }
        SettingSwitchItem("Sound on keypress", null, Icons.AutoMirrored.Filled.VolumeUp, sound) { scope.launch { prefs.setSound(it) } }
        SettingSwitchItem("Popup on keypress", null, Icons.AutoMirrored.Filled.Message, popupOnKeypress) { scope.launch { prefs.setPopupOnKeypress(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("Keypress vibration intensity", hapticIntensity.toFloat(), 0f..100f) { scope.launch { prefs.setHapticIntensity(it.toInt()) } }
        SettingSliderItem("Keypress sound volume", soundVol.toFloat(), 0f..100f) { scope.launch { prefs.setSoundVolume(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageKeysSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val voiceInputKey by prefs.voiceInputKey.collectAsState(initial = true)
    val showEmojiKey by prefs.showEmojiKey.collectAsState(initial = true)
    val showGlobeKey by prefs.showGlobeKey.collectAsState(initial = true)
    val allowOtherKeyboards by prefs.allowOtherKeyboards.collectAsState(initial = true)

    SettingsSubScaffold(title = "Language & Keys", onBack = onBack) {
        SettingSwitchItem("Voice input key", null, Icons.Default.Mic, voiceInputKey) { scope.launch { prefs.setVoiceInputKey(it) } }
        SettingSwitchItem("Show Emoji Key", "Switch to Emoji button", Icons.Default.EmojiEmotions, showEmojiKey) { scope.launch { prefs.setShowEmojiKey(it) } }
        SettingSwitchItem("Show Globe Key", "Switch keyboard language", Icons.Default.Language, showGlobeKey) { scope.launch { prefs.setShowGlobeKey(it) } }
        SettingSwitchItem("Allow Other Keyboards", "Globe key switches to others", Icons.Default.Keyboard, allowOtherKeyboards) { scope.launch { prefs.setAllowOtherKeyboards(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val showNumRow by prefs.showNumberRow.collectAsState(initial = false)
    val largeNumRow by prefs.largeNumberRow.collectAsState(initial = false)
    val hideHints by prefs.hideLongPressHints.collectAsState(initial = false)
    val enableResizing by prefs.enableKbResizing.collectAsState(initial = false)
    val splitKb by prefs.splitKeyboard.collectAsState(initial = false)
    val forcedEnter by prefs.forcedEnter.collectAsState(initial = false)
    val alwaysShowSuggestions by prefs.alwaysShowSuggestions.collectAsState(initial = false)
    val autoHideToolbar by prefs.autoHideToolbar.collectAsState(initial = false)

    SettingsSubScaffold(title = "Layout", onBack = onBack) {
        SettingSwitchItem("Enable number row", "Adds an extra row", Icons.Default.LooksOne, showNumRow) { scope.launch { prefs.setShowNumberRow(it) } }
        SettingSwitchItem("Large number row", null, Icons.Default.ViewStream, largeNumRow) { scope.launch { prefs.setLargeNumberRow(it) } }
        SettingSwitchItem("Hide long press hints", "Hide small labels from key corners", Icons.Default.VisibilityOff, hideHints) { scope.launch { prefs.setHideLongPressHints(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Enable keyboard resizing", null, Icons.Default.AspectRatio, enableResizing) { scope.launch { prefs.setEnableKbResizing(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Enable split keyboard", "For foldable phones", Icons.Default.VerticalSplit, splitKb) { scope.launch { prefs.setSplitKeyboard(it) } }
        SettingSwitchItem("Forced enter button", "Do not show emoji on enter", Icons.AutoMirrored.Filled.KeyboardReturn, forcedEnter) { scope.launch { prefs.setForcedEnter(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Always show suggestions", "Suggestion bar stays visible even when empty", Icons.Default.TextFields, alwaysShowSuggestions) { scope.launch { prefs.setAlwaysShowSuggestions(it) } }
        SettingSwitchItem("Auto-hide toolbar while typing", "Only suggestions bar visible; toggle button to show toolbar", Icons.Default.SwapHoriz, autoHideToolbar) { scope.launch { prefs.setAutoHideToolbar(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SizeSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val heightPortrait by prefs.kbHeightPortrait.collectAsState(initial = 100)
    val heightLandscape by prefs.kbHeightLandscape.collectAsState(initial = 100)
    val widthOneHandedPortrait by prefs.oneHandedWidthPortrait.collectAsState(initial = 85)
    val widthOneHandedLandscape by prefs.oneHandedWidthLandscape.collectAsState(initial = 40)

    SettingsSubScaffold(title = "Size", onBack = onBack) {
        SettingSliderItem("Keyboard Height (Portrait)", heightPortrait.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightPortrait(it.toInt()) } }
        SettingSliderItem("Keyboard Height (Landscape)", heightLandscape.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightLandscape(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("One Handed Width (Portrait)", widthOneHandedPortrait.toFloat(), 50f..100f) { scope.launch { prefs.setOneHandedWidthPortrait(it.toInt()) } }
        SettingSliderItem("One Handed Width (Landscape)", widthOneHandedLandscape.toFloat(), 30f..100f) { scope.launch { prefs.setOneHandedWidthLandscape(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val moveCursorSpace by prefs.moveCursorSpace.collectAsState(initial = true)
    val volumeCursor by prefs.volumeCursor.collectAsState(initial = false)
    val smartVolumeControl by prefs.smartVolumeControl.collectAsState(initial = true)

    SettingsSubScaffold(title = "Navigation", onBack = onBack) {
        SettingSwitchItem("Move Cursor Using Space Key", "Swipe space to move cursor", Icons.Default.SwapHoriz, moveCursorSpace) { scope.launch { prefs.setMoveCursorSpace(it) } }
        SettingSwitchItem("Volume Key Cursor Control", "Use volume buttons to move cursor", Icons.AutoMirrored.Filled.VolumeUp, volumeCursor) { scope.launch { prefs.setVolumeCursor(it) } }
        if (volumeCursor) {
            SettingSwitchItem("Smart Volume Control", "Do not move cursor when audio is playing", Icons.Default.MusicNote, smartVolumeControl) { scope.launch { prefs.setSmartVolumeControl(it) } }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasteSettingsScreen(onBack: () -> Unit, onNavigateToClipboardHistory: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val holdPasteEnabled by prefs.holdPasteEnabled.collectAsState(initial = false)
    val holdPasteDuration by prefs.holdPasteDuration.collectAsState(initial = 400)
    val holdPasteTriggerKey by prefs.holdPasteTriggerKey.collectAsState(initial = "v")
    val clipboardExpiry by prefs.clipboardExpiry.collectAsState(initial = 120)
    val clipboardRecent by prefs.clipboardRecent.collectAsState(initial = true)
    val clipboardImages by prefs.clipboardImages.collectAsState(initial = true)

    SettingsSubScaffold(title = "Paste & Clipboard", onBack = onBack) {
        SettingSwitchItem("Hold key to paste", "Long-press a key to paste clipboard text", Icons.Default.ContentPaste, holdPasteEnabled) { scope.launch { prefs.setHoldPasteEnabled(it) } }
        if (holdPasteEnabled) {
            SettingDropdownItem(
                title = "Trigger key",
                subtitle = "Key to hold for paste",
                icon = Icons.Default.Keyboard,
                selectedOption = holdPasteTriggerKey.uppercase(),
                options = listOf("V", "B", "N", "M", "G", "H", "Space", "Enter"),
                onOptionSelected = { scope.launch { prefs.setHoldPasteTriggerKey(it.lowercase()) } }
            )
            SettingSliderItem("Hold duration", holdPasteDuration.toFloat(), 200f..800f) { scope.launch { prefs.setHoldPasteDuration(it.toInt()) } }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Clipboard Recent Items", "Show recent copied or cut text in clipboard", Icons.Default.ContentPaste, clipboardRecent) { scope.launch { prefs.setClipboardRecent(it) } }
        val expiryOptions = listOf(0, 1, 5, 525600)
        val expiryLabels = listOf("Never", "1 minute", "5 minutes", "365 days")
        val selectedIndex = expiryOptions.indexOf(clipboardExpiry).let { if (it < 0) 0 else it }
        SettingDropdownItem(
            title = "Auto-delete clipboard items",
            subtitle = "Remove copies after the selected time",
            icon = Icons.Default.Timer,
            selectedOption = expiryLabels[selectedIndex],
            options = expiryLabels,
            onOptionSelected = { label ->
                val idx = expiryLabels.indexOf(label)
                val mins = expiryOptions[idx]
                scope.launch { prefs.setClipboardExpiry(mins) }
                ClipboardManager.setExpiryMinutes(mins)
            }
        )
        SettingSwitchItem("Show copied images on Clipboard", "Show screenshots or copied images", Icons.Default.Image, clipboardImages) { scope.launch { prefs.setClipboardImages(it) } }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .clickable { ClipboardManager.clearAllUnpinned() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Delete All Clipboard Items", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Remove all non-pinned copied texts", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingItem("View Clipboard History", "Browse all copied texts", Icons.Default.History, onClick = onNavigateToClipboardHistory)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedGroupSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val longPressDelay by prefs.longPressDelayMs.collectAsState(initial = 300)
    val popupDismiss by prefs.popupDismissDelay.collectAsState(initial = "Default")
    val spaceDelay by prefs.spaceCursorDelay.collectAsState(initial = 1000)
    val spaceSpeed by prefs.spaceCursorSpeed.collectAsState(initial = 150)
    val physicalKbEmoji by prefs.physicalKbEmoji.collectAsState(initial = true)
    val typedWordFirst by prefs.showTypedWordFirst.collectAsState(initial = true)
    val backspaceDelay by prefs.backspaceRepeatDelay.collectAsState(initial = 400)
    val backspaceSpeed by prefs.backspaceRepeatSpeed.collectAsState(initial = 50)

    SettingsSubScaffold(title = "Advanced", onBack = onBack) {
        SettingSliderItem("Key long press delay (ms)", longPressDelay.toFloat(), 100f..1000f) { scope.launch { prefs.setLongPressDelayMs(it.toInt()) } }
        val dismissOptions = listOf("Default", "Short", "Long")
        Text(text = "Popup dismiss delay: $popupDismiss", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            dismissOptions.forEach { option ->
                FilterChip(
                    selected = popupDismiss == option,
                    onClick = { scope.launch { prefs.setPopupDismissDelay(option) } },
                    label = { Text(option) }
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("Spacebar cursor move delay", spaceDelay.toFloat(), 500f..2000f) { scope.launch { prefs.setSpaceCursorDelay(it.toInt()) } }
        SettingSliderItem("Spacebar cursor move speed", spaceSpeed.toFloat(), 50f..300f) { scope.launch { prefs.setSpaceCursorSpeed(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Physical keyboard emoji key", "Enable emoji key shortcuts on hardware keyboard", Icons.Default.Keyboard, physicalKbEmoji) { scope.launch { prefs.setPhysicalKbEmoji(it) } }
        SettingSwitchItem("Show typed word first", "Exact typed word appears as first suggestion", Icons.Default.Title, typedWordFirst) { scope.launch { prefs.setShowTypedWordFirst(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("Backspace repeat delay (ms)", backspaceDelay.toFloat(), 200f..1000f) { scope.launch { prefs.setBackspaceRepeatDelay(it.toInt()) } }
        SettingSliderItem("Backspace repeat speed (ms)", backspaceSpeed.toFloat(), 20f..200f) { scope.launch { prefs.setBackspaceRepeatSpeed(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCorrectionSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val autoCorrection by prefs.autoCorrection.collectAsState(initial = true)
    val phoneticAutoCorrect by prefs.phoneticAutoCorrection.collectAsState(initial = true)
    val showSuggestions by prefs.showSuggestions.collectAsState(initial = true)
    val personalized by prefs.personalizedSuggestions.collectAsState(initial = true)
    val nextWord by prefs.nextWordSuggestions.collectAsState(initial = true)

    SettingsSubScaffold(title = "Text correction", onBack = onBack) {
        SettingSwitchItem("Auto-correction", "Punctuation corrects words", Icons.Default.Spellcheck, autoCorrection) { scope.launch { prefs.setAutoCorrection(it) } }
        SettingSwitchItem("Phonetic auto-correction", "Correct Bangla transliteration", Icons.Default.Translate, phoneticAutoCorrect) { scope.launch { prefs.setPhoneticAutoCorrection(it) } }
        SettingSwitchItem("Show suggestions", "Display words while typing", Icons.Default.Lightbulb, showSuggestions) { scope.launch { prefs.setShowSuggestions(it) } }
        SettingSwitchItem("Personalized suggestions", "Learn from communication", Icons.Default.Person, personalized) { scope.launch { prefs.setPersonalizedSuggestions(it) } }
        SettingSwitchItem("Next-word suggestions", "Use previous word", Icons.Default.History, nextWord) { scope.launch { prefs.setNextWordSuggestions(it) } }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsScreen(onBack: () -> Unit) {
    AdvancedGroupSettingsScreen(onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreLanguagesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val enableBanglaJatiyo by prefs.enableBanglaJatiyo.collectAsState(initial = true)
    val enableAvro by prefs.enableAvro.collectAsState(initial = true)
    val enableArabic by prefs.enableArabic.collectAsState(initial = true)

    SettingsSubScaffold(title = "Keyboard Languages", onBack = onBack) {
        Text(
            text = "Enable or disable active keyboard layouts",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
        )

        SettingSwitchItem("English", "QWERTY layout (Always Enabled)", Icons.Default.Language, true) {}
        SettingSwitchItem("Bangla (বাংলা)", "Bangladesh Standard National Layout", Icons.Default.Language, enableBanglaJatiyo) { scope.launch { prefs.setEnableBanglaJatiyo(it) } }
        SettingSwitchItem("Avro (অভ্র)", "Official Avro Phonetic Transliteration Engine", Icons.Default.Language, enableAvro) { scope.launch { prefs.setEnableAvro(it) } }
        SettingSwitchItem("Arabic (عربي)", "Arabic Letter Layout", Icons.Default.Language, enableArabic) { scope.launch { prefs.setEnableArabic(it) } }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GifQualitySettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    
    val highQual by prefs.highQualityGifs.collectAsState(initial = true)
    val sendHighQual by prefs.sendHighQualityGifs.collectAsState(initial = true)

    SettingsSubScaffold(title = "Gif Quality", onBack = onBack) {
        SettingSwitchItem("Show high quality Gifs", "Requires more data", Icons.Default.Gif, highQual) { scope.launch { prefs.setHighQualityGifs(it) } }
        SettingSwitchItem("Send high quality Gifs", "Download before sending", Icons.AutoMirrored.Filled.Send, sendHighQual) { scope.launch { prefs.setSendHighQualityGifs(it) } }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val recentEmojiExpiry by prefs.recentEmojiExpiry.collectAsState(initial = 30)

    SettingsSubScaffold(title = "Emoji", onBack = onBack) {
        val expiryOptions = listOf(1, 7, 30, 90, 0)
        val expiryLabels = listOf("1 day", "7 days", "30 days", "90 days", "Forever")
        val selectedIndex = expiryOptions.indexOf(recentEmojiExpiry).let { if (it < 0) 2 else it }
        SettingDropdownItem(
            title = "Recent emoji retention",
            subtitle = "How long to remember recently used emojis",
            icon = Icons.Default.EmojiEmotions,
            selectedOption = expiryLabels[selectedIndex],
            options = expiryLabels,
            onOptionSelected = { label ->
                val idx = expiryLabels.indexOf(label)
                val days = expiryOptions[idx]
                scope.launch { prefs.setRecentEmojiExpiry(days) }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class AppLanguageOption(val code: String, val displayName: String, val localName: String)

private val appLanguages = listOf(
    AppLanguageOption("en", "English", "English"),
    AppLanguageOption("bn", "Bengali", "বাংলা"),
    AppLanguageOption("hi", "Hindi", "हिन्दी"),
    AppLanguageOption("ar", "Arabic", "العربية"),
    AppLanguageOption("es", "Spanish", "Español"),
    AppLanguageOption("fr", "French", "Français"),
    AppLanguageOption("de", "German", "Deutsch"),
    AppLanguageOption("pt", "Portuguese", "Português"),
    AppLanguageOption("ru", "Russian", "Русский"),
    AppLanguageOption("ja", "Japanese", "日本語"),
    AppLanguageOption("ko", "Korean", "한국어"),
    AppLanguageOption("zh", "Chinese (Simplified)", "简体中文"),
    AppLanguageOption("zh_TW", "Chinese (Traditional)", "繁體中文"),
    AppLanguageOption("ur", "Urdu", "اردو"),
    AppLanguageOption("fa", "Persian", "فارسی")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSubScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            content = content
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageScreen(onBack: () -> Unit) {
    var selectedLanguage by remember { mutableStateOf("en") }

    SettingsSubScaffold(title = "App Language", onBack = onBack) {
        Text(
            text = "Choose the display language for the app interface",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
        )
        appLanguages.forEach { lang ->
            val isSelected = selectedLanguage == lang.code
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                    .clickable { selectedLanguage = lang.code }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lang.localName,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = lang.displayName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
