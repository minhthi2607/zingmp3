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
import java.util.Optional;

@Controller
@RequestMapping("/songs")
public class SongController {

    @Value("${file-upload}")
    private String fileUpload;

    private final ISongService songService;

    public SongController(ISongService songService) {
        this.songService = songService;
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
    public String store(@Validated @ModelAttribute("songForm") SongForm songForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/songs/create";
        }
        MultipartFile songImage = songForm.getImage();
        String songNameImage = songImage.getOriginalFilename();
        MultipartFile songAudio = songForm.getAudio();
        String songNameAudio = songAudio.getOriginalFilename();
        try {
            FileCopyUtils.copy(songForm.getImage().getBytes(), new File(fileUpload + songNameImage));
            FileCopyUtils.copy(songForm.getAudio().getBytes(), new File(fileUpload + songNameAudio));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Song song = new Song(songForm.getName(), songForm.getSinger(), songNameImage, songForm.getLyrics(), songNameAudio);
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
    public String update(@ModelAttribute("songForm") SongForm songForm, RedirectAttributes redirectAttributes) {
        Optional<Song> songOptional = songService.findById(songForm.getId());
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            MultipartFile songImage = songForm.getImage();
            String songNameImage = songImage.getOriginalFilename();
            MultipartFile songAudio = songForm.getAudio();
            String songNameAudio = songAudio.getOriginalFilename();

            if (songNameImage != null && !songNameImage.isEmpty()) {
                try {
                    FileCopyUtils.copy(songForm.getImage().getBytes(), new File(fileUpload + songNameImage));
                    song.setImage(songNameImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (songNameAudio != null && !songNameAudio.isEmpty()) {
                try {
                    FileCopyUtils.copy(songForm.getAudio().getBytes(), new File(fileUpload + songNameAudio));
                    song.setAudio(songNameAudio);
                } catch (IOException e) {
                    e.printStackTrace();
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
