# NexKey Keyboard v1.3.0 - Release Notes

## Overview

NexKey Keyboard v1.3.0 introduces the **Custom Theme Engine** — build your own keyboard look with live preview — plus a project-wide **floating navigation overlap fix** and several polish improvements.

## Highlights

- **Custom Theme Engine** — Customize background, primary keys, key text, special keys, accent color, suggestion strip, key popup, and sub-character hints from a dedicated design screen
- **Live Preview Pinned** — The keyboard preview stays fixed at the top while color pickers scroll below, so you always see your changes
- **No More Hidden Content** — Standardized 120dp bottom clearance on every screen so nothing sits behind the floating bottom navigation bar
- **Cursor Position Fix** — Moving the cursor mid-word no longer jumps it to the end of the text
- **Polished Insets** — Optimized system bar insets and bottom spacing across Home, Settings, Setup, Sandbox, About, Help, Developer, Typing Stats, and Store

## What's Changed

- Custom theme engine with 11 adjustable color groups + "Reset to Default Dark Neon"
- Scroll-safe bottom clearance for all Lazy lists, scrollable columns, and fixed screens
- Cursor movement now commits composing text before repositioning

## Files

- `NexKey-Keyboard-v1.3.0.apk` — installable debug build (23.4 MB)
- `NexKey-Keyboard-v1.3.0.apk.sha256` / `.md5` — integrity checksums

## Checksums (SHA-256)

```
cba81605d022b76a1e3089badea72abe02afee5a3075f967035eba732f2e3cc0  NexKey-Keyboard-v1.3.0.apk
```

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.3.0.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.
