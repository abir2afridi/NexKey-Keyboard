# NexKey Keyboard

**Original Multilingual Android Keyboard** — Ridmik-class Bangla phonetic typing + English + Arabic + unlimited language support.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin)
![API](https://img.shields.io/badge/minSdk-24-3DDC84)
![Target](https://img.shields.io/badge/targetSdk-36-3DDC84)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

---

## Features

| Feature | Status |
|---|---|
| QWERTY & Bangla Phonetic typing | ✅ |
| Bangla Jatiyo (National) layout | ✅ |
| Arabic layout | ✅ |
| Symbols & Numbers panels | ✅ |
| Composing-region state machine | ✅ |
| EditorInfo.inputType handling | ✅ |
| IME action labels (Search/Go/Done/Next) | ✅ |
| Trie-based prediction engine | ✅ |
| Persistent learned words (Room DB) | ✅ |
| Voice typing (Android SpeechRecognizer) | ✅ |
| Clipboard manager (history, pin, Room DB) | ✅ |
| Emoji panel | ✅ |
| Theme system (4 presets, DataStore) | ✅ |
| App theme (System/Light/Dark) | ✅ |
| Auto-capitalization | ✅ |
| Caps lock (double-tap shift) | ✅ |
| Smart punctuation (double-space period) | ✅ |
| Incognito mode (no learning, no clipboard) | ✅ |
| Password/sensitive field detection | ✅ |
| TalkBack accessibility labels (all keys) | ✅ |
| Lifecycle-aware IME for Jetpack Compose | ✅ |
| In-app Setup Wizard (enable + select) | ✅ |
| Continuous IME state polling for robust detection | ✅ |
| Gradle wrapper (CI-ready) | ✅ |
| Gesture/swipe typing | 🚧 Planned |
| DAWG dictionary | 🚧 Planned |
| Autocorrect with undo-on-backspace | 🚧 Planned |
| Text expansion / shortcuts | 🚧 Planned |
| Physical keyboard support | 🚧 Planned |
| Foldable & stylus support | 🚧 Planned |
| Handwriting interface | 🚧 Planned |
| App localization (BN/HI/AR) | 🚧 Planned |
| In-app updates | 🚧 Planned |
| Crash watchdog | 🚧 Planned |
| 46+ language support | ✅ |
| App Language selector | ✅ |

---

## Architecture

```
app/
└── src/main/java/com/example/
    ├── MainActivity.kt                     — Dashboard & live typing sandbox
    ├── ime/
    │   ├── NexKeyInputMethodService.kt     — Core IME service (input handling, key events)
    │   └── LifecycleInputMethodService.kt  — Compose-host IME base with LifecycleOwner
    ├── engine/
    │   ├── BanglaPhoneticEngine.kt         — Phonetic transliteration (80+ conjuncts)
    │   └── PredictionEngine.kt             — Trie-based prediction & Room-backed learning
    ├── ui/
    │   ├── SetupScreen.kt                  — In-app setup wizard (enable + select steps)
    │   ├── HomeScreen.kt                   — Dashboard home with status card
    │   ├── AppSettingsScreen.kt            — App theme, language, about
    │   ├── SettingsSubScreens.kt           — Language picker, settings scaffold
    │   ├── SettingsScreen.kt               — User preferences UI
    │   ├── KeyboardComposeView.kt          — Keyboard UI, toolbar, panels
    │   ├── KeyboardLayouts.kt              — Layout data models & key maps
    │   └── Components.kt                   — Reusable composables (SettingItem, etc.)
    ├── data/
    │   ├── AppDatabase.kt                  — Room database (clips, learned words)
    │   ├── ClipEntity.kt / ClipDao.kt              — Clipboard persistence
    │   ├── LearnedWordEntity.kt / LearnedWordDao.kt — Dictionary persistence
    │   └── UserPreferences.kt              — DataStore (theme, language, incognito, settings)
    ├── clipboard/
    │   └── ClipboardManager.kt             — Clipboard with Room persistence & incognito
    └── theme/
        └── KeyboardTheme.kt                — Theme data model & presets
```

---

## Setup Flow

When you first launch NexKey, the in-app setup wizard guides you through two steps:

1. **Enable** — Opens system Settings → Languages & input → On-screen keyboard. Toggle NexKey on.
2. **Select as Default** — Opens the system input method picker dialog. Choose NexKey.

The wizard polls every 500ms to detect when both conditions are met, then automatically proceeds to the home screen. No manual back-and-forth needed.

---

## Build

```bash
# Debug APK
./gradlew assembleDebug

# Install on device/emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Run unit tests
./gradlew testDebugUnitTest

# Run instrumented tests (emulator required)
./gradlew connectedDebugAndroidTest
```

**Prerequisites:** Android Studio, JDK 21+, Android SDK (compileSdk 36).

---

## How to Use

### From the App
1. Install the APK and open the NexKey app.
2. Follow the in-app setup wizard to enable and select NexKey.
3. Open any app with a text field — NexKey appears automatically.

### Manually from Settings
1. Go to **Settings → System → Languages & input → On-screen keyboard**.
2. Tap **NexKey** and enable it.
3. Go back and tap **Default keyboard** → select **NexKey**.
4. Switch to NexKey in any app via the keyboard switcher (globe) icon.

---

## Bangla Phonetic Typing

NexKey implements a full Ridmik-class phonetic transliteration engine from first principles. Type Latin phonetically and the engine converts to correct Unicode Bengali:

| Input | Output |
|---|---|
| `ami banglay likhi` | `আমি বাংলায় লিখি` |
| `sUNy khAd` | `শূন্য খাদ` |
| `kSiti` | `ক্ষিতি` |
| `a` + `A` | `আ` (independent + vowel sign) |
| `kk` | `ক্ক` (juktakkhor conjunct) |
| `..` | `।` (dari / sentence end) |

80+ conjunct rules, NFC normalization, Chandrabindu, Anusvara, Visarga, and Bangla numerals are supported.

---

## Troubleshooting

### Keyboard does not appear when tapping a text field
- Ensure NexKey is both **enabled** and **selected as default** in system settings.
- On emulators: the Android emulator often detects a "hardware keyboard" and suppresses the soft keyboard. NexKey overrides `onEvaluateInputViewShown()` to force the keyboard to show.
- Rarely, a reboot may be required after first-time setup.

### Setup wizard does not detect enabled/selected state
- The wizard polls every 500ms automatically — wait a moment after returning from settings.
- On Android 14+: the old `Settings.Secure.ENABLED_INPUT_METHODS` key is restricted. NexKey uses `InputMethodManager.getEnabledInputMethodList()` and `InputMethodManager.getCurrentInputMethodInfo()` instead.

---

## Accessibility

- **Full TalkBack support** — Every key, toolbar icon, emoji, and clipboard item has a proper `contentDescription`. Shift state is announced dynamically.
- **Role semantics** — All interactive elements use `Role.Button` for correct screen-reader navigation.
- System font scale respected at up to 200%.

---

## Privacy & Security

- **Password field detection** — Automatically disables prediction, learning, and clipboard history in password/sensitive fields.
- **Incognito mode** — Toggle on/off from toolbar. Zero word learning, zero clipboard history, zero prediction logging.
- **Persistent data** — Clipboard and learned words stored in Room database on-device only.
- **No network calls** — Zero network calls.
- **Permissions** — `RECORD_AUDIO` (voice typing), `VIBRATE` (haptic feedback), `POST_NOTIFICATIONS` (Android 13+).
- See [docs/PERMISSIONS.md](docs/PERMISSIONS.md) for full details.

---

## Recent Fixes

### IME Lifecycle for Compose (v1.0)
- `ViewTreeLifecycleOwner`, `ViewModelStoreOwner`, and `SavedStateRegistryOwner` are now set on **both** the input view and the window's DecorView, fixing Compose-in-IME crashes.
- Lifecycle dispatch moved from `onWindowShown()`/`onWindowHidden()` to `onStartInputView()`/`onFinishInputView()` for correct timing.
- `onEvaluateInputViewShown()` overridden to return `true`, ensuring the keyboard appears on emulators and devices with hardware keyboards.

### Setup Wizard Detection (v1.0)
- Step 2 now uses `InputMethodManager.showInputMethodPicker()` instead of opening settings again.
- Continuous polling (every 500ms) detects state changes that lifecycle events miss.
- `checkIsKeyboardSelected()` uses `InputMethodManager.getCurrentInputMethodInfo()` on API 34+, falls back to `Settings.Secure.DEFAULT_INPUT_METHOD` on older versions.

### App Theme Across All Screens (v1.0)
- All screens now use `MaterialTheme.colorScheme.*` instead of hardcoded `Color.White`.
- App theme (System/Light/Dark) setting in `AppSettingsScreen` persists via DataStore and updates every screen in real time.
- `key(appTheme)` forces recomposition when theme changes, ensuring NavHost destinations reflect the new theme.

### Gemini AI Removal (v1.0)
- Removed `AI_ASSIST` keyboard mode, `AiAssistPanel` composable, toolbar AI button, and all Gemini API key references.
- Removed secrets Gradle plugin, `.env.example`, and CI `GEMINI_API_KEY` env vars.

---

## Legal & Originality

- All code is original, written from first principles.
- Bangla phonetic rules built from Unicode Bengali block specification (Unicode 15.0/16.0).
- No source code, assets, or algorithms copied from Ridmik, Gboard, SwiftKey, or any other keyboard app.
- Third-party dependencies are OSS with permissive licenses (Apache-2.0, MIT, BSD).
- See [docs/DATA_SOURCES.md](docs/DATA_SOURCES.md) for dictionary corpora sources.
- See [docs/THIRD_PARTY_LICENSES.md](docs/THIRD_PARTY_LICENSES.md) for dependency licenses.

---

## License

```
Copyright 2026 NexKey Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
