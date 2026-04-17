package com.notebook.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class NotebookDto {

    private Long id;

    @NotEmpty(message = "Tiêu đề không được để trống")
    private String title;

    @NotEmpty(message = "Nội dung không được để trống")
    private String content;

    @NotEmpty(message = "Danh mục không được để trống")
    private String category;

    /** Comma-separated tag string from the multi-tag selector */
    private String tags;

    /** Primary color hex code selected by the user */
    private String color;

    private boolean pinned;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
}
