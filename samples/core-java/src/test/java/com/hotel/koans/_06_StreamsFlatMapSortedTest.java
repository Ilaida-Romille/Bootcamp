package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.CleaningTask;
import com.hotel.domain.Hotel;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code flatMap} for nested collections, and ordering with {@code sorted}.
 */
class _06_StreamsFlatMapSortedTest {

    private Hotel hotel;
    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        hotel = HotelFixtures.sampleHotel();
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * {@code flatMap} flattens a stream of collections into one stream — here,
     * every floor's rooms into all 8 rooms of the hotel.
     */
    @Test
    void flat_map_floors_to_rooms() {
        // TODO: koan — flatten hotel floors into their rooms (floors -> rooms stream)
        List<Room> allRooms = hotel.getFloors().stream().flatMap(floor -> floor.getRooms().stream()).toList();

        assertThat(allRooms).extracting(Room::getSpaceId)
                .containsExactly("101", "102", "103", "201", "202", "203", "301", "302");
    }

    /**
     * Same idea across the whole staff: flatten every staff member's
     * {@code assignedTasks}. Five tasks are assigned to someone.
     */
    @Test
    void flat_map_staff_to_tasks_hotel_wide() {
        // TODO: koan — flatten all staff's assignedTasks (one stream per staff member)
        List<CleaningTask> allTasks = hotel.getDepartments().stream().flatMap(department -> department.getMembers().stream()).flatMap(task -> task.getAssignedTasks().stream()).toList();

        assertThat(allTasks).hasSize(5);
        assertThat(allTasks).extracting(CleaningTask::getTaskId)
                .contains("CT-1001", "CT-1002", "CT-1003", "CT-1004", "CT-1007");
    }

    /**
     * The same flattening restricted to one department: no engineer is assigned
     * any cleaning task.
     */
    @Test
    void flat_map_engineering_department_tasks() {
        // TODO: koan — flatten the Engineering department members' assignedTasks
        List<CleaningTask> engineeringTasks = hotel.getDepartments().stream().filter(t -> t.getName() == "Engineering").flatMap(department -> department.getMembers().stream()).flatMap(members -> members.getAssignedTasks().stream()).toList();

        assertThat(engineeringTasks).isEmpty();
    }

    /**
     * {@code sorted(Comparator)} orders the stream. Rates run 120, 120, 130, 190,
     * 200, 210, 350, 380; ties keep encounter order (101 before 201).
     */
    @Test
    void sorted_by_nightly_rate() {
        // TODO: koan — sort rooms by nightlyRate using Comparator.comparing
        List<Room> byRate = rooms.stream().sorted(Comparator.comparing(Room::getNightlyRate)).toList();

        assertThat(byRate).extracting(Room::getSpaceId)
                .containsExactly("101", "201", "102", "202", "103", "301", "203", "302");
    }

    /**
     * Chain comparators with {@code thenComparing}: primary key department name,
     * secondary key staff name. Department has no natural order, so compare via
     * its {@code getName()}. "Engineering" sorts before "Housekeeping".
     */
    @Test
    void sorted_by_department_then_name() {
        // TODO: koan — sort staff by department name, then by staff name
        List<Staff> ordered = HotelFixtures.sampleStaff().stream().sorted(Comparator.comparing((Staff member) -> member.getDepartment().getName()).thenComparing(Staff::getName)).toList();

        assertThat(ordered).extracting(Staff::getName)
                .containsExactly("Lena Fischer", "Raj Patel", "Tom Alvarez", "Jonas Weber", "Maria Santos");
    }

    /**
     * {@code flatMap} followed by {@code distinct()} collects unique values across
     * a nested structure — here every certification held by any staff member.
     */
    @Test
    void distinct_certifications_hotel_wide() {
        // TODO: koan — flatten staff certifications, deduplicate
        List<Certification> certifications = HotelFixtures.sampleStaff().stream().flatMap(member -> member.getCertifications().stream()).distinct().toList();

        assertThat(certifications).containsExactlyInAnyOrder(
                Certification.FIRST_AID, Certification.FOOD_HANDLER, Certification.HVAC_LICENSED);
    }
}
