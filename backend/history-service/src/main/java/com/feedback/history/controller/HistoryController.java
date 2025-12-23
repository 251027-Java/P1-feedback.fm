package com.feedback.history.controller;

import com.feedback.history.model.HistoryRecord;
import com.feedback.history.repository.HistoryRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryRecordRepository repository;

    public HistoryController(HistoryRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{listenerId}")
    public List<HistoryRecord> list(@PathVariable String listenerId) {
        return repository.findByListenerIdOrderByPlayedAtDesc(listenerId);
    }

    @PostMapping("/{listenerId}")
    public ResponseEntity<HistoryRecord> add(@PathVariable String listenerId, @RequestBody HistoryRecord record) {
        record.setListenerId(listenerId);
        if (record.getPlayedAt() == null) record.setPlayedAt(Instant.now());
        HistoryRecord saved = repository.save(record);
        return ResponseEntity.created(URI.create("/api/history/" + listenerId + "/" + saved.getId())).body(saved);
    }
}
