package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomThemeScreen(
    onBack: () -> Unit
) {
    var bgColor by remember { mutableStateOf(Color(0xFF12131C)) }
    var keyColor by remember { mutableStateOf(Color(0xFF1E2136)) }
    var accentColor by remember { mutableStateOf(Color(0xFF00E5FF)) }

    val colorOptions = listOf(
        Color(0xFF12131C), Color(0xFF000000), Color(0xFF1A1A1A), 
        Color(0xFF263238), Color(0xFF0A1F1C), Color(0xFF1E2136),
        Color(0xFF00E5FF), Color(0xFFBB86FC), Color(0xFF00E676),
        Color(0xFFFF5252), Color(0xFFFFD740), Color(0xFFFFFFFF)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Theme") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Save", color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Preview
            Text(text = "Preview", color = Color.Gray, fontSize = 12.sp)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .padding(8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            repeat(if (row == 2) 7 else 9) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(keyColor)
                                )
                            }
                        }
                    }
                }
            }

            // Controls
            ColorPickerSection(title = "Background Color", selectedColor = bgColor, options = colorOptions) { bgColor = it }
            ColorPickerSection(title = "Key Color", selectedColor = keyColor, options = colorOptions) { keyColor = it }
            ColorPickerSection(title = "Accent Color", selectedColor = accentColor, options = colorOptions) { accentColor = it }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* Reset to default */ 
                    bgColor = Color(0xFF12131C)
                    keyColor = Color(0xFF1E2136)
                    accentColor = Color(0xFF00E5FF)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D314E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reset to Default")
            }
        }
    }
}

@Composable
fun ColorPickerSection(
    title: String,
    selectedColor: Color,
    options: List<Color>,
    onColorSelected: (Color) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.Medium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(options) { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 2.dp,
                            color = if (selectedColor == color) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedColor == color) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (color == Color.White) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
