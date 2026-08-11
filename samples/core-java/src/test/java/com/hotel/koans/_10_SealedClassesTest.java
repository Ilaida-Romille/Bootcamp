package com.hotel.koans;

import com.hotel.domain.ElectricalIssue;
import com.hotel.domain.HvacIssue;
import com.hotel.domain.MaintenanceIssue;
import com.hotel.domain.MaintenanceRequest;
import com.hotel.domain.PlumbingIssue;
import com.hotel.domain.StructuralIssue;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sealed types: a closed set of subtypes, every permitted subtype being
 * assignable to the sealed interface, and exhaustiveness — the compiler knows
 * all subtypes, so a switch expression needs no {@code default}.
 */
class _10_SealedClassesTest {

    private List<MaintenanceRequest> requests;

    @BeforeEach
    void setUp() {
        requests = HotelFixtures.sampleMaintenanceRequests();
    }

    /**
     * The sealed {@link MaintenanceIssue} permits exactly four records. All four
     * may be assigned to a {@code MaintenanceIssue} reference — the compiler
     * guarantees no other implementation exists.
     */
    @Test
    void permitted_subtypes_are_assignable() {
        // TODO: koan — one per permitted subtype:
        //              HvacIssue("pool_dehumidifier", 24), ElectricalIssue("wall_outlet", true),
        //              PlumbingIssue("shower_head", false), StructuralIssue("kitchen_floor", "minor")
        MaintenanceIssue dehumidifier = new HvacIssue("pool_dehumidifier", 24);
        MaintenanceIssue wallOutlet = new ElectricalIssue("wall_outlet", true);
        MaintenanceIssue showerHead = new PlumbingIssue("shower_head", false);
        MaintenanceIssue floor = new StructuralIssue("kitchen_floor", "minor");

        assertThat(dehumidifier).isInstanceOf(HvacIssue.class);
        assertThat(wallOutlet).isInstanceOf(ElectricalIssue.class);
        assertThat(showerHead).isInstanceOf(PlumbingIssue.class);
        assertThat(floor).isInstanceOf(StructuralIssue.class);
    }

    /**
     * A switch expression over a sealed type is exhaustive without a
     * {@code default}: every case is known at compile time. MR-1001 is an HVAC
     * issue at the pool.
     */
    @Test
    void switch_expression_needs_no_default_on_sealed_type() {
        MaintenanceIssue issue = requests.get(0).getIssue();
        // TODO: koan — write a switch expression over issue with all four type
        //              patterns (PlumbingIssue/ElectricalIssue/HvacIssue/StructuralIssue)
        //              returning the SHORT name, and NO default
        String category = switch (issue) {
            case PlumbingIssue plumbing -> "PLUMBING";
            case ElectricalIssue electrical -> "ELECTRICAL";
            case HvacIssue hvac -> "HVAC";
            case StructuralIssue structural -> "STRUCTURAL";
        };

        assertThat(category).isEqualTo("HVAC");
    }

    /**
     * {@code instanceof} chains stay safe: a new subtype can only appear if the
     * sealed declaration is changed. Extract the per-type detail field — MR-1001
     * is an HvacIssue whose unit is "pool_dehumidifier".
     */
    @Test
    void instanceof_chain_on_sealed_hierarchy() {
        MaintenanceIssue issue = requests.get(0).getIssue();
        // TODO: koan — if/else instanceof chain binding the record and reading its
        //              detail field: HvacIssue.unit(), ElectricalIssue.component(),
        //              PlumbingIssue.fixture(), StructuralIssue.area()
        String detail;
        if (issue instanceof HvacIssue hvac) {
            detail = hvac.unit();
        } else if (issue instanceof ElectricalIssue electrical) {
            detail = electrical.component();
        } else if (issue instanceof PlumbingIssue plumbing) {
            detail = plumbing.fixture();
        } else {
            detail = ((StructuralIssue) issue).area();
        }

        assertThat(detail).isEqualTo("pool_dehumidifier");
    }

    /**
     * Sealed patterns combine with streams: classify every request in one pass.
     * Requests come back in the order HVAC, ELECTRICAL, PLUMBING, STRUCTURAL.
     */
    @Test
    void classify_all_requests_with_sealed_patterns() {
        // TODO: koan — stream over requests, switch on getIssue(), collect SHORT names
        List<String> categories = requests.stream()
                .map(request -> switch (request.getIssue()) {
                    case PlumbingIssue plumbing -> "PLUMBING";
                    case ElectricalIssue electrical -> "ELECTRICAL";
                    case HvacIssue hvac -> "HVAC";
                    case StructuralIssue structural -> "STRUCTURAL";
                })
                .toList();

        assertThat(categories).containsExactly("HVAC", "ELECTRICAL", "PLUMBING", "STRUCTURAL");
    }

    /**
     * Records give patterns with deconstructed components: match on the record
     * pattern and bind the fields directly. MR-1001: unit "pool_dehumidifier",
     * target temperature 24C.
     */
    @Test
    void record_deconstruction_patterns_on_sealed_types() {
        MaintenanceIssue issue = requests.get(0).getIssue();
        // TODO: koan — switch expression over issue; the HvacIssue case binds
        //              (var unit, var targetTempC) and returns unit + " at " + targetTempC + "C"
        String summary = switch (issue) {
            case HvacIssue(var unit, var targetTempC) -> unit + " at " + targetTempC + "C";
            case ElectricalIssue(var component, boolean sparking) -> component;
            case PlumbingIssue(var fixture, boolean activeLeak) -> fixture;
            case StructuralIssue(var area, String severity) -> area;
        };

        assertThat(summary).isEqualTo("pool_dehumidifier at 24C");
    }
}
