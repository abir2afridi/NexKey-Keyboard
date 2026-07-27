# NexKey Keyboard

**NexKey** is an original, production-ready, multilingual Android keyboard (IME) built from scratch — targeting Ridmik-class Bangla typing quality with Gboard/SwiftKey-class UX for English and unlimited other languages.

Built as a real **Android InputMethodService** (not a web demo), compiled via Gradle into an installable APK/AAB that registers as a system keyboard.

---

## 🎯 Project Vision

| Goal | Target |
| :--- | :--- |
| **Bangla typing** | Match/exceed Ridmik phonetic + traditional + national layouts |
| **English & global** | Gboard/SwiftKey-class prediction, autocorrect, gesture typing |
| **Languages** | Unlimited via pluggable language packs (JSON/SQLite packs, Play Feature Delivery) |
| **Privacy** | Zero required network, local-only learning, encrypted storage, incognito mode |
| **Platform** | Min SDK 26 (Android 8), target latest stable, foldable/stylus/physical keyboard ready |

---

## 🏗 Architecture — Multi-Module Gradle Project

```text
nexkey/
├── app/                      # App shell, IME service registration, onboarding
├── core/
│   ├── common/               # Shared utils, Result, extensions
│   ├── designsystem/         # Compose M3 theme, tokens, dynamic color
│   ├── data/                 # Room, DataStore, repositories
│   ├── model/                # Pure Kotlin data models
│   └── analytics/            # Local-only event logging
├── feature/
│   ├── ime-core/             # InputMethodService, InputConnection handling
│   ├── keyboard-layout/      # Layout DSL, renderer, key hit-testing
│   ├── prediction-engine/    # DAWG dict, n-gram ranking, personal dict
│   ├── language-bangla/      # Bangla phonetic/traditional/national engines
│   ├── language-packs/       # Pluggable language pack loader
│   ├── gesture-typing/       # Glide/swipe decoding (DTW)
│   ├── clipboard/            # History, rich content, pinning, TTL
│   ├── emoji/                # Picker, search, combos, kaomoji, GIF provider
│   ├── themes/               # Theme engine, presets, live preview
│   ├── settings/             # Searchable settings, backup/restore, dev mode
│   ├── ai-assist/            # Optional on-device/cloud AI (opt-in only)
│   └── onboarding/           # Permission flow, IME enable wizard
├── build-logic/              # Convention plugins (build-logic/convention)
├── docs/                     # Architecture docs, data sources, perf reports
└── .github/workflows/ci.yml  # CI: lint → unit → instrumented → assembleRelease
```

**Key tech stack:** Kotlin, Jetpack Compose (hosted in `InputMethodService`), Hilt, Room, DataStore, Coroutines/Flow, Kotlinx Serialization, WorkManager, Jetpack WindowManager, Jetpack Security (EncryptedFile/MasterKey).

---

## 🚀 Phased Delivery (10 Phases)

| Phase | Focus | Gate |
| :--- | :--- | :--- |
| **0** | Project foundation, Gradle multi-module, CI, minimal working IME | `./gradlew build` + installs as selectable keyboard |
| **1** | IME Core: lifecycle, composing, IME actions, voice, hardware KB, foldables | Types in EditText/password/search across rotation/split/fold/BT keyboard |
| **2** | Layout Engine + Compose UI: data-driven layouts, all modes, RTL, handwriting stub | 60fps typing, all densities/themes |
| **3** | **Bangla Engine** (flagship): phonetic, conjuncts, NFC normalization, Banglish detect | 30 golden Bangla test phrases byte-exact |
| **3.5** | Universal typing intelligence: auto-cap, smart punctuation, undo/redo, per-lang rules | Scripted UI tests pass for EN/BN |
| **4** | Prediction, Autocorrect, Gesture Typing (DAWG, n-gram, DTW glide) | <16ms prediction, 90% glide accuracy on 50-word test |
| **5** | Clipboard, Emoji/Combos/GIF, Smart Toolbar, Language Pack Manager | No dead toolbar icons, clipboard persists, emoji search <50ms |
| **6** | Themes & Customization: engine, presets, live preview, drag-resize handle | Live preview, no restart |
| **7** | AI Assist (opt-in): on-device/cloud provider interface, rewrite/translate/tone | Zero network calls when OFF |
| **8** | Privacy, Security, Accessibility: incognito, encryption, TalkBack, contrast, switch access | Accessibility Scanner: 0 critical |
| **9** | Settings, Onboarding, Performance, Testing, Release (AAB, Play Core updates) | Macrobenchmark numbers, Play-ready AAB |
| **10** | App Shell Polish: app localization, in-app updates, crash watchdog, monetization hook | Bangla/Arabic/EN app UI, crash watchdog local-only |

