package com.example.demo.controller; 

import com.example.demo.model.Event; // Correct Import
import com.example.demo.repository.EventRepository; // Correct Import
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") 
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    // --- Utility to handle image file storage location ---
    private static final String UPLOAD_DIR = "event-images"; 

    // 1. POST (Create)
    @PostMapping
    public Event createEvent(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") String date, 
            @RequestParam("location") String location,
            @RequestParam("status") String status,
            @RequestParam("featured") boolean featured,
            @RequestParam("semester") String semester, 
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setDate(LocalDateTime.parse(date)); 
        event.setLocation(location);
        event.setStatus(status);
        event.setFeatured(featured);
        event.setSemester(semester); 

        // --- Image Saving Logic ---
        if (image != null && !image.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                
                Files.copy(image.getInputStream(), filePath);
                
                // Store the public URL path for the frontend
                event.setImageUrl("/api/events/event-images/" + uniqueFileName);

            } catch (Exception e) {
                System.err.println("Failed to upload image during creation: " + e.getMessage());
            }
        }
        
        return eventRepository.save(event); 
    }

    // 2. PUT (Update/Edit)
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") String date,
            @RequestParam("location") String location,
            @RequestParam("status") String status,
            @RequestParam("featured") boolean featured,
            @RequestParam("semester") String semester, 
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "existingImageUrl", required = false) String existingImageUrl 
            ) {
        
        return eventRepository.findById(id).map(existingEvent -> {
            existingEvent.setName(name);
            existingEvent.setDescription(description);
            existingEvent.setDate(LocalDateTime.parse(date));
            existingEvent.setLocation(location);
            existingEvent.setStatus(status);
            existingEvent.setFeatured(featured);
            existingEvent.setSemester(semester); 

            // --- Image Preservation/Update Logic ---
            if (image != null && !image.isEmpty()) {
                // Save new file logic 
                try {
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    if (!Files.exists(uploadPath)) { Files.createDirectories(uploadPath); }
                    String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(uniqueFileName);
                    Files.copy(image.getInputStream(), filePath);

                    existingEvent.setImageUrl("/api/events/event-images/" + uniqueFileName);
                } catch (Exception e) {
                    System.err.println("Failed to upload new image during update: " + e.getMessage());
                }
            } else if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                existingEvent.setImageUrl(existingImageUrl);
            } else {
                existingEvent.setImageUrl(null);
            }
            
            Event updatedEvent = eventRepository.save(existingEvent);
            return ResponseEntity.ok(updatedEvent);
            
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. GET (Read All)
    @GetMapping 
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 4. GET ENDPOINT TO SERVE IMAGES STATICALLY
    @GetMapping("/event-images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            // Locate the file in the event-images folder
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error serving file: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    // 5. DELETE (Delete by ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}