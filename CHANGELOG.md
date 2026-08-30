# Changelog

All notable changes to NexKey Keyboard are documented here.

The format is inspired by [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.7.0] — August 30, 2026

### Added
- Symbol mode toggle (`?123` button) — pure symbol toggle that no longer triggers language switching on tap; toggles between text and symbol modes cleanly
- Independent language-switch settings — `showGlobeKey` and `spacebarLanguageSwitch` can now be toggled separately with minimum-one enforcement
- Arabic keyboard layout enable/disable option
- Toolbar pinning — typing keeps the toolbar open until a new typing burst begins, preventing toolbar flicker during fast input
- Release signing with a dedicated keystore (`key.properties`)

### Fixed
- Backspace ghost repeat — rapid backspace taps no longer trigger phantom repeats after finger lift; the repeat job lifecycle is now properly managed with a remembered state variable and DisposableEffect cleanup
- Toolbar visibility decoupled from suggestion state — the toolbar no longer collapses when suggestions clear
- Spacebar hold-then-swipe — hold gate only activates language switch on swipe, not on hold alone
- `?123` button no longer changes keyboard language — bypasses `handleModeChange` language logic via dedicated `handleSymbolToggle()`
- Language switch DataStore sync — returning from symbol mode no longer reverts to the wrong language via `ImePreferenceCollector`

### Refactored
- Keyboard ComposeView — unified header state management with `toolbarPinned` + `prevUserTyping` flags
- Speed meter elapsed timer computed locally in Compose via `LaunchedEffect` instead of a background ticker job
- Suggestion engine (`updateCandidates`) moved to `Dispatchers.Default` to unblock the UI thread during fast typing
- `recentEmojis` list stability — a single reusable `mutableStateListOf` replaces per-recomposition allocation
- Removed `elapsedTickerJob` that caused 5 unnecessary recompositions/sec across the entire keyboard grid

### Performance
- Eliminated per-keypress recomposition cascade: removing the 200ms elapsed ticker and stabilizing the emoji list significantly reduced unnecessary keyboard re-renders during fast multi-finger typing
- Prediction engine suggestions now run off the main thread, preventing UI stalls on every letter

---

## [1.6.0] — August 7, 2026

### Added
- Real predictive dictionary engine: new `:feature:prediction-engine` module with a minimized DAWG (Direct Acyclic Word Graph) over real English and Bangla word-frequency data, replacing the old seeded trie. ~795 KB English (46,717 words) and ~546 KB Bangla (45,757 words) assets load in ~54 ms / ~37 ms respectively
- Typo auto-correction derived generically from the dictionary: recive→receive, beleive→believe, definately→definitely, enviroment→environment, plus swapped (hte→the), extra (thhe→the), missing (god→good) and repeated (goodd→good) letter cases — no hardcoded pairs, weighted by QWERTY key distance
- Next-word prediction from a learned bigram/trigram model with interpolated backoff — after "thank you" the keyboard offers "so" long before it is typed
- Personal learning with a confidence threshold (3 uses): "vhal" quickly surfaces "vhalo" first, and after committing "vhalo" the next-word strip offers "lagtase"
- Banglish disambiguation: korbo/korteci/korsi are ranked and corrected independently and never cross-corrected; a personal Banglish word at the threshold is protected from auto-correction
- Emoji prediction by keyword on the suggestion strip (happy/birthday/love/fire/laugh), toggleable like the other providers
- Live feature-flag gating for autocorrect, typo correction, next-word, personal learning, personalized suggestions, emoji prediction, and incognito — a Settings change takes effect on the next keystroke
- Autocorrect OFF now leaves committed text exactly as typed while the strip still offers the corrected spelling; ON rewrites it at the word boundary

### Changed
- Suggestion strip in the IME now runs on the new PredictionProvider (DictionaryManager): prefix-ranked candidates merge builtin dictionary, personal learning, next-word n-grams and emoji into one list
- The hardcoded English/Bangla seed dictionaries in `PredictionEngine` are gone — suggestions come from real frequency data
- Personal data (learned words, phrases, recents, favorites) is stored in its own Room database (`nexkey_prediction_database`) separate from app data
- Sandbox and IME screens share the same prediction engine instance; privacy gate applies to password/sensitive fields

