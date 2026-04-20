package com.notebook.dto;

import java.util.ArrayList;
import java.util.List;

public class NotebookDto {

    private Long id;

    private String title;

    private String content;

    /**
     * Danh sách tên category được chọn từ form (multi-chip selector).
     * Submitted dưới dạng nhiều hidden input cùng name="categoryNames".
     */
    private List<String> categoryNames = new ArrayList<>();

    /** Màu hex — tự động tính phía server, ẩn trong form */
    private String color;

    private boolean pinned;

    // ── Constructors ──────────────────────────────────────────────

    public NotebookDto() {
    }

    // ── Getters & Setters ─────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames != null ? categoryNames : new ArrayList<>();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
