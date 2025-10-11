package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*") // Allows your Vercel frontend to talk to this API
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Handles registration and cancellation requests
    @PostMapping
    public ResponseEntity<String> updateRegistration(
            @RequestParam String username,
            @RequestParam Long eventId,
            @RequestParam(required = false) String action) { // 'register' or 'cancel'

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            // FIX 1: Use .notFound().body(...) for 404 response with a message
            return ResponseEntity.notFound().build(); 
        }

        User user = userOptional.get();
        List<Long> registeredEvents = user.getRegisteredEvents();

        if ("cancel".equalsIgnoreCase(action)) {
            // Remove event ID from the list
            if (registeredEvents.remove(eventId)) {
                userRepository.save(user);
                // FIX 2: Use .ok().body(...) for successful 200 response
                return ResponseEntity.ok().body("Registration cancelled."); 
            }
            // FIX 3: Use .badRequest().body(...) for 400 response
            return ResponseEntity.badRequest().body("Not currently registered.");
        } 
        
        // Default action is to register
        if (!registeredEvents.contains(eventId)) {
            registeredEvents.add(eventId);
            userRepository.save(user);
            // FIX 4: Use .ok().body(...) for successful 200 response
            return ResponseEntity.ok().body("Registration successful.");
        }
        
        // FIX 5: Use .badRequest().body(...) for 400 response
        return ResponseEntity.badRequest().body("Already registered.");
    }
}
