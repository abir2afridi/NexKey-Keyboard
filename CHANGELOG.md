# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] - 2026-08-02

### 🚀 Features
- **Full internationalization (i18n)** — every hardcoded string extracted into resources and complete translations added for 14 languages: Bengali, Hindi, Arabic, Spanish, French, German, Portuguese, Russian, Japanese, Korean, Chinese (Simplified), Chinese (Traditional), Urdu, and Persian
- **HSV color picker dialog** — full color customization in the theme creator with independent hue/saturation/value sliders
- **Unified header** — animated auto-show/auto-hide header with smooth transitions

### 🐛 Bug Fixes
- **Fixed app turning black when switching between apps** — removed blocking DataStore read from `onCreate`; window background is now themed, so no more black flash on activity recreation
- **Fixed language switch needing a second click** — locale is now applied from a single source of truth (DataStore) instead of racing `AppCompatDelegate.setApplicationLocales()` against activity recreation
- **Fixed display flashing off/on during language switch** — app no longer recreates the activity; the whole Compose tree is wrapped in a locale-specific context (`createConfigurationContext`), so all strings (and RTL layout) update instantly in place, preserving navigation state
- **Fixed stored-label i18n bug** — `popupDismissDelay` settings no longer persist Android resource IDs; stable keys ("Default"/"Short"/"Long") are stored instead
- **Fixed color picker** — independent HSV sliders, correct hue gradient, custom button now appears first

### ♻️ Refactors
- Reactive language switching: composition-local context wrapping (`LocalContext`, `LocalConfiguration`, `LocalLayoutDirection`) replaces activity recreation; `Locale.setDefault()` kept in sync

## [1.3.1] - 2026-08-01

### 🐛 Bug Fixes
- **Fixed cursor jumping to the end when editing mid-word** — moving the cursor inside a composed word (e.g. tapping between the letters of "aple" to make "apple") now inserts at the cursor instead of replacing the whole word; composing state is properly reset whenever the cursor leaves the end of the composing region
- **Fixed emoji deletion leaving a "?" character** — backspace now deletes the full grapheme in one press: surrogate-pair emojis, ZWJ sequences (👨👩👧), variation selectors (❤️), and combining marks (é)
- **Fixed delete key doing nothing when text is selected** — selected text (e.g. select-all) is now fully removed on a single delete press

### ♻️ Refactors
- Restructured input method logic into dedicated handler files (text input, deletion, suggestions, preferences) with per-mode key handlers, and split the settings screen into per-screen files

### 📚 Documentation
- Updated README with release workflow and v1.3.1 release notes

## [1.3.0] - 2026-07-31

### 🚀 Features
- **Custom theme engine** — Full color customization: background, primary keys, key text, special keys, accent, suggestion strip, key popup, sub-character hints, all with a live high-fidelity preview
- **Live preview pinned at top** — The keyboard preview stays fixed while color pickers scroll below it

### 🐛 Bug Fixes
- Fixed cursor jumping to end when moving mid-word — composing buffer is now committed before cursor movement
- Fixed content hidden behind the floating navigation bar — standardized 120dp bottom safe-area clearance across all screens
- Fixed scrollable content clipping behind the bottom bar on Settings grid, Clipboard, Dictionary, Themes, Store tabs, and all sub-settings screens

### 🎨 UI/UX
- Optimized system bar insets and navigation padding across the app
- Increased bottom spacing on Home, Setup, Sandbox, About, Help, Developer, Typing Stats, Custom Theme, and App Settings screens

### ♻️ Refactors
- Decoupled live preview from scrollable customization controls in Custom Theme screen
- Enhanced theme preview visualization with detailed key rendering

## [1.2.0] - 2026-07-30

### 🚀 Features
- **Emoji search** — Keyword-based search inside the emoji panel with 400+ mapped emojis; results shown in horizontal scrollable rows
- **Emoji search layout settings** — Horizontal scroll toggle, visible rows (1–2), configurable in Emoji settings
- **Compact keyboard during emoji search** — Keyboard shrinks to 65% height when search is active for more results visibility
- **Blinking cursor in search bar** — Visual cursor indicator shows text position in the search bar
- **Recent emoji retention** — Configurable expiry (1/7/30/90 days, forever) for recently used emojis
- **Emoji settings screen** — Dedicated settings for emoji retention, search layout, and visible rows
- **GIF/Sticker tabs** — Tab bar in emoji panel with Emoji/GIF/Sticker sections
- **Delete button in emoji panel** — Backspace button in emoji category label row
- **Material Icons for emoji categories** — Replaced emoji icons with standard Material Icons
- **Customizable speed meter fonts** — Multiple font styles (DIGITAL, LCD, SEGMENT, MODERN) for the typing speed meter
- **Digital speed meter** — Real-time CPS (characters per second) meter in the smart toolbar

