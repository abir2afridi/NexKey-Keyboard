# NexKey Keyboard v1.3.1 - Release Notes

## Overview

NexKey Keyboard v1.3.1 is a patch release that fixes three editing bugs that made correcting text painful: cursor jumps when editing mid-word, emoji backspace leaving a "?" behind, and the delete key ignoring selected text.

## Highlights

- **Edit Mid-Word Without Jumping** — Move the cursor anywhere inside a composed word (e.g. tap between the letters of "aple" to type "apple") and new keys insert exactly where the cursor is — no more being thrown to the end of the sentence
- **Emoji Deletes Cleanly** — Backspace removes the entire emoji in one press: surrogate-pair emojis, family/flag ZWJ sequences, variation selectors (❤️), and combined marks (é) — no leftover "?" half-characters
- **Delete Respects Selection** — Select any text (or select-all) and press delete: the whole selection is removed at once

## What's Changed

- Composing-region handling rewritten: composing text is committed whenever the cursor moves off the end of the composing region
- Grapheme-aware backspace deletion with selection detection
- Input method logic restructured into dedicated handler files (text input, deletion, suggestions, preferences) and settings split into per-screen files

## Files

- `NexKey-Keyboard-v1.3.1.apk` — installable debug build
- `NexKey-Keyboard-v1.3.1.apk.sha256` / `.md5` — integrity checksums

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.3.1.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.