### Fixed
- Learned bigrams never surface after restart: the n-gram index is now rebuilt from persisted Room phrases on init, matching the module's own tests

---

## [1.5.0] — August 5, 2026

### Added
- Keyboard Resizing screen — unlock resizing from Layout settings, then adjust keyboard height and width for portrait and landscape with a live keyboard preview (real theme, current presets) and a one-tap reset
- Emoji Leaderboard — usage tracking and a multi-tab leaderboard screen ranking your most-used emojis
- All Emojis browsing screen with search and full localization
- App theme toggling (System/Light/Dark) applied live across every screen
- Store expanded with new tabs and localization for all new sections
- Info Box further customization — custom text color, font selection, additional frame presets, and a dedicated Info Box settings screen with live preview
- Speed Meter expanded — configurable display modes (meter/count) and record tracking, with all meter settings consolidated into a dedicated Speed Meter screen
- App Settings moved from the bottom bar to top actions for more keyboard space
- Spacebar language quick-switcher — long-press the spacebar to cycle through enabled languages (toggle in Language Keys settings)

### Fixed
- Letter preview popup appearing over the wrong key — the popup is now anchored to the actual tapped key and auto-dismisses faster
- Speed meter font styles not applying — bundled DSEG fonts are now actually wired up
- No visual feedback when pressing letter/space/enter keys — every key now has a press-and-bounce animation matching the delete key style
- Holding delete to clear text was slow and left words behind — now switches to word-by-word deletion after the initial repeat delay
- Last character of a word surviving delete — backspace now truly deletes the final composing character/word instead of committing it into the editor, so single words delete with one press and hold-delete clears the whole sentence
- Spacebar language switcher now supports swipe (left/right, the quick default) in addition to long-press when enabled
- Spacebar swipe gesture rebuilt to fire instantly on a quick flick (previously the tap detector cancelled the drag, so it only worked after holding)
- Spacebar language switch now shows a popup with the new language above the spacebar, and both swipe directions work (left = next language, right = previous), like Gboard
- Fixed: repeated spacebar swipes in the same direction now keep cycling through languages (previously the gesture handler kept a stale mode, so the language appeared stuck after the first swipe)
- Smoother typing: key press animation no longer bounces (quick tween like Gboard), and keypress sound no longer runs on the UI thread — fixes the laggy feel while typing
- Even faster fast-typing: keypress vibration now runs off the UI thread too, and auto-capitalize no longer queries the editor on every letter mid-word (that cross-process call was the main lag source, especially in Flutter apps) — one-shot Shift also now applies at input time so the first letter is always capitalized

### Refactored
- Emoji panel grid layout and sizing
- Emoji usage tracking logic optimized
- Info Box and speed meter theme configurations decoupled

---

## [1.4.0] — August 2, 2026

### Added
- Full internationalization — all hardcoded strings extracted to resources; complete translations for 14 languages: Bengali, Hindi, Arabic, Spanish, French, German, Portuguese, Russian, Japanese, Korean, Chinese (Simplified & Traditional), Urdu, Persian
- HSV color picker dialog to the theme creator (independent hue/saturation/value sliders, correct hue gradient, custom button first)
- Unified header with auto-show/auto-hide and animated transitions
- Info Box customization — swipe info (In/words/keys/speed/records) with selectable frames, swipe text color, and configurable swipe timeout
- Info Box custom texts — add/delete text entries with Off/Timed/Always modes, show duration, and an independent custom text color with live preview

### Fixed
- Black screen when switching between apps — removed blocking DataStore read from `onCreate`; themed window background
- Language switch requiring a second click — locale applied from a single source of truth (DataStore)
- Display flashing off/on during language switch — locale context wrapping (`createConfigurationContext` + composition locals) replaces activity recreation; strings and RTL update instantly in place, navigation state preserved
- Stored-label i18n bug — `popupDismissDelay` persists stable keys instead of Android resource IDs
- Voice error message formatting (`%1$s` → `%1$d`) and added missing Arabic `zero` plural quantity
- Custom texts never appearing — configured texts now display immediately (Always loops, Timed replays once) instead of waiting behind the swipe-info lines
- Custom texts not being added — the input is captured before the field clears, so the text at tap time is persisted correctly
- Blank entries showing as delete-only rows — blank strings are filtered on load and legacy entries render a visible placeholder
- Swipe text color not applying — color swatches now use a correct hex conversion

