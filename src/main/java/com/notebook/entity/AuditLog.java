package com.notebook.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String actionName;
    
    @Column(nullable = false)
    private String username;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
    
    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditLog(String actionName, String username, String details) {
        this.actionName = actionName;
        this.username = username;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
