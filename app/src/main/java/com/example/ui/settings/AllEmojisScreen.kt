package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed as gridItemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AllEmojis
import com.example.data.EmojiInfo
import com.example.data.TypingAnalytics
import kotlinx.coroutines.flow.collectLatest

private enum class EmojiViewMode { LIST, GRID }

private enum class EmojiSort { NAME_AZ, NAME_ZA, USAGE, CATEGORY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllEmojisScreen(onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    var viewMode by remember { mutableStateOf(EmojiViewMode.LIST) }
    var sort by remember { mutableStateOf(EmojiSort.USAGE) }
    var category by remember { mutableStateOf<String?>(null) }
    var showInfo by remember { mutableStateOf(false) }

    var usageMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    val db = remember { TypingAnalytics.getDatabase() }
    LaunchedEffect(db) {
        db?.emojiUsageDao()?.observeAll()?.collectLatest { usages ->
            usageMap = usages.associate { it.emoji to it.frequency }
        }
    }

    fun scoreOf(info: EmojiInfo): Int = usageMap[info.emoji] ?: 0

    val usageRanking = remember(usageMap) {
        usageMap.entries
            .sortedByDescending { it.value }
            .mapIndexed { i, e -> e.key to (i + 1) }
            .toMap()
    }

    val filtered = remember(query, sort, category, usageMap) {
        val q = query.trim().lowercase()
        val list = AllEmojis.list.filter { info ->
            (category == null || info.category == category) &&
                (q.isEmpty() || info.name.lowercase().contains(q) ||
                    info.description.lowercase().contains(q) ||
                    info.code.lowercase().contains(q) ||
                    info.emoji.contains(query))
        }
        when (sort) {
            EmojiSort.NAME_AZ -> list.sortedBy { it.name.lowercase() }
            EmojiSort.NAME_ZA -> list.sortedByDescending { it.name.lowercase() }
            EmojiSort.USAGE -> list.sortedWith(compareByDescending<EmojiInfo> { scoreOf(it) }.thenBy { it.name.lowercase() })
            EmojiSort.CATEGORY -> list.sortedWith(compareBy({ it.category }, { it.name.lowercase() }))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.all_emojis_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { showInfo = true }) {
                        Icon(Icons.Default.Info, contentDescription = stringResource(R.string.home_emojis_title))
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
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.all_emojis_search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var categoryExpanded by remember { mutableStateOf(false) }
                Box {
                    FilterChip(
                        selected = category != null,
                        onClick = { categoryExpanded = true },
                        label = { Text(category ?: stringResource(R.string.all_emojis_category)) },
                        leadingIcon = {
                            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    )
                    DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all_emojis_category_all)) },
                            onClick = { category = null; categoryExpanded = false }
                        )
                        AllEmojis.categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { category = cat; categoryExpanded = false }
                            )
                        }
                    }
                }

                var sortExpanded by remember { mutableStateOf(false) }
                Box {
                    FilterChip(
                        selected = sort != EmojiSort.USAGE,
                        onClick = { sortExpanded = true },
                        label = {
                            Text(
                                when (sort) {
                                    EmojiSort.NAME_AZ -> stringResource(R.string.all_emojis_sort_name_az)
                                    EmojiSort.NAME_ZA -> stringResource(R.string.all_emojis_sort_name_za)
                                    EmojiSort.USAGE -> stringResource(R.string.all_emojis_sort_usage)
                                    EmojiSort.CATEGORY -> stringResource(R.string.all_emojis_sort_category)
                                }
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    )
                    DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all_emojis_sort_usage)) },
                            onClick = { sort = EmojiSort.USAGE; sortExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all_emojis_sort_name_az)) },
                            onClick = { sort = EmojiSort.NAME_AZ; sortExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all_emojis_sort_name_za)) },
                            onClick = { sort = EmojiSort.NAME_ZA; sortExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all_emojis_sort_category)) },
                            onClick = { sort = EmojiSort.CATEGORY; sortExpanded = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        selected = viewMode == EmojiViewMode.LIST,
                        onClick = { viewMode = EmojiViewMode.LIST },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ViewList, contentDescription = null)
                    }
                    SegmentedButton(
                        selected = viewMode == EmojiViewMode.GRID,
                        onClick = { viewMode = EmojiViewMode.GRID },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Icon(Icons.Default.GridView, contentDescription = null)
                    }
                }
            }

            Text(
                text = stringResource(R.string.all_emojis_count, filtered.size),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            if (viewMode == EmojiViewMode.LIST) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filtered.size) { index ->
                        val info = filtered[index]
                        val score = scoreOf(info)
                        val rank = usageRanking[info.emoji] ?: 0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(38.dp)) {
                                Text(
                                    text = "${index + 1}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                if (score > 0) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stringResource(R.string.all_emojis_rank, rank),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = info.emoji,
                                fontSize = 26.sp,
                                modifier = Modifier.width(44.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = info.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = info.description,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                                Row {
                                    Text(
                                        text = info.code,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = info.category,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$score",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (score > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                                Text(
                                    text = stringResource(R.string.all_emojis_score),
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    gridItemsIndexed(filtered) { index, info ->
                        val score = scoreOf(info)
                        Column(
                            modifier = Modifier
                                .aspectRatio(0.9f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(info.emoji, fontSize = 30.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(info.name, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "♯${index + 1}",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$score",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (score > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            title = { Text(stringResource(R.string.all_emojis_info_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.all_emojis_info_desc), fontSize = 14.sp, lineHeight = 22.sp) },
            confirmButton = {
                TextButton(onClick = { showInfo = false }) {
                    Text(stringResource(R.string.home_got_it))
                }
            }
        )
    }
}