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


CREATE TABLE listener_stats (
    listener_id VARCHAR(128) PRIMARY KEY REFERENCES listeners(listener_id) ON DELETE CASCADE,
    total_listening_time_ms BIGINT,
    total_songs_played INTEGER,
    current_streak INTEGER
);
