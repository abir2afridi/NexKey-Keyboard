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

    // Settings Sub-screens
    object SettingsPreferences : Screen("settings_preferences")
    object SettingsAppearance : Screen("settings_appearance")
    object SettingsTextCorrection : Screen("settings_text_correction")
    object SettingsAdvanced : Screen("settings_advanced")
    object SettingsMoreLanguages : Screen("settings_more_languages")
    object SettingsGifQuality : Screen("settings_gif_quality")
    object SettingsEmoji : Screen("settings_emoji")
    object SettingsAppLanguage : Screen("settings_app_language")
    object AppSettings : Screen("app_settings")
    object Developer : Screen("developer")

    // Settings group screens
    object SettingsTyping : Screen("settings_typing")
    object SettingsFeedback : Screen("settings_feedback")
    object SettingsLanguageKeys : Screen("settings_language_keys")
    object SettingsLayout : Screen("settings_layout")
    object SettingsSize : Screen("settings_size")
    object SettingsNavigation : Screen("settings_navigation")
    object SettingsPaste : Screen("settings_paste")
    object SettingsAdvancedGroup : Screen("settings_advanced_group")
    object Store : Screen("store")
}
