package com.notebook.service;

import com.notebook.dto.NotebookDto;
import com.notebook.entity.Category;
import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import com.notebook.repository.CategoryRepository;
import com.notebook.repository.NotebookRepository;
import com.notebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public NotebookService(NotebookRepository notebookRepository,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.notebookRepository = notebookRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // ── Save (create / update) ─────────────────────────────────────

    @Transactional
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
        notebook.setUser(user);

        // Chỉ override pinned khi edit (không reset khi user chỉnh sửa nội dung)
        if (dto.getId() == null) {
            notebook.setPinned(false);
        }

        // Resolve categories: tìm hoặc tạo mới
        Set<Category> resolvedCategories = resolveOrCreateCategories(dto.getCategoryNames());
        notebook.setCategories(resolvedCategories);

        // Auto-compute color từ category đầu tiên
        notebook.setColor(resolveColorFromCategories(resolvedCategories));

        notebookRepository.save(notebook);
    }

    /**
     * Quick-add: tạo note chỉ với tiêu đề, gán category mặc định "Cá nhân".
     */
    @Transactional
    public void quickAddNotebook(String title, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notebook notebook = new Notebook();
        notebook.setTitle(title.trim());
        notebook.setContent("");
        notebook.setPinned(false);
        notebook.setUser(user);

        Set<Category> cats = resolveOrCreateCategories(List.of("Cá nhân"));
        notebook.setCategories(cats);
        notebook.setColor(resolveColorFromCategories(cats));

        notebookRepository.save(notebook);
    }

    // ── Search ─────────────────────────────────────────────────────

    /**
     * Smart search với keyword, category filter và sort mode.
     * sortBy: "recent" (updatedAt desc, mặc định) | "az" | "newest" | "oldest"
     */
    public List<Notebook> searchNotebooks(String username, String keyword,
                                          String category, String sortBy) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return List.of();

        String kw  = (keyword  == null || keyword.isBlank())  ? "" : keyword.trim();
        String cat = (category == null || category.isBlank()) ? "" : category.trim();

        return switch (sortBy == null ? "recent" : sortBy.toLowerCase()) {
            case "az"     -> notebookRepository.searchNotebooksAlphabetically(user, kw, cat);
            case "newest" -> notebookRepository.searchNotebooksNewest(user, kw, cat);
            case "oldest" -> notebookRepository.searchNotebooksOldest(user, kw, cat);
            default       -> notebookRepository.searchNotebooks(user, kw, cat);
        };
    }

    public List<Notebook> findNotebooksByUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null
                ? notebookRepository.findByUserOrderByPinnedDescUpdatedAtDesc(user)
                : List.of();
    }

    // ── Category helpers ────────────────────────────────────────────

    /**
     * Trả về tất cả category theo thứ tự A→Z (cho form dropdown / sidebar).
     */
    public List<Category> findAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    /**
     * Xoá category khỏi tất cả notebook của user hiện tại.
     * Logic:
     *  1. Tìm tất cả notebook của user có chứa category này
     *  2. Remove liên kết (notebook_categories) — KHÔNG xoá notebook
     *  3. Nếu category không còn được gắn vào notebook nào nữa → xoá category entity
     *
     * @return true nếu thành công, false nếu category không tồn tại
     */
    @Transactional
    public boolean deleteCategoryFromUser(Long categoryId, String username) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return false;

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return false;

        // Lấy tất cả notebook của user
        List<Notebook> userNotebooks = notebookRepository.findByUserOrderByUpdatedAtDesc(user);

        // Bước 1: Remove category khỏi notebook → xóa hàng trong notebook_categories
        for (Notebook nb : userNotebooks) {
            boolean existed = nb.getCategories().removeIf(c -> c.getId().equals(categoryId));
            if (existed) {
                notebookRepository.save(nb);
            }
        }

        // Bước 2: Nếu không còn notebook nào (từ bất kỳ user nào) gắn category → xoá entity
        long remaining = notebookRepository.countNotebooksByCategory(categoryId);
        if (remaining == 0) {
            categoryRepository.deleteById(categoryId);
        }

        return true;
    }

    /**
     * Với mỗi tên trong danh sách:
     *  - Tìm Category tồn tại theo name → dùng lại
     *  - Chưa tồn tại → tạo mới với màu tự động
     */
    @Transactional
    public Set<Category> resolveOrCreateCategories(List<String> names) {
        Set<Category> result = new HashSet<>();
        if (names == null) return result;

        for (String rawName : names) {
            String name = rawName == null ? "" : rawName.trim();
            if (name.isEmpty()) continue;

            Category cat = categoryRepository.findByName(name)
                    .orElseGet(() -> {
                        Category newCat = new Category(name, resolveCategoryColor(name));
                        return categoryRepository.save(newCat);
                    });
            result.add(cat);
        }
        return result;
    }

    // ── Pin ────────────────────────────────────────────────────────

    @Transactional
    public boolean togglePin(Long id, String username) {
        Notebook notebook = notebookRepository.findById(id).orElse(null);
        if (notebook == null || !notebook.getUser().getUsername().equals(username)) {
            return false;
        }
        notebook.setPinned(!notebook.isPinned());
        notebookRepository.save(notebook);
        return notebook.isPinned();
    }

    // ── Admin / general ────────────────────────────────────────────

    public List<Notebook> findAllNotebooks() {
        return notebookRepository.findAll();
    }

    public Notebook getNotebookById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteNotebook(Long id) {
        notebookRepository.deleteById(id);
    }

    // ── Private helpers ────────────────────────────────────────────

    /**
     * Lấy màu cho category đầu tiên trong Set (không có thứ tự cố định —
     * với các category đã biết dùng màu cố định, còn lại dùng indigo).
     */
    private String resolveColorFromCategories(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) return "#6366f1";
        return categories.iterator().next().getColor();
    }

    /** Map category name nổi tiếng → màu cố định; tên tự do → indigo. */
    private String resolveCategoryColor(String name) {
        if (name == null) return "#6366f1";
        return switch (name) {
            case "Công việc" -> "#3b82f6";
            case "Học tập"   -> "#f59e0b";
            case "Sức khỏe"  -> "#a855f7";
            case "Cá nhân"   -> "#22c55e";
            case "Khẩn cấp"  -> "#ef4444";
            case "Khác"      -> "#64748b";
            default          -> "#6366f1";
        };
    }
}
