package com.feedback.listener.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listeners")
@Data
public class Listener {
    @Id
    @Column(name = "listener_id", length = 128)
    private String listenerId; // Spotify ID or generated

    private String displayName;
    private String email;
    private String country;
    private String href;

    private Long totalListeningTimeMs;
    private Integer totalSongsPlayed;
}
