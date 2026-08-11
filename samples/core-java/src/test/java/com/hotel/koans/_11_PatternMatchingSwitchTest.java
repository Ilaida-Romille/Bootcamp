package com.hotel.koans;

import com.hotel.domain.Certification;
import com.hotel.domain.MaintenanceRequest;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.koans.fixtures.HotelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pattern matching for {@code switch}: expression form, arrow labels, block
 * rules with {@code yield}, type patterns, guards, and the {@code null} case.
 */
class _11_PatternMatchingSwitchTest {

    private List<Room> rooms;
    private List<Staff> staff;

    @BeforeEach
    void setUp() {
        rooms = HotelFixtures.sampleRooms();
        staff = HotelFixtures.sampleStaff();
    }

    /**
     * A switch expression returns a value; arrow rules need no {@code break}.
     * Give each housekeeping status a short label — room 101 is VACANT_CLEAN
     * ("ready"), room 302 is OUT_OF_ORDER ("blocked").
     */
    @Test
    void switch_expression_with_arrow_labels() {
        Room ready = rooms.get(0);
        Room blocked = rooms.get(7);
        // TODO: koan — write a switch expression over HousekeepingStatus with arrow rules:
        //              VACANT_CLEAN -> "ready", VACANT_DIRTY -> "to clean",
        //              OCCUPIED_CLEAN/OCCUPIED_DIRTY -> "occupied",
        //              OUT_OF_ORDER/OUT_OF_SERVICE -> "blocked"
        String statusOfReadyRoom = switch (ready.getHousekeepingStatus()) {
            case VACANT_CLEAN -> "ready";
            case VACANT_DIRTY -> "to clean";
            case OCCUPIED_CLEAN, OCCUPIED_DIRTY -> "occupied";
            case OUT_OF_ORDER, OUT_OF_SERVICE -> "blocked";
        };
        String statusOfBlockedRoom = switch (blocked.getHousekeepingStatus()) {
            case VACANT_CLEAN -> "ready";
            case VACANT_DIRTY -> "to clean";
            case OCCUPIED_CLEAN, OCCUPIED_DIRTY -> "occupied";
            case OUT_OF_ORDER, OUT_OF_SERVICE -> "blocked";
        };

        assertThat(statusOfReadyRoom).isEqualTo("ready");
        assertThat(statusOfBlockedRoom).isEqualTo("blocked");
    }

    /**
     * Block rules run several statements before the value; finish with
     * {@code yield}. Rate 380.00 belongs to the penthouse tier, anything else
     * below 200.00 to standard. BigDecimal has no {@code >=} — compare with
     * {@code compareTo(new BigDecimal("300")) >= 0}.
     */
    @Test
    void switch_expression_with_block_rules_and_yield() {
        Room suite = rooms.get(7);
        // TODO: koan — switch expression over suite.getNightlyRate() with type
        //              patterns and block rules (remember: a default is required):
        //              case BigDecimal rate when rate.compareTo(new BigDecimal("300")) >= 0 -> yield "penthouse";
        //              case BigDecimal rate when rate.compareTo(new BigDecimal("200")) >= 0 -> yield "deluxe";
        //              default -> yield "standard"
        String tier = switch (suite.getNightlyRate()) {
            case BigDecimal rate when rate.compareTo(new BigDecimal("300")) >= 0 -> {
                yield "penthouse";
            }
            case BigDecimal rate when rate.compareTo(new BigDecimal("200")) >= 0 -> {
                yield "deluxe";
            }
            default -> {
                yield "standard";
            }
        };

        assertThat(tier).isEqualTo("penthouse");
    }

    /**
     * Type patterns match a value against a type; the {@code case null} label
     * handles absence explicitly. MR-1001 has no assignee, MR-1002 is assigned
     * to Lena.
     */
    @Test
    void type_patterns_and_the_null_case() {
        List<MaintenanceRequest> requests = HotelFixtures.sampleMaintenanceRequests();
        // TODO: koan — write a switch expression over the nullable assignee:
        //              case Staff s -> s.getName(); case null -> "unassigned"
        String firstAssignee = switch (requests.get(0).getAssignee()) {
            case Staff member -> member.getName();
            case null -> "unassigned";
        };
        String secondAssignee = switch (requests.get(1).getAssignee()) {
            case Staff member -> member.getName();
            case null -> "unassigned";
        };

        assertThat(firstAssignee).isEqualTo("unassigned");
        assertThat(secondAssignee).isEqualTo("Lena Fischer");
    }

    /**
     * Guards ({@code when}) pick between patterns of the same type. Order
     * matters: check HVAC first, then FIRST_AID, then the rest. Raj holds both,
     * so he must come out HVAC.
     */
    @Test
    void guarded_type_patterns() {
        // TODO: koan — for each staff member, switch expression over the member with guards:
        //              Staff s when s.isCertifiedFor(HVAC_LICENSED) -> "HVAC";
        //              Staff s when s.isCertifiedFor(FIRST_AID) -> "FIRST_AID"; default -> "GENERAL"
        List<String> classifications = staff.stream()
                .map(member -> switch (member) {
                    case Staff s when s.isCertifiedFor(Certification.HVAC_LICENSED) -> "HVAC";
                    case Staff s when s.isCertifiedFor(Certification.FIRST_AID) -> "FIRST_AID";
                    default -> "GENERAL";
                })
                .toList();

        assertThat(classifications).containsExactly("FIRST_AID", "FIRST_AID", "HVAC", "HVAC", "GENERAL");
    }

    /**
     * A switch statement with arrow rules performs side effects without
     * fall-through. Classify each member of the engineering team into a shared
     * log line.
     */
    @Test
    void switch_statement_arrow_rules_for_side_effects() {
        List<Staff> engineering = List.of(staff.get(2), staff.get(3), staff.get(4));
        StringBuilder log = new StringBuilder();
        for (Staff member : engineering) {
            // TODO: koan — switch statement over member.getRole() with arrow rules:
            //              "Chief Engineer" -> log "lead", "Maintenance Technician" -> log "tech",
            //              anything else -> log "other"
            switch (member.getRole()) {
                case "Chief Engineer" -> log.append("lead;");
                case "Maintenance Technician" -> log.append("tech;");
                default -> log.append("other;");
            }
        }

        assertThat(log.toString()).isEqualTo("lead;tech;tech;");
    }

    /**
     * Enums are exhaustive too: a switch expression over {@link Certification}
     * compiles without a default. One line per constant, arrow rules.
     */
    @Test
    void enum_switch_expression_is_exhaustive() {
        // TODO: koan — switch expression over Certification.FIRST_AID:
        //              FIRST_AID -> "lifesaving", POOL_LIFEGUARD -> "pool",
        //              FOOD_HANDLER -> "food", HVAC_LICENSED -> "machinery"
        String focus = switch (Certification.FIRST_AID) {
            case FIRST_AID -> "lifesaving";
            case POOL_LIFEGUARD -> "pool";
            case FOOD_HANDLER -> "food";
            case HVAC_LICENSED -> "machinery";
        };

        assertThat(focus).isEqualTo("lifesaving");
    }
}
