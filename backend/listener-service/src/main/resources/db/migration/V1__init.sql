-- Listener service schema
CREATE TABLE listeners (
    listener_id VARCHAR(128) PRIMARY KEY,
    display_name VARCHAR(255),
    email VARCHAR(255),
    country VARCHAR(10),
    href VARCHAR(512),
    total_listening_time_ms BIGINT,
    total_songs_played INTEGER
);

CREATE TABLE history (
    id SERIAL PRIMARY KEY,
    listener_id VARCHAR(128) REFERENCES listeners(listener_id) ON DELETE CASCADE,
    song_id VARCHAR(255),
    played_at TIMESTAMP WITH TIME ZONE,
    duration_ms INTEGER
);

CREATE TABLE listener_stats (
    listener_id VARCHAR(128) PRIMARY KEY REFERENCES listeners(listener_id) ON DELETE CASCADE,
    total_listening_time_ms BIGINT,
    total_songs_played INTEGER,
    current_streak INTEGER
);
