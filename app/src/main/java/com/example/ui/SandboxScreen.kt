package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
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
import com.example.engine.BanglaPhoneticEngine
import com.example.engine.PredictionEngine
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SandboxScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var testInputText by remember { mutableStateOf("") }
    
    var sandboxMode by remember { mutableStateOf(KeyboardMode.BANGLA_PHONETIC) }
    var sandboxShift by remember { mutableStateOf(ShiftState.OFF) }
    var sandboxTheme by remember { mutableStateOf(KeyboardTheme.DarkNeon) }
    var sandboxComposing by remember { mutableStateOf("") }
    var sandboxSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    val predictionEngine = remember { PredictionEngine() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Typing Sandbox") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Test the keyboard in this playground. Your typing here won't be saved.",
                color = Color.Gray,
                fontSize = 13.sp
            )

            OutlinedTextField(
                value = testInputText,
                onValueChange = { testInputText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Try typing here...") },
                placeholder = { Text("e.g. ami bangla valobashi") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00E5FF),
                    unfocusedBorderColor = Color(0xFF2D314E)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF2D314E), RoundedCornerShape(16.dp))
            ) {
                KeyboardComposeView(
                    mode = sandboxMode,
                    shiftState = sandboxShift,
                    theme = sandboxTheme,
                    composingText = sandboxComposing,
                    suggestions = sandboxSuggestions,
                    actionLabel = "↵",
                    showNumberRow = true,
                    onKeyTap = { key ->
                        if (sandboxMode == KeyboardMode.BANGLA_PHONETIC && key.all { it.isLetter() || it == '.' || it == '^' }) {
                            sandboxComposing += key
                            sandboxSuggestions = predictionEngine.getPredictions(sandboxComposing, isBangla = true)
                        } else {
                            if (sandboxComposing.isNotEmpty()) {
                                testInputText += BanglaPhoneticEngine.parse(sandboxComposing)
                                sandboxComposing = ""
                                sandboxSuggestions = emptyList()
                            }
                            testInputText += key
                        }
                    },
                    onBackspaceTap = {
                        if (sandboxComposing.isNotEmpty()) {
                            sandboxComposing = sandboxComposing.dropLast(1)
                            sandboxSuggestions = if (sandboxComposing.isNotEmpty()) {
                                predictionEngine.getPredictions(sandboxComposing, isBangla = true)
                            } else {
                                emptyList()
                            }
                        } else if (testInputText.isNotEmpty()) {
                            testInputText = testInputText.dropLast(1)
                        }
                    },
                    onSpaceTap = {
                        if (sandboxComposing.isNotEmpty()) {
                            val word = BanglaPhoneticEngine.parse(sandboxComposing)
                            testInputText += "$word "
                            sandboxComposing = ""
                            sandboxSuggestions = emptyList()
                        } else {
                            testInputText += " "
                        }
                    },
                    onEnterTap = {
                        if (sandboxComposing.isNotEmpty()) {
                            val word = BanglaPhoneticEngine.parse(sandboxComposing)
                            testInputText += word
                            sandboxComposing = ""
                            sandboxSuggestions = emptyList()
                        }
                        testInputText += "\n"
                    },
                    onShiftTap = {
                        sandboxShift = when (sandboxShift) {
                            ShiftState.OFF -> ShiftState.SHIFT
                            ShiftState.SHIFT -> ShiftState.CAPS_LOCK
                            ShiftState.CAPS_LOCK -> ShiftState.OFF
                        }
                    },
                    onModeChange = { sandboxMode = it },
                    onSuggestionSelect = { word ->
                        testInputText += "$word "
                        sandboxComposing = ""
                        sandboxSuggestions = emptyList()
                    },
                    onVoiceClick = {},
                    onThemeToggle = {
                        sandboxTheme = when (sandboxTheme.preset) {
                            ThemePreset.DARK_NEON -> KeyboardTheme.LightMinimal
                            ThemePreset.LIGHT_MINIMAL -> KeyboardTheme.AmoledBlack
                            ThemePreset.AMOLED_BLACK -> KeyboardTheme.EmeraldGreen
                            else -> KeyboardTheme.DarkNeon
                        }
                    },
                    onOpenSettings = {},
                    onAiAction = { prompt ->
                        if (testInputText.isNotEmpty()) {
                            testInputText = "✨ $testInputText"
                        }
                    }
                )
            }
        }
    }
}
