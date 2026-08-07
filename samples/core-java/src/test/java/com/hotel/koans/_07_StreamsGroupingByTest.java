package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.Department;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Grouping with {@link Collectors#groupingBy}, including downstream collectors,
 * and boolean partitioning with {@link Collectors#partitioningBy}.
 */
class _07_StreamsGroupingByTest {

    private List<Room> rooms;
    private List<Staff> staff;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
        staff = HotelFixtures.sampleStaff();
    }

    /**
     * {@code groupingBy} classifies elements into a {@code Map<key, List<element>>}.
     * Statuses: 4 vacant-clean, 2 vacant-dirty, 1 occupied-dirty, 1 out-of-order.
     */
    @Test
    void grouping_rooms_by_housekeeping_status() {
        // TODO: koan — group rooms by their housekeeping status
        Map<Room.HousekeepingStatus, List<Room>> byStatus = null;

        assertThat(byStatus).hasSize(4);
        assertThat(byStatus.get(Room.HousekeepingStatus.VACANT_CLEAN)).hasSize(4);
        assertThat(byStatus.get(Room.HousekeepingStatus.VACANT_DIRTY)).hasSize(2);
        assertThat(byStatus.get(Room.HousekeepingStatus.OCCUPIED_DIRTY)).hasSize(1);
        assertThat(byStatus.get(Room.HousekeepingStatus.OUT_OF_ORDER)).hasSize(1);
    }

    /**
     * A downstream collector changes the value type: {@code groupingBy(key,
     * counting())} yields {@code Map<key, Long>}.
     */
    @Test
    void grouping_rooms_by_status_with_counts() {
        // TODO: koan — group rooms by status with a counting() downstream collector
        Map<Room.HousekeepingStatus, Long> counts = null;

        assertThat(counts.get(Room.HousekeepingStatus.VACANT_CLEAN)).isEqualTo(4L);
        assertThat(counts.get(Room.HousekeepingStatus.VACANT_DIRTY)).isEqualTo(2L);
        assertThat(counts.get(Room.HousekeepingStatus.OCCUPIED_DIRTY)).isEqualTo(1L);
        assertThat(counts.get(Room.HousekeepingStatus.OUT_OF_ORDER)).isEqualTo(1L);
    }

    /**
     * {@code mapping(...)} as a downstream collector transforms each element in
     * the group: department -> list of member names.
     */
    @Test
    void grouping_staff_by_department_with_mapped_names() {
        // TODO: koan — group staff by department, mapping each member to their name
        Map<Department, List<String>> membersByDepartment = null;

        assertThat(membersByDepartment.get(staff.get(0).getDepartment()))
                .containsExactly("Maria Santos", "Jonas Weber");
        assertThat(membersByDepartment.get(staff.get(2).getDepartment()))
                .containsExactly("Raj Patel", "Lena Fischer", "Tom Alvarez");
    }

    /**
     * {@code partitioningBy} splits into exactly two lists under the keys true and
     * false. Five rooms rate 200.00 or less; three rate more.
     */
    @Test
    void partitioning_rooms_by_rate_threshold() {
        // TODO: koan — partition rooms: rate strictly above 200.00 vs the rest
        Map<Boolean, List<Room>> partitioned = null;

        assertThat(partitioned.get(true)).extracting(Room::getSpaceId)
                .containsExactly("203", "301", "302");
        assertThat(partitioned.get(false)).extracting(Room::getSpaceId)
                .containsExactly("101", "102", "103", "201", "202");
    }

    /**
     * Multi-level grouping: nested {@code groupingBy} produces a two-level map.
     * Maria and Jonas hold FIRST_AID; Raj does; Lena and Tom do not.
     */
    @Test
    void grouping_staff_by_department_then_certification() {
        // TODO: koan — group staff by department, then by whether they hold FIRST_AID
        Map<Department, Map<Boolean, List<Staff>>> nested = null;

        assertThat(nested.get(staff.get(0).getDepartment()).get(true))
                .extracting(Staff::getName).containsExactly("Maria Santos", "Jonas Weber");
        assertThat(nested.get(staff.get(2).getDepartment()).get(true))
                .extracting(Staff::getName).containsExactly("Raj Patel");
        assertThat(nested.get(staff.get(2).getDepartment()).get(false))
                .extracting(Staff::getName).containsExactly("Lena Fischer", "Tom Alvarez");
    }

    /**
     * A downstream collector on a different axis: staff count per department.
     */
    @Test
    void grouping_staff_per_department_with_counts() {
        // TODO: koan — group staff by department with counting()
        Map<Department, Long> headcounts = null;

        assertThat(headcounts.get(staff.get(0).getDepartment())).isEqualTo(2L);
        assertThat(headcounts.get(staff.get(2).getDepartment())).isEqualTo(3L);
    }
}
