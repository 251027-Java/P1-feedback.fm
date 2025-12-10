package com.feedback.fm.feedbackfm.service;

import com.feedback.fm.feedbackfm.dtos.ListenerDTO;
import com.feedback.fm.feedbackfm.model.Listener;
import com.feedback.fm.feedbackfm.repository.ListenerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ListenerServiceImpl implements ListenerService {

    private final ListenerRepository repository;

    public ListenerServiceImpl(ListenerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ListenerDTO> getAllListeners() {
        return repository.findAll().stream()
                .map(this::listenerToDto)
                .toList();
    }

    @Override
    public Optional<ListenerDTO> getById(String id) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Listener ID cannot be null or blank");
        }
        return repository.findById(id)
                .map(this::listenerToDto);
    }

    @Override
    public List<ListenerDTO> findByDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return List.of();
        }
        return repository.findByDisplayName(displayName).stream()
                .map(this::listenerToDto)
                .toList();
    }

    @Override
    public List<ListenerDTO> searchByDisplayName(String namePart) {
        if (namePart == null || namePart.isBlank()) {
            return List.of();
        }
        return repository.findByDisplayNameContainingIgnoreCase(namePart).stream()
                .map(this::listenerToDto)
                .toList();
    }

    @Override
    public Optional<ListenerDTO> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Email cannot be null or blank");
        }
        Listener listener = repository.findByEmail(email);
        return listener != null ? Optional.of(listenerToDto(listener)) : Optional.empty();
    }

    @Override
    @Transactional
    public ListenerDTO create(ListenerDTO dto) {
        validateListenerDTO(dto);
        
        // check existing listener
        if (repository.existsById(dto.listenerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Listener with ID '" + dto.listenerId() + "' already exists");
        }
        
        // check existing email
        if (dto.email() != null && !dto.email().isBlank()) {
            Listener existingListener = repository.findByEmail(dto.email());
            if (existingListener != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Email '" + dto.email() + "' is already registered");
            }
        }
        
        Listener listener = new Listener(
                dto.listenerId(),
                dto.displayName(),
                dto.email(),
                dto.country(),
                dto.href()
        );
        return listenerToDto(repository.save(listener));
    }

    @Override
    @Transactional
    public ListenerDTO update(String id, ListenerDTO dto) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Listener ID cannot be null or blank");
        }
        
        Listener listener = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Listener not found with id: " + id));

        validateListenerDTO(dto);
        
        // check existing email
        if (dto.email() != null && !dto.email().isBlank() 
                && !dto.email().equals(listener.getEmail())) {
            Listener existingListener = repository.findByEmail(dto.email());
            if (existingListener != null && !existingListener.getListenerId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Email '" + dto.email() + "' is already registered to another listener");
            }
        }
        
        listener.setDisplayName(dto.displayName());
        listener.setEmail(dto.email());
        listener.setCountry(dto.country());
        listener.setHref(dto.href());

        return listenerToDto(repository.save(listener));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Listener ID cannot be null or blank");
        }
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Listener not found with id: " + id);
        }
        repository.deleteById(id);
    }
    
    private void validateListenerDTO(ListenerDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Listener data cannot be null");
        }
        
        if (dto.listenerId() == null || dto.listenerId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Listener ID is required");
        }
        
        // validate email format
        if (dto.email() != null && !dto.email().isBlank()) {
            if (!isValidEmail(dto.email())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Invalid email format: " + dto.email());
            }
        }
        
        // validate country code format
        if (dto.country() != null && !dto.country().isBlank()) {
            if (dto.country().length() > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Country code must be 10 characters or less");
            }
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        // must contain @ and at least one dot after @
        int atIndex = email.indexOf('@');
        return atIndex > 0 && atIndex < email.length() - 1 
                && email.substring(atIndex + 1).contains(".");
    }
    
    private ListenerDTO listenerToDto(Listener listener) {
        return new ListenerDTO(
                listener.getListenerId(),
                listener.getDisplayName(),
                listener.getEmail(),
                listener.getCountry(),
                listener.getHref()
        );
    }
}

