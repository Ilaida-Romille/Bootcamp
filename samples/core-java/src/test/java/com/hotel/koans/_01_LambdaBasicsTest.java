package com.hotel.koans;

import com.hotel.domain.Guest;
import com.hotel.domain.GuestStay;
import com.hotel.domain.Hotel;
import com.hotel.domain.Room;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lambda basics: no-arg lambdas, expression vs block bodies, capturing
 * effectively-final variables, and the four method reference forms.
 */
class _01_LambdaBasicsTest {

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * A lambda with no parameters: {@code () -> ...}. The target type is
     * {@link Supplier}, whose single abstract method takes nothing and returns a value.
     */
    @Test
    void greeting_from_a_supplier() {
        // TODO: koan — supply a greeting containing the hotel name "Grand Meridian"
        Supplier<String> greeting = null;

        assertThat(HotelFixtures.isLambda(greeting)).isTrue();
        assertThat(greeting.get()).contains("Grand Meridian");
    }

    /**
     * Sort rooms by {@code nightlyRate} using a single-expression lambda body.
     * The expression's value is returned implicitly.
     */
    @Test
    void sort_with_expression_body() {
        List<Room> sorted = new ArrayList<>(rooms);
        // TODO: koan — expression-body lambda for the comparator (compare by nightlyRate)
        Comparator<Room> byRate = null;
        sorted.sort(byRate);

        assertThat(byRate).isNotNull();
        assertThat(HotelFixtures.isLambda(byRate)).isTrue();
        assertThat(sorted).extracting(Room::getSpaceId)
                .containsExactly("101", "201", "102", "202", "103", "301", "203", "302");
    }

    /**
     * The same sort with a block body: braces, an explicit {@code return}, and a
     * semicolon. Both forms are equivalent; block bodies allow multiple statements.
     */
    @Test
    void sort_with_block_body() {
        List<Room> sorted = new ArrayList<>(rooms);
        // TODO: koan — block-body lambda for the comparator (compare by nightlyRate)
        Comparator<Room> byRate = null;
        sorted.sort(byRate);

        assertThat(byRate).isNotNull();
        assertThat(HotelFixtures.isLambda(byRate)).isTrue();
        assertThat(sorted).extracting(Room::getSpaceId)
                .containsExactly("101", "201", "102", "202", "103", "301", "203", "302");
    }

    /**
     * A lambda can capture local variables that are effectively final — never
     * reassigned after declaration. Build a predicate on the captured threshold.
     */
    @Test
    void capturing_an_effectively_final_variable() {
        BigDecimal threshold = new BigDecimal("200.00");

        // TODO: koan — a Predicate<Room> that captures `threshold` (rate > threshold)
        Predicate<Room> expensive = null;

        List<Room> matches = rooms.stream().filter(expensive).toList();
        assertThat(HotelFixtures.isLambda(expensive)).isTrue();
        assertThat(matches).extracting(Room::getSpaceId).containsExactly("203", "301", "302");
    }

    /**
     * Static method reference: {@code Integer::parseInt}. Works anywhere a
     * functional interface whose method takes the static method's arguments is expected.
     */
    @Test
    void static_method_reference() {
        Function<String, Integer> parseRoomNumber = null;

        assertThat(HotelFixtures.isLambda(parseRoomNumber)).isTrue();
        assertThat(parseRoomNumber.apply("302")).isEqualTo(302);
    }

    /**
     * Unbound instance method reference: {@code Room::getNightlyRate}. The receiver
     * becomes the first parameter, so it fits a {@link Function}{@code <Room, BigDecimal>}.
     */
    @Test
    void unbound_instance_method_reference() {
        Function<Room, BigDecimal> rateOf = null;

        assertThat(HotelFixtures.isLambda(rateOf)).isTrue();
        assertThat(rateOf.apply(rooms.get(0))).isEqualByComparingTo("120.00");
    }

    /**
     * Bound instance method reference: {@code hotel::getName}. The receiver is
     * already captured, so only the remaining arguments are needed — here none.
     */
    @Test
    void bound_instance_method_reference() {
        Hotel hotel = HotelFixtures.sampleHotel();
        Supplier<String> hotelName = null;

        assertThat(HotelFixtures.isLambda(hotelName)).isTrue();
        assertThat(hotelName.get()).isEqualTo("Grand Meridian");
    }

    /**
     * Constructor reference: {@code GuestStay::new}. The constructor's parameters
     * must match the functional interface's method signature exactly.
     */
    @Test
    void constructor_reference() {
        Guest alice = HotelFixtures.sampleGuests().get(0);
        Room room101 = rooms.get(0);

        // TODO: koan — use a constructor reference for GuestStay::new
        StayFactory factory = null;

        assertThat(HotelFixtures.isLambda(factory)).isTrue();
        GuestStay stay = factory.create("S-TMP", alice, room101, 1, LocalDate.of(2026, 8, 7));
        assertThat(stay.getStayId()).isEqualTo("S-TMP");
        assertThat(stay.getGuest().getName()).isEqualTo("Alice Nguyen");
        assertThat(stay.getPartySize()).isEqualTo(1);
    }

    @FunctionalInterface
    interface StayFactory {
        GuestStay create(String stayId, Guest guest, Room room, int partySize, LocalDate checkInDate);
    }
}
