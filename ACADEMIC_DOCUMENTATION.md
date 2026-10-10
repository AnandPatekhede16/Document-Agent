# Academic Project Report & Documentation

**Course:** Mobile Application Development  
**Project Title:** Document Requirement Bot  
**Student Name:** Anand Patekhede  
**Package Name:** `com.mit.AnandPatekhede`  
**Application Platform:** Android (Jetpack Compose, Kotlin)  

---

## 1. Title
**Document Requirement Bot: An Offline-First Intelligent Citizen Assistance Mobile Application for Government and Public Service Document Prerequisites.**

---

## 2. Introduction
In modern governance, digital e-governance initiatives have made government and public services accessible online. However, navigating the prerequisites for each service remains an overwhelming hurdle for the average citizen, particularly students, rural residents, and non-technical users. Every year, millions of citizens make redundant visits to citizen service centers (CSC), Taluk offices, and RTOs due to missing, outdated, or mismatched documentation. The **Document Requirement Bot** addresses this critical information gap by providing a consolidated, offline-first mobile reference guide and conversational AI assistant.

---

## 3. Problem Statement
*“Citizens frequently do not know which specific documents, proofs, and eligibility criteria are legally required when applying for government and public services, resulting in frequent application rejections, loss of time, and reliance on exploitative middlemen.”*

Key challenges identified:
1. **Ambiguity of Terminology:** Users often do not know what constitutes acceptable "Address Proof" or "Proof of Date of Birth".
2. **Scattered Information:** Requirements are buried across disparate state department websites and outdated gazette notifications.
3. **Connectivity Constraints:** Users often need checklist information in remote administrative centers where high-speed mobile internet is unreliable.
4. **Lack of Interactive Guidance:** Static websites lack conversational guidance for specific user queries.

---

## 4. Objectives
1. To develop a lightweight, offline-first Android application using modern declarative UI (**Jetpack Compose**).
2. To compile and maintain verified, structured checklists for 12 essential public services (Aadhaar, PAN, Passport, Driving Licence, Voter ID, Income Certificate, Caste Certificate, Domicile Certificate, Birth Certificate, Death Certificate, Ration Card, Scholarship Application).
3. To design an interactive **Document Detail Modal** explaining the legal purpose, acceptable alternative examples, and common pitfalls for every document.
4. To build a conversational, rule-based **Document Requirement Bot Engine** that interprets user questions and returns immediate guidance offline without external paid API dependencies.
5. To implement persistent bookmarking and recently viewed caches using **Jetpack DataStore**.
6. To adhere strictly to **Material Design 3 (M3)** guidelines for high usability, readability, and accessibility.

---

## 5. Existing System
In the existing ecosystem, citizens rely on:
- Disparate government websites (e.g., UIDAI, Parivahan, Passport Seva) which often suffer from server timeouts, cluttered interfaces, and non-responsive mobile views.
- Unofficial internet forums and blogs which frequently display obsolete fees, outdated guidelines, or misleading claims.
- Physical inquiry counters and middlemen (touts/agents) who charge exorbitant fees for basic checklist advice.

---

## 6. Proposed System
The proposed application provides:
- **Consolidated Offline Repository:** 12 primary services categorized under Identity, Certificates, Travel, Transport, Education, and Benefits.
- **Micro-Detail Explanations:** Deep breakdown of every required vs. optional document.
- **Smart Natural Search:** Real-time partial keyword matching and category filtering.
- **Conversational Assistant:** Local chatbot engine mapping intent keywords to structured service datasets.
- **Personalized Persistence:** Bookmarking frequently consulted services and keeping a rolling history of recently opened services.

---

## 7. Scope
The scope of this project encompasses:
- Educational and pre-application guidance for citizens across India.
- High school and college students applying for scholarships, caste validity, income, and domicile certificates.
- Common applicants preparing documentation for passports, driver's licences, PAN cards, and voter enrollment.

---

