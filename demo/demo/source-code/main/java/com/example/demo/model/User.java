package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;

    private String password;
    private String semester;
    
    // CRITICAL FIX: Add tracking fields (used by frontend and controller)
    private String joinedDate;
    private String lastLogin;
    private String lastLogout;

    @ElementCollection
    private List<Long> registeredEvents;

    public User() {}

    // --- Getters and Setters (The methods the controller is looking for) ---
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // FIX: Getters/Setters for JoinedDate
    public String getJoinedDate() { return joinedDate; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }

    // FIX: Getters/Setters for LastLogin
    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    // FIX: Getters/Setters for LastLogout (used during logout simulation)
    public String getLastLogout() { return lastLogout; }
    public void setLastLogout(String lastLogout) { this.lastLogout = lastLogout; }
    
    // Remaining methods (omitted for brevity, but include all others: semester, registeredEvents, etc.)
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public List<Long> getRegisteredEvents() { return registeredEvents; }
    public void setRegisteredEvents(List<Long> registeredEvents) { this.registeredEvents = registeredEvents; }
}
