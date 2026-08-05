package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToTyping: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    onNavigateToLanguageKeys: () -> Unit = {},
    onNavigateToLayout: () -> Unit = {},
    onNavigateToSize: () -> Unit = {},
    onNavigateToNavigation: () -> Unit = {},
    onNavigateToPaste: () -> Unit = {},
    onNavigateToAdvancedGroup: () -> Unit = {},
    onNavigateToTextCorrection: () -> Unit = {},
    onNavigateToMoreLanguages: () -> Unit = {},
    onNavigateToGifQuality: () -> Unit = {},
    onNavigateToHeaderAnimation: () -> Unit = {},
    onNavigateToEmoji: () -> Unit = {},
    onNavigateToSpeedMeter: () -> Unit = {},
    onNavigateToInfoBox: () -> Unit = {},
    appTheme: String = "SYSTEM",
    onToggleTheme: () -> Unit = {},
    onNavigateToAppSettings: () -> Unit = {}
) {
    // ── Header search state ──────────────────────────────────────────────
    // searchActive = true turns the app-bar title into a text field and hides
    // the Theme/App-Settings icons. The back arrow then exits search instead of
    // leaving the screen. query holds whatever the user has typed so far.
    var searchActive by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    // Master list of every settings category card shown on this screen.
    // Each entry pairs a (localized) title + description with its icon and the
    // navigation callback that opens that sub-screen.
    val settingsItems =
        listOf(
            SettingCategory(stringResource(R.string.settings_typing), stringResource(R.string.settings_typing_desc), Icons.Default.TextFormat, onNavigateToTyping),
            SettingCategory(stringResource(R.string.settings_feedback), stringResource(R.string.settings_feedback_desc), Icons.Default.Vibration, onNavigateToFeedback),
            SettingCategory(stringResource(R.string.settings_language_keys), stringResource(R.string.settings_language_keys_desc), Icons.Default.Language, onNavigateToLanguageKeys),
            SettingCategory(stringResource(R.string.settings_layout), stringResource(R.string.settings_layout_desc), Icons.Default.Keyboard, onNavigateToLayout),
            SettingCategory(stringResource(R.string.settings_size), stringResource(R.string.settings_size_desc), Icons.Default.AspectRatio, onNavigateToSize),
            SettingCategory(stringResource(R.string.settings_navigation), stringResource(R.string.settings_navigation_desc), Icons.Default.SwapHoriz, onNavigateToNavigation),
            SettingCategory(stringResource(R.string.settings_paste), stringResource(R.string.settings_paste_desc), Icons.Default.ContentPaste, onNavigateToPaste),
            SettingCategory(stringResource(R.string.settings_emoji), stringResource(R.string.settings_emoji_desc), Icons.Default.EmojiEmotions, onNavigateToEmoji),
            SettingCategory(stringResource(R.string.settings_advanced), stringResource(R.string.settings_advanced_desc), Icons.Default.Tune, onNavigateToAdvancedGroup),
            SettingCategory(stringResource(R.string.settings_text_correction), stringResource(R.string.settings_text_correction_desc), Icons.Default.Spellcheck, onNavigateToTextCorrection),
            SettingCategory(stringResource(R.string.settings_more_languages), stringResource(R.string.settings_more_languages_desc), Icons.Default.Language, onNavigateToMoreLanguages),
            SettingCategory(stringResource(R.string.settings_gif_quality), stringResource(R.string.settings_gif_quality_desc), Icons.Default.Gif, onNavigateToGifQuality),
            SettingCategory(stringResource(R.string.settings_header_animation), stringResource(R.string.settings_header_animation_desc), Icons.Default.Animation, onNavigateToHeaderAnimation),
            SettingCategory(stringResource(R.string.settings_speed_meter), stringResource(R.string.settings_speed_meter_desc), Icons.Default.Speed, onNavigateToSpeedMeter),
            SettingCategory(stringResource(R.string.settings_info_box), stringResource(R.string.settings_info_box_desc), Icons.Default.Info, onNavigateToInfoBox)
        )

    // Live filter: when the search box is non-empty we keep only the categories
    // whose title OR description contains the query (case-insensitive). Because
    // title/description come from stringResource(), matching happens in the
    // user's currently-selected app language. An empty query shows everything.
    val searchQuery = query.trim().lowercase()
    val filteredItems = if (searchQuery.isEmpty()) settingsItems
        else settingsItems.filter {
            it.title.lowercase().contains(searchQuery) || it.description.lowercase().contains(searchQuery)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searchActive) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.settings_search_hint)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = { query = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.settings_search_clear))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (searchActive) {
                            searchActive = false
                            query = ""
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.settings_back))
                    }
                },
                // In normal (non-search) mode the app bar shows three action
                // icons: Search (opens the inline search field), Theme toggle,
                // and App Settings. In search mode these are hidden so the text
                // field gets the full width.
                actions = {
                    if (!searchActive) {
                        // Tap the magnifier to enter search mode.
                        IconButton(onClick = { searchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = stringResource(R.string.settings_search), tint = MaterialTheme.colorScheme.primary)
                        }
                        ThemeToggleButton(appTheme = appTheme, onToggleTheme = onToggleTheme)
                        IconButton(onClick = onNavigateToAppSettings) {
                            Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.app_settings_title), tint = MaterialTheme.colorScheme.primary)
                        }
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section header label. This always shows (even while searching) so
            // the grid keeps its visual structure above the filtered results.
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Text(
                    text = stringResource(R.string.settings_customization),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }

            // If the search query matched nothing, show a centred empty-state
            // message spanning the full grid width. Otherwise render the
            // (possibly filtered) category cards.
            if (filteredItems.isEmpty()) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.settings_search_no_results),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // filteredItems equals the full list when the search box is
                // empty, so this single branch covers both "all items" and
                // "search results" rendering.
                items(filteredItems.size) { index ->
                    val item = filteredItems[index]
                    SettingGridItem(
                        title = item.title,
                        subtitle = item.description,
                        icon = item.icon,
                        onClick = item.onClick
                    )
                }
            }
            
        }
    }
}

private data class SettingCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
