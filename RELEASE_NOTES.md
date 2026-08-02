# NexKey Keyboard v1.4.0 - Release Notes

## Overview

NexKey Keyboard v1.4.0 is a minor release that brings the app to 15 languages, a professional HSV color picker for themes, and fixes two long-standing UI bugs: the black screen flash when switching apps, and the language switcher that needed a second tap (and flashed the display when it worked).

## Highlights

- **Now in 15 languages** — Full translations for English, Bengali, Hindi, Arabic, Spanish, French, German, Portuguese, Russian, Japanese, Korean, Chinese (Simplified & Traditional), Urdu, and Persian, switchable from App Settings → Language
- **Instant, flicker-free language switching** — switching language now updates every screen in place with zero activity recreation, zero display flash, and no black screen — even RTL (Arabic/Urdu/Persian) mirrors instantly and your place in the app is preserved
- **No more black screen when switching apps** — app resumes instantly, no blocking disk reads, no black flash
- **HSV color picker** — theme creator now has independent Hue/Saturation/Value sliders with a correct hue gradient
- **Animated header** — auto-show/auto-hide header with smooth transitions

## What's Changed

- All hardcoded strings extracted to resources; 14 locale folders (425 keys each, zero missing)
- Language switching rewritten: composition-local locale context replaces `AppCompatDelegate` activity recreation
- `popupDismissDelay` now persists stable keys instead of resource IDs (settings no longer revert after restart)
- Color picker sliders decoupled and fixed

## Files

- `NexKey-Keyboard-v1.4.0.apk` — installable debug build
- `NexKey-Keyboard-v1.4.0.apk.sha256` / `.md5` — integrity checksums

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.4.0.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.
