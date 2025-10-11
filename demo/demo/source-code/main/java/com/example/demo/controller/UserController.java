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
        
        // Ensure default tracking fields are set if null (CRITICAL for JPA)
        if (newUser.getRegisteredEvents() == null) {
            newUser.setRegisteredEvents(Collections.emptyList());
        }
        // Set Joined Date (Used by the frontend's local storage simulation on first sign-up)
        newUser.setJoinedDate(Instant.now().toString());
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
            
            // Simple password check
            if (user.getPassword().equals(loginAttempt.getPassword())) {
                
                // Update last login time and save to database
                user.setLastLogin(Instant.now().toString());
                userRepository.save(user);
                
                return ResponseEntity.ok(user); // 200 OK, returns user data
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    // 3. Endpoint to fetch ALL users (GET /api/users - For Admin Panel)
    @GetMapping
    public List<User> getAllUsers() {
        // This fetches all users from the live PostgreSQL database for the admin panel
        return userRepository.findAll();
    }
}
