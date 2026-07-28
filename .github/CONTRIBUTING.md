# 🤝 Contributing to NexKey Keyboard

Thank you for your interest in contributing to **NexKey Keyboard** — an original multilingual Android keyboard with Ridmik-class Bangla phonetic typing.

## 📖 Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Branching Strategy](#branching-strategy)
- [Commit Convention](#commit-convention)
- [Code Style](#code-style)
- [Testing](#testing)
- [Pull Request Process](#pull-request-process)
- [Reporting Issues](#reporting-issues)
- [Community](#community)

## 🤝 Code of Conduct

This project follows the [Contributor Covenant v2.1](CODE_OF_CONDUCT.md). By participating, you agree to uphold this code.

## 📚 Prerequisites

- 📱 **Android Studio** (Ladybug or later recommended)
- ☕ **JDK 21+** (Temurin recommended)
- 🤖 **Android SDK** with platform 36 (compileSdk)
- 📱 **Android Emulator** or physical device running Android 12+ (for instrumentation tests)

## 🚀 Getting Started

```bash
# Clone the repository
git clone https://github.com/abir2afridi/NexKey-Keyboard.git
cd NexKey-Keyboard

# Build debug APK
./gradlew assembleDebug

# Install on device/emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Open the project in Android Studio and let Gradle sync complete. You can run the app directly from the IDE.

## 📋 Development Workflow

1. 🔍 Pick an issue from the issue tracker (or create one first for significant changes).
2. 🔀 Create a feature branch from `main`.
3. ✏️ Make your changes following the code style guidelines.
4. 🧪 Write or update tests as needed.
5. 🔧 Run lint and tests locally.
6. 🔄 Submit a pull request.

## 🔀 Branching Strategy

- 📜 `main` — stable, release-ready branch. All PRs merge here.
- 🔀 Feature branches: `feat/<short-description>` (e.g., `feat/gesture-typing`)
- 🐛 Bug fix branches: `fix/<short-description>` (e.g., `fix/bangla-conjunct-rule`)
- ⛔ No long-lived branches — keep PRs small and focused.

## 📝 Commit Convention

We follow **Conventional Commits**:

```
<type>: <short description>

[optional body]
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `ci`, `security`

Examples:
```
feat: add swipe-typing gesture recognizer
fix: correct kk conjunct rendering in Bangla phonetic engine
docs: update PERMISSIONS.md with new voice typing requirement
test: add unit tests for prediction engine trie
```

## 🎨 Code Style

- 🔵 **Kotlin**: Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- 📄 **XML**: 2-space indentation.
- 🎨 **Compose**: Follow [Jetpack Compose API Guidelines](https://developer.android.com/jetpack/compose/api-guidelines).
- 🔧 Run `./gradlew lint` before committing — it catches style issues, unused imports, and potential bugs.

Key conventions:
- Use `MaterialTheme.colorScheme.*` colors instead of hardcoded values (supports dark mode).
- All interactive elements must have `contentDescription` for TalkBack accessibility.
- Use `remember` / `collectAsState` for state management; avoid mutable state on ViewModels unless necessary.
- Private composable functions prefixed with lowercase (e.g., `fun SettingItem(...)`).

## 🧪 Testing

```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run instrumentation tests (emulator/device required)
./gradlew connectedDebugAndroidTest

# Run lint
./gradlew lint
```

- Unit tests use **Robolectric** (JVM-based Android testing) and **Roborazzi** (screenshot comparison).
- Instrumentation tests use **Espresso** and **Compose UI Test**.
- Add tests for new functionality. At minimum, unit tests for engine logic and data layer changes.

## 🔄 Pull Request Process

1. 🔄 Ensure your branch is up to date with `main`.
2. 🔧 Run `./gradlew lint testDebugUnitTest` — both must pass.
3. 📷 If your PR changes UI, include before/after screenshots.
4. 🔗 Link the issue your PR resolves: `Closes #123`.
5. 🔍 Request review from a maintainer.
6. 💬 Address review feedback. Keep the PR focused — one feature/bug per PR.

## 🐛 Reporting Issues

Use the appropriate issue template:
- [🐛 Bug Report](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=01-bug-report.yml) — for crashes, incorrect behavior
- [✨ Feature Request](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=02-feature-request.yml) — for new ideas
- [📖 Documentation](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=03-documentation.yml) — for docs issues
- [⚡ Performance](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=04-performance.yml) — for lag, memory, battery issues
- [🎨 UI/UX](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=05-ui-ux.yml) — for visual or usability bugs
- [📱 Android-Specific](https://github.com/abir2afridi/NexKey-Keyboard/issues/new?template=09-mobile-bug.yml) — for IME/system integration issues

## 👥 Community

- 📝 **GitHub Issues** — bug reports and feature requests
- 💬 **GitHub Discussions** — questions, ideas, and community help
- 🔒 **Security** — report vulnerabilities via [GitHub Security Advisories](../../security/advisories/new)

Thank you for contributing to NexKey! 🤝
