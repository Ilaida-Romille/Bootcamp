package com.pointwest.bootcamp.eventhubri.config;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.BreakSession;
import com.pointwest.bootcamp.eventhubri.model.PresentationSession;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final EventRepository repository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            LocalDateTime now = LocalDateTime.now();

            // ----------------------------------------------------
            // Event 1: Spring Boot Workshop
            // ----------------------------------------------------
            Event event1 = new Event();
            event1.setEventId("EVT-1001");
            event1.setTitle("Spring Boot Workshop");
            event1.setDescription("Hands-on guide to REST APIs");
            event1.setOrganizerId("ORG-01");
            event1.setOrganizerName("Pointwest Academy");
            event1.setStatus("UPCOMING");
            event1.setStartDateTime(now.plusDays(5));
            event1.setEndDateTime(now.plusDays(5).plusHours(6));
            event1.setVenue("Room 101");
            event1.setCapacity("50");

            // 1. Presentation Session
            PresentationSession session1 = new PresentationSession();
            session1.setAgendaItemId("ITEM-01");
            session1.setTitle("Keynote: Spring Boot Architecture");
            session1.setDescription("Introduction to IoC, DI, and Auto-Configuration");
            session1.setLocation("Room 101");
            session1.setStartDateTime(now.plusDays(5).plusHours(1));
            session1.setEndDateTime(now.plusDays(5).plusHours(2));
            session1.setSpeaker("Romille Ilaida");

            // 2. Break Session
            BreakSession break1 = new BreakSession();
            break1.setAgendaItemId("ITEM-02");
            break1.setTitle("Coffee & Networking Break");
            break1.setDescription("Short refreshment break");
            break1.setLocation("Cafeteria");
            break1.setStartDateTime(now.plusDays(5).plusHours(2));
            break1.setEndDateTime(now.plusDays(5).plusHours(2).plusMinutes(30));
            break1.setBreakType("COFFEE_BREAK");

            // Build Agenda for Event 1
            Agenda agenda1 = new Agenda();
            agenda1.setDescription("Full-day technical workshop schedule");
            agenda1.getSessions().add(session1);
            agenda1.getSessions().add(break1);

            // Link Agenda to Event
            event1.setAgenda(agenda1);


            // ----------------------------------------------------
            // Event 2: Java 21 Deep Dive
            // ----------------------------------------------------
            Event event2 = new Event();
            event2.setEventId("EVT-1002");
            event2.setTitle("Java 21 Deep Dive");
            event2.setDescription("Exploring Virtual Threads and Pattern Matching");
            event2.setOrganizerId("ORG-01");
            event2.setOrganizerName("Pointwest Academy");
            event2.setStatus("UPCOMING");
            event2.setStartDateTime(now.plusDays(12));
            event2.setEndDateTime(now.plusDays(12).plusHours(4));
            event2.setVenue("Auditorium B");
            event2.setCapacity("120");

            // Presentation Session for Event 2
            PresentationSession session2 = new PresentationSession();
            session2.setAgendaItemId("ITEM-03");
            session2.setTitle("Virtual Threads & Concurrency");
            session2.setDescription("High-throughput lightweight threads in Project Loom");
            session2.setLocation("Auditorium B");
            session2.setStartDateTime(now.plusDays(12).plusHours(1));
            session2.setEndDateTime(now.plusDays(12).plusHours(3));
            session2.setSpeaker("John Smith");

            // Build Agenda for Event 2
            Agenda agenda2 = new Agenda();
            agenda2.setDescription("Deep dive technical session schedule");
            agenda2.getSessions().add(session2);

            // Link Agenda to Event
            event2.setAgenda(agenda2);


            // Save Events (CascadeType.ALL saves Agenda and Sessions automatically)
            repository.save(event1);
            repository.save(event2);

            System.out.println("✅ Sample events with Presentation and Break sessions initialized successfully!");
        };
    }
}