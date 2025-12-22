package com.feedback.listener.repository;

import com.feedback.listener.model.ListenerStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListenerStatsRepository extends JpaRepository<ListenerStats, String> {
}
