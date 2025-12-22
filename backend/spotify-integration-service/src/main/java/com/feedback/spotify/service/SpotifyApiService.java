package com.feedback.spotify.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyApiService {
    public Map<String,Object> getRecentlyPlayed(String accessToken, int limit) {
        // Placeholder - implement Spotify Web API call
        return new HashMap<>();
    }

    public Map<String,Object> getTopArtists(String accessToken, String range) {
        return new HashMap<>();
    }

    public Map<String,Object> getTopTracks(String accessToken, String range) {
        return new HashMap<>();
    }

    public Map<String,Object> getCurrentUser(String accessToken) {
        // Return minimal mock user
        Map<String,Object> u = new HashMap<>();
        u.put("id", "mock-spotify-id");
        u.put("display_name", "Mock User");
        u.put("email", "mock@example.com");
        u.put("country", "US");
        u.put("external_urls", Map.of("spotify", "https://open.spotify.com/user/mock-spotify-id"));
        return u;
    }
}
