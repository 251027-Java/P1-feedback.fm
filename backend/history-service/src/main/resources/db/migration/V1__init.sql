-- History/Stats service schema
CREATE TABLE history_records (
    id SERIAL PRIMARY KEY,
    listener_id VARCHAR(128) NOT NULL,
    song_id VARCHAR(255),
    played_at TIMESTAMP WITH TIME ZONE,
    duration_ms INTEGER
);

CREATE TABLE stats_aggregate (
    listener_id VARCHAR(128) PRIMARY KEY,
    total_listening_time_ms BIGINT,
    total_songs_played INTEGER,
    current_streak INTEGER
);

CREATE INDEX idx_history_listener ON history_records(listener_id);
