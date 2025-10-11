package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            newUser.setRegisteredEvents(java.util.Collections.emptyList());
        }
        if (newUser.getJoinedDate() == null) {
            newUser.setJoinedDate(java.time.Instant.now().toString());
        }

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
                // Update last login time
                user.setLastLogin(java.time.Instant.now().toString());
                userRepository.save(user);
                return ResponseEntity.ok(user); // 200 OK
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    // 3. Endpoint to fetch ALL users (GET /api/users - For Admin Panel)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
