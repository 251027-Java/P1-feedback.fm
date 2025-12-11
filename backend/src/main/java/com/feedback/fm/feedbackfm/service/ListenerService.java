package com.feedback.fm.feedbackfm.service;

import com.feedback.fm.feedbackfm.dtos.ListenerDTO;
import java.util.List;
import java.util.Optional;

public interface ListenerService {
    List<ListenerDTO> getAllListeners();
    Optional<ListenerDTO> getById(String id);
    List<ListenerDTO> findByDisplayName(String displayName);
    List<ListenerDTO> searchByDisplayName(String namePart);
    Optional<ListenerDTO> findByEmail(String email);
    ListenerDTO create(ListenerDTO dto);
    ListenerDTO update(String id, ListenerDTO dto);
    void delete(String id);
}
