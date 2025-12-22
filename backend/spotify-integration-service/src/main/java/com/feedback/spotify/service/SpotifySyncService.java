package com.feedback.spotify.service;

import org.springframework.stereotype.Service;

@Service
public class SpotifySyncService {
    public void syncRecentlyPlayed(String spotifyToken, String listenerId) {
        // Placeholder: implement sync logic (pull recently played and emit events)
    }

    public void recalculateStatsFromHistory(String listenerId) {
        // Placeholder: if service stores aggregate stats, recalc here
    }
}
