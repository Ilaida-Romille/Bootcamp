package com.hotel.koans;

import com.hotel.domain.Facility;
import com.hotel.domain.Guest;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Terminal operations: {@code reduce} for aggregation, and {@code collect} with
 * {@code toList}, {@code joining}, {@code summingInt}, {@code averagingDouble},
 * and {@code toMap}.
 */
class _05_StreamsReduceCollectTest {

    private List<Room> rooms;
    private List<Staff> staff;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
        staff = HotelFixtures.sampleStaff();
        guests = HotelFixtures.sampleGuests();
    }

    /**
     * {@code reduce} with an identity folds a stream into one value. Start at
     * {@link BigDecimal#ZERO} and add every nightly rate — the eight rooms sum to 1700.00.
     */
    @Test
    void reduce_sum_of_rates_with_identity() {
        // TODO: koan — reduce the rates stream: identity ZERO, then BigDecimal::add
        BigDecimal total = rooms.stream().map(room -> room.getNightlyRate()).reduce(BigDecimal.ZERO, (subtotal, rate) -> subtotal.add(rate));

        assertThat(total).isEqualByComparingTo("1700.00");
    }

    /**
     * Without an identity the result is an {@link Optional} — empty if the stream
     * is empty. Same total, same stream.
     */
    @Test
    void reduce_sum_of_rates_without_identity() {
        // TODO: koan — reduce the rates stream with just BigDecimal::add
        Optional<BigDecimal> total = rooms.stream().map(room -> room.getNightlyRate()).reduce(BigDecimal::add);

        assertThat(total).isPresent();
        assertThat(total.orElseThrow()).isEqualByComparingTo("1700.00");
    }

    /**
     * {@code reduce} can pick the "best" element too. Jonas has 3 assigned
     * cleaning tasks — the most of any staff member.
     * (Alternative: {@code max(Comparator.comparingInt(...))} gives the same result.)
     */
    @Test
    void reduce_to_busiest_staff_member() {
        Function<Staff, Integer> taskCount = s -> s.getAssignedTasks().size();
        // TODO: koan — reduce staff to the member with the highest taskCount
        Optional<Staff> busiest = staff.stream()
                                        .reduce((s1, s2) -> taskCount.apply(s1) >= taskCount.apply(s2) ? s1 : s2);

        assertThat(busiest).isPresent();
        assertThat(busiest.orElseThrow().getName()).isEqualTo("Jonas Weber");
    }

    /**
     * {@code Stream.toList()} (Java 16+) and {@code collect(Collectors.toList())}
     * produce the same result; the explicit collector also works for sets and maps.
     */
    @Test
    void collect_to_list_vs_stream_to_list() {
        // TODO: koan — collect vacant-clean rooms with .toList(), and the same again with collect(Collectors.toList())
        List<Room> viaStreamToList = rooms.stream().filter(room -> room.getHousekeepingStatus() == room.getHousekeepingStatus().VACANT_CLEAN).toList();
        List<Room> viaCollect = rooms.stream().filter(room -> room.getHousekeepingStatus() == room.getHousekeepingStatus().VACANT_CLEAN).collect(Collectors.toList());

        assertThat(viaStreamToList).extracting(Room::getSpaceId)
                .containsExactly("101", "103", "202", "301");
        assertThat(viaCollect).containsExactlyElementsOf(viaStreamToList);
    }

    /**
     * {@link Collectors#joining} concatenates strings with a delimiter, prefix,
     * and suffix. Guest names, in fixture order.
     */
    @Test
    void collect_joining_guest_names() {
        // TODO: koan — collect guest names with joining(", ", "[", "]")
        String csv = guests.stream().map(guest -> guest.getName()).collect(Collectors.joining(", ", "[", "]"));

        assertThat(csv).isEqualTo("[Alice Nguyen, Bob Okafor, Carol Diaz, Dave Kim]");
    }

    /**
     * {@link Collectors#summingInt} adds int values. Facility capacities:
     * pool 40 + gym 25 + breakfast hall 80.
     */
    @Test
    void collect_summing_facility_capacities() {
        List<Facility> facilities = HotelFixtures.sampleHotel().getFacilities();
        // TODO: koan — summingInt over facility capacities
        int totalCapacity = facilities.stream().collect(Collectors.summingInt(facility -> facility.getCapacity()));

        assertThat(totalCapacity).isEqualTo(145);
    }

    /**
     * {@link Collectors#averagingDouble} returns the mean as a double:
     * 1700.00 / 8 rooms.
     */
    @Test
    void collect_averaging_room_rates() {
        // TODO: koan — averagingDouble over room nightly rates
        double averageRate = rooms.stream().collect(Collectors.averagingDouble(room -> room.getNightlyRate().doubleValue()));

        assertThat(averageRate).isEqualTo(212.5);
    }

    /**
     * {@link Collectors#toMap} builds a map from key and value functions. Room
     * number -> nightly rate, all eight rooms.
     */
    @Test
    void collect_to_map_of_room_rates() {
        // TODO: koan — toMap: key = space id, value = nightly rate
        Map<String, BigDecimal> ratesByRoom = rooms.stream()
                                                    .collect(Collectors.toMap(room -> room.getSpaceId(), room -> room.getNightlyRate()));

        assertThat(ratesByRoom).hasSize(8);
        assertThat(ratesByRoom.get("302")).isEqualByComparingTo("380.00");
    }
}
