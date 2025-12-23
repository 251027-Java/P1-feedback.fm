Music Metadata Service

Run locally (requires PostgreSQL on localhost:5432 with database `music_metadata`):

Build:

```bash
mvn -f backend/music-metadata-service clean package
```

Run:

```bash
java -jar backend/music-metadata-service/target/music-metadata-service-0.0.1-SNAPSHOT.jar
```

Docker:

```bash
docker build -t music-metadata-service:local backend/music-metadata-service
```

API endpoints: `/api/artists`, `/api/albums`, `/api/songs`, `/api/playlists`.
