package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDatabase
import com.example.data.LearnedWordEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()
    var words by remember { mutableStateOf<List<LearnedWordEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        words = database.learnedWordDao().getAllWords()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Dictionary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (words.isNotEmpty()) {
                        IconButton(onClick = {
                            scope.launch {
                                database.learnedWordDao().clearAll()
                                words = emptyList()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear All", tint = Color(0xFFD93025))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF202124),
                    navigationIconContentColor = Color(0xFF202124)
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else if (words.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Your dictionary is empty. NexKey learns new words as you type.",
                    color = Color(0xFF5F6368),
                    fontSize = 15.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        text = "Learned Words (${words.size})",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
                    )
                }
                items(words) { wordEntity ->
                    WordItem(
                        word = wordEntity.word,
                        isBangla = wordEntity.isBangla,
                        onDelete = {
                            scope.launch {
                                database.learnedWordDao().deleteWord(wordEntity.word)
                                words = words.filter { it.word != wordEntity.word }
                            }
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF1F3F4), thickness = 1.dp)
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun WordItem(
    word: String,
    isBangla: Boolean,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = word, color = Color(0xFF202124), fontSize = 17.sp, fontWeight = FontWeight.Medium)
            Text(
                text = if (isBangla) "Bangla" else "English",
                color = Color(0xFF5F6368),
                fontSize = 13.sp
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFBDC1C6), modifier = Modifier.size(22.dp))
        }
    }
}
