package com.feedback.fm.feedbackfm.service;

import com.feedback.fm.feedbackfm.dtos.ArtistDTO;
import com.feedback.fm.feedbackfm.model.Artist;
import com.feedback.fm.feedbackfm.repository.ArtistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository repository;

    public ArtistServiceImpl(ArtistRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ArtistDTO> getAllArtists() {
        return repository.findAll().stream()
                .map(this::artistToDto)
                .toList();
    }

    @Override
    public Optional<ArtistDTO> getById(String id) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist ID cannot be null or blank");
        }
        return repository.findById(id)
                .map(this::artistToDto);
    }

    @Override
    public List<ArtistDTO> findByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return repository.findByName(name).stream()
                .map(this::artistToDto)
                .toList();
    }

    @Override
    public List<ArtistDTO> searchByName(String namePart) {
        if (namePart == null || namePart.isBlank()) {
            return List.of();
        }
        return repository.findByNameContainingIgnoreCase(namePart).stream()
                .map(this::artistToDto)
                .toList();
    }

    @Override
    @Transactional
    public ArtistDTO create(ArtistDTO dto) {
        validateArtistDTO(dto);
        
        // check existing artist
        if (repository.existsById(dto.artistId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Artist with ID '" + dto.artistId() + "' already exists");
        }
        
        Artist artist = new Artist(
                dto.artistId(),
                dto.name(),
                dto.href()
        );
        return artistToDto(repository.save(artist));
    }

    @Override
    @Transactional
    public ArtistDTO update(String id, ArtistDTO dto) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist ID cannot be null or blank");
        }
        
        Artist artist = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Artist not found with id: " + id));

        validateArtistDTO(dto);
        
        artist.setName(dto.name());
        artist.setHref(dto.href());

        return artistToDto(repository.save(artist));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist ID cannot be null or blank");
        }
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Artist not found with id: " + id);
        }
        repository.deleteById(id);
    }
    
    private void validateArtistDTO(ArtistDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist data cannot be null");
        }
        
        if (dto.artistId() == null || dto.artistId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist ID is required");
        }
        
        if (dto.name() == null || dto.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Artist name is required");
        }
    }

    private ArtistDTO artistToDto(Artist artist) {
        List<String> albumIds = artist.getAlbums().stream()
                .map(album -> album.getAlbumId())
                .collect(Collectors.toList());

        return new ArtistDTO(
                artist.getArtistId(),
                artist.getName(),
                artist.getHref(),
                albumIds
        );
    }
}

