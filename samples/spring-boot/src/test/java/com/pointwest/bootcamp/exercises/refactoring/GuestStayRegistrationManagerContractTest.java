package com.pointwest.bootcamp.exercises.refactoring;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pointwest.bootcamp.hotelservices.exception.RoomUnavailableException;
import com.pointwest.bootcamp.hotelservices.model.ContactInfo;
import com.pointwest.bootcamp.hotelservices.model.Guest;
import com.pointwest.bootcamp.hotelservices.model.GuestStay;
import com.pointwest.bootcamp.hotelservices.model.Room;

/**
 * Characterization tests for the God Class refactoring exercise.
 *
 * <p>These tests intentionally know nothing about the manager's internal
 * collaborators. You may freely rename and reorganize extracted classes as
 * long as this external contract continues to hold.</p>
 */
class GuestStayRegistrationManagerContractTest {

    private static final Instant NOW = Instant.parse("2026-08-13T02:30:00Z");
    private static final LocalDate TODAY = LocalDate.of(2026, 8, 13);

    private GuestStayRegistrationManager manager;

    @BeforeEach
    void setUp() {
        manager = GuestStayRegistrationManager.inMemory(Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void registers_and_persists_a_valid_guest_stay() {
        Guest guest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Room room = room("101", "Standard");

        GuestStay stay = manager.register(guest, room, 2, TODAY);

        assertThat(stay.getStayId()).isEqualTo("STAY-0001");
        assertThat(stay.getGuest()).isSameAs(guest);
        assertThat(stay.getRoom()).isSameAs(room);
        assertThat(stay.getPartySize()).isEqualTo(2);
        assertThat(stay.getCheckInDate()).isEqualTo(TODAY);
        assertThat(manager.findStay("STAY-0001")).containsSame(stay);

        assertThat(guest.getStays()).containsExactly(stay);
        assertThat(room.getHousekeepingStatus()).isEqualTo(Room.HousekeepingStatus.OCCUPIED_DIRTY);
    }

    @Test
    void sends_a_confirmation_and_records_an_audit_entry() {
        Guest guest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Room room = room("101", "Standard");

        manager.register(guest, room, 2, TODAY);

        assertThat(manager.sentEmails()).containsExactly(new SentEmail(
                "alice@example.com",
                "Check-in confirmed: Room 101",
                "Welcome, Alice Nguyen. Stay STAY-0001 is checked in to Room 101."));
        assertThat(manager.auditEntries()).containsExactly(new AuditEntry(
                NOW,
                "STAY_REGISTERED",
                "STAY-0001 guest=G-101 room=101 party=2"));
    }

    @Test
    void rejects_invalid_input_without_creating_partial_side_effects() {
        Guest guest = guest("G-101", "Alice Nguyen", "not-an-email");
        Room room = room("101", "Standard");

        assertThatThrownBy(() -> manager.register(guest, room, 2, TODAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("guest must have a valid email address");

        assertThat(manager.findStay("STAY-0001")).isEmpty();
        assertThat(manager.sentEmails()).isEmpty();
        assertThat(manager.auditEntries()).isEmpty();
        assertThat(guest.getStays()).isEmpty();
        assertThat(room.getHousekeepingStatus()).isEqualTo(Room.HousekeepingStatus.VACANT_CLEAN);

        Guest validGuest = guest("G-102", "Ben Santos", "ben@example.com");
        GuestStay firstSuccessfulStay = manager.register(validGuest, room, 1, TODAY);
        assertThat(firstSuccessfulStay.getStayId()).isEqualTo("STAY-0001");
    }

    @Test
    void enforces_room_capacity_before_checking_in_the_guest() {
        Guest guest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Room room = room("101", "Standard");

        assertThatThrownBy(() -> manager.register(guest, room, 3, TODAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("party size 3 exceeds room capacity 2");

        assertThat(guest.getStays()).isEmpty();
        assertThat(room.getHousekeepingStatus()).isEqualTo(Room.HousekeepingStatus.VACANT_CLEAN);
        assertThat(manager.sentEmails()).isEmpty();
        assertThat(manager.auditEntries()).isEmpty();
    }

    @Test
    void registers_only_guests_who_are_checking_in_today() {
        Guest guest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Room room = room("101", "Standard");

        assertThatThrownBy(() -> manager.register(guest, room, 1, TODAY.plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("check-in date must be today: 2026-08-13");

        assertThat(guest.getStays()).isEmpty();
        assertThat(room.getHousekeepingStatus()).isEqualTo(Room.HousekeepingStatus.VACANT_CLEAN);
        assertThat(manager.sentEmails()).isEmpty();
        assertThat(manager.auditEntries()).isEmpty();
    }

    @Test
    void refuses_to_register_an_unavailable_room() {
        Guest firstGuest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Guest secondGuest = guest("G-102", "Ben Santos", "ben@example.com");
        Room room = room("101", "Standard");
        manager.register(firstGuest, room, 1, TODAY);

        assertThatThrownBy(() -> manager.register(secondGuest, room, 1, TODAY))
                .isInstanceOf(RoomUnavailableException.class)
                .hasMessageContaining("Room 101 is not available");

        assertThat(secondGuest.getStays()).isEmpty();
        assertThat(manager.sentEmails()).hasSize(1);
        assertThat(manager.auditEntries()).hasSize(1);
    }

    @Test
    void assigns_sequential_ids_to_successful_registrations_only() {
        Guest firstGuest = guest("G-101", "Alice Nguyen", "alice@example.com");
        Guest secondGuest = guest("G-102", "Ben Santos", "ben@example.com");

        GuestStay first = manager.register(firstGuest, room("101", "Standard"), 1, TODAY);
        GuestStay second = manager.register(secondGuest, room("203", "Deluxe"), 3, TODAY);

        assertThat(first.getStayId()).isEqualTo("STAY-0001");
        assertThat(second.getStayId()).isEqualTo("STAY-0002");
        assertThat(manager.findStay("STAY-0001")).containsSame(first);
        assertThat(manager.findStay("STAY-0002")).containsSame(second);
    }

    @Test
    void does_not_expose_mutable_observation_collections() {
        manager.register(
                guest("G-101", "Alice Nguyen", "alice@example.com"),
                room("101", "Standard"),
                1,
                TODAY);

        assertThatThrownBy(() -> manager.sentEmails().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> manager.auditEntries().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(manager.sentEmails()).hasSize(1);
        assertThat(manager.auditEntries()).hasSize(1);
    }

    private static Guest guest(String id, String name, String email) {
        return new Guest(id, name, new ContactInfo(email, "+63 917 555 0101"));
    }

    private static Room room(String roomNumber, String roomType) {
        return new Room(roomNumber, 1, roomType, 2, Room.HousekeepingStatus.VACANT_CLEAN);
    }
}
