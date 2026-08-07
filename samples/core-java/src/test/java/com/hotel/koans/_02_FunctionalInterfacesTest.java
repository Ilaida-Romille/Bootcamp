package com.hotel.koans;

import com.hotel.domain.Guest;
import com.hotel.domain.Room;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional interfaces: defining a custom {@code @FunctionalInterface}, using
 * default methods on it, and the common built-in shapes {@link BiFunction} and
 * {@link UnaryOperator}.
 */
class _02_FunctionalInterfacesTest {

    /** A custom functional interface: exactly one abstract method. */
    @FunctionalInterface
    interface RateAdjuster {
        BigDecimal adjust(BigDecimal base);

        /** Default methods are allowed and do not break the single-abstract-method rule. */
        default RateAdjuster andThen(RateAdjuster after) {
            return base -> after.adjust(adjust(base));
        }
    }

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
    }

    /**
     * A lambda can implement a custom functional interface: the lambda body is the
     * implementation of its single abstract method. Room 101 rates at 120.00.
     */
    @Test
    void custom_functional_interface_with_lambda() {
        // TODO: koan — a RateAdjuster that raises a rate by 10.00
        RateAdjuster raiseByTen = null;

        assertThat(HotelFixtures.isLambda(raiseByTen)).isTrue();
        assertThat(raiseByTen.adjust(new BigDecimal("120.00"))).isEqualByComparingTo("130.00");
    }

    /**
     * Pass a lambda into a method that expects a functional interface — the
     * idiomatic way to make behavior pluggable.
     */
    @Test
    void lambda_passed_to_a_method() {
        // TODO: koan — pass a RateAdjuster lambda that doubles the rate
        BigDecimal doubled = adjustedRate(rooms.get(0), null);

        assertThat(doubled).isEqualByComparingTo("240.00");
    }

    /**
     * A default method composes an existing functional interface. Here
     * {@code andThen} chains two adjustments: +10.00 followed by -5.00.
     */
    @Test
    void default_method_composition() {
        RateAdjuster raiseByTen = rate -> rate.add(new BigDecimal("10.00"));
        // TODO: koan — compose raiseByTen with a -5.00 adjustment via the default method
        RateAdjuster net = null;

        assertThat(HotelFixtures.isLambda(net)).isTrue();
        assertThat(net.adjust(new BigDecimal("120.00"))).isEqualByComparingTo("125.00");
    }

    /**
     * {@link BiFunction} takes two arguments. Keep the transformation pure: build a
     * NEW room with the scaled rate instead of mutating the original.
     */
    @Test
    void bifunction_scaling_a_room_rate() {
        // TODO: koan — a BiFunction that returns a new Room whose rate is the original times the factor
        BiFunction<Room, BigDecimal, Room> scaleRate = null;

        Room scaled = scaleRate.apply(rooms.get(0), new BigDecimal("1.50"));
        assertThat(scaled.getSpaceId()).isEqualTo("101");
        assertThat(scaled.getNightlyRate()).isEqualByComparingTo("180.00");
        assertThat(rooms.get(0).getNightlyRate()).isEqualByComparingTo("120.00");
    }

    /**
     * {@link UnaryOperator} is a {@code Function<T, T>} — same input and output
     * type. Use one as a name normalizer.
     */
    @Test
    void unary_operator_normalizing_a_name() {
        // TODO: koan — a UnaryOperator that uppercases a guest name
        UnaryOperator<String> normalize = null;

        assertThat(HotelFixtures.isLambda(normalize)).isTrue();
        assertThat(normalize.apply("Alice Nguyen")).isEqualTo("ALICE NGUYEN");
    }

    /**
     * A method reference can implement a custom functional interface too, as long
     * as its signature matches the abstract method. {@code BigDecimal::negate}
     * fits {@code adjust(BigDecimal) -> BigDecimal}.
     */
    @Test
    void method_reference_as_custom_functional_interface() {
        // TODO: koan — implement RateAdjuster with a BigDecimal::negate method reference
        RateAdjuster negate = null;

        assertThat(HotelFixtures.isLambda(negate)).isTrue();
        assertThat(negate.adjust(new BigDecimal("120.00"))).isEqualByComparingTo("-120.00");
    }

    private static BigDecimal adjustedRate(Room room, RateAdjuster adjuster) {
        return adjuster.adjust(room.getNightlyRate());
    }
}
