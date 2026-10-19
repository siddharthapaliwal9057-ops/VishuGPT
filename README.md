# JARVIS Voice Assistant (VishuGPT Edition)

AI Voice Assistant powered by Gemini Live with real-time on-device voice processing, offline speech recognition (Vosk), accessibility automation, and custom personas.

Created & Developed by **VISHAL** (@vishaljadam18)

---

## 🚀 GitHub par Upload aur APK Build karne ka Tarika (Step-by-Step)

Is project me **GitHub Actions Workflow** (`.github/workflows/build-apk.yml`) pehle se configured hai. Jab aap is repository ko GitHub par upload karenge, toh GitHub automatically bina kisi computer/Android Studio ke cloud par **APK compile** karke download ke liye de dega!

### Step 1: GitHub par New Repository banayein
1. [GitHub](https://github.com) par login karein.
2. Top-right me **+** icon par click karke **New repository** select karein.
3. Repository name rakhein (jaise: `jarvis-assistant`).
4. Repository ko **Public** ya **Private** select karein aur **Create repository** par click karein.

---

### Step 2: Code ko GitHub par Push karein
Apne computer / terminal me is project ke folder ke andar ye commands run karein:

```bash
# 1. Git repository initialize karein (agar pehle se nahi hai)
git init

# 2. Saari files stage karein
git add .

# 3. Initial commit karein
git commit -m "Initial commit - JARVIS Assistant with Auto APK Build"

# 4. Main branch set karein
git branch -M main

# 5. Apni GitHub repository ka link add karein (apna username aur repo name dalein)
git remote add origin https://github.com/<AAPKA_GITHUB_USERNAME>/<REPO_NAME>.git

# 6. Code ko GitHub par upload karein
git push -u origin main
```

---

### Step 3: APK Automatic Build & Download
1. Code push hone ke baad GitHub par apni repository open karein.
2. Upar **Actions** tab par click karein.
3. Aapko **Build Android APK** workflow automatically chalta hua dikhega (🟡 in progress).
4. Build complete hone par green checkmark (🟢) aayega (approx 3-5 minutes).
5. Us workflow run par click karein.
6. Niche **Artifacts** section me **`JARVIS-Debug-APK`** milega.
7. Us par click karte hi **`app-debug.apk`** download ho jayegi! Aap ise kisi bhi Android phone me install kar sakte hain.

---

### (Optional) Step 4: GitHub Secrets me Gemini API Key add karna
Agar aap chahte hain ki APK me aapki API key build time par hi inject ho jaye:
1. GitHub Repo me **Settings** -> **Secrets and variables** -> **Actions** par jayein.
2. **New repository secret** par click karein.
3. Name: `GEMINI_API_KEY`
4. Value: Apni Google Gemini API key paste karein.
*(Note: Aap APK install karne ke baad directly App ke andar **Settings > Gemini API Key** me bhi apni key enter kar sakte hain!)*

---

## 🛠 Local Computer par Gradle se Build karna
Agar aap apne laptop/PC par compile karna chahte hain:
```bash
# Linux / macOS:
chmod +x gradlew
./gradlew assembleDebug

# Windows (Command Prompt / PowerShell):
gradlew.bat assembleDebug
```
Output APK yahan milegi:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 Core Features
- **Live Gemini Voice Streaming**: Ultra-fast voice conversational model over WebSocket.
- **Hands-Free Automation**: Real-time phone physical commands, hands-free call control (auto pickup & cut), screen control, and typing.
- **ChatGPT Image Prompting**: In-app prompt automation on ChatGPT and creation platforms.
- **Custom Personas**: Jarvis Pro, Pari, Kabir, VishuGPT, aur Custom Name.
- **Offline Wake Word**: Offline Vosk speech recognition.

---

## 🌐 Community & Socials
- **Telegram**: [@vishaljadam](https://t.me/vishaljadam)
- **Instagram**: [@vishaljadam18](https://www.instagram.com/vishaljadam18)
- **YouTube**: [@vishaljadam18](https://youtube.com/@vishaljadam18)
