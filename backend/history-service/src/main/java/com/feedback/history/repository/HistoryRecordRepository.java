package com.feedback.history.repository;

import com.feedback.history.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Long> {
    List<HistoryRecord> findByListenerIdOrderByPlayedAtDesc(String listenerId);
}