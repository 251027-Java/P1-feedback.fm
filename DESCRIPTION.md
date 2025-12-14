# feedback.fm - Project Description

## App Description

**feedback.fm** is a full-stack web application that integrates with Spotify's Web API to provide users with comprehensive insights into their music listening habits. Similar to Last.fm, the application tracks and displays user statistics including top artists, top songs, currently playing tracks, listening history, and playlist information.

### Key Features
- **Spotify OAuth Integration**: Secure authentication using Spotify's OAuth 2.0 flow
- **User Dashboard**: Personalized dashboard showing listening statistics and trends
- **Top Artists & Songs**: View top artists and songs across different time ranges (short-term, medium-term, long-term)
- **Currently Playing**: Real-time display of currently playing tracks
- **Listening History**: Track and view listening history
- **Playlist Management**: View and manage playlists
- **Secure API**: JWT-based authentication for API endpoints

### Technology Stack
- **Frontend**: React 19 with TypeScript, Vite, React Router
- **Backend**: Spring Boot 4.0 with Spring Security, Spring Data JPA
- **Database**: PostgreSQL 16 running in Docker
- **Authentication**: JWT tokens with Spotify OAuth integration

---

## User Stories

As a **Spotify user**, I want to **authenticate with my Spotify account**, so that I can **securely access my personal music data**.

As a **Spotify user**, I want to **view my top artists**, so that I can **discover my music preferences and see which artists I listen to most**.

As a **Spotify user**, I want to **view my top songs**, so that I can **see my favorite tracks across different time periods**.

As a **Spotify user**, I want to **see my currently playing track**, so that I can **share what I'm listening to in real-time**.

As a **Spotify user**, I want to **view my listening history**, so that I can **track my music consumption patterns over time**.

As a **Spotify user**, I want to **see my personalized dashboard**, so that I can **get a comprehensive overview of my listening statistics in one place**.

As a **Spotify user**, I want to **view my playlists**, so that I can **manage and explore my music collections**.

As a **Spotify user**, I want to **filter my top artists by time range** (short-term, medium-term, long-term), so that I can **see how my music taste has evolved**.

As a **Spotify user**, I want to **search for artists and songs**, so that I can **quickly find specific content**.

As a **Spotify user**, I want to **view my listening statistics**, so that I can **understand my music consumption habits**.

---

## Wireframes

### Login Page
```
┌─────────────────────────────────────┐
│          feedback.fm                │
│                                     │
│    ┌───────────────────────────┐   │
│    │                           │   │
│    │   Login with Spotify      │   │
│    │                           │   │
│    │   [Connect to Spotify]    │   │
│    │                           │   │
│    └───────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘
```

### Dashboard
```
┌─────────────────────────────────────┐
│  Dashboard  |  Top Artists  | ...  │ ← Navigation
├─────────────────────────────────────┤
│                                     │
│  Welcome, [Username]                │
│                                     │
│  ┌──────────┐  ┌──────────┐        │
│  │  Total   │  │  Songs   │        │
│  │  Time    │  │  Played  │        │
│  │  120 hrs │  │   1,234  │        │
│  └──────────┘  └──────────┘        │
│                                     │
│  Top Artists                        │
│  ┌────┐ ┌────┐ ┌────┐             │
│  │Art1│ │Art2│ │Art3│             │
│  └────┘ └────┘ └────┘             │
│                                     │
│  Top Songs                          │
│  1. Song 1 - Artist                 │
│  2. Song 2 - Artist                 │
│  3. Song 3 - Artist                 │
│                                     │
└─────────────────────────────────────┘
```

