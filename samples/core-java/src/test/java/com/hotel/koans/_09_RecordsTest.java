package com.hotel.koans;

import com.hotel.domain.ContactInfo;
import com.hotel.domain.Room;
import com.hotel.domain.RoomOccupancySnapshot;
import com.hotel.domain.StayDuration;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Records: auto-generated accessors/equals/toString, compact constructor
 * validation, records as stream map targets, record pattern matching, and
 * records implementing an interface.
 */
class _09_RecordsTest {

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * A record's constructor and accessors are generated for you. Recreate
     * Alice's contact details (see HotelFixtures): alice@example.com / +1-555-0101.
     */
    @Test
    void record_accessors() {
        // TODO: koan — construct the ContactInfo with Alice's email and phone
        ContactInfo contact = new ContactInfo("alice@example.com", "+1-555-0101");

        assertThat(contact.email()).isEqualTo("alice@example.com");
        assertThat(contact.phone()).isEqualTo("+1-555-0101");
    }

    /**
     * Records compare by value, not identity: two equal records are {@code equals}
     * and share a hashCode. Their toString is also generated automatically.
     */
    @Test
    void record_equality_and_to_string() {
        ContactInfo a = new ContactInfo("alice@example.com", "+1-555-0101");
        ContactInfo b = new ContactInfo("alice@example.com", "+1-555-0101");
        // TODO: koan — what does the auto-generated toString look like? (ContactInfo[email=..., phone=...])
        String replace_this_with_your_answer = "ContactInfo[email=alice@example.com, phone=+1-555-0101]";

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).isEqualTo(replace_this_with_your_answer);
    }

    /**
     * A compact constructor can validate state. StayDuration must reject
     * negative nights — make the value negative so construction throws.
     */
    @Test
    void record_compact_constructor_validation() {
        // TODO: koan — make this negative so StayDuration rejects it
        long replace_this_with_your_answer = -1L;

        assertThatThrownBy(() -> new StayDuration(replace_this_with_your_answer))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(new StayDuration(3).nights()).isEqualTo(3L);
    }

    /**
     * Records make excellent stream map targets. Build one snapshot per room —
     * room 101 maps to (101, VACANT_CLEAN, 1).
     */
    @Test
    void record_as_stream_map_target() {
        // TODO: koan — map each room to a RoomOccupancySnapshot(spaceId, housekeepingStatus, floor)
        List<RoomOccupancySnapshot> snapshots = rooms.stream()
                .map(room -> new RoomOccupancySnapshot(room.getSpaceId(),
                        room.getHousekeepingStatus(), room.getFloor()))
                .toList();

        assertThat(snapshots).hasSize(8);
        assertThat(snapshots.get(0)).isEqualTo(new RoomOccupancySnapshot("101",
                Room.HousekeepingStatus.VACANT_CLEAN, 1));
    }

    /**
     * Record patterns deconstruct an instance in an {@code instanceof} check.
     * Destructure the snapshot for room 302 into its three components.
     */
    @Test
    void record_pattern_in_instanceof() {
        RoomOccupancySnapshot snapshot = new RoomOccupancySnapshot("302",
                Room.HousekeepingStatus.OUT_OF_ORDER, 3);
        String summary = "none";
        if (snapshot instanceof RoomOccupancySnapshot(var number, var status, var floor)) {
            summary = number + "/" + status + "/" + floor;
         }
        // TODO: koan — replace the if above with an instanceof record pattern
        //              binding (var number, var status, var floor) and a body that
        //              sets summary = number + "/" + status + "/" + floor

        assertThat(summary).isEqualTo("302/OUT_OF_ORDER/3");
    }

    /**
     * Records may implement interfaces. Complete {@link Priced} so its price is
     * the room's nightly rate.
     */
    @Test
    void record_implementing_an_interface() {
        Priced priced = new NightlyRoomRate(rooms.get(0));

        assertThat(priced.price()).isEqualByComparingTo("120.00");
    }

    /** A small interface for the koan above. */
    interface Priced {
        BigDecimal price();
    }

    /** A record implementing {@link Priced}. */
    record NightlyRoomRate(Room room) implements Priced {
        @Override
        public BigDecimal price() {
            // TODO: koan — return the room's nightly rate
            return room.getNightlyRate();
        }
    }
}
