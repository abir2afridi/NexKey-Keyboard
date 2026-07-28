package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.checkIsKeyboardEnabled
import com.example.ui.checkIsKeyboardSelected

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val prefs = androidx.compose.runtime.remember { com.example.data.UserPreferences(context) }
            val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")

            MyApplicationTheme(appTheme = appTheme) {
                NexKeyApp()
            }
        }
    }
}

@Composable
fun NexKeyApp() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val startDestination = if (checkIsKeyboardEnabled(context) && checkIsKeyboardSelected(context)) {
        Screen.Home.route
    } else {
        Screen.Setup.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentDestination?.route == Screen.Home.route) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    tonalElevation = 0.dp // Flat design as per screenshot
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Home", fontSize = 12.sp) },
                        selected = currentDestination.hierarchy.any { it.route == Screen.Home.route },
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2E7D32),
                            selectedTextColor = Color(0xFF2E7D32),
                            indicatorColor = Color(0xFFE8F5E9), // Light green pill
                            unselectedIconColor = Color(0xFF5F6368),
                            unselectedTextColor = Color(0xFF5F6368)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Tutorial", fontSize = 12.sp) },
                        selected = false,
                        onClick = { navController.navigate(Screen.Help.route) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color(0xFF5F6368),
                            unselectedTextColor = Color(0xFF5F6368)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Profile", fontSize = 12.sp) },
                        selected = false,
                        onClick = { navController.navigate(Screen.About.route) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color(0xFF5F6368),
                            unselectedTextColor = Color(0xFF5F6368)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Setup.route) {
                SetupScreen(
                    onSetupComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Setup.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToThemes = { navController.navigate(Screen.Themes.route) },
                    onNavigateToHelp = { navController.navigate(Screen.Help.route) },
                    onNavigateToSetup = { navController.navigate(Screen.Setup.route) },
                    onNavigateToSandbox = { navController.navigate(Screen.Sandbox.route) },
                    onNavigateToStats = { navController.navigate(Screen.Stats.route) },
                    onNavigateToAppSettings = { navController.navigate(Screen.AppSettings.route) },
                    onNavigateToPreferences = { navController.navigate(Screen.SettingsPreferences.route) },
                    onNavigateToAppearance = { navController.navigate(Screen.SettingsAppearance.route) },
                    onNavigateToTextCorrection = { navController.navigate(Screen.SettingsTextCorrection.route) },
                    onNavigateToMoreLanguages = { navController.navigate(Screen.SettingsMoreLanguages.route) },
                    onNavigateToAdvanced = { navController.navigate(Screen.SettingsAdvanced.route) },
                    onNavigateToGifQuality = { navController.navigate(Screen.SettingsGifQuality.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onNavigateToAppLanguage = { navController.navigate(Screen.SettingsAppLanguage.route) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToPreferences = { navController.navigate(Screen.SettingsPreferences.route) },
                    onNavigateToAppearance = { navController.navigate(Screen.SettingsAppearance.route) },
                    onNavigateToTextCorrection = { navController.navigate(Screen.SettingsTextCorrection.route) },
                    onNavigateToMoreLanguages = { navController.navigate(Screen.SettingsMoreLanguages.route) },
                    onNavigateToAdvanced = { navController.navigate(Screen.SettingsAdvanced.route) },
                    onNavigateToGifQuality = { navController.navigate(Screen.SettingsGifQuality.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onNavigateToAppLanguage = { navController.navigate(Screen.SettingsAppLanguage.route) }
                )
            }
            composable(Screen.SettingsPreferences.route) {
                PreferencesSettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsAppearance.route) {
                AppearanceSettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsTextCorrection.route) {
                TextCorrectionSettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsAdvanced.route) {
                AdvancedSettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsMoreLanguages.route) {
                MoreLanguagesScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsGifQuality.route) {
                GifQualitySettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SettingsAppLanguage.route) {
                AppLanguageScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.AppSettings.route) {
                AppSettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Themes.route) {
                ThemesScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Help.route) {
                HelpScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Sandbox.route) {
                SandboxScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Dictionary.route) {
                DictionaryScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.ClipboardManager.route) {
                ClipboardScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.About.route) {
                AboutScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Stats.route) {
                TypingStatsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.CustomTheme.route) {
                CustomThemeScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
