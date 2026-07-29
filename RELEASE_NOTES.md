# NexKey Keyboard v1.1.0 - Release Notes

## Overview

NexKey Keyboard v1.1.0 brings major new input capabilities: **Avro phonetic layout**, **long-press backspace continuous delete**, **voice typing**, **always-visible suggestion strip**, **auto-hide toolbar**, and a completely overhauled navigation and analytics system.

## Highlights

- **Avro Phonetic Layout** — Type Bangla in Avro phonetic mode, cycles alongside Phonetic/Jatiyo/Arabic
- **Long-Press Backspace Repeat** — Hold backspace to continuously delete; customize initial delay and repeat speed in Advanced settings
- **Always-Show Suggestion Strip** — Suggestion bar stays visible even when empty (enabled by default)
- **Auto-Hide Toolbar** — Toolbar hides while typing; tap the ⟨⟨ toggle to show it again
- **Key Long-Press Hints** — Long-press top-row keys for numbers, letter keys for accented characters
- **Voice Input** — RECORD_AUDIO permission with smart fallback to App Settings if denied
- **More Languages Screen** — Dedicated screen to toggle English/Phonetic/Jatiyo/Avro/Arabic
- **Enhanced Emoji Panel** — 500+ emojis across 8 categories with tab navigation
- **Enter Key Intelligence** — Multiline fields show ↵, single-line fields show Done/Search/Go

## What's New in v1.1.0

### New Input Modes
- **Avro phonetic layout** for Bangla — shares the phonetic engine, toggled via More Languages screen
- **4-way mode cycling**: Phonetic → Jatiyo → Avro → Arabic

### Suggestion & Toolbar Improvements
- **Always-show suggestions** toggle in Layout settings (ON by default)
- When suggestions are empty, a muted "Suggestions" label keeps the strip visible
- **Auto-hide toolbar** — toolbar disappears while typing; a small ⟨⟨ button lets you show it again

### Backspace Repeat (Advanced Settings)
- **Initial delay**: how long to hold before repeat starts (default: 400ms)
- **Repeat speed**: interval between deletions while holding (default: 50ms)
- Two new sliders in Settings → Advanced

### Key Popups
- English letter keys show long-press candidates:
  - Top row (Q-P): the corresponding number (Q→1, W→2, etc.)
  - Other letters: accented/special variants (A→áàâäã, N→ñ, etc.)

### Voice Typing
- RECORD_AUDIO permission request on first use
- If denied, opens App Settings so the user can grant it manually
- Clear error messages for permission-denied and recognition-failed states

### Analytics & Navigation
- Typing analytics dashboard (wpm, accuracy, key counts)
- Store screen for in-app content
- Overhauled navigation with expanded screen routing
- Settings screen redesigned with grid layout

## Fixes
- Enter key no longer shows "Done"/dismisses keyboard in multiline text fields — now inserts \n and shows ↵
- Backspace no longer triggers auto-language switch outside Phonetic/Avro modes
- CI emulator architecture casing corrected

## Installation

### Sideload the APK

1. Download `app-release.apk` from this release
2. Enable "Unknown sources" in Android settings
3. Tap the downloaded APK to install

### Google Play (Coming Soon)

The app will be available on Google Play Store once published.

## Known Issues

- Voice input may not work on emulators without Google services

## Checksums

| File | SHA256 | MD5 |
|------|--------|-----|
| app-release.apk | TBD | TBD |

## Download Links

- **APK**: [app-release.apk](../releases/download/v1.1.0/app-release.apk)
- **Release URL**: [https://github.com/abir2afridi/NexKey-Keyboard/releases/tag/v1.1.0](../releases/tag/v1.1.0)

## System Requirements

- Android 7.0 (API 24) or higher
- 15+ MB storage space
- Internet connection for voice input (optional)

## Next Steps

- Report bugs via GitHub Issues
- Request features in the feature request template
- Participate in discussions via GitHub Discussions
- Star the repository if you find it useful!
