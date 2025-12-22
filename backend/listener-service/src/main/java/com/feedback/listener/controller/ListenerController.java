package com.feedback.listener.controller;

import com.feedback.listener.model.HistoryEntry;
import com.feedback.listener.model.Listener;
import com.feedback.listener.model.ListenerStats;
import com.feedback.listener.repository.HistoryRepository;
import com.feedback.listener.repository.ListenerRepository;
import com.feedback.listener.repository.ListenerStatsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ListenerController {
    private final ListenerRepository listenerRepository;
    private final HistoryRepository historyRepository;
    private final ListenerStatsRepository statsRepository;

    public ListenerController(ListenerRepository listenerRepository,
                              HistoryRepository historyRepository,
                              ListenerStatsRepository statsRepository) {
        this.listenerRepository = listenerRepository;
        this.historyRepository = historyRepository;
        this.statsRepository = statsRepository;
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
        ListenerStats stats = statsRepository.findById(id).orElseGet(() -> {
            ListenerStats s = new ListenerStats();
            s.setListenerId(id);
            s.setTotalListeningTimeMs(listener.getTotalListeningTimeMs() == null ? 0L : listener.getTotalListeningTimeMs());
            s.setTotalSongsPlayed(listener.getTotalSongsPlayed() == null ? 0 : listener.getTotalSongsPlayed());
            s.setCurrentStreak(0);
            return s;
        });

        Map<String,Object> resp = new HashMap<>();
        resp.put("userId", listener.getListenerId());
        resp.put("displayName", listener.getDisplayName());
        resp.put("email", listener.getEmail());
        resp.put("stats", stats);
        // For now topArtists/topSongs left empty — frontend can call Spotify Integration Service directly
        resp.put("topArtists", List.of());
        resp.put("topSongs", List.of());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ListenerStats> getStats(@PathVariable String id) {
        return statsRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
        // expect listener.listenerId to be provided (from Spotify) or generate
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
        // stub token for now
        Map<String,Object> resp = new HashMap<>();
        resp.put("listenerId", l.getListenerId());
        resp.put("displayName", l.getDisplayName());
        resp.put("email", l.getEmail());
        resp.put("token", "mock-jwt-token");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/history")
    public ResponseEntity<HistoryEntry> addHistory(@PathVariable String id, @RequestBody HistoryEntry entry) {
        entry.setListenerId(id);
        if (entry.getPlayedAt() == null) entry.setPlayedAt(Instant.now());
        HistoryEntry saved = historyRepository.save(entry);
        // Update aggregate stats (simple increment)
        var statsOpt = statsRepository.findById(id);
        ListenerStats s = statsOpt.orElseGet(() -> {
            ListenerStats ns = new ListenerStats();
            ns.setListenerId(id);
            ns.setTotalListeningTimeMs(0L);
            ns.setTotalSongsPlayed(0);
            ns.setCurrentStreak(0);
            return ns;
        });
        s.setTotalListeningTimeMs((s.getTotalListeningTimeMs() == null ? 0L : s.getTotalListeningTimeMs()) + (entry.getDurationMs() == null ? 0 : entry.getDurationMs()));
        s.setTotalSongsPlayed((s.getTotalSongsPlayed() == null ? 0 : s.getTotalSongsPlayed()) + 1);
        statsRepository.save(s);
        return ResponseEntity.status(201).body(saved);
    }
}
