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
import com.notebook.service.AuditLogService;
import com.notebook.entity.AuditLog;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final NotebookService notebookService;
    private final AuditLogService auditLogService;

    @Autowired
    public AdminController(UserService userService, NotebookService notebookService, AuditLogService auditLogService) {
        this.userService = userService;
        this.notebookService = notebookService;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(value = "q", required = false) String q, Model model) {
        List<User> users = userService.searchUsers(q).stream()
                 .filter(u -> u.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN")))
                 .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalNotes", notebookService.findAllNotebooks().size());
        model.addAttribute("recentLogs", auditLogService.getRecentLogs());
        model.addAttribute("q", q != null ? q : "");
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
        auditLogService.logAction("DELETE_USER", "ADMIN", "Deleted user ID: " + id);
        return "redirect:/admin/dashboard?deleted";
    }

    @GetMapping("/user/lock/{id}")
    public String lockUser(@PathVariable("id") Long id) {
        userService.toggleLock(id);
        auditLogService.logAction("TOGGLE_LOCK", "ADMIN", "Toggled lock for user ID: " + id);
        return "redirect:/admin/dashboard?updated";
    }

    @GetMapping("/notebooks/delete/{id}")
    public String adminDeleteNotebook(@PathVariable("id") Long id) {
        notebookService.forceDeleteNotebook(id);
        auditLogService.logAction("DELETE_NOTEBOOK", "ADMIN", "Hard deleted notebook ID: " + id);
        return "redirect:/admin/notebooks?deleted";
    }
}
