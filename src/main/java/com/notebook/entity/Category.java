package com.notebook.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Màu hex hiển thị trong UI (tự động gán dựa vào tên khi tạo) */
    @Column(length = 20)
    private String color = "#6366f1";

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Notebook> notebooks = new HashSet<>();

    public Category() {
    }

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // ── Getters & Setters ──────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Notebook> getNotebooks() {
        return notebooks;
    }

    public void setNotebooks(Set<Notebook> notebooks) {
        this.notebooks = notebooks;
    }
}
