# AI Usage Report

This document describes how AI tools were used during the development of this project.

---

## 1. AI Tools Used

- ChatGPT (OpenAI GPT-5)
- Android Studio AI Assistant (for minor syntax suggestions)

Primary AI support was provided by ChatGPT for:
- Architecture discussions
- Debugging guidance
- Code structure improvements
- Paging + RemoteMediator implementation clarification
- Clean Architecture refactoring guidance

---

## 2. Where AI Was Used

AI assistance was used in the following modules:

### Architecture & Structure
- Clean Architecture refactoring (domain layer introduction)
- Repository interface + implementation separation
- UseCase layer creation
- Dependency injection setup (Hilt bindings)

### Data Layer
- RemoteMediator debugging and correction
- RemoteKeys implementation
- Paging 3 configuration guidance
- Offline-first detail screen design
- Mapper pattern guidance (DTO → Entity → Domain)

### Presentation Layer
- LoadState handling improvements
- Sticky offline banner implementation
- Paging error state handling
- Retry strategy improvements

### Debugging
- RemoteMediator pagination stopping issue
- Hilt MissingBinding error resolution
- Room migration / schema issues
- Append LoadState error analysis

---

## 3. AI Output Accepted vs Modified

### Accepted As-Is
- Basic RemoteKeys structure
- Hilt @Binds module pattern
- Paging LoadState handling pattern
- Clean Architecture layer explanation
- UseCase structure template

### Modified Before Integration
- RemoteMediator page calculation logic (adapted for Rick & Morty API)
- Extracted next page from API URL instead of using `page + 1`
- Adjusted PagingConfig to match API page size (20)
- Improved REFRESH key logic beyond AI’s initial simple implementation
- Simplified overly complex Flow handling in ViewModel

AI provided guidance, but final implementation was refactored to fit project needs.

---

## 4. AI Suggestions Rejected (and Why)

### 1. Using DTO directly as Room Entity
Rejected because:
- Violates clean architecture separation
- Couples network schema to database schema
- Makes long-term maintenance harder

Instead:
- Separate DTO, Entity, and Domain model were used.

---

### 2. Keeping UiState in Domain layer
Rejected because:
- UiState is presentation concern
- Domain must remain pure Kotlin
- Violates clean architecture boundaries

UiState was moved to presentation layer.

---

### 3. Always forcing API refresh even when cache exists
Rejected because:
- Breaks true offline-first behavior
- Causes unnecessary network calls

Implemented proper Room-first strategy instead.

---

## 5. Example of Improving AI Output Using Own Judgment

During RemoteMediator implementation:

AI initially suggested:
- Calculating next page using `page + 1`.

After testing, pagination stopped early due to incorrect key handling.

Improvement applied:
- Extracted next page number from API `info.next` URL.
- Implemented full RemoteKeys table.
- Added proper REFRESH key resolution logic.
- Ensured correct ordering in DAO query.

This significantly stabilized pagination and made it production-ready.

---

## 6. Overall AI Usage Philosophy

AI was used as:
- A reasoning assistant
- A debugging helper
- An architecture discussion partner

All code was:
- Reviewed manually
- Tested in emulator
- Debugged using logs
- Refactored for clarity and correctness

Final architecture decisions were made using personal judgment and understanding of Android best practices.

---

## 7. Conclusion

AI accelerated development and debugging, but:
- Final implementation decisions
- Architectural boundaries
- Clean architecture separation
- Offline-first behavior
- Paging correctness

were verified and improved manually.

The project reflects both AI-assisted development and independent engineering decisions.
