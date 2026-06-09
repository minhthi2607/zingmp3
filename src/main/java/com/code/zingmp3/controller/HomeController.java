package com.code.zingmp3.controller;

import com.code.zingmp3.model.Song;
import com.code.zingmp3.model.User;
import com.code.zingmp3.service.ISongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("")
@SessionAttributes("user")
public class HomeController {
    private final ISongService songService;

    @ModelAttribute("user")
    public User setupUser() {
        return new User();
    }

    public HomeController(ISongService songService) {
        this.songService = songService;
    }

    @GetMapping(value = {"", "/"})
    public String index(Model model) {
        Iterable<Song> songs = songService.findAll();
        model.addAttribute("songs", songs);
        return "/index";
    }
}
