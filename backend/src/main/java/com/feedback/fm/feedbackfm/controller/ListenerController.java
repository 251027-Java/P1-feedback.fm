package com.feedback.fm.feedbackfm.controller;

import com.feedback.fm.feedbackfm.dtos.ArtistDTO;
import com.feedback.fm.feedbackfm.dtos.ListenerDTO;
import com.feedback.fm.feedbackfm.dtos.SongDTO;
import com.feedback.fm.feedbackfm.service.ArtistService;
import com.feedback.fm.feedbackfm.service.ListenerService;
import com.feedback.fm.feedbackfm.service.SongService;
import com.feedback.fm.feedbackfm.service.spotify.SpotifyApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class ListenerController {

    private final ListenerService listenerService;
    private final ArtistService artistService;
    private final SongService songService;
    private final SpotifyApiService spotifyApiService;

    public ListenerController(ListenerService listenerService, ArtistService artistService, 
                             SongService songService, SpotifyApiService spotifyApiService) {
        this.listenerService = listenerService;
        this.artistService = artistService;
        this.songService = songService;
        this.spotifyApiService = spotifyApiService;
    }

    // Get user profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<ListenerDTO> getUserProfile(@PathVariable String id) {
        return listenerService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Get complete dashboard data for a user
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @PathVariable String id,
            @RequestHeader(value = "X-Spotify-Token", required = false) String spotifyToken) {
        Optional<ListenerDTO> listenerOpt = listenerService.getById(id);
        if (listenerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ListenerDTO listener = listenerOpt.get();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalListeningTime", "0 hours");
        stats.put("songsPlayed", 0);
        stats.put("currentStreak", 0);
        
        // Calculate stats from Spotify data if token is provided
        if (spotifyToken != null && !spotifyToken.isBlank()) {
            try {
                // Get recently played to calculate stats
                Map<String, Object> recentlyPlayed = spotifyApiService.getRecentlyPlayed(spotifyToken, 50);
                if (recentlyPlayed != null && recentlyPlayed.containsKey("items")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) recentlyPlayed.get("items");
                    if (items != null && !items.isEmpty()) {
                        stats.put("songsPlayed", items.size());
                        
                        // Calculate total listening time (estimate 3 minutes per song)
                        int totalMinutes = items.size() * 3;
                        int hours = totalMinutes / 60;
                        int minutes = totalMinutes % 60;
                        if (hours > 0) {
                            stats.put("totalListeningTime", hours + " hours " + minutes + " minutes");
                        } else {
                            stats.put("totalListeningTime", minutes + " minutes");
                        }
                    }
                }
            } catch (Exception e) {
                // Keep default stats if calculation fails
            }
        }
        
        List<Map<String, Object>> topArtistsData = List.of();
        List<Map<String, Object>> topSongsData = List.of();
        
        // Get top artists and songs from Spotify API if token is provided
        if (spotifyToken != null && !spotifyToken.isBlank()) {
            try {
                // Get top artists
                Map<String, Object> artistsResponse = spotifyApiService.getTopArtists(spotifyToken, "medium_term");
                if (artistsResponse != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> artistsItems = (List<Map<String, Object>>) artistsResponse.get("items");
                    if (artistsItems != null) {
                        topArtistsData = artistsItems.stream()
                            .limit(10)
                            .map(item -> {
                                Map<String, Object> artistMap = new HashMap<>();
                                artistMap.put("id", item.get("id"));
                                artistMap.put("name", item.get("name"));
                                @SuppressWarnings("unchecked")
                                Map<String, Object> externalUrls = (Map<String, Object>) item.get("external_urls");
                                if (externalUrls != null) {
                                    artistMap.put("href", externalUrls.get("spotify"));
                                }
                                return artistMap;
                            })
                            .collect(Collectors.toList());
                    }
                }
                
                // Get top songs
                Map<String, Object> tracksResponse = spotifyApiService.getTopTracks(spotifyToken, "medium_term");
                if (tracksResponse != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> tracksItems = (List<Map<String, Object>>) tracksResponse.get("items");
                    if (tracksItems != null) {
                        topSongsData = tracksItems.stream()
                            .limit(10)
                            .map(item -> {
                                Map<String, Object> songMap = new HashMap<>();
                                songMap.put("id", item.get("id"));
                                songMap.put("name", item.get("name"));
                                
                                // Get artist name
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> artists = (List<Map<String, Object>>) item.get("artists");
                                String artistName = "Unknown Artist";
                                if (artists != null && !artists.isEmpty()) {
                                    artistName = (String) artists.get(0).get("name");
                                }
                                songMap.put("artistName", artistName);
                                
                                @SuppressWarnings("unchecked")
                                Map<String, Object> externalUrls = (Map<String, Object>) item.get("external_urls");
                                if (externalUrls != null) {
                                    songMap.put("href", externalUrls.get("spotify"));
                                }
                                return songMap;
                            })
                            .collect(Collectors.toList());
                    }
                }
            } catch (Exception e) {
                // If Spotify API fails, return empty lists
                // Stats will still be returned
            }
        }
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("userId", listener.listenerId());
        dashboard.put("username", listener.displayName() != null ? listener.displayName() : "");
        dashboard.put("email", listener.email() != null ? listener.email() : "");
        dashboard.put("stats", stats);
        dashboard.put("topArtists", topArtistsData);
        dashboard.put("topSongs", topSongsData);
        return ResponseEntity.ok(dashboard);
    }

    // Get user statistics summary
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String id) {
        Optional<ListenerDTO> listenerOpt = listenerService.getById(id);
        if (listenerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalListeningTime", "0 hours");
        stats.put("songsPlayed", 0);
        stats.put("songsPlayedToday", 0);
        stats.put("currentStreak", 0);
        stats.put("topGenre", "N/A");
        return ResponseEntity.ok(stats);
    }

    // Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<ListenerDTO> updateUser(@PathVariable String id, @RequestBody ListenerDTO listenerDTO) {
        ListenerDTO updated = listenerService.update(id, listenerDTO);
        return ResponseEntity.ok(updated);
    }

    // Delete user account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        listenerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Simple login endpoint. Later fix
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(400).body(Map.of("error", "Email is required"));
        }
        
        Optional<ListenerDTO> listenerOpt = listenerService.findByEmail(email);
        if (listenerOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        
        ListenerDTO listener = listenerOpt.get();
        
        Map<String, Object> response = new HashMap<>();
        response.put("listenerId", listener.listenerId());
        response.put("displayName", listener.displayName());
        response.put("email", listener.email());
        response.put("country", listener.country());
        response.put("token", "mock-jwt-token-12345");
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<ListenerDTO> register(@RequestBody ListenerDTO listenerDTO) {
        ListenerDTO created = listenerService.create(listenerDTO);
        return ResponseEntity.status(201).body(created);
    }
}