### Top Artists Page
```
┌─────────────────────────────────────┐
│  Dashboard  |  Top Artists  | ...  │
├─────────────────────────────────────┤
│                                     │
│  Top Artists                        │
│                                     │
│  Time Range: [Last 6 months ▼]     │
│  Search: [________________]         │
│                                     │
│  ┌──────────────┐                  │
│  │ Artist Name  │                  │
│  │ [Image]      │                  │
│  │ Plays: 234   │                  │
│  └──────────────┘                  │
│                                     │
│  ┌──────────────┐                  │
│  │ Artist Name  │                  │
│  │ [Image]      │                  │
│  │ Plays: 189   │                  │
│  └──────────────┘                  │
│                                     │
└─────────────────────────────────────┘
```

### Currently Playing
```
┌─────────────────────────────────────┐
│  Dashboard  |  ... | Now Playing    │
├─────────────────────────────────────┤
│                                     │
│  Currently Playing                  │
│                                     │
│  ┌──────────────────────────────┐  │
│  │                              │  │
│  │    [Album Art]               │  │
│  │                              │  │
│  │    Song Name                 │  │
│  │    Artist Name               │  │
│  │    Album Name                │  │
│  │                              │  │
│  │    Status: Playing           │  │
│  │                              │  │
│  └──────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

---

## Entity Relationship Diagram (ERD)

```
┌─────────────────┐
│    listener     │
├─────────────────┤
│ listener_id (PK)│
│ display_name    │
│ email           │
│ country         │
│ href            │
└────────┬────────┘
         │
         │ 1
         │
         │ M
    ┌────┴────┐         ┌──────────┐
    │ history │         │ playlist │
    ├────┬────┤         ├──────────┤
    │ id │(PK)│         │playlist  │
    │    │    │         │_id (PK)  │
    │listener│─┼──M─────┤owner_id  │
    │_id (FK)│         │name      │
    │song_id │         │is_public │
    │(FK)    │         │href      │
    │played  │         └────┬─────┘
    │_at     │              │
    └────┬───┘              │ M
         │ M                │
         │                  │
    ┌────┴────┐      ┌──────┴──────┐
    │  song   │      │playlists_   │
    ├─────────┤      │songs        │
    │song_id  │◄─────┤(junction)   │
    │(PK)     │  M   ├─────────────┤
    │name     │      │playlist_id  │
    │duration │      │song_id      │
    │_ms      │      └─────────────┘
    │href     │
    └────┬────┘
         │ M
    ┌────┴──────┐      ┌──────────┐
    │artists_   │      │ artist   │
    │songs      │      ├──────────┤
    │(junction) │  M   │artist_id │
    ├───────────┤      │(PK)      │
    │artist_id  │◄─────┤name      │
    │song_id    │      │href      │
    └───────────┘      └────┬─────┘
                            │ 1
                            │
                            │ M
                    ┌───────┴────┐
                    │  album     │
                    ├────────────┤
                    │album_id    │
                    │(PK)        │
                    │title       │
                    │release_year│
                    │artist_id   │
                    │(FK)        │
                    │href        │
                    └──────┬─────┘
                           │ 1
                           │
                           │ M
                    ┌──────┴──────┐
                    │albums_songs │
                    │(junction)   │
                    ├─────────────┤
                    │album_id     │
                    │song_id      │
                    └─────────────┘
