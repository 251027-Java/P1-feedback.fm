package com.feedback.history.repository;

import com.feedback.history.model.StatsAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsAggregateRepository extends JpaRepository<StatsAggregate, String> {
}