### 🐛 Bug Fixes
- Fixed popup candidates expanding keyboard to full screen
- Fixed duplicate close/clear buttons in emoji search bar — now single "Close" button
- Fixed keyboard staying in compact mode after switching modes via toolbar — emoji search now auto-closes on mode change
- Fixed keyboard mode not restoring after closing emoji search — now returns to previous mode
- Fixed CI compatibility — default debug signing config
- Removed offensive word filtering (was deprecated)

### ♻️ Refactors
- Enhanced keyboard mode switching with `lastTextMode` tracking for seamless transitions
- Expanded `popupCandidates` for number and symbol keys
- Gboard/Ridmik-style Bangla Jatiyo and Avro keyboard arrangements

### 📚 Documentation
- Added Avro phonetic engine documentation to README
- Updated README for v1.1.0 release

### 🔧 Infrastructure
- CI: removed instrumentation test job (no KVM/HVF on runners)
- CI: switched to macOS runner, fixed ARM64 emulator config
- CI: parallelized builds with Gradle cache

## [1.1.0] - 2026-07-30

### 🚀 Features
- **Avro phonetic layout** — New Bangla Avro input mode, cycles with Phonetic/Jatiyo/Arabic
- **Always-show suggestion strip** — Suggestion bar stays visible even when empty (default: on)
- **Auto-hide toolbar** — Toolbar auto-hides while typing, toggle button shows it back
- **Long-press backspace repeat** — Continuous delete with configurable initial delay and repeat speed
- **Key long-press hints** — Number symbols on top-row keys, accented characters on others
- **Voice input** — RECORD_AUDIO permission handling with App Settings fallback
- **More Languages screen** — Toggle English/Phonetic/Jatiyo/Avro/Arabic layouts on/off
- **Typing analytics** — Track and display typing statistics on the home screen
- **Store screen** — In-app store and navigation
- **Overhauled navigation architecture** — Expanded keyboard capabilities and screen routing
- **Settings grid layout** — Redesigned settings screen with grid-based category layout
- **Dashboard metadata** — Section info tooltips and dynamic user greeting
- **Enhanced emoji panel** — Expanded to 500+ emojis across 8 categories with category tabs

### 🎨 UI/UX
- Suggestion strip shows "Suggestions" placeholder label when empty
- Candidate strip visible by default (alwaysShowSuggestions defaults to true)
- Enter key shows ↵ for multiline fields, action labels (Done/Search/Go) for single-line
- Improved emoji category navigation with tab bar

### 🐛 Bug Fixes
- Fixed enter key showing "Done" and dismissing keyboard in multiline fields
- Fixed auto-language switch on backspace (now only triggers in Phonetic/Avro modes)
- Fixed emulator architecture casing in CI workflow

### ♻️ Refactors
- Extracted composing buffer commitment to dedicated method
- Simplified toolbar badge logic and key grid updates
- Removed standalone typing analysis screen (merged into analytics)

### 🔧 Infrastructure
- Optimized instrumentation test runner in CI

### 🚀 Features
- Initial release with full keyboard functionality
- Bangla Phonetic and English language support
- Smart punctuation auto-correction
- Dynamic theme system with Material 3 color tokens
- Custom accent color support
- Bento grid UI components
- Language preference selector
- Comprehensive user settings screen
- Auto-correction engine with learning capability
- Clipboard management with history
- Emoji panel
- Voice input support

### 🎨 UI/UX
- Material 3 design system migration
- Customizable keyboard height and key radius
- Smart toolbar with globe key, copy, emoji, and theme toggle
- Home screen with theme switcher and grid navigation
- Settings screen with category navigation
- Hold-to-paste feature for clipboard access
- Theme library with customization options
- Hold key to paste functionality

### ⚙️ Configuration
- User preferences via DataStore
- Keyboard customization (layout, themes, spacing)
- Auto-correction toggle
- Smart punctuation toggle
- Voice input key toggles
- Number row toggles
- One-handed mode support
- Split keyboard support
- Incognito mode for password fields

### 🔧 Infrastructure
- Community templates (.github)
- CI workflows for linting and automation
- Labeler and stale issue management
- Code of conduct and contributing guidelines
- Issue templates for bug reports, feature requests, documentation
- Security policy and support guidelines

### 🐛 Bug Fixes
- Fixed missing super call in `onEvaluateInputViewShown()`
- Fixed gradlew execution permissions in CI
- Fixed logo image source in README