```

### Relationship Summary:
- **Listener** → **History** (1:M) - One listener has many history records
- **Listener** → **Playlist** (1:M) - One listener owns many playlists
- **Song** ↔ **Artist** (M:M) - Many songs have many artists (via `artists_songs`)
- **Song** ↔ **Playlist** (M:M) - Many songs in many playlists (via `playlists_songs`)
- **Song** ↔ **Album** (M:M) - Many songs in many albums (via `albums_songs`)
- **Artist** → **Album** (1:M) - One artist has many albums
- **Song** → **History** (1:M) - One song has many history records

### Tables:
1. `listener` - User/listener information
2. `artist` - Artist information
3. `song` - Song information
4. `album` - Album information
5. `playlist` - Playlist information
6. `history` - Listening history records
7. `artists_songs` - Junction table (Many-to-Many)
8. `playlists_songs` - Junction table (Many-to-Many)
9. `albums_songs` - Junction table (Many-to-Many)

---

## API Endpoint Documentation

### Base URL
```
http://localhost:8080/api
```

All endpoints require JWT authentication except `/auth/**` endpoints.

---

### Authentication Endpoints

#### GET /api/auth/login
Get Spotify authorization URL for OAuth flow.

**Response:**
```json
{
  "authUrl": "https://accounts.spotify.com/authorize?..."
}
```

**Status Codes:**
- `200 OK` - Success

---

#### GET /api/auth/callback
Handle OAuth callback from Spotify and return JWT token.

**Query Parameters:**
- `code` (string, required) - Authorization code from Spotify

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "listenerId": "user123",
  "user": {
    "id": "user123",
    "display_name": "John Doe",
    "email": "john@example.com"
  },
  "spotifyAccessToken": "BQC...",
  "spotifyRefreshToken": "AQD..."
}
```

**Status Codes:**
- `200 OK` - Authentication successful
- `400 Bad Request` - Invalid authorization code
- `500 Internal Server Error` - Authentication failed

---

#### POST /api/auth/refresh
Refresh Spotify access token.

**Request Body:**
```json
{
  "refreshToken": "AQD..."
}
```

**Response:**
```json
{
  "access_token": "BQC...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

### User/Listener Endpoints

#### GET /api/users/{id}
Get user profile by ID.

**Path Parameters:**
- `id` (string, required) - User/Listener ID

**Headers:**
- `Authorization: Bearer <jwt_token>` (required)

**Response:**
```json
{
  "listenerId": "user123",
  "displayName": "John Doe",
  "email": "john@example.com",
  "country": "US",
  "href": "https://open.spotify.com/user/user123"
}
```

**Status Codes:**
- `200 OK` - Success
- `404 Not Found` - User not found
- `401 Unauthorized` - Invalid or missing token

---

#### GET /api/users/{id}/dashboard
Get complete dashboard data for a user.

**Path Parameters:**
- `id` (string, required) - User ID

**Headers:**
- `Authorization: Bearer <jwt_token>` (required)

**Response:**
```json
{
  "userId": "user123",
  "username": "John Doe",
  "email": "john@example.com",
  "stats": {
    "totalListeningTime": "120 hours",
    "songsPlayed": 1234,
    "currentStreak": 7
  },
  "topArtists": [],
  "topSongs": []
}
```

---

#### GET /api/users/{id}/stats
Get user statistics summary.

**Response:**
```json
{
  "totalListeningTime": "120 hours",
  "songsPlayed": 1234,
  "songsPlayedToday": 23,
  "currentStreak": 7,
  "topGenre": "Pop"
}
```

---

#### PUT /api/users/{id}
Update user profile.

**Request Body:**
```json
{
  "listenerId": "user123",
  "displayName": "John Doe",
  "email": "john@example.com",
  "country": "US",
  "href": "https://..."
}
```

**Response:**
```json
{
  "listenerId": "user123",
  "displayName": "John Doe Updated",
  "email": "john@example.com",
  "country": "US",
  "href": "https://..."
}
```

**Status Codes:**
- `200 OK` - Success
- `404 Not Found` - User not found
- `400 Bad Request` - Invalid data

---

#### DELETE /api/users/{id}
Delete user account.

**Status Codes:**
- `204 No Content` - Success
- `404 Not Found` - User not found

---

### Artist Endpoints

#### GET /api/artists
Get all artists with optional filtering.

**Query Parameters:**
- `query` (string, optional) - Search by name
- `page` (integer, optional) - Page number
- `size` (integer, optional) - Page size

**Response:**
```json
[
  {
    "artistId": "artist1",
    "name": "Artist Name",
    "href": "https://...",
    "albums": ["album1", "album2"]
  }
]
```

---

#### GET /api/artists/{id}
Get artist by ID.

**Response:**
```json
{
  "artistId": "artist1",
  "name": "Artist Name",
  "href": "https://...",
  "albums": ["album1"]
}
```

---

#### GET /api/artists/top
Get top artists from Spotify.

**Query Parameters:**
- `time_range` (string, optional) - `short_term`, `medium_term`, or `long_term` (default: `medium_term`)

**Response:**
```json
[
  {
    "artistId": "artist1",
    "name": "Top Artist",
    "href": "https://...",
    "albums": []
  }
]
```

---

#### POST /api/artists
Create a new artist.

**Request Body:**
```json
{
  "artistId": "artist1",
  "name": "New Artist",
  "href": "https://...",
  "albums": []
}
```

**Status Codes:**
- `201 Created` - Success
- `400 Bad Request` - Invalid data or duplicate ID

---

#### PUT /api/artists/{id}
Update artist.

**Status Codes:**
- `200 OK` - Success
- `404 Not Found` - Artist not found

---

#### DELETE /api/artists/{id}
Delete artist.

**Status Codes:**
- `204 No Content` - Success
- `404 Not Found` - Artist not found

---

### Song Endpoints

#### GET /api/songs
Get all songs.

**Query Parameters:**
- `query` (string, optional) - Search by name
- `page`, `size` (integer, optional) - Pagination

**Response:**
```json
[
  {
    "songId": "song1",
    "name": "Song Name",
    "href": "https://...",
    "durationMs": 200000,
    "artists": ["artist1"],
    "albums": ["album1"]
  }
]
```

---

#### GET /api/songs/{id}
Get song by ID.

---

#### GET /api/songs/top
Get top songs from Spotify.

**Query Parameters:**
- `time_range` (string, optional) - Time range filter

---

#### GET /api/songs/currently-playing
Get currently playing track.

**Response:**
```json
{
  "isPlaying": true,
  "name": "Currently Playing Song",
  "artist": "Artist Name",
  "album": "Album Name"
}
```

---

#### POST /api/songs
Create a new song.

#### PUT /api/songs/{id}
Update song.

#### DELETE /api/songs/{id}
Delete song.

---

### History Endpoints

#### GET /api/history
Get listening history.

**Query Parameters:**
- `listenerId` (string, optional) - Filter by listener
- `limit` (integer, optional) - Limit results (default: 50)
- `dateRange` (string, optional) - Date range filter
- `query` (string, optional) - Search query

**Response:**
```json
[
  {
    "historyId": 1,
    "playedAt": "2024-01-15T10:30:00Z",
    "listenerId": "user123",
    "songId": "song1"
  }
]
```

---

#### GET /api/history/recent
Get recently played songs.

**Query Parameters:**
- `listenerId` (string, required) - Listener ID
- `limit` (integer, optional) - Limit results (default: 20)

---

#### POST /api/history
Add a listening history record.

**Request Body:**
```json
{
  "listenerId": "user123",
  "songId": "song1",
  "playedAt": "2024-01-15T10:30:00Z"
}
```

---

#### DELETE /api/history/{historyId}
Delete a history record.

**Status Codes:**
- `204 No Content` - Success
- `404 Not Found` - History record not found

---

### Error Responses

All endpoints return consistent error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Listener not found with id: user123",
  "path": "/api/users/user123"
}
```

**Common Status Codes:**
- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Success (no content)
- `400 Bad Request` - Invalid request
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error
- `503 Service Unavailable` - Spotify API error

---

## Security

- **JWT Authentication**: All endpoints (except `/auth/**`) require a valid JWT token in the `Authorization` header
- **Spotify OAuth**: Secure authentication flow with Spotify
- **CORS**: Configured for `http://localhost:3000`
- **Input Validation**: All inputs are validated before processing
- **Exception Handling**: Global exception handler provides consistent error responses

---

## Database Schema

The database is in **Third Normal Form (3NF)** with proper normalization:
- All tables have primary keys
- Foreign keys maintain referential integrity
- Junction tables handle many-to-many relationships
- No redundant data storage
