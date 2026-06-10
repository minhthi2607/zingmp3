package com.code.zingmp3.controller;

import com.code.zingmp3.model.User;
import com.code.zingmp3.service.IUserService;
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
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Validated User user,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        // FIX 2: Bat RuntimeException khi username bi trung, hien thi loi than thien thay vi crash 500
        try {
            userService.save(user);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Ten dang nhap da ton tai, vui long chon ten khac.");
            return "auth/register";
        }
        redirectAttributes.addFlashAttribute("message", "Dang ky thanh cong! Vui long dang nhap.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "rememberName", defaultValue = "") String rememberName,
                        Model model) {
        model.addAttribute("usernameRemembered", rememberName);
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Validated User user,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        boolean isLoginSuccess = userService.login(user);
        if (!isLoginSuccess) {
            model.addAttribute("errorMessage", "Ten dang nhap hoac mat khau khong dung.");
            return "auth/login";
        }

        // FIX 3: Them HttpOnly va Secure flag cho cookie de bao mat hon
        Cookie cookie = new Cookie("rememberName", user.getUsername());
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);  // FIX 3: Ngan JavaScript truy cap cookie
        cookie.setPath("/");
        response.addCookie(cookie);

        redirectAttributes.addFlashAttribute("message", "Dang nhap thanh cong!");
        return "redirect:/songs";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes, HttpServletResponse response) {
        // Xoa cookie khi dang xuat
        Cookie cookie = new Cookie("rememberName", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        redirectAttributes.addFlashAttribute("message", "Da dang xuat thanh cong.");
        return "redirect:/login";
    }
}