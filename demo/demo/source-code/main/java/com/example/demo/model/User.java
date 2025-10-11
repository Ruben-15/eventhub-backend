package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Use a specific table name for Postgres
public class User {

    @Id
    private String username;

    private String password;
    private String semester;
    private String joinedDate;
    private String lastLogin;
    private String lastLogout;

    // CRITICAL: This list will store the IDs of events the user registers for
    @ElementCollection
    private List<Long> registeredEvents;

    public User() {}

    // --- You must manually add Getters and Setters for all fields here ---
    // (Include methods for username, password, semester, and registeredEvents)
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<Long> getRegisteredEvents() { return registeredEvents; }
    public void setRegisteredEvents(List<Long> registeredEvents) { this.registeredEvents = registeredEvents; }

    // (Add the remaining getters/setters for semester, joinedDate, etc.)
}
