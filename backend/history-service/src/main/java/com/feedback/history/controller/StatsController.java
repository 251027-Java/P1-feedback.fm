package com.feedback.history.controller;

import com.feedback.history.model.StatsAggregate;
import com.feedback.history.repository.StatsAggregateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsAggregateRepository repository;

    public StatsController(StatsAggregateRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{listenerId}")
    public ResponseEntity<StatsAggregate> get(@PathVariable String listenerId) {
        Optional<StatsAggregate> opt = repository.findById(listenerId);
        return opt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{listenerId}")
    public ResponseEntity<StatsAggregate> createOrUpdate(@PathVariable String listenerId, @RequestBody StatsAggregate stats) {
        stats.setListenerId(listenerId);
        StatsAggregate saved = repository.save(stats);
        return ResponseEntity.ok(saved);
    }
}
