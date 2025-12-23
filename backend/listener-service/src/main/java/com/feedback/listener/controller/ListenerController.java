package com.feedback.listener.controller;

// HistoryEntry endpoints removed; history is owned by history-service
import com.feedback.listener.model.Listener;
import com.feedback.listener.model.ListenerStats;
import com.feedback.listener.repository.ListenerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ListenerController {
    private final ListenerRepository listenerRepository;
    private final RestTemplate restTemplate;

    @Value("${history.service.url:http://localhost:8088}")
    private String historyServiceUrl;

    public ListenerController(ListenerRepository listenerRepository) {
        this.listenerRepository = listenerRepository;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listener> getUser(@PathVariable String id) {
        return listenerRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<Map<String,Object>> getDashboard(@PathVariable String id) {
        var listenerOpt = listenerRepository.findById(id);
        if (listenerOpt.isEmpty()) return ResponseEntity.notFound().build();

        Listener listener = listenerOpt.get();

        // Attempt to fetch stats from history-service; fall back to listener fields
        ListenerStats stats = null;
        try {
            ResponseEntity<ListenerStats> resp = restTemplate.getForEntity(historyServiceUrl + "/api/stats/{id}", ListenerStats.class, id);
            if (resp.getStatusCode().is2xxSuccessful()) stats = resp.getBody();
        } catch (HttpClientErrorException.NotFound nf) {
            // not found — leave stats null to fallback
        } catch (Exception e) {
            // on error, log and fallback
            System.err.println("Error fetching stats from history-service: " + e.getMessage());
        }

        if (stats == null) {
            stats = new ListenerStats();
            stats.setListenerId(id);
            stats.setTotalListeningTimeMs(listener.getTotalListeningTimeMs() == null ? 0L : listener.getTotalListeningTimeMs());
            stats.setTotalSongsPlayed(listener.getTotalSongsPlayed() == null ? 0 : listener.getTotalSongsPlayed());
            stats.setCurrentStreak(0);
        }

        Map<String,Object> resp = new HashMap<>();
        resp.put("userId", listener.getListenerId());
        resp.put("displayName", listener.getDisplayName());
        resp.put("email", listener.getEmail());
        resp.put("stats", stats);
        resp.put("topArtists", List.of());
        resp.put("topSongs", List.of());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ListenerStats> getStats(@PathVariable String id) {
        try {
            ResponseEntity<ListenerStats> resp = restTemplate.getForEntity(historyServiceUrl + "/api/stats/{id}", ListenerStats.class, id);
            return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
        } catch (HttpClientErrorException.NotFound nf) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Listener> updateUser(@PathVariable String id, @RequestBody Listener listener) {
        return listenerRepository.findById(id).map(existing -> {
            existing.setDisplayName(listener.getDisplayName());
            existing.setEmail(listener.getEmail());
            existing.setCountry(listener.getCountry());
            existing.setHref(listener.getHref());
            listenerRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Listener> register(@RequestBody Listener listener) {
        if (listener.getListenerId() == null || listener.getListenerId().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Listener saved = listenerRepository.save(listener);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getListenerId())).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) return ResponseEntity.badRequest().build();
        var opt = listenerRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
        Listener l = opt.get();
        Map<String,Object> resp = new HashMap<>();
        resp.put("listenerId", l.getListenerId());
        resp.put("displayName", l.getDisplayName());
        resp.put("email", l.getEmail());
        resp.put("token", "mock-jwt-token");
        return ResponseEntity.ok(resp);
    }

    // History endpoint removed: history ingestion is handled directly by the history-service.
}
