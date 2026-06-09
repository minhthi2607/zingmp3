package com.code.zingmp3.controller;

import com.code.zingmp3.model.User;
import com.code.zingmp3.service.IUserService;
import com.code.zingmp3.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User setupUser() {
        return new User();
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "/users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/users/register";
        }
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/songs";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "rememberName", defaultValue = "") String rememberName, Model model) {
        Cookie cookie = new Cookie("rememberName", rememberName);
        model.addAttribute("usernameRemembered", rememberName);
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "/users/login";
        }
        boolean isLoginSuccess = userService.login(user);
        if (!isLoginSuccess) {
            redirectAttributes.addFlashAttribute("message", "Invalid username or password");
            return "redirect:/";
        }

        Cookie cookie = new Cookie("rememberName", user.getUsername());
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        redirectAttributes.addFlashAttribute("message", "User logged in successfully");
        return "redirect:/songs";
    }


}
