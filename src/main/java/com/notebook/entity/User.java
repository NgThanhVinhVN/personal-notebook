package com.notebook.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notebook> notebooks = new ArrayList<>();

<<<<<<< HEAD
    public User() {}

    public User(Long id, String username, String password, String fullName, String email, List<Role> roles, List<Notebook> notebooks) {
=======
    // ── Constructors ──────────────────────────────────────────
    public User() {}

    public User(Long id, String username, String password, String fullName,
                String email, List<Role> roles, List<Notebook> notebooks) {
>>>>>>> 6d61c47 (Ket noi sang MySQL)
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
        this.notebooks = notebooks;
    }

<<<<<<< HEAD
=======
    // ── Getters & Setters ─────────────────────────────────────
>>>>>>> 6d61c47 (Ket noi sang MySQL)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }

    public List<Notebook> getNotebooks() { return notebooks; }
    public void setNotebooks(List<Notebook> notebooks) { this.notebooks = notebooks; }
}