## 8. Features
1. **Splash Screen:** Engaging visual branding with app icon, title, and tagline in a deep midnight indigo gradient.
2. **Dashboard / Home Screen:** Personalized greeting, elevated search bar, category filters, quick service cards with live readiness progress, recently viewed section, and "How It Works" guide.
3. **Dedicated Search Screen:** Case-insensitive, partial keyword search with instant result counters and empty-state guidance.
4. **Interactive Document Readiness Tracker:** Check off documents as collected; computes live readiness percentage (0-100%) and persists state across restarts.
5. **Government Statutory Fees & Timelines:** Verified estimated government fees and processing times for every service.
6. **State & Regional Portals Selector:** Direct links and state-specific notes for state revenue/citizen services (Maharashtra, UP, Karnataka, Delhi, Tamil Nadu, Bihar).
7. **Rejection Prevention Checklist:** Prominently lists common pitfalls and grounds for application rejection to prevent user failure.
8. **One-Tap Checklist Export:** Share structured checklists via WhatsApp, messaging, or note applications.
9. **Service Details Screen:** Comprehensive checklist, required vs. optional indicators, structured eligibility criteria, numbered step-by-step application procedures, critical precautions, and FAQs.
10. **Document Detail Dialog:** Explains the legal purpose, approved alternative proofs, verification precautions, and includes a direct "Mark as Ready" toggle.
11. **Chatbot Screen:** Interactive chat with suggestion chips, auto-scrolling message bubbles, typing simulation, one-tap response copying, and direct deep-linking to service checklists.
12. **Saved Services Screen:** Local DataStore persistence for bookmarked services.
13. **About Screen:** Academic project information, developer credits, and institutional disclaimers.

---

## 9. Functional Requirements
- **FR1:** The system shall retrieve and render service records with zero network latency.
- **FR2:** The search filter must match partial strings across service names, descriptions, categories, and document titles with relevance-based ranking.
- **FR3:** Tapping a document card must launch a dialog containing purpose, accepted examples, and verification precautions.
- **FR4:** Tapping document checkboxes must toggle document readiness and immediately update the service readiness progress bar.
- **FR5:** The chatbot engine must parse input text, identify matching service keywords or document types (e.g., "address proof"), and synthesize responses with deep links.
- **FR6:** Bookmark state and checklist progress changes must update synchronously in memory and asynchronously in persistent storage.
- **FR7:** The recent services list must prepend newly opened services while capping the list at a maximum of 5 items without duplicates.

---

## 10. Non-Functional Requirements
- **Performance:** App launch in under 2 seconds; search filtering completes in under 16ms per frame.
- **Availability:** 100% offline availability; no internet connection required for core functions.
- **Usability:** Minimum touch target size of 48dp x 48dp adhering to WCAG and Material 3 standards.
- **Reliability:** Zero crashes on unexpected user input, empty queries, or backstack navigations.
- **Maintainability:** Clean separation of concerns adhering to MVVM design principles.

---

## 11. Technology Stack
| Layer | Technology |
|---|---|
| Language | Kotlin 2.2 |
| UI Framework | Android Jetpack Compose (BOM 2024.09.00) |
| Architecture Pattern | MVVM (Model-View-ViewModel) + Repository Pattern |
| Navigation | AndroidX Navigation Compose 2.8.9 |
| State Management | Kotlin Coroutines & StateFlow |
| Local Persistence | AndroidX DataStore Preferences 1.1.7 |
| Testing | JUnit 4, Robolectric 4.16.1 |
| Build Tool | Gradle Kotlin DSL (`build.gradle.kts`) |

---

## 12. System Architecture
The application employs standard **Clean Architecture + MVVM**:
```
  [ UI Layer (Jetpack Compose Composables) ]
             ▲                   │
     StateFlow (State)      Events (User Actions)
             │                   ▼
     [ ViewModel (DocBotViewModel) ]
             ▲                   │
      Domain Models         Repository Calls
             │                   ▼
   [ Data Layer (ServiceRepository & UserPreferencesRepository) ]
             ▲                   ▲
             │                   │
    [ In-Memory Datasets ]  [ Jetpack DataStore (Disk) ]
```

---

## 13. Application Workflow
```
[ App Launch ] ──► [ Splash Screen (1.4s delay) ]
                          │
                          ▼
                  [ Home Dashboard ]
               ┌──────────┴──────────┐
               ▼                     ▼
     [ Search / Categories ]   [ Quick Service Cards ]
               │                     │
               └──────────┬──────────┘
                          │
                          ▼
              [ Service Details Screen ]
               ├──► Document Details Modal (on tap)
               ├──► Interactive Checkboxes (Readiness Progress)
               ├──► State Portal Selector & Fee / Timeline Badges
               ├──► Avoid Common Rejections Guide
               ├──► Share / Export Checklist
               ├──► Bookmark Toggle (DataStore)
               └──► "Ask Bot About This" (Pre-populates Chat)
                          │
                          ▼
            [ Document Requirement Bot ]
             (Local NLP Keyword Analysis)
```

