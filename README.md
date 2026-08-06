# NexKey Keyboard

<!-- markdownlint-disable MD033 -->
<p align="center">
  <img src="https://github.com/abir2afridi/NexKey-Keyboard/blob/main/app/src/main/res/drawable/icon_header.png" alt="NexKey Logo" width="128" height="128">
</p>
<!-- markdownlint-enable MD033 -->

**Original Multilingual Android Keyboard** — Ridmik-class Bangla phonetic typing + English + Arabic + unlimited language support, with the app itself localized into 15 languages.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin)
![API](https://img.shields.io/badge/minSdk-24-3DDC84)
![Target](https://img.shields.io/badge/targetSdk-36-3DDC84)
![Version](https://img.shields.io/badge/version-1.5.0-blue)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)
[![Contributing](https://img.shields.io/badge/contributing-guide-2ea043)](.github/CONTRIBUTING.md)
[![Code of Conduct](https://img.shields.io/badge/code%20of%20conduct-Contributor%20Covenant-7057ff)](.github/CODE_OF_CONDUCT.md)
[![Security](https://img.shields.io/badge/security-policy-e4e669)](.github/SECURITY.md)
[![Changelog](https://img.shields.io/badge/changelog-1.5.0-A97BFF)](CHANGELOG.md)
[![Issues](https://img.shields.io/badge/issue%20templates-7%20forms-1B5E20)](https://github.com/abir2afridi/NexKey-Keyboard/issues/new/choose)
[![Releases](https://img.shields.io/badge/releases-1.5.0-ff69b4)](../releases)

---

## Features

| Feature | Status |
|---|---|---|
| **Typing & Languages** | |
| QWERTY & Bangla Phonetic typing | ✅ |
| Bangla Jatiyo (National) layout | ✅ |
| Bangla Avro phonetic layout | ✅ |
| Avro phonetic engine (Avro-style transliteration) | ✅ |
| Arabic layout | ✅ |
| 80+ Bangla conjunct (juktakkhor) rules | ✅ |
| 46+ language support | ✅ |
| More Languages screen (toggle English/Phonetic/Jatiyo/Avro/Arabic) | ✅ |
| App Language selector | ✅ |
| **Text Input** | |
| Symbols & Numbers panels | ✅ |
| Auto-capitalization | ✅ |
| Caps lock (double-tap shift) | ✅ |
| Smart punctuation (double-space period) | ✅ |
| Double-space tab | ✅ |
| Composing-region state machine | ✅ |
| EditorInfo.inputType handling | ✅ |
| IME action labels (Search/Go/Done/Next) with multiline detection | ✅ |
| Space bar cursor move (swipe to position caret) | ✅ |
| **Prediction & Correction** | |
| Trie-based prediction engine | ✅ |
| Persistent learned words (Room DB) | ✅ |
| Always-show suggestion strip | ✅ |
| Autocorrect with undo-on-backspace | 🚧 Planned |
| DAWG dictionary | 🚧 Planned |
| **Voice, Clipboard & Paste** | |
| Voice typing (Android SpeechRecognizer) with permission handling | ✅ |
| Clipboard manager (history, pin, Room DB, auto-expiry, system listener) | ✅ |
| Hold-to-paste (configurable trigger key & duration) | ✅ |
| **Emoji & Stickers** | |
| Emoji panel (500+ emojis, 8 categories, category tabs) | ✅ |
| Emoji search (keyword-based, 400+ mapped emojis) | ✅ |
| Emoji search layout settings (horizontal/vertical, visible rows) | ✅ |
| Recent emoji retention (configurable expiry) | ✅ |
| GIF/Sticker tabs (placeholder) | ✅ |
| **Themes & Appearance** | |
| Theme system (4 presets, DataStore) | ✅ |
| Custom theme engine (11 color groups + live preview) | ✅ |
| HSV color picker (theme creator) | ✅ |
| App theme (System/Light/Dark) | ✅ |
| Unified animated header (auto-show/hide) | ✅ |
| Auto-hide toolbar | ✅ |
| **Speed Meter & Info Box** | |
| Typing speed meter (CPS / words / keys counts, themes, fonts, intervals) | ✅ |
| Info Box with selectable frames & swipe text color | ✅ |
| Info Box custom texts (Off / Timed / Always modes, show duration, custom color) | ✅ |
| **Privacy & Security** | |
| Incognito mode (no learning, no clipboard) | ✅ |
| Password/sensitive field detection | ✅ |
| **Accessibility & Input Feedback** | |
| TalkBack accessibility labels (all keys) | ✅ |
| Key long-press hint popups (numbers for top row, accents for others) | ✅ |
| Long-press backspace continuous delete (customizable speed & delay) | ✅ |
| **IME & System** | |
| Lifecycle-aware IME for Jetpack Compose | ✅ |
| In-app Setup Wizard (enable + select) | ✅ |
| Continuous IME state polling for robust detection | ✅ |
| Instant flicker-free language switching (no activity recreation) | ✅ |
| Settings group screens (Typing, Feedback, Layout, Advanced, etc.) | ✅ |
| Bottom navigation (Home, Store, Keyboard, Leaderboard) + App Settings in page headers | ✅ |
| Gradle wrapper (CI-ready) | ✅ |
| **App Localization** | |
| App localization (14 languages + English) | ✅ |
| **Planned** | |
| Gesture/swipe typing | 🚧 Planned |
| Text expansion / shortcuts | 🚧 Planned |
| Physical keyboard support | 🚧 Planned |
| Foldable & stylus support | 🚧 Planned |
| Handwriting interface | 🚧 Planned |
| In-app updates | 🚧 Planned |
| Crash watchdog | 🚧 Planned |

---

## Architecture

```text
app/
└── src/main/java/com/example/
    ├── MainActivity.kt                     — Navigation host, bottom nav, route registration, locale-aware composition (createConfigurationContext + composition locals)
    ├── ime/
    │   ├── NexKeyInputMethodService.kt     — Core IME service (state, lifecycle, view wiring)
    │   ├── LifecycleInputMethodService.kt  — Compose-host IME base with LifecycleOwner
    │   ├── TextInputHandler.kt             — Key tap, space, enter, shift, mode change, cursor
    │   ├── TextDeletion.kt                 — Backspace / text deletion logic
    │   ├── SuggestionHandler.kt            — Candidate updates, suggestion commit, composing commit
    │   ├── SpeedMeterHandler.kt            — Typing speed window, burst stats, swipe info lines
    │   ├── ImePreferenceCollector.kt       — All DataStore preference collection
    │   └── modes/
    │       ├── KeyboardModeDispatcher.kt   — Mode-aware compose/parse dispatch
    │       ├── BanglaPhoneticMode.kt       — Phonetic-mode rules
    │       ├── AvroMode.kt                 — Avro-mode rules
    │       ├── EnglishMode.kt              — English-mode rules
    │       ├── ArabicMode.kt               — Arabic-mode rules
    │       └── BanglaJatiyoMode.kt         — Jatiyo-mode rules
    ├── engine/
    │   ├── BanglaPhoneticEngine.kt         — Phonetic transliteration (80+ conjuncts)
    │   ├── AvroPhoneticEngine.kt           — Avro-style transliteration (distinct from phonetic)
    │   └── PredictionEngine.kt             — Trie-based prediction & Room-backed learning
    ├── ui/
    │   ├── navigation/
    │   │   └── NavGraph.kt                 — Screen route definitions
    │   ├── settings/                       — One file per settings screen
    │   │   ├── SettingsSubScaffold.kt      — Shared settings scaffold (back bar + bottom clearance)
    │   │   ├── TypingSettingsScreen.kt     — Auto-cap, double-space period/tab
    │   │   ├── FeedbackSettingsScreen.kt   — Haptics, sound, popup, intensity sliders
    │   │   ├── LanguageKeysSettingsScreen.kt — Voice/emoji/globe keys
    │   │   ├── LayoutSettingsScreen.kt     — Number row, split keyboard, suggestions bar
    │   │   ├── SizeSettingsScreen.kt       — Height/width sliders
    │   │   ├── NavigationSettingsScreen.kt — Space & volume cursor control
    │   │   ├── PasteSettingsScreen.kt      — Hold-to-paste, clipboard expiry
    │   │   ├── AdvancedGroupSettingsScreen.kt — Long-press delay, backspace repeat
    │   │   ├── TextCorrectionSettingsScreen.kt — Autocorrect, suggestions toggles
    │   │   ├── MoreLanguagesScreen.kt      — Enable/disable keyboard languages
    │   │   ├── GifQualitySettingsScreen.kt — GIF quality options
    │   │   ├── EmojiSettingsScreen.kt      — Recent emoji, search layout
    │   │   └── AppLanguageScreen.kt        — App interface language selector
    │   ├── SetupScreen.kt                  — In-app setup wizard (enable + select steps)
    │   ├── HomeScreen.kt                   — Dashboard home with status card
    │   ├── StoreScreen.kt                  — Store (Shop / Themes / Meter / Info Box tabs)
    │   ├── AppSettingsScreen.kt            — App theme, language, about
    │   ├── SettingsScreen.kt               — Keyboard Settings hub (groups index)
    │   ├── KeyboardComposeView.kt          — Keyboard UI, toolbar, panels
    │   ├── KeyboardLayouts.kt              — Layout data models & key maps
    │   ├── Components.kt                   — Reusable composables (SettingItem, etc.)
    │   ├── ClipboardScreen.kt              — Full-screen clipboard history manager
    │   └── ClipboardPanel.kt               — Inline keyboard clipboard panel
    ├── data/
    │   ├── AppDatabase.kt                  — Room database (clips, learned words)
    │   ├── ClipEntity.kt / ClipDao.kt              — Clipboard persistence
    │   ├── LearnedWordEntity.kt / LearnedWordDao.kt — Dictionary persistence
    │   └── UserPreferences.kt              — DataStore (theme, language, incognito, settings)
    ├── clipboard/
    │   └── ClipboardManager.kt             — Clipboard with Room persistence, system OnPrimaryClipChangedListener & auto-expiry loop
    └── theme/
        ├── KeyboardTheme.kt                — Theme data model & presets
        └── MeterTheme.kt                   — Typing speed meter theming
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

## Latest Release

**Version:** 1.5.0 | **Published:** August 5, 2026

- **APK:** [Download v1.5.0](https://github.com/abir2afridi/NexKey-Keyboard/releases/latest)
- **Release Notes:** See [RELEASE_NOTES.md](RELEASE_NOTES.md) for full details

**Highlights:**

- ✅ Keyboard resizing — adjust keyboard height/width for portrait and landscape with a live preview
- ✅ Emoji leaderboard and all-emoji browsing screen with search
- ✅ App theme (System/Light/Dark) applied live across every screen
- ✅ Info Box and Speed Meter customization — colors, fonts, frames, display modes, record tracking
- ✅ Key press popup now anchored to the exact tapped key with fast auto-dismiss
- ✅ App settings moved from the bottom bar to top actions

**Checksums:**

| File | SHA256 | MD5 |
| --- | --- | --- |
| NexKey-Keyboard-v1.5.0.apk | `1ca57b5be84674108b6a1ed0354c6c32adf8cfcd4a1d6de6984b972cdbfd6896` | `6ac9ed774eb926c196c2a846c05653f2` |

[View all releases →](../releases)

---

## How to Use

### Install Latest Release

1. Download the APK from the [Latest Release](https://github.com/abir2afridi/NexKey-Keyboard/releases/latest)
2. Enable "Unknown sources" in Android settings
3. Tap the downloaded APK to install
4. Open NexKey and follow the setup wizard

### From the App

1. Install the APK and open the NexKey app.
2. Follow the in-app setup wizard to enable and select NexKey.
3. Open any app with a text field — NexKey appears automatically.
4. Use the bottom navigation bar to access **Keyboard Settings** (grouped screens for Typing, Feedback, Layout, Size, etc.) and **App Settings** (theme, language, about).

### Manually from Settings

1. Go to **Settings → System → Languages & input → On-screen keyboard**.
2. Tap **NexKey** and enable it.
3. Go back and tap **Default keyboard** → select **NexKey**.
4. Switch to NexKey in any app via the keyboard switcher (globe) icon.

---

## Bangla Phonetic Typing

NexKey implements a full Ridmik-class phonetic transliteration engine from first principles. Type Latin phonetically and the engine converts to correct Unicode Bengali:

| Input | Output |
| --- | --- |
| `ami banglay likhi` | `আমি বাংলায় লিখি` |
| `sUNy khAd` | `শূন্য খাদ` |
| `kSiti` | `ক্ষিতি` |
| `a` + `A` | `আ` (independent + vowel sign) |
| `kk` | `ক্ক` (juktakkhor conjunct) |
| `..` | `।` (dari / sentence end) |

80+ conjunct rules, NFC normalization, Chandrabindu, Anusvara, Visarga, and Bangla numerals are supported.

---

## Avro Phonetic Typing

NexKey includes a dedicated Avro phonetic engine that provides a distinct typing experience from the standard Bangla phonetic mode. The Avro engine uses Avro-style transliteration rules for a more familiar experience for Avro users.

| Feature | Description |
| --- | --- |
| Avro phonetic engine | Dedicated `AvroPhoneticEngine` with Avro-specific transliteration rules |
| Avro-specific layouts | Dedicated `AvroRow` layouts in `BanglaLayout` |
| Popup candidates | Expanded popup candidates for English and Bangla layouts |
| Mode switching | Toggle between Phonetic, Jatiyo, Avro, and Arabic via More Languages screen |

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

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for the full release history.

---

## Contributing

We welcome contributions! See [CONTRIBUTING.md](.github/CONTRIBUTING.md) for:

- 🐛 [Bug reports](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=01-bug-report.yml)
- 💡 [Feature requests](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=02-feature-request.yml)
- 📖 [Documentation fixes](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=03-documentation.yml)
- ⚡ [Performance reports](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=04-performance.yml)
- 🎨 [UI/UX feedback](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=05-ui-ux.yml)
- 🔒 [Security advisories](.github/SECURITY.md)

All contributors must follow our [Code of Conduct](.github/CODE_OF_CONDUCT.md).

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

```text
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
