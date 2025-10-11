package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") 
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Endpoint for User Registration (POST /api/users/signup)
    @PostMapping("/signup")
    public ResponseEntity<User> signupUser(@RequestBody User newUser) {
        if (userRepository.existsById(newUser.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
        }
        
        // Ensure default tracking fields are set
        if (newUser.getRegisteredEvents() == null) {
            newUser.setRegisteredEvents(Collections.emptyList());
        }
        // Set Joined Date
        if (newUser.getJoinedDate() == null) {
            newUser.setJoinedDate(Instant.now().toString());
        }
        // Set Initial Last Login
        newUser.setLastLogin(Instant.now().toString());

        User savedUser = userRepository.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 201 Created
    }

    // 2. Endpoint for User Login (POST /api/users/login)
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginAttempt) {
        Optional<User> userOptional = userRepository.findById(loginAttempt.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Simple password check (This validates the user against the DB)
            if (user.getPassword().equals(loginAttempt.getPassword())) {
                
                // Update last login time and save to database
                user.setLastLogin(Instant.now().toString());
                userRepository.save(user);
                
                return ResponseEntity.ok(user); // 200 OK, returns user data
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized (Invalid credentials)
    }

    // 3. Endpoint to fetch ALL users (GET /api/users - CRITICAL for Admin Panel)
    @GetMapping
    public List<User> getAllUsers() {
        // This fetches all users from the live PostgreSQL database for the admin panel
        return userRepository.findAll();
    }
    
    // 4. Endpoint to fetch a single user's registrations (GET /api/users/{username}/registrations - CRITICAL for home.html)
    @GetMapping("/{username}/registrations")
    public ResponseEntity<List<Long>> getUserRegistrations(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findById(username);
        
        if (userOptional.isPresent()) {
            // Return the list of registered event IDs
            return ResponseEntity.ok(userOptional.get().getRegisteredEvents());
        }
        // Return empty list if user not found (safe default)
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }
}
