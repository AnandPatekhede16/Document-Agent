# Document Agent 🤖📄
> **Document Requirement Bot & Educational Document Assistant for Android**  
> *Developed by Anand Patekhede (MIT Academy of Engineering)*  
> **Package ID:** `com.mitaoe.anandpatekhede.documentBot`  
> **Repository:** [https://github.com/AnandPatekhede16/Document-Agent.git](https://github.com/AnandPatekhede16/Document-Agent.git)

---

## 📌 Overview

**Document Agent** is an Android application built with **Kotlin** and **Jetpack Compose (Material 3)**. It is designed to assist citizens, college students, and first-time applicants by answering questions about which documents, eligibility criteria, and procedures are required before applying for **government public services** and **higher education admissions**.

### Problems It Solves:
1. **Uncertainty & Rejections:** Citizens and students often make repeated trips to government offices or college administration departments due to missing, uncertified, or incorrect documents.
2. **Confusing Proof Requirements:** Applicants struggle to know what counts as valid address proof, identity proof, or date-of-birth proof.
3. **Complex Educational Paperwork:** Students preparing for Engineering, Medical, MBA, or Master's admissions frequently face hurdles with Caste Validity, Gap Affidavits, Bonafide Certificates, Non-Creamy Layer (NCL), and Migration certificates.

Document Agent eliminates these hurdles with an interactive document readiness tracker, detailed statutory guidelines, rejection prevention checklists, and a **Google Gemini AI-powered Document Bot**.

---

## 🚀 Key Features

### 1. 🤖 Live Gemini AI Document Assistant
* Powered by Google's **Gemini AI** (`gemini-3.5-flash` / `gemini-3.8-flash` / `gemini-flash-latest`).
* **Dynamic Reasoning:** Generates tailored, in-depth responses to any specific question regarding educational documents, admission guidelines, legal affidavits, or government certificates.
* **Dual-Mode Hybrid Architecture:** Seamlessly falls back to a comprehensive local offline engine if the device has no internet access.

### 2. 🎓 Comprehensive Educational Documents Support
Detailed guidance, preparation steps, and accepted formats for:
* **Bonafide Certificate:** Application procedure, required fee receipts, uses for scholarships, travel concessions, student accounts, and education loans.
* **Transfer Certificate (TC) / School Leaving Certificate (LC):** Institutional clearance / No-Dues procedures, duplicate bond requirements.
* **Migration Certificate:** University/Board migration vs. TC, digital DigiLocker verification.
* **Caste Validity Certificate:** Mandatory requirements for professional course admissions (Engineering, Medical, MBA), Scrutiny Committee rules, and ancestral lineage proofs (pre-1950/1967 records).
* **Gap Certificate / Affidavit:** Format and procedure for notarized ₹100 stamp paper affidavits explaining drop years (JEE, NEET, UPSC).
* **Non-Creamy Layer (NCL) & EWS Certificates:** 3-year gross income criteria (< ₹8 Lakhs), family asset limits, and application workflows.
* **College & University Admissions Master Checklist:** 10th/12th marksheets, seat allotment letters, domicile, and category documents.
* **Education Loans:** Bank requirements for students and co-borrower parents (ITR, salary slips, admission letter, collateral).

### 3. 🏛️ 12 Pre-Populated Government & Public Services
Complete checklists, eligibility criteria, step-by-step application procedures, statutory fees, and timelines for:
1. **Aadhaar Services** (Enrolment, Address Update, Mobile Linking)
2. **PAN Card** (New Form 49A, Corrections)
3. **Indian Passport** (Fresh Normal & Tatkaal)
4. **Driving Licence** (Learner's Licence & Permanent DL)
5. **Voter ID (EPIC)** (Form 6 New Registration)
6. **Income Certificate** (Tahsildar / Revenue Department)
7. **Caste Certificate** (SDO / Tehsildar verification)
8. **Domicile & Nationality Certificate** (Proof of state residence)
9. **Birth Certificate** (CRS Portal & Municipal Corporation)
10. **Death Certificate** (Registration of Births & Deaths Act)
11. **Ration Card** (NFSA Priority / Antyodaya / White)
12. **Scholarship Application** (NSP, MahaDBT, State Portals)

### 4. 📋 Interactive Document Readiness Tracker
* Check off documents as you gather them.
* Live progress indicator (*"3 of 5 Documents Ready (60%)"*).
* State is persisted locally via **Jetpack DataStore**.

### 5. ⚠️ Rejection Prevention Checklist ("Avoid Common Rejections")
* Detailed warnings for verification pitfalls (e.g., utility bills older than 90 days, spelling discrepancies between 10th marksheet and Aadhaar, missing notary seals).

### 6. 🌐 Desktop Web & Mobile Accessibility
* Built-in mouse wheel and trackpad scroll listener (`webMouseWheelScroll`).
* Floating on-screen `↓` Down and `↑` Up quick-scroll buttons.
* Quick Jump Page Switcher (Instant jump between services 1–6 and 7–12).

---

## 📱 How to Download & Install the APK on Your Android Phone

If you just want to install the app on your Android device:

### Step 1: Download the APK
- Download **`documentBot-debug.apk`** from this repository (or from the project root directory).

### Step 2: Transfer to Your Phone
- Transfer the downloaded `.apk` file to your Android phone via WhatsApp, Google Drive, email, or a USB cable.

### Step 3: Install
1. Open the file on your Android phone using your **Files** or **Downloads** app.
2. If prompted by Android with *"Install unknown apps"*, tap **Settings** and toggle **Allow from this source**.
3. Tap **Install**.

### Step 4: Open & Use
- Launch **Document Requirement Bot** from your app drawer.
- The app comes pre-configured and works immediately!

---

## 💻 Developer Setup & Running from Source

Follow these steps to set up and build the project in **Android Studio**:

### Prerequisites
* **Android Studio:** Hedgehog (2023.1.1) or newer (Ladybug / Meerkat recommended).
* **JDK:** Java 17 or Java 21 configured.
* **Android SDK:** Platform 34 or 36 installed.

### Step-by-Step Instructions

#### 1. Clone the Repository
```bash
git clone https://github.com/AnandPatekhede16/Document-Agent.git
cd Document-Agent
```

#### 2. Open in Android Studio
1. Open Android Studio.
2. Select **File > Open...** and choose the `Document-Agent` directory.
3. Allow Gradle to sync dependencies automatically.

#### 3. Configure Environment Variables (Optional)
Create a `.env` file in the project root:
```properties
GEMINI_API_KEY=YOUR_GEMINI_API_KEY_HERE
```
*(If left empty, the app will continue to work seamlessly using its rich offline knowledge base).*

#### 4. Build and Run
* **Run on Emulator / Connected Device:** Click the green **Run ▶** button (`Shift + F10`) or press `Ctrl + R`.
* **Build Debug APK via Command Line:**
  ```bash
  gradle assembleDebug
  ```
  The resulting APK will be generated at:
  `app/build/outputs/apk/debug/app-debug.apk`

#### 5. Run JVM Unit & Robolectric Tests
```bash
gradle :app:testDebugUnitTest
```

---

## 📂 Project Structure

```
Document-Agent/
├── .env.example                          # Template for secrets & API keys
├── build.gradle.kts                      # Root Gradle configuration
├── settings.gradle.kts                   # Project settings & repositories
├── documentBot-debug.apk                 # Pre-built installable Android APK
│
├── app/
│   ├── build.gradle.kts                  # App-level dependencies & plugins
│   ├── src/main/
│   │   ├── AndroidManifest.xml           # App manifest & permissions
│   │   │
│   │   ├── java/com/example/             # Kotlin source code
│   │   │   ├── MainActivity.kt           # Edge-to-edge root activity
│   │   │   │
│   │   │   ├── bot/
│   │   │   │   ├── GeminiDocumentService.kt # Live Gemini AI REST client (gemini-3.5-flash)
│   │   │   │   └── DocumentBotEngine.kt     # Offline educational & public document rule engine
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── local/UserPreferencesRepository.kt # DataStore for bookmarks & checklist states
│   │   │   │   ├── model/ServiceModels.kt             # Data classes for services, docs, FAQs
│   │   │   │   ├── model/ChatMessage.kt              # ChatMessage model & AI status
│   │   │   │   └── repository/ServiceRepository.kt    # Catalog of 12 public services & search
│   │   │   │
│   │   │   ├── navigation/
│   │   │   │   ├── Screen.kt             # Sealed navigation destinations
│   │   │   │   └── DocBotNavGraph.kt     # Jetpack Navigation Compose graph
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── components/           # Reusable UI cards, bars, scroll listener
│   │   │   │   ├── screens/
│   │   │   │   │   ├── home/HomeScreen.kt
│   │   │   │   │   ├── details/ServiceDetailScreen.kt
│   │   │   │   │   ├── bot/ChatbotScreen.kt
│   │   │   │   │   ├── search/SearchScreen.kt
│   │   │   │   │   ├── saved/SavedScreen.kt
│   │   │   │   │   ├── wizard/EligibilityWizardScreen.kt
│   │   │   │   │   ├── about/AboutScreen.kt
│   │   │   │   │   └── splash/SplashScreen.kt
│   │   │   │   └── theme/                # Material 3 Color, Theme, Typography
│   │   │   │
│   │   │   └── viewmodel/
│   │   │       └── DocBotViewModel.kt    # Unified ViewModel managing all app state
│   │   │
│   │   └── res/                          # Vector drawables, launcher icons, strings
```

---

## 🛠️ Tech Stack & Libraries

| Technology | Purpose |
|---|---|
| **Kotlin 2.x** | Modern, concise, type-safe programming language |
| **Jetpack Compose** | Declarative UI toolkit for Android |
| **Material Design 3 (M3)** | Dynamic colors, accessible typography, elevation |
| **Jetpack Navigation** | Type-safe screen navigation and backstack management |
| **Android Jetpack DataStore** | Asynchronous key-value persistence for bookmarks & checklists |
| **Google Gemini API** | Live AI reasoning on document questions (`gemini-3.5-flash`) |
| **OkHttp 4.x** | Efficient asynchronous HTTP client with timeout handling |
| **Kotlin Coroutines & Flow** | Reactive state management (`StateFlow`, `collectAsState`) |
| **Robolectric & JUnit 4** | Fast local JVM unit testing without emulator |

---

## 👨‍💻 Author & Credits

* **Developer:** Anand Patekhede
* **Institution:** MIT Academy of Engineering (MIT AOE)
* **Email:** 202401040261@mitaoe.ac.in
* **GitHub:** [@AnandPatekhede16](https://github.com/AnandPatekhede16)
* **Repository:** [https://github.com/AnandPatekhede16/Document-Agent.git](https://github.com/AnandPatekhede16/Document-Agent.git)

---

## 📜 Disclaimer
*Document Agent is an educational citizen guidance application. While requirements and fees are curated to match official government and university guidelines, applicants should always verify final documentation requirements on official portals before making statutory payments or attending physical appointments.*
