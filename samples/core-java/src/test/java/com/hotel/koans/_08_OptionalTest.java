package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.Guest;
import com.hotel.domain.GuestStay;
import com.hotel.domain.Hotel;
import com.hotel.domain.MaintenanceRequest;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link Optional} — the three factory methods, {@code map}/{@code filter}
 * chaining, the terminal operations {@code orElse}/{@code orElseGet}/{@code
 * orElseThrow}, and {@code ifPresentOrElse}.
 */
class _08_OptionalTest {

    private List<Guest> guests;
    private List<Staff> staff;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        guests = HotelFixtures.sampleGuests();
        staff = HotelFixtures.sampleStaff();
        hotel = HotelFixtures.sampleHotel();
    }

    /**
     * The three factory methods: {@code Optional.of} for guaranteed values,
     * {@code Optional.empty} for absence, and {@code ofNullable} for values that
     * may be null. Carol's tier is PLATINUM.
     */
    @Test
    void optional_factory_methods() {
        // TODO: koan — Optional.of("GOLD") for Alice, Optional.empty() for nobody, Optional.ofNullable on Carol's tier
        Optional<String> aliceTier = null;
        Optional<String> empty = null;
        Optional<String> carolTier = null;

        assertThat(aliceTier).contains("GOLD");
        assertThat(empty).isEmpty();
        assertThat(carolTier).contains("PLATINUM");
    }

    /**
     * {@code map} transforms a present value, {@code filter} keeps it only if a
     * predicate holds. Tom's supervisor is Raj; Raj has no supervisor.
     */
    @Test
    void optional_map_and_filter_chaining() {
        Optional<Staff> supervisor = staff.get(4).getSupervisor();
        // TODO: koan — map the supervisor to their name; flatMap to the supervisor's
        //              supervisor (also an Optional); filter by HVAC_LICENSED certification
        Optional<String> supervisorName = null;
        Optional<Staff> topOfChain = null;
        Optional<Staff> licensedSupervisor = null;

        assertThat(supervisorName).contains("Raj Patel");
        assertThat(topOfChain).isEmpty();
        assertThat(licensedSupervisor).isPresent();
        assertThat(licensedSupervisor.orElseThrow().getName()).isEqualTo("Raj Patel");
    }

    /**
     * Choosing a fallback: {@code orElse} for a constant, {@code orElseGet} for a
     * lazily-computed one (not evaluated when the value is present), and
     * {@code orElseThrow} when absence is an error. Bob has no tier; Carol and
     * Dave do.
     */
    @Test
    void or_else_or_else_get_or_else_throw() {
        // TODO: koan — bob: orElse("STANDARD"); carol: orElseThrow(); dave: orElseGet(() -> "GENERIC")
        String bobTier = null;
        String carolTier = null;
        String daveTier = null;

        assertThat(bobTier).isEqualTo("STANDARD");
        assertThat(carolTier).isEqualTo("PLATINUM");
        assertThat(daveTier).isEqualTo("SILVER");
    }

    /**
     * A stream pipeline ending in {@code findFirst()} yields an
     * {@code Optional<Room>} — search for room "302".
     */
    @Test
    void find_room_by_number_returns_optional() {
        List<Room> rooms = hotel.getFloors().stream()
                .flatMap(floor -> floor.getRooms().stream())
                .toList();
        // TODO: koan — stream filter for space id "302", then findFirst()
        Optional<Room> found = null;

        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getHousekeepingStatus())
                .isEqualTo(Room.HousekeepingStatus.OUT_OF_ORDER);
    }

    /**
     * {@code Optional<LocalDate>} for a nullable checkout date: S-1004 (Carol) is
     * still in-house, so compute nights up to today instead of crashing.
     */
    @Test
    void nights_stayed_for_in_house_stay() {
        GuestStay carolStay = hotel.getStayHistory().stream()
                .filter(stay -> stay.getStayId().equals("S-1004"))
                .findFirst()
                .orElseThrow();
        // TODO: koan — nights between check-in and checkout, falling back to today via orElse
        long nights = -1L;

        assertThat(nights).isEqualTo(
                ChronoUnit.DAYS.between(carolStay.getCheckInDate(), LocalDate.now()));
    }

    /**
     * {@code ifPresentOrElse} runs one action when present and another when empty.
     * MR-1001 is unassigned; MR-1002 is assigned to Lena. The assignee accessor is
     * nullable, so wrap it in {@code Optional.ofNullable} first.
     */
    @Test
    void if_present_or_else_on_assignment() {
        List<MaintenanceRequest> requests = HotelFixtures.sampleMaintenanceRequests();
        StringBuilder log = new StringBuilder();
        // TODO: koan — wrap the maybe-null assignee in Optional.ofNullable, then
        //              ifPresentOrElse: present -> log "assigned to <name>", empty -> log "unassigned"
        Optional.ofNullable(requests.get(0).getAssignee()).ifPresentOrElse(null, null);
        Optional.ofNullable(requests.get(1).getAssignee()).ifPresentOrElse(null, null);

        assertThat(log.toString()).isEqualTo("unassigned; assigned to Lena Fischer");
    }
}
