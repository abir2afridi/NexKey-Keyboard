package com.example.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

@Composable
fun SetupScreen(
    onSetupComplete: () -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(checkIsKeyboardEnabled(context)) }
    var isSelected by remember { mutableStateOf(checkIsKeyboardSelected(context)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isEnabled = checkIsKeyboardEnabled(context)
                isSelected = checkIsKeyboardSelected(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            isEnabled = checkIsKeyboardEnabled(context)
            isSelected = checkIsKeyboardSelected(context)
            if (isEnabled && isSelected) {
                onSetupComplete()
                return@LaunchedEffect
            }
            delay(500)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                // App logo — same drawable as the Home screen header (icon_header),
                // not a generic Material library icon.
                Icon(
                    painter = painterResource(R.drawable.icon_header),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.setup_welcome),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.setup_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            SetupStepCard(
                stepNumber = "1",
                title = stringResource(R.string.setup_enable_title),
                description = stringResource(R.string.setup_enable_desc),
                isCompleted = isEnabled,
                buttonText = if (isEnabled) stringResource(R.string.setup_enabled) else stringResource(R.string.setup_open_settings),
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SetupStepCard(
                stepNumber = "2",
                title = stringResource(R.string.setup_select_title),
                description = stringResource(R.string.setup_select_desc),
                isCompleted = isSelected,
                buttonText = if (isSelected) stringResource(R.string.setup_selected) else stringResource(R.string.setup_open_picker),
                onClick = {
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showInputMethodPicker()
                }
            )

            if (isEnabled && isSelected) {
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onSetupComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(stringResource(R.string.setup_finish), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

fun checkIsKeyboardEnabled(context: Context): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val enabledMethods = imm.enabledInputMethodList
    val packageName = context.packageName
    return enabledMethods.any { it.packageName == packageName }
}

fun checkIsKeyboardSelected(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= 34) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.currentInputMethodInfo?.packageName == context.packageName
    } else {
        try {
            val currentIme = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.DEFAULT_INPUT_METHOD
            )
            currentIme != null && currentIme.contains(context.packageName)
        } catch (_: SecurityException) {
            false
        }
    }
}
