package com.feedback.listener.repository;

import com.feedback.listener.model.HistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntry, Long> {
    List<HistoryEntry> findByListenerIdOrderByPlayedAtDesc(String listenerId);
}
