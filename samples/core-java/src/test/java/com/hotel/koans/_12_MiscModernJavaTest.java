package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.ContactInfo;
import com.hotel.domain.Room;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Modern Java conveniences: text blocks, {@code String.formatted}, immutable
 * copies with {@code List.of}/{@code List.copyOf}, the unmodifiable stream
 * {@code toList()}, switch expressions with {@code yield}, and record patterns
 * in an {@code instanceof} check.
 */
class _12_MiscModernJavaTest {

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * Text blocks strip incidental indentation and keep the trailing newline.
     * This is a guest notice pinned to room 101.
     */
    @Test
    void text_block_literal() {
        // TODO: koan — write a text block producing exactly (with trailing newline):
        //              ROOM 101 READY
        //                rate: 120.00
        //                status: VACANT_CLEAN
        String notice = "";

        assertThat(notice).isEqualTo("ROOM 101 READY\n  rate: 120.00\n  status: VACANT_CLEAN\n");
    }

    /**
     * Incidental indentation stripping: a text block removes the leading
     * whitespace common to every line, using the closing {@code """}'s own
     * column as part of the calculation. Move the closing delimiter left and
     * more indentation survives in the content — here the second line keeps
     * a 2-space indent relative to the first.
     */
    @Test
    void text_block_indentation_stripping() {
        // TODO: koan — a text block producing exactly "STATUS:\n  OK\n":
        //              closing """ aligned with the first line's column,
        //              second line with 2 extra leading spaces
        String status = "WRONG";

        assertThat(status).isEqualTo("STATUS:\n  OK\n");
    }

    /**
     * A trailing backslash at the end of a text-block source line suppresses
     * that line's newline in the resulting {@code String} — wrap long source
     * lines without adding a line break to the value.
     */
    @Test
    void text_block_line_continuation_suppresses_newline() {
        // TODO: koan — a text block across two source lines joined by a
        //              trailing "\" whose value is the single line "Room 101 is vacant"
        String sentence = "WRONG";

        assertThat(sentence).isEqualTo("Room 101 is vacant");
        assertThat(sentence).doesNotContain("\n");
    }

    /**
     * {@code String.formatted} fills {@code %s}/{@code %d} placeholders with
     * arguments — room number and nightly rate of room 101.
     */
    @Test
    void formatted_string_placeholders() {
        Room room = rooms.get(0);
        // TODO: koan — "%s at $%s" formatted with the room's space id and nightly rate
        String line = "";

        assertThat(line).isEqualTo("101 at $120.00");
    }

    /**
     * {@code List.copyOf} returns an unmodifiable view backed by its own
     * storage. Mutation must fail with UnsupportedOperationException.
     */
    @Test
    void list_copy_of_is_unmodifiable() {
        // TODO: koan — List.copyOf(rooms); then note that adding throws
        List<Room> fixed = null;

        assertThat(fixed).hasSize(8);
        assertThatThrownBy(() -> fixed.add(rooms.get(0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    /**
     * The stream {@code toList()} is unmodifiable; {@code Collectors.toList()}
     * is not. Both collect the same rooms.
     */
    @Test
    void stream_to_list_vs_collectors_to_list() {
        // TODO: koan — immutable = rooms.stream().toList(); mutable = rooms.stream().collect(Collectors.toList())
        List<Room> immutable = null;
        List<Room> mutable = null;

        assertThatThrownBy(() -> immutable.clear())
                .isInstanceOf(UnsupportedOperationException.class);
        mutable.clear();
        assertThat(mutable).isEmpty();
    }

    /**
     * A switch expression with block rules computes a value before {@code yield}
     * — here a certification description that needs a built string.
     */
    @Test
    void switch_expression_yield_in_block_rule() {
        // TODO: koan — switch expression over Certification.HVAC_LICENSED:
        //              block rules ending in yield: FIRST_AID -> "lifesaving",
        //              POOL_LIFEGUARD -> "pool", FOOD_HANDLER -> "food",
        //              HVAC_LICENSED -> "machinery"
        String requirement = null;

        assertThat(requirement).isEqualTo("machinery");
    }

    /**
     * Record patterns deconstruct in an {@code instanceof}: bind both fields of
     * a ContactInfo in one check.
     */
    @Test
    void record_pattern_in_instanceof_on_contact() {
        ContactInfo contact = new ContactInfo("alice@example.com", "+1-555-0101");
        String summary = "";
        if (contact instanceof ContactInfo) {
            summary = "no pattern";
        }
        // TODO: koan — replace the if above with an instanceof record pattern
        //              binding (var email, var phone) and summary = email + "/" + phone

        assertThat(summary).isEqualTo("alice@example.com/+1-555-0101");
    }
}
