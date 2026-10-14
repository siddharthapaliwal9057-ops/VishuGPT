# JARVIS Voice Assistant (VishuGPT Edition)

AI Voice Assistant powered by Gemini Live with real-time on-device voice processing, offline speech recognition (Vosk), accessibility automation, and custom personas.

Created & Developed by **VISHAL** (@vishaljadam18)

---

## Features
- **Live Gemini Voice Streaming**: Ultra-fast voice conversational model over WebSocket.
- **Custom Personas**:
  - **Jarvis Pro**: Intelligent, witty assistant.
  - **Pari**: Sweet and caring mode.
  - **Kabir**: Confident and supportive mode.
  - **VishuGPT Mode**: High-intellect neural engineering mode.
  - **+ Custom Assistant**: Set any custom name of your choice, which always speaks in Pari's voice!
- **Offline Wake Word & Standby**: Offline Vosk speech recognition.
- **Hands-Free Automation**: Automate screen actions, YouTube search/playback, WhatsApp messaging, and device controls.
- **Room Database Memory**: Saves preferences, routines, personal details, and notes locally.

---

## How to Build the APK

### Option 1: Automatic Build via GitHub Actions
This repository includes a GitHub Actions workflow (`.github/workflows/build-apk.yml`).
1. Push this repository to your GitHub account.
2. Go to the **Actions** tab on your GitHub repository.
3. Click on the **Build Android APK** workflow and run it (or it runs automatically on push).
4. Download the compiled `jarvis-debug-apk` from the workflow summary!

### Option 2: Build Locally with Gradle
Requirements:
- JDK 17
- Android SDK (API 34 or 36)

Run in terminal:
```bash
# Make gradlew executable (Linux/macOS)
chmod +x gradlew

# Build the Debug APK
./gradlew assembleDebug
```
The output APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## API Key Configuration
1. Open `.env.example` or create a `.env` file in the root directory:
   ```
   GEMINI_API_KEY=YOUR_GEMINI_API_KEY_HERE
   ```
2. Or in the app: Go to **Settings > Gemini API Key** and enter your API key directly in encrypted storage.

---

## Community & Socials
- **Telegram**: [@vishaljadam](https://t.me/vishaljadam)
- **Instagram**: [@vishaljadam18](https://www.instagram.com/vishaljadam18)
- **YouTube**: [@vishaljadam18](https://youtube.com/@vishaljadam18)
