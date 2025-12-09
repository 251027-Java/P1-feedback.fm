package com.feedback.fm.feedbackfm.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @Column(name = "song_id", length = 64)
    private String songId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String href;

    @Column(name = "duration_ms", nullable = false)
    private Integer durationMs;

    @ManyToMany // defining the junction table to represent many to many
    @JoinTable(
        name = "artists_songs",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")    
    )
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany(mappedBy = "songs")
    private Set<Playlist> playlists = new HashSet<>();

    // kenneth added this: many:many for albums and songs
    @ManyToMany
    @JoinTable(
        name = "albums_songs",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private Set<Album> albums = new HashSet<>();

    public Song() {}
    public Song(String songId, String name, Integer durationMs, String href) {
        this.songId = songId;
        this.name = name;
        this.durationMs = durationMs;
        this.href = href;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    // kenneth added this so the new album model can be added to the song model
    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "Songs{" +
                "songId='" + songId + '\'' +
                ", name='" + name + '\'' +
                ", durationMs=" + durationMs +
                '}';
    }
}