---

## 14. Module Description
1. **`data.model`:** Immutable Kotlin data classes (`ServiceItem`, `DocumentItem`, `ApplicationStep`, `FaqItem`, `ChatMessage`, `StatePortalInfo`).
2. **`data.repository`:** Central data source containing detailed records for 12 public services, fees, processing times, and search ranking algorithms.
3. **`data.local`:** Asynchronous DataStore repository handling persistence of bookmark sets, checked document keys, and recent service histories.
4. **`bot.DocumentBotEngine`:** Rule-based natural language processing engine executing keyword extraction, intent parsing, and service linking.
5. **`viewmodel.DocBotViewModel`:** Central state coordinator exposing unidirectional `StateFlow` streams.
6. **`navigation.DocBotNavGraph`:** Single-activity type-safe routing graph with bottom bar visibility rules.
7. **`ui.screens.*` & `ui.components.*`:** Modular Compose screens and atomic UI components.

---

## 15. Database / Local Storage Design
The application utilizes **Jetpack DataStore Preferences** with key-value pairs:
- `KEY_BOOKMARKS` (`stringSetPreferencesKey`): Stores set of unique service IDs bookmarked by the user.
- `KEY_RECENT_SERVICES` (`stringPreferencesKey`): Stores comma-separated service IDs ordered by recent access timestamp (max 5).
- `KEY_CHECKED_DOCS` (`stringSetPreferencesKey`): Stores set of composite keys (`"$serviceId:$docId"`) representing collected documents.

---

## 16. UI Design
Designed according to **Material Design 3 (M3)** with a luxury civic tech aesthetic:
- Primary Color: `#3730A3` (Deep Midnight Indigo)
- Secondary Color: `#0F766E` (Deep Elegant Teal)
- Tertiary Color: `#B45309` (Warm Bronze Amber)
- Background: `#F8FAFC` (Slate-50 crisp ultra-clean background)
- Category Accent Palettes: Distinct jewel-tone badges (Identity: Indigo, Certificates: Emerald, Travel: Sky, Transport: Amber, Education: Violet, Benefits: Rose).
- Rounded corners: 14dp to 24dp cards for contemporary feel.
- Dynamic pill indicators for active bottom navigation items.
- Rounded corners: 12dp to 20dp cards for contemporary feel.
- Dynamic pill indicators for active bottom navigation items.

---

## 17. Testing
Unit tests implemented in `ExampleRobolectricTest.kt`:
1. `read app_name string from context`: Validates Android resource mapping.
2. `verify twelve sample services loaded`: Ensures dataset integrity.
3. `verify service search functionality`: Tests full, partial, and category search algorithms.
4. `verify bot engine keyword recognition`: Tests NLP rule matching, document concept resolution, and graceful fallbacks.

---

## 18. Advantages
1. **Completely Offline:** Works reliably without requiring an internet connection or costly AI server APIs.
2. **High Accessibility:** Clear fonts, high color contrast, and 48dp touch targets.
3. **Zero Maintenance Cost:** No ongoing cloud server hosting fees or subscription APIs.
4. **Beginner Friendly:** Step-by-step guidance eliminates bureaucratic confusion.

---

## 19. Limitations
1. Dynamic state-level rule variations (e.g., fee differences between states) require app updates rather than instant cloud fetch.
2. Chatbot utilizes local keyword heuristics rather than full large language model parsing.

---

## 20. Future Scope
- Integration with DigiLocker API for document pre-verification.
- Multilingual localization in Indian regional languages.
- Voice-enabled speech-to-text queries.
- On-device camera OCR for document verification.

---

## 21. Conclusion
The **Document Requirement Bot** successfully bridges the knowledge barrier between citizens and government service requirements. Developed in Kotlin using Jetpack Compose and MVVM, the application delivers a complete, responsive, and robust offline solution suitable for academic evaluation and real-world deployment.
