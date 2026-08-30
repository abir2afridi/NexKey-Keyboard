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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.BanglaPhoneticEngine
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
    val predictionEngine = remember {
        com.example.prediction.engine.DictionaryManager(
            com.example.data.PreferenceFeatureFlags(com.example.data.UserPreferences.getInstance(context))
        ).also { it.init(context) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sandbox_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.sandbox_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = testInputText,
                onValueChange = { testInputText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.sandbox_try)) },
                placeholder = { Text(stringResource(R.string.sandbox_placeholder)) },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp))
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
                            sandboxSuggestions = predictionEngine.getSuggestions(
                                sandboxComposing, isBangla = true, previousWords = emptyList(),
                                showTypedWordFirst = false, limit = 4, now = System.currentTimeMillis()
                            ).map { it.word }
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
                                predictionEngine.getSuggestions(
                                    sandboxComposing, isBangla = true, previousWords = emptyList(),
                                    showTypedWordFirst = false, limit = 4, now = System.currentTimeMillis()
                                ).map { it.word }
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
                        val themes = ThemePreset.values()
                        val nextIndex = (sandboxTheme.preset.ordinal + 1) % themes.size
                        sandboxTheme = KeyboardTheme.fromPreset(themes[nextIndex])
                    },
                    onOpenSettings = {}
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
