# 🎵 Dhun Music - KMP Player

A lifecycle-aware, cross-platform music player built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. This project demonstrates proficiency in shared logic architecture, robust networking, and modern Material 3 UI design.

---

## ⚙️ How to Run the App

### 📱 Android
1. **Clone** this repository.
2. Open the project in **Android Studio Koala (or newer)**.
3. Ensure your JDK is set to **Java 11** or higher in Gradle settings.
4. Select the `composeApp` run configuration and click **Run**.
5. **Build directly via Terminal (Windows):**
   ```shell
   .\gradlew.bat :composeApp:assembleDebug
   
## 📡 API Choice
**API Used:** iTunes Search API
**Why?:** I selected this API for its high uptime, rich metadata (Artist, Title, Duration), and reliable CDN-hosted artwork. It provides a perfect dataset for Bollywood content, allowing the app to showcase high-resolution thumbnails and accurate 30-second streaming previews.

## 🧠 Assumptions & Technical Decisions
**Process-Level Playback:** The MediaPlayer instance is managed within a companion object in the shared AudioPlayer class. This ensures the media instance persists in the application process memory even when the UI is not in the foreground, providing a stable background listening experience.
**Auto-Next Engine:** Since iTunes previews are approximately 30 seconds long, I implemented a polling loop in a LaunchedEffect. It monitors the MediaPlayer position and automatically triggers the next track when the current preview reaches 29 seconds.
**Orientation Guarding:** To handle configuration changes (screen rotation), I implemented a custom listSaver with rememberSaveable. This serializes the MusicTrack collection into the Android Bundle system, ensuring the user's Sorting Order and current progress are never lost.
**Network Security:** Included android:usesCleartextTraffic="true" in the Manifest to ensure smooth playback and image loading for URLs that may redirect to non-HTTPS endpoints.

## 🌟 Key Features
**🎨 Dynamic Branding:** Support for Light and Dark modes with a custom high-contrast toggle switching between Purple and Golden themes.
**🖼️ Circular Thumbnails:** Modern UI featuring circular artwork posters implemented via Compose .clip(CircleShape) modifiers for a sleek aesthetic.
**📊 Advanced Sorting:** Real-time list reorganization by Name (A-Z) or Duration via a professional Material 3 Dropdown Menu.
**🛠️ Robust Image Loading:** Utilizes SubcomposeAsyncImage (Coil 2.7.0) to handle network images with dedicated error/loading states, preventing common KMP ImageVector crashes.
**⏭️ Sequential Playback:** Fully automated "Auto-Next" logic for a seamless, continuous listening experience.

## 🛠️ Tech Stack
**Language:** Kotlin
**Framework:** Kotlin Multiplatform (KMP) & Compose Multiplatform
**Networking:** Ktor Client with Kotlinx Serialization
**Image Loading:** Coil (Stable 2.7.0)
**Architecture:** MVVM / Clean Separation of Concerns

## 📁 APK File
The ready-to-install APK can be found here: 👉 [Download APK](./Dhun_Submission.apk)