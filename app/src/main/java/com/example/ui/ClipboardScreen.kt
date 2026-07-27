package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clipboard.ClipboardManager
import com.example.clipboard.ClipItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipboardScreen(
    onBack: () -> Unit
) {
    val clips by ClipboardManager.clips.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clipboard Manager") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (clips.isNotEmpty()) {
                        IconButton(onClick = { ClipboardManager.clearAllUnpinned() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Unpinned", tint = Color.Gray)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F1017),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0F1017)
    ) { paddingValues ->
        if (clips.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No clips found in history", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(clips) { clip ->
                    ClipboardItemCard(clip = clip)
                }
            }
        }
    }
}

@Composable
fun ClipboardItemCard(clip: ClipItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1C28)),
        shape = RoundedCornerShape(12.dp),
        border = if (clip.isPinned) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pin",
                        tint = if (clip.isPinned) Color(0xFF00E5FF) else Color.Gray.copy(alpha = 0.4f),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { ClipboardManager.togglePin(clip.id) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (clip.isPinned) "Pinned" else "Recent",
                        color = if (clip.isPinned) Color(0xFF00E5FF) else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { ClipboardManager.deleteClip(clip.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = clip.text,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
