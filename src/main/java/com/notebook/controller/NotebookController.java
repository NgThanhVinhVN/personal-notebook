package com.notebook.controller;

import com.notebook.dto.NotebookDto;
import com.notebook.entity.Notebook;
import com.notebook.service.NotebookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notebook")
public class NotebookController {

    private final NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @GetMapping
    public String listNotebooks(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Notebook> notebooks = notebookService.findNotebooksByUser(username);
        model.addAttribute("notebooks", notebooks);
        model.addAttribute("username", username);
        return "notebook-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        NotebookDto notebookDto = new NotebookDto();
        model.addAttribute("notebook", notebookDto);
        return "notebook-form";
    }

    @PostMapping("/save")
    public String saveNotebook(@Valid @ModelAttribute("notebook") NotebookDto notebookDto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notebook", notebookDto);
            return "notebook-form";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        notebookService.saveNotebook(notebookDto, username);
        return "redirect:/notebook?success";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Notebook notebook = notebookService.getNotebookById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Security check
        if (notebook == null || !notebook.getUser().getUsername().equals(username)) {
            return "redirect:/notebook?error";
        }

        NotebookDto notebookDto = new NotebookDto();
        notebookDto.setId(notebook.getId());
        notebookDto.setTitle(notebook.getTitle());
        notebookDto.setContent(notebook.getContent());
        notebookDto.setCategory(notebook.getCategory());
        
        model.addAttribute("notebook", notebookDto);
        return "notebook-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteNotebook(@PathVariable("id") Long id) {
        Notebook notebook = notebookService.getNotebookById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Security check
        if (notebook != null && notebook.getUser().getUsername().equals(username)) {
            notebookService.deleteNotebook(id);
        }
        return "redirect:/notebook?deleted";
    }
}
