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

import com.pointwest.bootcamp.exercises.refactoring.audit.AuditService;
import com.pointwest.bootcamp.exercises.refactoring.audit.AuditServiceImpl;
import com.pointwest.bootcamp.exercises.refactoring.notification.NotificationService;
import com.pointwest.bootcamp.exercises.refactoring.notification.NotificationServiceImpl;
import com.pointwest.bootcamp.exercises.refactoring.room.RoomCapacityCalculator;
import com.pointwest.bootcamp.exercises.refactoring.room.RoomCapacityCalculatorImpl;
import com.pointwest.bootcamp.exercises.refactoring.stay.StayRepository;
import com.pointwest.bootcamp.exercises.refactoring.stay.StayRepositoryImpl;
import com.pointwest.bootcamp.hotelservices.exception.RoomUnavailableException;
import com.pointwest.bootcamp.hotelservices.model.Guest;
import com.pointwest.bootcamp.hotelservices.model.GuestStay;
import com.pointwest.bootcamp.hotelservices.model.Room;

public class GuestStayRegistrationManager {

    private final Clock clock;
    private final StayRepository stayRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final RoomCapacityCalculator capacityCalculator;

    private GuestStayRegistrationManager(
            Clock clock,
            StayRepository stayRepository,
            NotificationService notificationService,
            AuditService auditService,
            RoomCapacityCalculator capacityCalculator) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
        this.stayRepository = Objects.requireNonNull(stayRepository);
        this.notificationService = Objects.requireNonNull(notificationService);
        this.auditService = Objects.requireNonNull(auditService);
        this.capacityCalculator = Objects.requireNonNull(capacityCalculator);
    }

    /** Don't change this method */
    public static GuestStayRegistrationManager inMemory(Clock clock) {
        return new GuestStayRegistrationManager(
                clock,
                new StayRepositoryImpl(),
                new NotificationServiceImpl(),
                new AuditServiceImpl(clock),
                new RoomCapacityCalculatorImpl());
    }

    /** Don't change this method */
    public static GuestStayRegistrationManager inMemory() {
        return inMemory(Clock.systemDefaultZone());
    }

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

        int roomCapacity = capacityCalculator.getCapacity(room.getRoomType());

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
        if (room.getStatus() != Room.Status.OPERATIONAL
                || room.getHousekeepingStatus() != Room.HousekeepingStatus.VACANT_CLEAN) {
            throw new RoomUnavailableException(
                    "Room " + room.getRoomNumber()
                            + " is not available (status: " + room.getHousekeepingStatus() + ")");
        }

        String stayId = stayRepository.generateNextId();
        GuestStay stay = new GuestStay(stayId, room, partySize, checkInDate);
        stayRepository.save(stay);

        notificationService.sendConfirmation(email, guest.getName(), stayId, room.getRoomNumber());
        auditService.logRegistration(stayId, guest.getGuestId(), room.getRoomNumber(), partySize);

        return stay;
    }

    public Optional<GuestStay> findStay(String stayId) {
        return stayRepository.findById(stayId);
    }

    public List<SentEmail> sentEmails() {
        return notificationService.getSentEmail();
    }

    public List<AuditEntry> auditEntries() {
        return auditService.getAuditEntries();
    }
}