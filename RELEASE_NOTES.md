# NexKey Keyboard v1.5.0 - Release Notes

## Overview

NexKey Keyboard v1.5.0 is a minor release centered on what you see while you type: a keyboard you can resize to fit your hands, a leaderboard for your most-used emojis, live app-wide theming, and a richer Info Box and Speed Meter. It also fixes the key-popup that could show in the wrong place.

## Highlights

- **Resize your keyboard** — from Layout settings, open "Enable keyboard resizing" and fine-tune height and width for portrait and landscape on a new Resize screen with a live keyboard preview and instant reset
- **Emoji Leaderboard & All Emojis** — track and rank your most-used emojis, and browse/search the full emoji set with its own screen
- **Theme everywhere** — switch System / Light / Dark and every screen updates live
- **Rich Info Box & Speed Meter** — custom text color, font selection, extra frame styles, display modes (meter/count), record tracking, and dedicated settings screens for each
- **Correct key popup** — the letter preview now pops up over the exact key you tapped and dismisses quickly
- **Every key responds to your touch** — letter, space, enter, and all other keys now have a press-and-bounce animation, just like the delete key
- **Faster sentence clearing** — hold backspace and it switches to word-by-word deletion after the initial delay, just like Gboard
- **Cleaner Settings** — app settings moved from the bottom bar to top actions

## What's Changed

- New `KeyboardResizeScreen` with live `KeyboardComposeView` preview and portrait/landscape height & width sliders
- Emoji usage tracking and a multi-tab leaderboard; full emoji browsing screen with localization
- Speed meter settings consolidated; bundled DSEG fonts wired up so the meter styles actually render
- Info Box color/font/frame settings decoupled from the speed meter theme config
- Tap-popup anchored to the tapped key with faster auto-dismiss
- Spacebar long-press cycles through enabled languages (English → Bangla → Avro → Arabic)

## Files

- `NexKey-Keyboard-v1.5.0.apk` — installable debug build
- `NexKey-Keyboard-v1.5.0.apk.sha256` / `.md5` — integrity checksums

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.5.0.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.