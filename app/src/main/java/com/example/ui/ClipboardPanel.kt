package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clipboard.ClipboardManager
import com.example.theme.KeyboardTheme

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
