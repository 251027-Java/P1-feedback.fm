-- 01_artist.sql
CREATE TABLE artist (
    artist_id   VARCHAR(64) PRIMARY KEY,
    name        TEXT        NOT NULL,
    href        TEXT
);

-- 02_song.sql
CREATE TABLE song (
    song_id      VARCHAR(64) PRIMARY KEY,
    name         TEXT        NOT NULL,
    duration_ms  INTEGER     NOT NULL,
    artist_id    VARCHAR(64) NOT NULL REFERENCES artist(artist_id),
    href         TEXT
);

-- 03_listeners.sql
CREATE TABLE listeners (
    listener_id   VARCHAR(64) PRIMARY KEY,
    display_name  TEXT,
    email         TEXT,
    country       VARCHAR(10),
    href          TEXT
);

-- 04_playlists.sql
CREATE TABLE playlists (
    playlist_id   VARCHAR(64) PRIMARY KEY,
    name          TEXT        NOT NULL,
    owner_id      VARCHAR(64) NOT NULL REFERENCES listeners(listener_id),
    song_id       VARCHAR(64) NOT NULL REFERENCES song(song_id),
    is_public     BOOLEAN     NOT NULL DEFAULT TRUE,
    href          TEXT
);

-- 05_history.sql
CREATE TABLE history (
    history_id   BIGSERIAL PRIMARY KEY,
    listener_id  VARCHAR(64) NOT NULL REFERENCES listeners(listener_id),
    song_id      VARCHAR(64) NOT NULL REFERENCES song(song_id),
    played_at    TIMESTAMPTZ NOT NULL
);

--From root
--docker compose up -d

--Check it is set up
-- docker compose ps

--confirm schema
-- docker exec -it spotify_db psql -U spotify_user -d spotifydb

-- pgAdmin in the browser at http://localhost:5050
-- (Connect to the server port: 5432, user/pass: spotify_user / spotify_pass).