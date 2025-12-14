# Project Requirements Checklist

## ✅ COMPLETED Requirements

### Full Stack Application
- ✅ Database/Persistence layer (PostgreSQL in Docker)
- ✅ Server/API (Spring Boot REST API)
- ✅ Web Frontend/SPA (React)

### Frontend Requirements (React SPA)
- ✅ Routing to at least 2 separate pages/views
  - Currently have 6 routes: `/`, `/dashboard`, `/top-artists`, `/top-songs`, `/history`, `/currently-playing`
- ✅ HTTP requests to API
  - All components use `axios` via `services/api.ts`
- ✅ Authentication and Authorization
  - JWT-based authentication with Spotify OAuth
  - `JwtAuthenticationFilter` and `SecurityConfig` implemented
- ✅ At least 5 different components
  - Currently have 7: `Dashboard`, `Login`, `TopArtists`, `TopSongs`, `ListeningHistory`, `CurrentlyPlaying`, `Navbar`
- ✅ Two-way binding
  - Implemented in `Login`, `Dashboard`, `TopArtists`, `TopSongs` components

### Backend Requirements
- ✅ RESTful API
  - All controllers follow REST conventions
- ✅ Spring Data JPA
  - All repositories extend `JpaRepository`
- ✅ Separation of concerns through application layers
  - Controllers → Services → Repositories → Models
  - DTOs for API communication
- ✅ At least 2 custom classes
  - **Custom Exceptions**: `ResourceNotFoundException`, `InvalidRequestException`, `SpotifyApiException`, `AuthenticationException` (4 classes)
  - **Custom DTOs**: 6 record classes (ListenerDTO, SongDTO, ArtistDTO, AlbumDTO, PlaylistDTO, HistoryDTO)
  - **Custom Utilities**: `JwtUtil`, `JwtAuthenticationFilter`
- ✅ Verbose exception handling
  - `GlobalExceptionHandler` with `@RestControllerAdvice`
  - Custom exceptions used throughout services
  - Prevents unexpected crashes with proper error responses

### Database Requirements
- ✅ PostgreSQL in Docker
  - `docker-compose.yml` configured
- ✅ 3NF (Third Normal Form)
  - Database schema is normalized
  - Junction tables for many-to-many relationships
- ✅ At least 5 tables
  - 6 tables: `listener`, `artist`, `song`, `album`, `playlist`, `history`
  - Plus 3 junction tables: `artists_songs`, `playlists_songs`, `albums_songs`
- ✅ At least 1 Many-to-Many relationship
  - **3 Many-to-Many relationships**:
    1. Artists ↔ Songs (`artists_songs`)
    2. Playlists ↔ Songs (`playlists_songs`)
    3. Albums ↔ Songs (`albums_songs`)

### Git Requirements
- ✅ Repository exists
  - Multiple branches visible: `main`, `kennLaptop`, `Sidney`, `SidneyG`, `juan`, `kenneth`, `omar`, `update-readme`
- ⚠️ Branching strategy
  - Branches exist, but need to verify no commits directly to main

---

## ⚠️ NEEDS ATTENTION

### Backend - Test Coverage
- ⚠️ **50%+ test coverage requirement**
  - Test files exist for all service classes
  - **CRITICAL**: Test files use `ResponseStatusException` but services now use custom exceptions
  - Need to update all test files to use custom exceptions:
    - `InvalidRequestException` instead of `ResponseStatusException` for BAD_REQUEST
    - `ResourceNotFoundException` instead of `ResponseStatusException` for NOT_FOUND
  - **JaCoCo plugin not configured** - need to add to `pom.xml` to generate coverage reports
  - Files needing updates:
    - `ListenerServiceTest.java` ✅ (partially fixed)
    - `SongServiceTest.java`
    - `ArtistServiceTest.java`
    - `AlbumServiceTest.java`
    - `PlaylistServiceTest.java`
    - `HistoryServiceTest.java`

### Documentation Requirements
- ❌ **README.md at root level**
  - Currently only exists in `documentation/` folder
  - Need comprehensive root-level README with:
    - How to start and run the full stack app
    - Prerequisites
    - Installation steps
    - Database setup
    - Backend setup
    - Frontend setup
    - Running the application

- ❌ **Description file (DESCRIPTION.md)**
  - Must include:
    1. **App description** - What the app does, its purpose, features
    2. **User stories** - Format: "As a ____, I want to ____, so that I can ____"
    3. **Wireframes** - Frontend UI mockups (can be hand-drawn or digital)
    4. **ERD** - Entity Relationship Diagram showing all tables and relationships
    5. **API endpoint documentation** - Complete documentation of all API endpoints

---

## 📋 ACTION ITEMS

### Priority 1 (Critical for Requirements)
1. **Fix all test files** to use custom exceptions instead of `ResponseStatusException`
2. **Add JaCoCo plugin** to `pom.xml` for test coverage reporting
3. **Create root-level README.md** with complete setup instructions
4. **Create DESCRIPTION.md** with:
   - App description
   - User stories (5+)
   - Wireframes (screenshots/mockups)
   - ERD (diagram of database schema)
   - API documentation (all endpoints with request/response examples)

### Priority 2 (Verify/Polish)
1. Verify no commits directly to main branch (use pull requests)
2. Run test coverage report to verify 50%+ coverage
3. Review wireframes match actual implementation
4. Ensure ERD matches database schema exactly

---

## 📊 Current Status Summary

| Category | Status | Completion |
|----------|--------|------------|
| Full Stack Architecture | ✅ Complete | 100% |
| Frontend Requirements | ✅ Complete | 100% |
| Backend Requirements | ⚠️ Mostly Complete | 90% |
| Database Requirements | ✅ Complete | 100% |
| Testing | ⚠️ Needs Fixes | 70% |
| Documentation | ❌ Incomplete | 30% |
| **Overall** | **⚠️ Needs Documentation & Test Fixes** | **~80%** |

---

## Next Steps
1. Fix test exception imports (high priority - affects test coverage)
2. Add JaCoCo plugin to pom.xml
3. Create comprehensive README.md
4. Create DESCRIPTION.md with all required sections
5. Generate test coverage report to verify 50%+ coverage
6. Final review before presentation
