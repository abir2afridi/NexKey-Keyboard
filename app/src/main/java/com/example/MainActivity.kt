package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NexKeyDashboardScreen(
                        modifier = Modifier.padding(innerPadding),
                        onEnableKeyboard = {
                            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                        },
                        onSelectKeyboard = {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.showInputMethodPicker()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NexKeyDashboardScreen(
    modifier: Modifier = Modifier,
    onEnableKeyboard: () -> Unit,
    onSelectKeyboard: () -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(checkIsKeyboardEnabled(context)) }
    var isSelected by remember { mutableStateOf(checkIsKeyboardSelected(context)) }

    var testInputText by remember { mutableStateOf("") }
    var testPhoneticInput by remember { mutableStateOf("ami banglay gan gai") }

    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFF0F1017)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NexKey Keyboard",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Multilingual • Ridmik-Class Bangla Phonetic",
                        color = Color(0xFF00E5FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF00E5FF).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "NexKey Logo",
                        tint = Color(0xFF00E5FF)
                    )
                }
            }

            // Step 1: Enable NexKey
            SetupStepCard(
                stepNumber = "1",
                title = "Enable NexKey Keyboard",
                description = "Grant NexKey permission in Android On-screen Keyboard settings.",
                isCompleted = isEnabled,
                buttonText = if (isEnabled) "Enabled ✓" else "Enable in Settings",
                onClick = {
                    onEnableKeyboard()
                    isEnabled = checkIsKeyboardEnabled(context)
                }
            )

            // Step 2: Select NexKey as Active
            SetupStepCard(
                stepNumber = "2",
                title = "Select as Default Input Method",
                description = "Choose NexKey from system input method switcher.",
                isCompleted = isSelected,
                buttonText = if (isSelected) "Active Selected ✓" else "Select Active Keyboard",
                onClick = {
                    onSelectKeyboard()
                    isSelected = checkIsKeyboardSelected(context)
                }
            )

            // Interactive Live Typing Sandbox
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1C28)),
                shape = RoundedCornerShape(16.dp)
            ) {
                var sandboxMode by remember { mutableStateOf(com.example.ui.KeyboardMode.BANGLA_PHONETIC) }
                var sandboxShift by remember { mutableStateOf(com.example.ui.ShiftState.OFF) }
                var sandboxTheme by remember { mutableStateOf(com.example.theme.KeyboardTheme.DarkNeon) }
                var sandboxComposing by remember { mutableStateOf("") }
                var sandboxSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
                val predictionEngine = remember { com.example.engine.PredictionEngine() }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Live Interactive Typing Sandbox",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Type in the field or test the embedded NexKey keyboard below:",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = testInputText,
                        onValueChange = { testInputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Sandbox Text Field") },
                        placeholder = { Text("e.g. ami bangla valobashi (আমি বাংলা ভালোবাসি)") },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF2D314E), RoundedCornerShape(12.dp))
                    ) {
                        com.example.ui.KeyboardComposeView(
                            mode = sandboxMode,
                            shiftState = sandboxShift,
                            theme = sandboxTheme,
                            composingText = sandboxComposing,
                            suggestions = sandboxSuggestions,
                            actionLabel = "↵",
                            onKeyTap = { key ->
                                if (sandboxMode == com.example.ui.KeyboardMode.BANGLA_PHONETIC && key.all { it.isLetter() || it == '.' || it == '^' }) {
                                    sandboxComposing += key
                                    val parsed = com.example.engine.BanglaPhoneticEngine.parse(sandboxComposing)
                                    sandboxSuggestions = predictionEngine.getPredictions(sandboxComposing, isBangla = true)
                                } else {
                                    if (sandboxComposing.isNotEmpty()) {
                                        testInputText += com.example.engine.BanglaPhoneticEngine.parse(sandboxComposing)
                                        sandboxComposing = ""
                                        sandboxSuggestions = emptyList()
                                    }
                                    testInputText += key
                                }
                            },
                            onBackspaceTap = {
                                if (sandboxComposing.isNotEmpty()) {
                                    sandboxComposing = sandboxComposing.dropLast(1)
                                    if (sandboxComposing.isNotEmpty()) {
                                        sandboxSuggestions = predictionEngine.getPredictions(sandboxComposing, isBangla = true)
                                    } else {
                                        sandboxSuggestions = emptyList()
                                    }
                                } else if (testInputText.isNotEmpty()) {
                                    testInputText = testInputText.dropLast(1)
                                }
                            },
                            onSpaceTap = {
                                if (sandboxComposing.isNotEmpty()) {
                                    val word = com.example.engine.BanglaPhoneticEngine.parse(sandboxComposing)
                                    testInputText += "$word "
                                    predictionEngine.learnWord(word, isBangla = true)
                                    sandboxComposing = ""
                                    sandboxSuggestions = emptyList()
                                } else {
                                    testInputText += " "
                                }
                            },
                            onEnterTap = {
                                if (sandboxComposing.isNotEmpty()) {
                                    val word = com.example.engine.BanglaPhoneticEngine.parse(sandboxComposing)
                                    testInputText += word
                                    sandboxComposing = ""
                                    sandboxSuggestions = emptyList()
                                }
                                testInputText += "\n"
                            },
                            onShiftTap = {
                                sandboxShift = when (sandboxShift) {
                                    com.example.ui.ShiftState.OFF -> com.example.ui.ShiftState.SHIFT
                                    com.example.ui.ShiftState.SHIFT -> com.example.ui.ShiftState.CAPS_LOCK
                                    com.example.ui.ShiftState.CAPS_LOCK -> com.example.ui.ShiftState.OFF
                                }
                            },
                            onModeChange = { sandboxMode = it },
                            onSuggestionSelect = { word ->
                                testInputText += "$word "
                                sandboxComposing = ""
                                sandboxSuggestions = emptyList()
                            },
                            onVoiceClick = {
                                android.widget.Toast.makeText(context, "Voice typing triggered!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            onThemeToggle = {
                                sandboxTheme = when (sandboxTheme.preset) {
                                    com.example.theme.ThemePreset.DARK_NEON -> com.example.theme.KeyboardTheme.LightMinimal
                                    com.example.theme.ThemePreset.LIGHT_MINIMAL -> com.example.theme.KeyboardTheme.AmoledBlack
                                    com.example.theme.ThemePreset.AMOLED_BLACK -> com.example.theme.KeyboardTheme.EmeraldGreen
                                    else -> com.example.theme.KeyboardTheme.DarkNeon
                                }
                            },
                            onOpenSettings = {
                                android.widget.Toast.makeText(context, "Settings Panel active", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            onAiAction = { prompt ->
                                if (testInputText.isNotEmpty()) {
                                    testInputText = "✨ $testInputText"
                                } else {
                                    android.widget.Toast.makeText(context, "Type something first to run AI action!", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }

            // Phonetic Transliteration Guide
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161824)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = " Ridmik-Class Phonetic Cheat Sheet",
                        color = Color(0xFF00E5FF),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    PhoneticRow("ami", "আমি", "I / Me")
                    PhoneticRow("bangla", "বাংলা", "Bengali")
                    PhoneticRow("amar", "আমার", "My / Mine")
                    PhoneticRow("shonai", "সোনার", "Golden")
                    PhoneticRow("kormo", "কর্ম", "Work / Karma")
                    PhoneticRow("kS", "ক্ষ", "Juktakkhor (Conjunct)")
                    PhoneticRow("..", "।", "Bangla Dari (Sentence end)")
                }
            }

            // Feature Highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureBadge(
                    icon = Icons.Default.ContentCopy,
                    title = "Clipboard",
                    desc = "History & Pinning",
                    modifier = Modifier.weight(1f)
                )
                FeatureBadge(
                    icon = Icons.Default.ColorLens,
                    title = "Themes",
                    desc = "Neon, Light, AMOLED",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SetupStepCard(
    stepNumber: String,
    title: String,
    description: String,
    isCompleted: Boolean,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1C28)),
        shape = RoundedCornerShape(16.dp),
        border = if (isCompleted) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isCompleted) Color(0xFF00E5FF) else Color(0xFF2D314E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber,
                            color = if (isCompleted) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF00E5FF),
                    contentColor = if (isCompleted) Color(0xFF00E5FF) else Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = buttonText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PhoneticRow(latin: String, bangla: String, meaning: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF222436))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = latin, color = Color(0xFF80D8FF), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = "➜", color = Color.Gray, fontSize = 12.sp)
        Text(text = bangla, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = "($meaning)", color = Color.LightGray, fontSize = 11.sp)
    }
}

@Composable
fun FeatureBadge(
    icon: ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1B1C28))
            .padding(12.dp)
    ) {
        Column {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = desc, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

private fun checkIsKeyboardEnabled(context: Context): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val enabledMethods = imm.enabledInputMethodList
    val packageName = context.packageName
    return enabledMethods.any { it.packageName == packageName }
}

private fun checkIsKeyboardSelected(context: Context): Boolean {
    val currentIme = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.DEFAULT_INPUT_METHOD
    )
    return currentIme != null && currentIme.contains(context.packageName)
}
