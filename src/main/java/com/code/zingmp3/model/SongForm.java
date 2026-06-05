package com.code.zingmp3.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class SongForm {
    private Long id;

    @NotBlank(message = "Song name cannot be empty")
    private String name;
    @NotBlank(message = "Singer name cannot be empty")
    private String singer;
    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;
    @NotBlank(message = "Lyrics cannot be empty")
    private String lyrics;
    @NotNull(message = "Audio cannot be empty")
    private MultipartFile audio;

    public SongForm() {
    }

    public SongForm(String name, String singer, MultipartFile image, String lyrics, MultipartFile audio) {
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public MultipartFile getAudio() {
        return audio;
    }

    public void setAudio(MultipartFile audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "SongForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", image=" + image +
                ", lyrics='" + lyrics + '\'' +
                ", audio=" + audio +
                '}';
    }
}
