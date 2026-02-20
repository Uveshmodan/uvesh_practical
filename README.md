# UveshPractical

An offline-first Android application built with Jetpack Compose and Clean Architecture.  
The app displays characters from the Rick & Morty API with pagination, local caching, and full offline support.

---

# 📱 Features

- Infinite scrolling using Paging 3
- Offline-first architecture (Room as source of truth)
- RemoteMediator for automatic sync
- Character detail screen (works offline)
- Sticky offline banner
- Clean Architecture (Presentation → Domain → Data)
- Dependency Injection using Hilt

---

# 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Paging 3
- Room Database
- RemoteMediator
- Retrofit
- Hilt
- Coroutines & Flow
- MVVM
- Clean Architecture

---

# ⚙️ Setup Instructions

## 1. Clone the repository
git clone https://github.com/your-username/UveshPractical.git

2. Open in Android Studio
	•	Android Studio Hedgehog or newer recommended
	•	Use JDK 17

3. Sync Gradle

Make sure you have:
	•	Kotlin 2.x
	•	Latest stable AGP
	•	Internet connection for dependency resolution

4. Run the app
	•	Use an emulator (API 26+ recommended)
	•	Or a physical Android device

No API key is required as Rick & Morty API is public.

⸻

🏗 Architecture Overview

This project follows Clean Architecture principles with clear layer separation:
presentation → domain → data

📌 Presentation Layer
	•	Jetpack Compose UI
	•	ViewModels
	•	UiState
	•	Navigation

📌 Domain Layer
	•	Pure Kotlin models
	•	Repository interfaces
	•	UseCases

📌 Data Layer
	•	Retrofit API
	•	Room database
	•	RemoteMediator
	•	Paging 3
	•	Repository implementation
	•	DTO ↔ Entity ↔ Domain mappers

Room acts as the single source of truth.
UI observes database changes via Flow.

⸻

🔄 Offline-First Strategy

The app uses Paging 3 + RemoteMediator with Room:
	1.	UI loads cached data from Room immediately.
	2.	RemoteMediator fetches fresh data from API.
	3.	API response is stored in Room.
	4.	Room invalidates PagingSource.
	5.	UI updates automatically.

If the device is offline:
	•	Cached data is displayed.
	•	No crash occurs.
	•	Sticky offline banner is shown.

⸻

🎯 Key Technical Decisions

1️⃣ Room as Single Source of Truth

Chosen to ensure consistent offline-first behavior.

Trade-off:
	•	Slightly more complex setup (RemoteKeys table required).

⸻

2️⃣ RemoteMediator Instead of Manual Pagination

Ensures:
	•	Proper pagination restoration
	•	Stability after process death
	•	Scalable architecture

Trade-off:
	•	Requires RemoteKeys management.

⸻

3️⃣ Clean Architecture with UseCases

Improves:
	•	Testability
	•	Separation of concerns
	•	Scalability

Trade-off:
	•	Slightly more boilerplate for small apps.

⸻

4️⃣ DTO ≠ Entity ≠ Domain Model

Separate models were used to:
	•	Avoid coupling layers
	•	Improve maintainability

Trade-off:
	•	Additional mapper code.

⸻

⚠️ Known Limitations
	1.	No manual migration strategy (uses destructive migration during development).
	2.	No automated UI tests.
	3.	No background sync scheduling (refresh only occurs during active usage).
	4.	Error messaging is basic and not localized.
	5.	No search functionality implemented.

⸻

📌 Future Improvements
	•	Add search with pagination
	•	Add pull-to-refresh
	•	Implement proper Room migrations
	•	Add UI & unit tests
	•	Add shimmer loading state
	•	Improve error UI/UX

⸻

📚 API Reference

Data source:
https://rickandmortyapi.com/

⸻

🧠 Learning Goals

This project demonstrates:
	•	Advanced Paging 3 usage
	•	RemoteMediator implementation
	•	Proper RemoteKeys handling
	•	Clean Architecture implementation
	•	Offline-first Android design

⸻

👨‍💻 Author

Uvesh Modan
Senior Android Developer
