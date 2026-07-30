# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
