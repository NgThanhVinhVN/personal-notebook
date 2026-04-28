package com.notebook.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("theme")
    public String populateTheme(@CookieValue(value = "theme", defaultValue = "light") String theme) {
        // Only allow valid themes to prevent tampering
        if (!"dark".equals(theme) && !"light".equals(theme)) {
            return "light";
        }
        return theme;
    }
}
