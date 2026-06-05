package com.code.zingmp3.controller;

import com.code.zingmp3.model.Song;
import com.code.zingmp3.model.SongForm;
import com.code.zingmp3.service.ISongService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/songs")
public class SongController {

    @Value("${file-upload}")
    private String fileUpload;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg", "audio/mp3", "audio/ogg", "audio/wav", "audio/x-wav", "audio/flac"
    );

    private final ISongService songService;

    public SongController(ISongService songService) {
        this.songService = songService;
    }

    private String generateSafeFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        return UUID.randomUUID().toString() + extension;
    }

    private boolean isValidFileType(MultipartFile file, List<String> allowedTypes) {
        String contentType = file.getContentType();
        return contentType != null && allowedTypes.contains(contentType);
    }

    private void saveFile(MultipartFile file, String savedName) throws IOException {
        File dest = new File(fileUpload + savedName);
        FileCopyUtils.copy(file.getBytes(), dest);
    }

    @GetMapping
    public String index(@RequestParam(value = "page", defaultValue = "0") int page,
                        Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Song> songs = songService.findAll(pageable);
        model.addAttribute("songs", songs);
        return "/songs/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("songForm", new SongForm());
        return "/songs/create";
    }

    @PostMapping("/create")
    public String store(@Validated @ModelAttribute("songForm") SongForm songForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/songs/create";
        }

        MultipartFile songImage = songForm.getImage();
        MultipartFile songAudio = songForm.getAudio();

        if (!isValidFileType(songImage, ALLOWED_IMAGE_TYPES)) {
            model.addAttribute("imageError", "Chi chap nhan file anh: JPG, PNG, GIF, WEBP");
            return "/songs/create";
        }
        if (!isValidFileType(songAudio, ALLOWED_AUDIO_TYPES)) {
            model.addAttribute("audioError", "Chi chap nhan file audio: MP3, OGG, WAV, FLAC");
            return "/songs/create";
        }

        String savedImageName = generateSafeFileName(songImage.getOriginalFilename());
        String savedAudioName = generateSafeFileName(songAudio.getOriginalFilename());

        try {
            saveFile(songImage, savedImageName);
            saveFile(songAudio, savedAudioName);
        } catch (IOException e) {
            model.addAttribute("uploadError", "Loi khi upload file, vui long thu lai.");
            return "/songs/create";
        }

        Song song = new Song(songForm.getName(), songForm.getSinger(), savedImageName, songForm.getLyrics(), savedAudioName);
        songService.save(song);
        redirectAttributes.addFlashAttribute("message", "Song created successfully");
        return "redirect:/songs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Optional<Song> song = songService.findById(id);
        if (song.isPresent()) {
            Song s = song.get();
            SongForm songForm = new SongForm(s.getName(), s.getSinger(), null, s.getLyrics(), null);
            songForm.setId(s.getId());
            model.addAttribute("songForm", songForm);
            model.addAttribute("song", s);
            return "/songs/edit";
        }
        return "redirect:/songs";
    }

    @PostMapping("/update")
    public String update(@Validated @ModelAttribute("songForm") SongForm songForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/songs/edit";
        }

        Optional<Song> songOptional = songService.findById(songForm.getId());
        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            MultipartFile songImage = songForm.getImage();
            if (songImage != null && !songImage.isEmpty()) {
                if (!isValidFileType(songImage, ALLOWED_IMAGE_TYPES)) {
                    model.addAttribute("imageError", "Chi chap nhan file anh: JPG, PNG, GIF, WEBP");
                    model.addAttribute("song", song);
                    return "/songs/edit";
                }
                String savedImageName = generateSafeFileName(songImage.getOriginalFilename());
                try {
                    saveFile(songImage, savedImageName);
                    song.setImage(savedImageName);
                } catch (IOException e) {
                    model.addAttribute("uploadError", "Loi khi upload anh, vui long thu lai.");
                    model.addAttribute("song", song);
                    return "/songs/edit";
                }
            }

            MultipartFile songAudio = songForm.getAudio();
            if (songAudio != null && !songAudio.isEmpty()) {
                if (!isValidFileType(songAudio, ALLOWED_AUDIO_TYPES)) {
                    model.addAttribute("audioError", "Chi chap nhan file audio: MP3, OGG, WAV, FLAC");
                    model.addAttribute("song", song);
                    return "/songs/edit";
                }
                String savedAudioName = generateSafeFileName(songAudio.getOriginalFilename());
                try {
                    saveFile(songAudio, savedAudioName);
                    song.setAudio(savedAudioName);
                } catch (IOException e) {
                    model.addAttribute("uploadError", "Loi khi upload audio, vui long thu lai.");
                    model.addAttribute("song", song);
                    return "/songs/edit";
                }
            }

            song.setName(songForm.getName());
            song.setSinger(songForm.getSinger());
            song.setLyrics(songForm.getLyrics());
            songService.save(song);
            redirectAttributes.addFlashAttribute("message", "Song updated successfully");
        }
        return "redirect:/songs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        songService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Song deleted successfully");
        return "redirect:/songs";
    }
}
