package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Setup : Screen("setup")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Themes : Screen("themes")
    object Help : Screen("help")
    object Sandbox : Screen("sandbox")
    object Dictionary : Screen("dictionary")
    object ClipboardManager : Screen("clipboard_manager")
    object About : Screen("about")
    object Stats : Screen("stats")
    object CustomTheme : Screen("custom_theme")
}
