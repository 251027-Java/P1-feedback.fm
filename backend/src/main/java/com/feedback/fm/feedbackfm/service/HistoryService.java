package com.feedback.fm.feedbackfm.service;

import com.feedback.fm.feedbackfm.dtos.HistoryDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoryService {
    List<HistoryDTO> getAllHistory();
    Optional<HistoryDTO> getById(Long id);
    List<HistoryDTO> findByListenerId(String listenerId);
    List<HistoryDTO> findBySongId(String songId);
    List<HistoryDTO> findByListenerIdAndSongId(String listenerId, String songId);
    List<HistoryDTO> getRecentHistoryByListener(String listenerId, int limit);
    List<HistoryDTO> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<HistoryDTO> findByListenerIdAndDateRange(String listenerId, LocalDateTime start, LocalDateTime end);
    HistoryDTO create(HistoryDTO dto);
    HistoryDTO update(Long id, HistoryDTO dto);
    void delete(Long id);
}
