package com.feedback.history.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stats_aggregate")
@Data
public class StatsAggregate {
    @Id
    private String listenerId;

    private Long totalListeningTimeMs;
    private Integer totalSongsPlayed;
    private Integer currentStreak;
}
