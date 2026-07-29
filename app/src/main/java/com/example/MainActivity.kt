package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clipboard.ClipboardManager
import com.example.ui.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.checkIsKeyboardEnabled
import com.example.ui.checkIsKeyboardSelected

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ClipboardManager.init(applicationContext)
        setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val prefs = androidx.compose.runtime.remember { com.example.data.UserPreferences(context) }
            val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")
            val accentColor by prefs.accentColor.collectAsState(initial = "#FF2E7D32")

            MyApplicationTheme(appTheme = appTheme, accentColorHex = accentColor) {
                NexKeyApp()
            }
        }
    }
}

@Composable
fun NexKeyApp() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = androidx.compose.runtime.remember { com.example.data.UserPreferences(context) }
    val navigationStyle by prefs.navigationStyle.collectAsState(initial = "STANDARD")
    val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")

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
            if (navigationStyle == "STANDARD") {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Home", fontSize = 12.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Store", fontSize = 12.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Store.route } == true,
                        onClick = {
                            navController.navigate(Screen.Store.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Keyboard, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("Keyboard", fontSize = 12.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Settings.route } == true,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(26.dp)) },
                        label = { Text("App Settings", fontSize = 12.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.AppSettings.route } == true,
                        onClick = {
                            navController.navigate(Screen.AppSettings.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                    val scope = rememberCoroutineScope()
                    HomeScreen(
                        appTheme = appTheme,
                        onToggleTheme = {
                            scope.launch {
                                prefs.setAppTheme(
                                    when (appTheme) {
                                        "SYSTEM" -> "DARK"
                                        "DARK" -> "LIGHT"
                                        else -> "SYSTEM"
                                    }
                                )
                            }
                        },
                        onNavigateToSetup = { navController.navigate(Screen.Setup.route) },
                        onNavigateToSandbox = { navController.navigate(Screen.Sandbox.route) },
                        onNavigateToStats = { navController.navigate(Screen.Stats.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                        onNavigateToClipboard = { navController.navigate(Screen.ClipboardManager.route) },
                        onNavigateToTextCorrection = { navController.navigate(Screen.SettingsTextCorrection.route) },
                        onNavigateToMoreLanguages = { navController.navigate(Screen.SettingsMoreLanguages.route) },
                        onNavigateToGifQuality = { navController.navigate(Screen.SettingsGifQuality.route) },
                        onNavigateToTypingAnalysis = { navController.navigate(Screen.TypingAnalysis.route) }
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToTyping = { navController.navigate(Screen.SettingsTyping.route) },
                        onNavigateToFeedback = { navController.navigate(Screen.SettingsFeedback.route) },
                        onNavigateToLanguageKeys = { navController.navigate(Screen.SettingsLanguageKeys.route) },
                        onNavigateToLayout = { navController.navigate(Screen.SettingsLayout.route) },
                        onNavigateToSize = { navController.navigate(Screen.SettingsSize.route) },
                        onNavigateToNavigation = { navController.navigate(Screen.SettingsNavigation.route) },
                        onNavigateToPaste = { navController.navigate(Screen.SettingsPaste.route) },
                        onNavigateToAdvancedGroup = { navController.navigate(Screen.SettingsAdvancedGroup.route) },
                        onNavigateToTextCorrection = { navController.navigate(Screen.SettingsTextCorrection.route) },
                        onNavigateToMoreLanguages = { navController.navigate(Screen.SettingsMoreLanguages.route) },
                        onNavigateToGifQuality = { navController.navigate(Screen.SettingsGifQuality.route) }
                    )
                }
                composable(Screen.SettingsTyping.route) {
                    TypingSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsFeedback.route) {
                    FeedbackSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsLanguageKeys.route) {
                    LanguageKeysSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsLayout.route) {
                    LayoutSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsSize.route) {
                    SizeSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsNavigation.route) {
                    NavigationSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsPaste.route) {
                    PasteSettingsScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToClipboardHistory = { navController.navigate(Screen.ClipboardManager.route) }
                    )
                }
                composable(Screen.SettingsAdvancedGroup.route) {
                    AdvancedGroupSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.SettingsTextCorrection.route) {
                    TextCorrectionSettingsScreen(onBack = { navController.popBackStack() })
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
                    AppSettingsScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToAppLanguage = { navController.navigate(Screen.SettingsAppLanguage.route) },
                        onNavigateToAbout = { navController.navigate(Screen.About.route) }
                    )
                }
                composable(Screen.Developer.route) {
                    DeveloperScreen(onBack = { navController.popBackStack() })
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
                    AboutScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToDeveloper = { navController.navigate(Screen.Developer.route) }
                    )
                }
                composable(Screen.Stats.route) {
                    TypingStatsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.TypingAnalysis.route) {
                    TypingAnalysisScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.CustomTheme.route) {
                    CustomThemeScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.Store.route) {
                    StoreScreen(
                        onNavigateToThemes = { navController.navigate(Screen.Themes.route) },
                        onNavigateToCustomTheme = { navController.navigate(Screen.CustomTheme.route) }
                    )
                }
            }
            
            if (navigationStyle == "FLOATING") {
                FloatingNavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp)
                ) {
                    FloatingNavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text("Home", fontSize = 11.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    FloatingNavigationBarItem(
                        icon = { Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text("Store", fontSize = 11.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Store.route } == true,
                        onClick = {
                            navController.navigate(Screen.Store.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    FloatingNavigationBarItem(
                        icon = { Icon(Icons.Default.Keyboard, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text("Keyboard", fontSize = 11.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.Settings.route } == true,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    FloatingNavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text("App Settings", fontSize = 11.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == Screen.AppSettings.route } == true,
                        onClick = {
                            navController.navigate(Screen.AppSettings.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
