package com.feedback.musicmetadata.repository;

import com.feedback.musicmetadata.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(String ownerId);
}
