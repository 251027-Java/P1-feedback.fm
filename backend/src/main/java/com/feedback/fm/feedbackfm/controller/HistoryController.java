package com.feedback.fm.feedbackfm.controller;

import com.feedback.fm.feedbackfm.dtos.HistoryDTO;
import com.feedback.fm.feedbackfm.service.HistoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:3000")
public class HistoryController {

	private final HistoryService historyService;

	public HistoryController(HistoryService historyService) {
		this.historyService = historyService;
	}

	// Get listening history for a user with optional filters
	@GetMapping
	public ResponseEntity<List<HistoryDTO>> getListeningHistory(
			@RequestParam(required = false) String listenerId, 
			@RequestParam(required = false) Integer limit,
			@RequestParam(required = false) String dateRange, 
			@RequestParam(required = false) String query) {
		if (listenerId != null && !listenerId.isBlank()) {
			List<HistoryDTO> history = historyService.findByListenerId(listenerId);
			if (limit != null && limit > 0) {
				history = history.stream().limit(limit).toList();
			}
			return ResponseEntity.ok(history);
		}
		// If no listenerId, return empty list (or could return all with limit)
		List<HistoryDTO> allHistory = historyService.getAllHistory();
		if (limit != null && limit > 0) {
			allHistory = allHistory.stream().limit(limit).toList();
		}
		return ResponseEntity.ok(allHistory);
	}

	// Get recently played songs
	@GetMapping("/recent")
	public ResponseEntity<List<HistoryDTO>> getRecentlyPlayed(@RequestParam String listenerId, @RequestParam(defaultValue = "20") int limit) {
		return ResponseEntity.ok(historyService.getRecentHistoryByListener(listenerId, limit));
	}

	// Add a listening history record
	@PostMapping
	public ResponseEntity<HistoryDTO> addHistoryRecord(@RequestBody HistoryDTO historyDTO) {
		HistoryDTO created = historyService.create(historyDTO);
		return ResponseEntity.status(201).body(created);
	}

	// Delete a listening history record
	@DeleteMapping("/{historyId}")
	public ResponseEntity<Void> deleteHistoryRecord(@PathVariable Long historyId) {
		historyService.delete(historyId);
		return ResponseEntity.noContent().build();
	}

	// Get listening statistics for a user
	@GetMapping("/stats")
	public ResponseEntity<Map<String, Object>> getHistoryStats(@RequestParam String listenerId) {
		List<HistoryDTO> history = historyService.findByListenerId(listenerId);
		Map<String, Object> stats = Map.of(
			"totalSongsPlayed", history.size(),
			"totalListeningTime", "0 hours",
			"topGenre", "N/A",
			"currentStreak", 0
		);
		return ResponseEntity.ok(stats);
	}
}
