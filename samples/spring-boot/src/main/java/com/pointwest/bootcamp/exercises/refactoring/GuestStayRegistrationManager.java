package com.pointwest.bootcamp.exercises.refactoring;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.pointwest.bootcamp.hotelservices.model.Guest;
import com.pointwest.bootcamp.hotelservices.model.GuestStay;
import com.pointwest.bootcamp.hotelservices.model.Room;

public class GuestStayRegistrationManager {

    private final Clock clock;
    private final Map<String, GuestStay> stays = new LinkedHashMap<>();
    private final List<SentEmail> sentEmails = new ArrayList<>();
    private final List<AuditEntry> auditEntries = new ArrayList<>();
    private int nextStayNumber = 1;

    private GuestStayRegistrationManager(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    /** Don't change this method */
    public static GuestStayRegistrationManager inMemory(Clock clock) {
        return new GuestStayRegistrationManager(clock);
    }

    /** Don't change this method */
    public static GuestStayRegistrationManager inMemory() {
        return inMemory(Clock.systemDefaultZone());
    }

    /**
     * Registers a guest who is checking in today.
     *
     * @throws IllegalArgumentException when the request is invalid
     * @throws com.hotel.domain.RoomUnavailableException when the room cannot be occupied
     */
    public GuestStay register(Guest guest, Room room, int partySize, LocalDate checkInDate) {
        Objects.requireNonNull(guest, "guest must not be null");
        
        if (guest.getName() == null || guest.getName().isBlank()) {
            throw new IllegalArgumentException("guest name must not be blank");
        }
        
        String email = guest.getContact()
        .map(contact -> contact.email())
        .filter(candidate -> candidate.contains("@"))
        .orElseThrow(() -> new IllegalArgumentException("guest must have a valid email address"));
        
        if (partySize < 1) {
            throw new IllegalArgumentException("party size must be at least 1");
        }
        
        int roomCapacity = switch (room.getRoomType().toLowerCase(Locale.ROOT)) {
            case "standard" -> 2;
            case "deluxe" -> 4;
            case "suite" -> 6;
            case "penthouse" -> 8;
            default -> throw new IllegalArgumentException("unsupported room type: " + room.getRoomType());
        };

        Objects.requireNonNull(checkInDate, "checkInDate must not be null");
        if (partySize > roomCapacity) {
            throw new IllegalArgumentException(
                "party size " + partySize + " exceeds room capacity " + roomCapacity);
        }
            
        LocalDate today = LocalDate.now(clock);
        if (!checkInDate.equals(today)) {
            throw new IllegalArgumentException("check-in date must be today: " + today);
        }
        
        Objects.requireNonNull(room, "room must not be null");
        room.requireAvailable();

        String stayId = "STAY-%04d".formatted(nextStayNumber++);
        GuestStay stay = new GuestStay(stayId, guest, room, partySize, checkInDate);
        stays.put(stayId, stay);

        String subject = "Check-in confirmed: Room " + room.getSpaceId();
        String body = "Welcome, %s. Stay %s is checked in to Room %s."
                .formatted(guest.getName(), stayId, room.getSpaceId());
        sentEmails.add(new SentEmail(email, subject, body));

        String details = "%s guest=%s room=%s party=%d"
                .formatted(stayId, guest.getGuestId(), room.getSpaceId(), partySize);
        auditEntries.add(new AuditEntry(Instant.now(clock), "STAY_REGISTERED", details));

        return stay;
    }

    public Optional<GuestStay> findStay(String stayId) {
        return Optional.ofNullable(stays.get(stayId));
    }

    public List<SentEmail> sentEmails() {
        return List.copyOf(sentEmails);
    }

    public List<AuditEntry> auditEntries() {
        return List.copyOf(auditEntries);
    }
}
