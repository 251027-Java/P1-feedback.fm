package com.feedback.fm.feedbackfm.service;

import com.feedback.fm.feedbackfm.dtos.ArtistDTO;

import java.util.List;
import java.util.Optional;

public interface ArtistService {
    List<ArtistDTO> getAllArtists();
    Optional<ArtistDTO> getById(String id);
    List<ArtistDTO> findByName(String name);
    List<ArtistDTO> searchByName(String namePart);
    ArtistDTO create(ArtistDTO dto);
    ArtistDTO update(String id, ArtistDTO dto);
    void delete(String id);
}
