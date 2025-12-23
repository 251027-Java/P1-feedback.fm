package com.feedback.listener.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listener_stats")
@Data
public class ListenerStats {
    @Id
    private String listenerId;

    private Long totalListeningTimeMs;
    private Integer totalSongsPlayed;
    private Integer currentStreak;
}
