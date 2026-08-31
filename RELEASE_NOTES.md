# NexKey Keyboard v1.8.0

**Published:** August 31, 2026

## What's New

### Doubled Symbol Access
The `?123` symbol keyboard now shows **4 rows of half-height keys** instead of 2 full-height rows. This doubles the directly accessible symbols — currencies (€, £, ¥, ₹, ৳), math operators (÷, ≠, ≈), and more are now one-tap away. Symbols that still don't fit remain available via long-press.

### Text Editing Toolbar
Quick access to common text editing actions: select all, cut, copy, paste, undo, and redo.

### System Logs & Debug UI
A new 3-tab debug screen for viewing app logs, process information, and clearing log history — useful for troubleshooting on-device.

## Stability Fixes

- **Keyboard auto-close fix** — uncaught exceptions in coroutine handlers no longer kill the entire IME process
- Crash resilience for DataStore, Room database operations, and prediction engine initialization
- Missing Bangla/Arabic translations added
- Keyboard height now stays consistent when switching between ABC and `?123` modes

## Performance

- Zero `runBlocking` on the typing path (3-15ms saved per keystroke)
- Speed meter recompositions reduced from 5 to 1 per keystroke
- Suggestion pipeline debounced at 50ms
- DAWG correction moved off main thread
- Room DB operations moved to IO dispatcher

## Files Changed

- `app/build.gradle.kts` — version bump
- `app/src/main/java/com/example/ui/KeyboardLayouts.kt` — SymbolsRow4 added
- `app/src/main/java/com/example/ui/KeyboardComposeView.kt` — half-height symbol rendering, spacing fix
- `CHANGELOG.md` — v1.8.0 entry
- `README.md` — version badges updated

## APK

Download the latest APK from the [Releases page](https://github.com/abir2afridi/NexKey-Keyboard/releases/tag/v1.8.0).
