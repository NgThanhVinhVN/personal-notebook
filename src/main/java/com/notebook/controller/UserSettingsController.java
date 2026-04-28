package com.notebook.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.notebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class UserSettingsController {

    private final UserService userService;

    @Autowired
    public UserSettingsController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/theme")
    public ResponseEntity<?> setTheme(@RequestParam("theme") String theme, HttpServletResponse response, Principal principal) {
        if (!"dark".equals(theme) && !"light".equals(theme)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid theme value"));
        }

        if (principal != null) {
            userService.updateTheme(principal.getName(), theme);
        }

        Cookie cookie = new Cookie("theme", theme);
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1 year cookie
        cookie.setPath("/");
        cookie.setHttpOnly(false); // Accessible to client just in case, though we primarily use it server-side mapping
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("success", true, "theme", theme));
    }
}
