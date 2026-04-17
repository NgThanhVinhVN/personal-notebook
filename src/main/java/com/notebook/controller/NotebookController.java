package com.notebook.controller;

import com.notebook.dto.NotebookDto;
import com.notebook.entity.Notebook;
import com.notebook.service.NotebookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notebook")
public class NotebookController {

    private final NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    /** Main list with optional search/filter/sort */
    @GetMapping
    public String listNotebooks(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false, defaultValue = "recent") String sort,
            Model model) {

        String username = getUsername();
        List<Notebook> notebooks;

        boolean hasFilter = StringUtils.hasText(q) || StringUtils.hasText(category);
        if (hasFilter || !"recent".equals(sort)) {
            notebooks = notebookService.searchNotebooks(username, q, category, sort);
        } else {
            notebooks = notebookService.findNotebooksByUser(username);
        }

        model.addAttribute("notebooks", notebooks);
        model.addAttribute("username", username);
        model.addAttribute("q", q != null ? q : "");
        model.addAttribute("activeCategory", category != null ? category : "");
        model.addAttribute("sort", sort);
        model.addAttribute("totalCount", notebooks.size());
        return "notebook-list";
    }

    /** Create form */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("notebook", new NotebookDto());
        return "notebook-form";
    }

    /** Save (create or update) */
    @PostMapping("/save")
    public String saveNotebook(@Valid @ModelAttribute("notebook") NotebookDto dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("notebook", dto);
            return "notebook-form";
        }
        notebookService.saveNotebook(dto, getUsername());
        return "redirect:/notebook?success";
    }

    /** Edit form */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Notebook notebook = notebookService.getNotebookById(id);
        String username = getUsername();

        if (notebook == null || !notebook.getUser().getUsername().equals(username)) {
            return "redirect:/notebook?error";
        }

        NotebookDto dto = new NotebookDto();
        dto.setId(notebook.getId());
        dto.setTitle(notebook.getTitle());
        dto.setContent(notebook.getContent());
        dto.setCategory(notebook.getCategory());
        dto.setTags(notebook.getTags());
        dto.setColor(notebook.getColor());
        dto.setPinned(notebook.isPinned());

        model.addAttribute("notebook", dto);
        return "notebook-form";
    }

    /** Delete */
    @GetMapping("/delete/{id}")
    public String deleteNotebook(@PathVariable("id") Long id) {
        Notebook notebook = notebookService.getNotebookById(id);
        String username = getUsername();
        if (notebook != null && notebook.getUser().getUsername().equals(username)) {
            notebookService.deleteNotebook(id);
        }
        return "redirect:/notebook?deleted";
    }

    /** Toggle pin — returns JSON for AJAX call */
    @PostMapping("/pin/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> togglePin(@PathVariable("id") Long id) {
        String username = getUsername();
        boolean pinned = notebookService.togglePin(id, username);
        return ResponseEntity.ok(Map.of("pinned", pinned));
    }

    /** Quick-add from the header input bar */
    @PostMapping("/quick-add")
    public String quickAdd(@RequestParam("title") String title) {
        if (StringUtils.hasText(title)) {
            notebookService.quickAddNotebook(title, getUsername());
        }
        return "redirect:/notebook?success";
    }

    // ── helpers ──────────────────────────────────────────────────────────────
    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
