# NexKey Keyboard v1.6.0 — Release Notes

## Overview

NexKey Keyboard v1.6.0 replaces the old hardcoded prediction seed with a real, data-driven prediction engine. The keyboard now ships with an actual English + Bangla word-frequency dictionary, learns what *you* type, fixes your typos, and predicts your next word — all offline on-device, with privacy gates and live toggles for every feature.

## Highlights

- **Real dictionary, real speed** — a new `:feature:prediction-engine` module packs the dictionary into a minimized DAWG (directed acyclic word graph): ~46,467 English words in ~797 KB, ~45,757 Bangla words in ~546 KB, both loading in tens of milliseconds. The old hardcoded ~100-word seed trie is gone.
- **Typo auto-correction** — `recive`→`receive`, `beleive`→`believe`, `definately`→`definitely`, `enviroment`→`environment`, plus swapped (`hte`), extra (`thhe`), missing (`god`), and repeated (`goodd`) letters. Correctly-typed words are never touched.
- **Learns like you do** — after committing a word a few times it jumps to the front of the strip, and it remembers phrases: after "thank you" it already suggests "so"-style continuations long before you finish typing.
- **Banglish aware** — `vhalo`/`vhalo` style Bangla text typed in Latin is learned independently (vhal→vhalo first, "vhalo lagtase" completes), and the Banglish word variants `korbo` / `korteci` / `korsi` are never confused with each other.
- **Emoji on the strip** — start typing "happy", "love", "fire", or "birthday" and the keyboard's suggestion strip offers the matching emoji alongside word candidates.
- **Autocorrect OFF is honest** — what you type is committed exactly as typed, but the strip still shows the corrected spelling so you can pick it if you want.
- **Settings take effect instantly** — every prediction toggle (autocorrect, typo correction, next-word, personal learning, emoji, incognito) is read live, so flipping a switch works on the very next keystroke.
- **Privacy** — password and sensitive fields (email/URI) never learn; incognito mode blocks all learning; your learned data lives in its own `nexkey_prediction_database` separate from app data.

## What's Changed

- New Gradle module `:feature:prediction-engine` (DAWG lookup, weighted Damerau-Levenshtein correction with QWERTY key-proximity costs, interpolated bigram/trigram backoff, personal trie + Room persistence, Banglish detection, CLDR emoji keywords, LRU cache)
- The IME suggestion strip now runs on the new `PredictionProvider` engine, merging builtin dictionary, personal learning, next-word n-grams, and emoji into one ranked strip
- `com.example.engine.PredictionEngine` removed; Sandbox shares the same engine
- 56 new unit tests covering the scenario spec (learning, Banglish disambiguation, toggle enforcement, privacy, n-gram context, DAWG round-trip, asset load times)

## Files

- `NexKey-Keyboard-v1.6.0.apk` — installable debug build
- `NexKey-Keyboard-v1.6.0.apk.sha256` / `.md5` — integrity checksums

## Installation

Download the APK and enable "Install unknown apps" for your file manager or browser. Or install via ADB:

```
adb install NexKey-Keyboard-v1.6.0.apk
```

Then enable the keyboard in **Settings → System → Languages & input → NexKey Keyboard**.