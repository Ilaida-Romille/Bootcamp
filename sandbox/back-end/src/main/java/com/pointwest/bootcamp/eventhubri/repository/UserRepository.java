package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Attendee;
import com.pointwest.bootcamp.eventhubri.model.Organizer;
import com.pointwest.bootcamp.eventhubri.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        // Pre-seeded mock data for testing
        Attendee attendee = new Attendee("U101", "John Doe", "john@example.com", "A101");
        Organizer organizer = new Organizer("U201", "Jane Smith", "jane@corp.com", "O201", "TechCorp");
        this.users.add(attendee);
        this.users.add(organizer);
    }

    public List<User> findAll() {
        return new ArrayList<>(this.users);
    }

    public Optional<User> findById(String userId) {
        return this.users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return this.users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public User save(User user) {
        this.users.removeIf(u -> u.getUserId().equals(user.getUserId()));
        this.users.add(user);
        return user;
    }
}