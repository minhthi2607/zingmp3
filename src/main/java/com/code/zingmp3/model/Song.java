package com.code.zingmp3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Song name cannot be empty")
    private String name;

    @NotBlank(message = "Singer name cannot be empty")
    private String singer;

    // Stored as UUID-based safe filename - not validated with @NotBlank
    // because it is set programmatically after file upload, not from form input
    private String image;

    @NotBlank(message = "Lyrics cannot be empty")
    @Column(columnDefinition = "TEXT")
    private String lyrics;

    // Stored as UUID-based safe filename - same rationale as image
    private String audio;

    public Song() {
    }

    public Song(String name, String singer, String image, String lyrics, String audio) {
        this.name = name;
        this.singer = singer;
        this.image = image;
        this.lyrics = lyrics;
        this.audio = audio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", image='" + image + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }
}