---

## [1.3.0]

### Added
- Custom Theme Engine — customize background, primary keys, key text, special keys, accent color, suggestion strip, key popup, and sub-character hints
- Live keyboard preview pinned at top of the custom theme screen while pickers scroll below

### Fixed
- Cursor jumping to end when moving mid-word (composing buffer commits before cursor movement)
- Content hidden behind the floating navigation bar — standardized 120dp bottom clearance on all screens

### Refactored
- IME service into per-feature modules: `TextInputHandler`, `TextDeletion`, `SuggestionHandler`, `ImePreferenceCollector`, and per-language mode files
- Monolith settings file into one screen per file under `ui/settings/`

---

## [1.2.0]

### Added
- Emoji search with 400+ keyword mappings across all categories
- Horizontal scrollable search results (configurable 1–2 rows)
- Compact keyboard during emoji search (65% height)
- Blinking cursor indicator in emoji search bar
- Recent emoji retention with configurable expiry (1/7/30/90 days, forever)
- Emoji settings screen (retention, search layout, visible rows)
- GIF/Sticker tabs in emoji panel (placeholder for future support)
- Delete button in emoji category label row
- Customizable speed meter fonts (DIGITAL, LCD, SEGMENT, MODERN)
- Digital speed meter for real-time typing metrics

### Fixed
- Popup candidates expanding keyboard to full screen
- Duplicate close/clear buttons in emoji search bar
- Keyboard staying in compact mode after switching modes via toolbar
- Keyboard mode not restoring after closing emoji search
- Cursor position lost when moving cursor mid-word (space bar swipe / volume keys now commit composing text first)

### Changed
- Removed offensive word filtering (was deprecated)
- Enhanced keyboard mode switching with `lastTextMode` tracking
- Gboard/Ridmik-style Bangla Jatiyo and Avro keyboard arrangements

### CI
- Removed instrumentation test job (no KVM/HVF on runners)
- Switched to macOS runner, fixed ARM64 emulator config

---

## [1.1.0]

### Added
- Avro phonetic engine (`AvroPhoneticEngine`) for Avro-style transliteration
- Dedicated AvroRow layouts in BanglaLayout
- Enhanced EnglishLayout and BanglaLayout with expanded popup candidates
- Updated KeyboardComposeView to support Avro mode UI and punctuation logic

### Fixed
- CI emulator: removed instrumentation test job (GitHub Actions lacks KVM/HVF for hardware-accelerated emulation)

### Changed
- Removed custom debug signing config for CI compatibility (CI uses Android's auto-generated debug keystore)
- Added `MissingGoogleServicesStrategy.WARN` so builds succeed without `google-services.json` in CI

---

## [1.0.0]

### IME Lifecycle for Compose
- `ViewTreeLifecycleOwner`, `ViewModelStoreOwner`, and `SavedStateRegistryOwner` are now set on **both** the input view and the window's DecorView, fixing Compose-in-IME crashes.
- Lifecycle dispatch moved from `onWindowShown()`/`onWindowHidden()` to `onStartInputView()`/`onFinishInputView()` for correct timing.
- `onEvaluateInputViewShown()` overridden to return `true`, ensuring the keyboard appears on emulators and devices with hardware keyboards.

### Setup Wizard Detection
- Step 2 now uses `InputMethodManager.showInputMethodPicker()` instead of opening settings again.
- Continuous polling (every 500ms) detects state changes that lifecycle events miss.
- `checkIsKeyboardSelected()` uses `InputMethodManager.getCurrentInputMethodInfo()` on API 34+, falls back to `Settings.Secure.DEFAULT_INPUT_METHOD` on older versions.

### App Theme Across All Screens
- All screens now use `MaterialTheme.colorScheme.*` instead of hardcoded `Color.White`.
- App theme (System/Light/Dark) setting in `AppSettingsScreen` persists via DataStore and updates every screen in real time.
- `key(appTheme)` forces recomposition when theme changes, ensuring NavHost destinations reflect the new theme.
