# NexKey Keyboard v1.7.0 — Release Notes

## Overview

NexKey Keyboard v1.7.0 focuses on typing feel and correctness. The backspace ghost-repeat bug is fixed, the `?123` symbol button no longer accidentally switches languages, the speed meter ticker that was silently re-rendering the entire keyboard 5 times per second is gone, and the suggestion engine no longer blocks the UI thread. A handful of new features round it out: independent language-switch settings, Arabic layout toggle, and toolbar pinning.

## Highlights

- **Backspace ghost repeat fixed** — rapid taps no longer produce phantom deletes after your finger lifts. The repeat job lifecycle is now managed with a remembered state variable and cleanup on composition dispose.
- **`?123` button no longer switches language** — a dedicated `handleSymbolToggle()` bypasses the language-change path, so tapping `?123` toggles to symbols without touching the active language.
- **Typing lag resolved** — the 200ms elapsed-ticker job that recomposed every key in the grid 5×/sec is gone. The speed meter now computes elapsed time locally in Compose. The suggestion engine runs on `Dispatchers.Default` instead of blocking the main thread on every letter.
- **Emoji list stabilized** — `recentEmojis` is now a single reusable `mutableStateListOf` instead of a fresh allocation per recomposition, eliminating a full-keyboard re-render on every state change.
- **Independent language-switch settings** — `showGlobeKey` and `spacebarLanguageSwitch` can be toggled independently in Language Keys settings.
- **Toolbar pinning** — typing keeps the toolbar header visible until a new typing burst begins.
- **Arabic layout toggle** — enable/disable the Arabic keyboard from settings.

## What's Changed

- New `handleSymbolToggle()` in `TextInputHandler.kt` for pure symbol mode switching
- `DisposableEffect` + remembered `backspaceRepeatJob` state in `KeyboardComposeView.kt`
- `updateCandidates()` now launches prediction on `Dispatchers.Default` via coroutine
- `DigitalSpeedMeter` computes `localElapsedSec` via `LaunchedEffect(isLive)` — no external ticker
- Stable `recentEmojisList` on `NexKeyInputMethodService` kept in sync by `ImePreferenceCollector`
- `LanguageKeysSettingsScreen` rewritten with Scaffold, SnackbarHost, and minimum-one enforcement
- `liveElapsedSec` parameter removed from all Compose function signatures

## Files

- `NexKey-Keyboard-v1.7.0.apk` — signed production release
- `NexKey-Keyboard-v1.7.0.apk.sha256` / `.md5` — integrity checksums

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.7.0.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.
