package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // 🛑 Import required annotation

import com.example.demo.model.Event;

@Repository // 🛑 Recommended: Explicitly mark this interface as a Spring repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // No code needed here! Spring handles all the database logic.
}