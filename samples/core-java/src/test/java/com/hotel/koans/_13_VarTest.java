package com.hotel.koans;

import com.hotel.domain.Room;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;

/**
 * Local-variable type inference ({@code var}, JEP 286): the compiler infers
 * the type from the initializer, so the declaration must still have one.
 * {@code var} is sugar over the <em>source</em>, not the bytecode — there is
 * no way to assert at runtime whether a line "used var", so these koans
 * grade on the resulting value, the same as every other koan in this suite.
 */
class _13_VarTest {

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * {@code var} with the diamond operator: {@code new ArrayList<String>()}
     * gives the compiler everything it needs to infer the full generic type.
     * Collect every SEA_VIEW room's number — 103, 202, 203, 302 in fixture order.
     */
    @Test
    void var_with_diamond_operator_collection() {
        // TODO: koan — replace with: var seaViewNumbers = new ArrayList<String>();
        var seaViewNumbers = new ArrayList<String>();
        for (Room room : rooms) {
            if (room.getTags().contains("SEA_VIEW")) {
                seaViewNumbers.add(room.getSpaceId());
            }
        }

        assertThat(seaViewNumbers).containsExactly("103", "202", "203", "302");
    }

    /**
     * {@code var} in a for-each loop infers the element type from the
     * collection being iterated — here {@code Room}, not {@code Object}.
     * Accumulate every BALCONY-tagged room's number: 103, 203, 301.
     */
    @Test
    void var_in_enhanced_for_loop() {
        List<String> balconyNumbers = new ArrayList<>();
        // TODO: koan — use var for the loop variable: for (var room : rooms)
        for (var room : rooms) {
            if (room.getTags().contains("BALCONY")) {
                balconyNumbers.add(room.getSpaceId());
            }
        }

        assertThat(balconyNumbers).containsExactly("103", "203", "301");
    }

    /**
     * {@code var} holding a stream pipeline's result: the type is whatever
     * the terminal operation returns, inferred without spelling it out.
     * The STANDARD rooms are 101, 102, 201.
     */
    @Test
    void var_with_stream_pipeline_result() {
        // TODO: koan — replace with: var standardRoomNumbers = rooms.stream()
        //              filter roomType "STANDARD", map to space id, toList()
        var standardRoomNumbers = rooms.stream()
                .filter(room -> room.getRoomType().equals("STANDARD"))
                .map(Room::getSpaceId)
                .toList();

        assertThat(standardRoomNumbers).containsExactly("101", "102", "201");
    }

    /**
     * {@code var} works in a try-with-resources header too — the type of the
     * resource is inferred from the initializer, and {@code close()} still
     * runs when the block ends.
     */
    @Test
    void var_in_try_with_resources() {
        StringBuilder log = new StringBuilder();
        // TODO: koan — replace with: try (var session = new LoggingSession(log)) { log.append("work;"); }
        try (var session = new LoggingSession(log)) {
            log.append("work;");
         }

        assertThat(log.toString()).isEqualTo("open;work;closed;");
    }

    /**
     * Readability guideline: {@code var} is a win when the initializer makes
     * the type obvious, a cost when it hides it. Which declaration is
     * clearer for a nightly rate?
     * <pre>
     *   A: var rate = room.getNightlyRate();
     *   B: BigDecimal rate = room.getNightlyRate();
     * </pre>
     */
    @Test
    void var_readability_guideline() {
        Room room = rooms.get(0);
        // TODO: koan — pick the clearer declaration, "A" or "B" (see Javadoc)
        String betterChoice = "B";

        assertThat(betterChoice).isEqualTo("B");
    }

    /** Teaching prop: an AutoCloseable that logs its lifecycle. */
    private static final class LoggingSession implements AutoCloseable {
        private final StringBuilder log;

        LoggingSession(StringBuilder log) {
            this.log = log;
            log.append("open;");
        }

        @Override
        public void close() {
            log.append("closed;");
        }
    }
}
