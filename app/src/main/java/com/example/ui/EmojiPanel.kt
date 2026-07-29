package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.KeyboardTheme

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
