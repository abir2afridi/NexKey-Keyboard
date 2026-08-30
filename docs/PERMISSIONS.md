# NexKey Permission Rationale

NexKey adheres strictly to privacy-first design principles. Permissions are requested only when required for user-initiated actions:

| Permission | Purpose | Opt-In / Runtime Request |
| --- | --- | --- |
| `RECORD_AUDIO` | System Voice Typing (speech-to-text recognition trigger) | Requested only when tapping the Microphone toolbar icon |
| `VIBRATE` | Tactile haptic feedback on key presses | Granted automatically at install; toggleable in Settings |
| `POST_NOTIFICATIONS` | Download progress notifications for optional language packs | Requested on Android 13+ when downloading packs |
| `INTERNET` | Optional cloud AI helper & optional GIF search | Off by default; requires explicit user opt-in |