---

## 🛡 Legal & Originality

- **Zero copied code/assets** from Ridmik, Gboard, SwiftKey, Samsung Keyboard, etc.
- Bangla rules built from **Unicode Bengali block + public phoneme tables** (cited in `docs/DATA_SOURCES.md`)
- Dictionaries from **open corpora only** (Wikipedia, Common Crawl, OpenSubtitles) — cited in `docs/DATA_SOURCES.md`
- All deps: **Apache-2.0 / MIT / BSD only** — listed in `docs/THIRD_PARTY_LICENSES.md`

---

## 🔧 Building

```bash
# Requires: JDK 21, Android SDK (API 34+), Android Studio Koala+
./gradlew assembleDebug       # Debug APK
./gradlew assembleRelease     # Release AAB (requires signing config)
./gradlew lint                # Lint
./gradlew test                # Unit tests
./gradlew connectedAndroidTest # Instrumented tests (emulator required)
```

Install debug APK:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Then enable in **Settings → Languages → On-screen keyboard → NexKey**.

---

## 📚 Documentation

| Doc | Purpose |
| :--- | :--- |
| `docs/plan.md` | Full 10-phase spec (this project's source of truth) |
| `docs/DATA_SOURCES.md` | Corpus sources & licenses per language |
| `docs/THIRD_PARTY_LICENSES.md` | All third-party dependency licenses |
| `docs/DICTIONARY_FORMAT.md` | DAWG binary format for language packs |
| `docs/HANDWRITING_ARCHITECTURE.md` | Handwriting provider interface contract |
| `docs/PERF_REPORT.md` | Macrobenchmark results |
| `docs/ACCESSIBILITY_CHECKLIST.md` | TalkBack/switch-access verification |
| `docs/QA_APP_MATRIX.md` | 20-app manual test matrix |
| `docs/RELEASE_PROCESS.md` | Staged rollout, rollback, signing |
| `docs/PERMISSIONS.md` | Permission justification table |

---

## 🔐 Privacy & Security

- **Permissions requested:** Only `RECORD_AUDIO` (voice typing) + `INTERNET` (optional AI/cloud sync, both opt-in)
- **Encrypted storage:** Room + DataStore encrypted via Jetpack Security (`MasterKey`)
- **Incognito mode:** Zero learning, zero clipboard, zero analytics, visible indicator
- **Password fields:** Auto-disable learning, clipboard, predictions
- **OTP fields:** Auto-purge from clipboard after 2 min
- **Analytics:** Local-only, opt-in, no PII, exportable/deletable

---

## ♿ Accessibility

- TalkBack labels on every key/toolbar icon (dynamic state-aware)
- System font scale up to 200%+
- High-contrast & color-blind-safe palettes (deuteranopia/protanopia/tritanopia tested)
- Full hardware keyboard / switch access support
- Verified via Accessibility Scanner (0 critical issues gate)

---

## 📦 Release & Distribution

- **AAB** via GitHub Actions → Play Console (internal/closed/open tracks)
- **Dynamic Feature Modules** for optional language packs (base APK stays small)
- **In-App Updates** (Play Core) for critical fixes
- **SemVer** + auto-changelog from conventional commits

---

## 🧪 Testing Strategy

| Layer | Tools | Targets |
| :--- | :--- | :--- |
| Unit | JUnit5, Turbine, MockK | Prediction ranking, Bangla transliteration goldens, autocorrect FSM, clipboard TTL |
| Instrumented | Compose Test, Espresso | Keyboard render, key tap → char, language switch, theme switch, a11y tree |
| Benchmark | Macrobenchmark | Cold IME show <150ms, steady RSS <60MB, glide 60fps |
| Manual | 20-app matrix | Browser, SMS, Email, WhatsApp, Telegram, Slack, Keep, Docs, etc. |

---

## 🤝 Contributing

1. Read `docs/plan.md` — this is the spec
2. Pick an open checklist item from the current phase
3. Implement **fully** (no TODOs on in-scope items)
4. Run `./gradlew lint test connectedAndroidTest`
5. PR with checklist gate verification

---

## 📄 License

**Apache-2.0** — see `LICENSE`.

---

## 🙏 Acknowledgments

- Unicode Consortium (Bengali block, CLDR emoji annotations)
- Wikipedia / Common Crawl / OpenSubtitles (open corpora)
- Android Open Source Project (IME framework reference)
- Jetpack Compose, Room, Hilt, WindowManager teams

---

> **NexKey** — Built from first principles, for real users, on real devices. No web tech. No shortcuts.
