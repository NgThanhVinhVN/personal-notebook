package com.notebook.controller;

import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import com.notebook.service.NotebookService;
import com.notebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final NotebookService notebookService;

    @Autowired
    public AdminController(UserService userService, NotebookService notebookService) {
        this.userService = userService;
        this.notebookService = notebookService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers().stream()
                 .filter(u -> u.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN")))
                 .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/notebooks")
    public String viewAllNotebooks(Model model) {
        List<Notebook> notebooks = notebookService.findAllNotebooks();
        model.addAttribute("notebooks", notebooks);
        return "admin-notebooks";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/dashboard?deleted";
    }
}
