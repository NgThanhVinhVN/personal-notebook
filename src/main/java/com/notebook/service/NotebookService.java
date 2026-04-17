package com.notebook.service;

import com.notebook.dto.NotebookDto;
import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import com.notebook.repository.NotebookRepository;
import com.notebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotebookService(NotebookRepository notebookRepository, UserRepository userRepository) {
        this.notebookRepository = notebookRepository;
        this.userRepository = userRepository;
    }

    public void saveNotebook(NotebookDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notebook notebook;
        if (dto.getId() != null) {
            notebook = notebookRepository.findById(dto.getId()).orElse(new Notebook());
        } else {
            notebook = new Notebook();
        }

        notebook.setTitle(dto.getTitle());
        notebook.setContent(dto.getContent());
        notebook.setCategory(dto.getCategory());
        notebook.setTags(dto.getTags() != null ? dto.getTags() : "");
        notebook.setColor(dto.getColor() != null ? dto.getColor() : resolveCategoryColor(dto.getCategory()));
        // Don't override pinned state on edit unless DTO carries it explicitly
        if (dto.getId() == null) {
            notebook.setPinned(false);
        }
        notebook.setUser(user);

        notebookRepository.save(notebook);
    }

    /**
     * Quick-add: create a note with just a title, default category "Cá nhân".
     */
    public void quickAddNotebook(String title, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notebook notebook = new Notebook();
        notebook.setTitle(title.trim());
        notebook.setContent("");
        notebook.setCategory("Cá nhân");
        notebook.setTags("Cá nhân");
        notebook.setColor(resolveCategoryColor("Cá nhân"));
        notebook.setPinned(false);
        notebook.setUser(user);

        notebookRepository.save(notebook);
    }

    /**
     * Smart search with optional keyword, category filter and sort mode.
     * sortBy: "recent" (default) | "az"
     */
    public List<Notebook> searchNotebooks(String username, String keyword, String category, String sortBy) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return List.of();

        String kw = (keyword == null || keyword.isBlank()) ? "" : keyword.trim();
        String cat = (category == null || category.isBlank()) ? "" : category.trim();

        if ("az".equalsIgnoreCase(sortBy)) {
            return notebookRepository.searchNotebooksAlphabetically(user, kw, cat);
        }
        return notebookRepository.searchNotebooks(user, kw, cat);
    }

    public List<Notebook> findNotebooksByUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return notebookRepository.findByUserOrderByPinnedDescUpdatedAtDesc(user);
        }
        return List.of();
    }

    /**
     * Toggle pin status. Returns the new pinned state.
     */
    public boolean togglePin(Long id, String username) {
        Notebook notebook = notebookRepository.findById(id).orElse(null);
        if (notebook == null || !notebook.getUser().getUsername().equals(username)) {
            return false;
        }
        notebook.setPinned(!notebook.isPinned());
        notebookRepository.save(notebook);
        return notebook.isPinned();
    }

    public List<Notebook> findAllNotebooks() {
        return notebookRepository.findAll();
    }

    public Notebook getNotebookById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    public void deleteNotebook(Long id) {
        notebookRepository.deleteById(id);
    }

    /** Map category to a color hex. */
    private String resolveCategoryColor(String category) {
        if (category == null) return "#64748b";
        return switch (category) {
            case "Công việc"  -> "#3b82f6";
            case "Học tập"    -> "#f59e0b";
            case "Sức khỏe"  -> "#a855f7";
            case "Cá nhân"   -> "#22c55e";
            case "Khẩn cấp"  -> "#ef4444";
            default           -> "#64748b";
        };
    }
}
