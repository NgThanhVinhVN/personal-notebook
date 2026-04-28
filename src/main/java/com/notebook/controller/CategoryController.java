package com.notebook.controller;

import com.notebook.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final NotebookService notebookService;

    @Autowired
    public CategoryController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    /**
     * Xoá category khỏi các notebook của user hiện tại.
     * Nếu category không còn notebook nào → xoá entity.
     * Trả về JSON cho AJAX call từ sidebar.
     *
     * POST /categories/{id}/delete
     */
    @PostMapping("/{id}/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable("id") Long id) {
        String username = getUsername();
        boolean success = notebookService.deleteCategoryFromUser(id, username);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true));
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không tìm thấy danh mục"));
    }

    // ── helper ────────────────────────────────────────────────────
    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
