package com.notebook.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notebooks")
<<<<<<< HEAD
=======

>>>>>>> 6d61c47 (Ket noi sang MySQL)
public class Notebook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private boolean isArchived = false;
	
	@Column(name = "is_deleted")
	private boolean isDeleted = false;
	
	@Column(name = "is_pinned", nullable = false)
	private boolean isPinned = false;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

<<<<<<< HEAD
    /** Comma-separated tags, e.g. "Công việc,Khẩn cấp" */
    @Column(name = "tags")
    private String tags;

    /** HEX color code representing the primary tag, e.g. "#3b82f6" */
    @Column(name = "color", length = 20)
    private String color;

    /** Whether this note is pinned to the top */
    @Column(name = "is_pinned", columnDefinition = "boolean default false")
    private boolean pinned = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
=======
	@Column(nullable = false)
	private String category;
>>>>>>> 6d61c47 (Ket noi sang MySQL)

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notebook() {}

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
=======
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// ── Constructors ──────────────────────────────────────────
	public Notebook() {
	}

	public Notebook(Long id, String title, String content, String category, LocalDateTime createdAt,
			LocalDateTime updatedAt, User user) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.category = category;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
	}

	// ── Getters & Setters ─────────────────────────────────────
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
>>>>>>> 6d61c47 (Ket noi sang MySQL)
}
