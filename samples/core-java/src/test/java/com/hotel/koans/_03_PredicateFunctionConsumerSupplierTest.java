package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.CleaningTask;
import com.hotel.domain.Inspection;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The four built-in functional interface families: {@link Predicate},
 * {@link Function}, {@link Consumer}, {@link Supplier} — plus their combinators
 * ({@code and}, {@code or}, {@code negate}, {@code andThen}) and the two-argument
 * variant {@link BiPredicate}.
 */
class _03_PredicateFunctionConsumerSupplierTest {

    private List<Room> rooms;
    private List<Staff> staff;
    private List<CleaningTask> tasks;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
        staff = HotelFixtures.sampleStaff();
        tasks = HotelFixtures.sampleCleaningTasks();
    }

    /**
     * {@link Predicate}{@code <T>} tests a condition: {@code T -> boolean}.
     * Room 101 is VACANT_CLEAN; room 102 is VACANT_DIRTY.
     */
    @Test
    void predicate_testing_vacancy() {
        // TODO: koan — a Predicate that is true when the room's housekeeping status is VACANT_CLEAN
        Predicate<Room> isVacant = room -> room.getHousekeepingStatus() == Room.HousekeepingStatus.VACANT_CLEAN;

        assertThat(HotelFixtures.isLambda(isVacant)).isTrue();
        assertThat(isVacant.test(rooms.get(0))).isTrue();
        assertThat(isVacant.test(rooms.get(1))).isFalse();
    }

    /**
     * {@code .and(...)} combines two predicates: both must hold. Room 202 is
     * VACANT_CLEAN but rates 190.00 — not below 150.00.
     */
    @Test
    void predicate_combined_with_and() {
        Predicate<Room> isVacant = room -> room.getHousekeepingStatus() == Room.HousekeepingStatus.VACANT_CLEAN;
        // TODO: koan — second predicate: rate is below 150.00, then combine with .and(isVacant)
        Predicate<Room> vacantAndCheap = isVacant.and(room -> room.getNightlyRate().compareTo(new BigDecimal("150")) < 0);
        assertThat(vacantAndCheap.test(rooms.get(0))).isTrue();
        assertThat(vacantAndCheap.test(rooms.get(4))).isFalse();
    }

    /**
     * {@code .or(...)} accepts either condition; {@code .negate()} inverts one.
     * Room 302 is OUT_OF_ORDER, room 102 is not vacant.
     */
    @Test
    void predicate_combined_with_or_and_negate() {
        Predicate<Room> isVacant = room -> room.getHousekeepingStatus() == Room.HousekeepingStatus.VACANT_CLEAN;
        // TODO: koan — `.or(...)` with OUT_OF_ORDER, then `.negate()` the original predicate
        Predicate<Room> vacantOrOutOfOrder = isVacant.or(room -> room.getHousekeepingStatus() == Room.HousekeepingStatus.OUT_OF_ORDER);
        Predicate<Room> notVacant = isVacant.negate();

        assertThat(vacantOrOutOfOrder.test(rooms.get(7))).isTrue();
        assertThat(vacantOrOutOfOrder.test(rooms.get(3))).isFalse();
        assertThat(notVacant.test(rooms.get(1))).isTrue();
        assertThat(notVacant.test(rooms.get(0))).isFalse();
    }

    /**
     * {@link Function}{@code <T, R>} maps one value to another: {@code T -> R}.
     * Extract the department name from a staff member.
     */
    @Test
    void function_extracting_department_name() {
        // TODO: koan — a Function<Staff, String> returning the department name
        Function<Staff, String> departmentName = staff -> staff.getDepartment().getName();

        assertThat(HotelFixtures.isLambda(departmentName)).isTrue();
        assertThat(departmentName.apply(staff.get(0))).isEqualTo("Housekeeping");
        assertThat(departmentName.apply(staff.get(2))).isEqualTo("Engineering");
    }

    /**
     * {@code .andThen(...)} chains two functions: apply the first, then feed the
     * result into the second. Raj is in Engineering.
     */
    @Test
    void function_chained_with_and_then() {
        Function<Staff, String> departmentName = staffMember -> staffMember.getDepartment().getName();
        // TODO: koan — chain .andThen(String::toUpperCase) onto departmentName
        Function<Staff, String> uppercaseDepartment = departmentName.andThen(String::toUpperCase);

        assertThat(uppercaseDepartment.apply(staff.get(2))).isEqualTo("ENGINEERING");
    }

    /**
     * {@link Consumer}{@code <T>} takes a value and returns nothing — for side
     * effects. CT-1005 (Breakfast Hall) starts PENDING.
     */
    @Test
    void consumer_completing_a_task() {
        CleaningTask task = tasks.get(4);
        // TODO: koan — a Consumer<CleaningTask> that marks the task complete (method reference)
        Consumer<CleaningTask> complete = CleaningTask::complete;

        assertThat(HotelFixtures.isLambda(complete)).isTrue();
        complete.accept(task);
        assertThat(task.getStatus()).isEqualTo(CleaningTask.Status.COMPLETED);
    }

    /**
     * {@code .andThen(...)} chains two consumers: both run, in order. CT-1006
     * (room 302) starts PENDING.
     */
    @Test
    void consumer_chained_with_and_then() {
        Consumer<CleaningTask> complete = CleaningTask::complete;
        // TODO: koan — chain a logging consumer (prints the task id) after `complete`
        Consumer<CleaningTask> completeAndLog = complete.andThen(System.out::print);

        completeAndLog.accept(tasks.get(5));
        assertThat(tasks.get(5).getStatus()).isEqualTo(CleaningTask.Status.COMPLETED);
    }

    /**
     * {@link Supplier}{@code <T>} produces a value on demand — deferring work
     * until {@code get()} is actually called.
     */
    @Test
    void supplier_building_an_inspection() {
        Room room301 = rooms.get(6);
        Staff maria = staff.get(0);
        // TODO: koan — a Supplier<Inspection> for a new "HEALTH_AND_SAFETY" inspection of room 301 by Maria
        Supplier<Inspection> inspectionSupplier = () ->  new Inspection("IN-9999", room301, "HEALTH_AND_SAFETY", maria);

        assertThat(HotelFixtures.isLambda(inspectionSupplier)).isTrue();
        Inspection inspection = inspectionSupplier.get();
        assertThat(inspection.getInspectionId()).isEqualTo("IN-9999");
        assertThat(inspection.getLocation().getSpaceId()).isEqualTo("301");
    }

    /**
     * {@link BiPredicate} takes two arguments and returns a boolean — e.g. "is
     * this staff member certified for this certification?" Maria holds FIRST_AID;
     * Tom holds none.
     */
    @Test
    void bipredicate_checking_certification() {
        // TODO: koan — a BiPredicate<Staff, Certification> delegating to isCertifiedFor
        BiPredicate<Staff, Certification> isCertifiedFor = Staff::isCertifiedFor;

        assertThat(HotelFixtures.isLambda(isCertifiedFor)).isTrue();
        assertThat(isCertifiedFor.test(staff.get(0), Certification.FIRST_AID)).isTrue();
        assertThat(isCertifiedFor.test(staff.get(4), Certification.FIRST_AID)).isFalse();
    }
}
