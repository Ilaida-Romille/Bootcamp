package com.hotel.koans;

import com.hotel.domain.MaintenanceRequest;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Intermediate stream operations: {@code filter}, {@code map}, and the match
 * operations {@code anyMatch} / {@code allMatch} / {@code noneMatch}.
 */
class _04_StreamsFilterMapTest {

    private List<Room> rooms;
    private List<Staff> staff;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
        staff = HotelFixtures.sampleStaff();
    }

    /**
     * {@link java.util.stream.Stream#filter} keeps elements matching a
     * {@link java.util.function.Predicate}. Vacant-clean rooms: 101, 103, 202, 301.
     */
    @Test
    void filter_rooms_by_housekeeping_status() {
        // TODO: koan — filter rooms with housekeeping status VACANT_CLEAN
        List<Room> vacantClean = null;

        assertThat(vacantClean).extracting(Room::getSpaceId)
                .containsExactly("101", "103", "202", "301");
    }

    /**
     * Combine {@code filter} with {@code count}. Only MR-1001 is still REPORTED;
     * the other requests were assigned or resolved.
     */
    @Test
    void filter_and_count_open_requests() {
        List<MaintenanceRequest> requests = HotelFixtures.sampleMaintenanceRequests();
        // TODO: koan — count requests whose status is REPORTED
        long reported = -1L;

        assertThat(reported).isEqualTo(1L);
    }

    /**
     * {@link java.util.stream.Stream#map} transforms each element. Map rooms to
     * their room numbers in floor order: 101..302.
     */
    @Test
    void map_rooms_to_room_numbers() {
        // TODO: koan — map each room to its space id (room number)
        List<String> roomNumbers = null;

        assertThat(roomNumbers).containsExactly(
                "101", "102", "103", "201", "202", "203", "301", "302");
    }

    /**
     * After {@code map}, {@code distinct()} removes duplicates. Staff belong to
     * only two departments.
     */
    @Test
    void map_staff_to_department_names_distinct() {
        // TODO: koan — map staff to their department names and deduplicate
        List<String> departments = null;

        assertThat(departments).containsExactly("Housekeeping", "Engineering");
    }

    /**
     * {@code anyMatch} answers "does at least one element match?" Rooms 102 and
     * 203 carry the ACCESSIBLE tag.
     */
    @Test
    void any_room_tagged_accessible() {
        // TODO: koan — anyMatch over rooms' tags for "ACCESSIBLE"
        boolean anyAccessible = false;

        assertThat(anyAccessible).isTrue();
    }

    /**
     * {@code allMatch} requires every element to match. Not every room is tagged
     * SEA_VIEW, so the answer is false.
     */
    @Test
    void every_room_tagged_sea_view() {
        // TODO: koan — allMatch over rooms' tags for "SEA_VIEW"
        boolean allSeaView = true;

        assertThat(allSeaView).isFalse();
    }

    /**
     * {@code noneMatch} requires no match at all. No room carries a PENTHOUSE tag.
     */
    @Test
    void no_room_tagged_penthouse() {
        // TODO: koan — noneMatch over rooms' tags for "PENTHOUSE"
        boolean nonePenthouse = false;

        assertThat(nonePenthouse).isTrue();
    }
}
