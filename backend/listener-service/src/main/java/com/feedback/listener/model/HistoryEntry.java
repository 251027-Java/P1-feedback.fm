package com.feedback.listener.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "history")
@Data
public class HistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String listenerId;

    private String songId;

    private Instant playedAt;

    private Integer durationMs;
}
