package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events") // <--- CRITICAL FIX: Forces table name for PostgreSQL
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The fields to match the data sent from admin.html
    private String name;
    private String description;
    private LocalDateTime date; 
    private String location;
    private String status;
    private boolean featured;
    
    // ⭐ FIELD FOR IMAGE UPLOAD ⭐
    private String imageUrl; 

    // 🛑 NEW CRITICAL FIELD: Stores comma-separated semesters (e.g., "S1, S3, S5") 🛑
    private String semester; 
    
    // 🛑 FIELD 1 OF 2 ADDED FROM FRONTEND: Max Capacity 🛑
    private int maxCapacity;
    
    // 🛑 FIELD 2 OF 2 ADDED FROM FRONTEND: Requires Registration 🛑
    private boolean requiresRegistration;

    // Required: Default (No-Args) Constructor
    public Event() {} 

    // --- GETTERS AND SETTERS ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // ... (All existing getters/setters for description, date, location, status, featured, imageUrl, semester) ...

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    // 🛑 GETTER AND SETTER FOR MAX CAPACITY 🛑
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    
    // 🛑 GETTER AND SETTER FOR REQUIRES REGISTRATION 🛑
    public boolean isRequiresRegistration() { return requiresRegistration; }
    public void setRequiresRegistration(boolean requiresRegistration) { this.requiresRegistration = requiresRegistration; }
}
