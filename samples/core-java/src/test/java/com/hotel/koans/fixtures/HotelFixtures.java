package com.hotel.koans.fixtures;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.hotel.domain.AmenityUsage;
import com.hotel.domain.Certification;
import com.hotel.domain.CleaningTask;
import com.hotel.domain.ContactInfo;
import com.hotel.domain.Department;
import com.hotel.domain.ElectricalIssue;
import com.hotel.domain.Facility;
import com.hotel.domain.Floor;
import com.hotel.domain.Guest;
import com.hotel.domain.GuestStay;
import com.hotel.domain.Hotel;
import com.hotel.domain.HvacIssue;
import com.hotel.domain.Inspection;
import com.hotel.domain.MaintenanceRequest;
import com.hotel.domain.MealAttendance;
import com.hotel.domain.MealSession;
import com.hotel.domain.PlumbingIssue;
import com.hotel.domain.Room;
import com.hotel.domain.Staff;
import com.hotel.domain.StructuralIssue;

/**
 * Shared test data factory for the koan exercises.
 *
 * <p>Every koan file builds from these factories so assertions can rely on the
 * literal values below. Rooms are returned in ascending room-number order
 * (index 0 = room 101 ... index 7 = room 302); staff in the order
 * [Maria, Jonas, Raj, Lena, Tom]; guests in the order [Alice, Bob, Carol, Dave].
 *
 * <p><b>SAMPLE DATA</b> (koans may assert these exact values):
 * <pre>
 * HOTEL: "Grand Meridian"
 *
 * ROOMS (8) — room number, floor, type, tags, nightly rate, final housekeeping status
 *   101 | 1 | STANDARD | -                               | 120.00 | VACANT_CLEAN
 *   102 | 1 | STANDARD | ACCESSIBLE                      | 130.00 | VACANT_DIRTY
 *   103 | 1 | DELUXE   | SEA_VIEW, BALCONY               | 200.00 | VACANT_CLEAN
 *   201 | 2 | STANDARD | -                               | 120.00 | OCCUPIED_DIRTY
 *   202 | 2 | DELUXE   | SEA_VIEW                         | 190.00 | VACANT_CLEAN
 *   203 | 2 | SUITE    | SEA_VIEW, BALCONY, ACCESSIBLE   | 350.00 | VACANT_DIRTY
 *   301 | 3 | DELUXE   | BALCONY                          | 210.00 | VACANT_CLEAN
 *   302 | 3 | SUITE    | SEA_VIEW                         | 380.00 | OUT_OF_ORDER
 *   -> sum of nightly rates: 1700.00; SEA_VIEW: 4 rooms; ACCESSIBLE: 2; BALCONY: 3
 *   -> available rooms (OPERATIONAL + VACANT_CLEAN): 101, 103, 202, 301
 *   -> floors: 1 hosts [101,102,103], 2 hosts [201,202,203], 3 hosts [301,302]
 *
 * FACILITIES (3): "Main Pool" (POOL, cap 40), "Fitness Center" (GYM, cap 25),
 *                 "Breakfast Hall" (FOOD_AND_BEVERAGE, cap 80)
 *
 * DEPARTMENTS (2): "Housekeeping", "Engineering"
 *
 * STAFF (5) — id, name, role, department, certifications, supervisor
 *   STAFF-1 Maria Santos  | Housekeeping Supervisor | Housekeeping | FIRST_AID, FOOD_HANDLER | -
 *   STAFF-2 Jonas Weber   | Housekeeper             | Housekeeping | FIRST_AID               | Maria
 *   STAFF-3 Raj Patel     | Chief Engineer          | Engineering  | HVAC_LICENSED, FIRST_AID | -
 *   STAFF-4 Lena Fischer  | Maintenance Technician  | Engineering  | HVAC_LICENSED          | Raj
 *   STAFF-5 Tom Alvarez   | Maintenance Technician  | Engineering  | -                       | Raj
 *
 * GUESTS (4): GUEST-1 Alice Nguyen (GOLD, contact), GUEST-2 Bob Okafor (no tier, contact),
 *             GUEST-3 Carol Diaz (PLATINUM, contact), GUEST-4 Dave Kim (SILVER, no contact)
 *
 * STAYS (5) — id, guest, room, party, check-in, check-out (null = still in-house)
 *   S-1001 Alice | 102 | 2 | 2026-07-10 -> 2026-07-13 | 3 nights
 *   S-1002 Alice | 203 | 1 | 2026-07-20 -> 2026-07-22 | 2 nights
 *   S-1003 Bob   | 103 | 3 | 2026-07-15 -> 2026-07-16 | 1 night
 *   S-1004 Carol | 201 | 2 | 2026-08-05 -> (in-house) | nightsStayed() depends on today
 *   S-1005 Dave  | 102 | 1 | 2026-08-01 -> 2026-08-04 | 3 nights
 *
 * AMENITY USAGES (5): pool S-1004 75min, gym S-1004 45min, pool S-1005 60min,
 *                     gym S-1003 60min, pool S-1001 60min
 *
 * MEAL SESSIONS (3, all at Breakfast Hall): MS-2026-08-05-BREAKFAST (S-1004 x2, S-1005 x1),
 *              MS-2026-08-06-BREAKFAST (S-1004 x2), MS-2026-07-15-LUNCH (S-1003 x3)
 *
 * CLEANING TASKS (7): CT-1001 room 101 COMPLETED (Jonas), CT-1002 room 103 COMPLETED (Maria),
 *                     CT-1003 room 201 IN_PROGRESS (Jonas), CT-1004 pool IN_PROGRESS (Maria),
 *                     CT-1005 Breakfast Hall PENDING, CT-1006 room 302 PENDING,
 *                     CT-1007 room 202 IN_PROGRESS (Jonas)
 *   -> assigned-task counts: Jonas 3 (unique max), Maria 2, Raj/Lena/Tom 0
 *
 * MAINTENANCE REQUESTS (4): MR-1001 pool HVAC REPORTED, MR-1002 room 302 electrical ASSIGNED (Lena),
 *                           MR-1003 gym plumbing ASSIGNED (Tom), MR-1004 Breakfast Hall structural RESOLVED (Tom)
 *
 * INSPECTIONS (3): room 301 HOUSEKEEPING_QA PASS (Maria), Breakfast Hall FOOD_SAFETY PASS (Maria),
 *                  pool HEALTH_AND_SAFETY FAIL (Raj)
 * </pre>
 */
public final class HotelFixtures {

    private HotelFixtures() {
    }

    /** True if the given reference is a lambda or method reference (javac naming convention). */
    public static <T> boolean isLambda(T reference) {
        return reference.getClass().getSimpleName().contains("$$Lambda");
    }

    /** The full property: 3 floors, 8 rooms, 3 facilities, 2 departments, 5 staff, 5 stays. */
    public static Hotel sampleHotel() {
        Hotel hotel = new Hotel("Grand Meridian");

        List<Room> rooms = buildRooms();
        Floor floor1 = new Floor(1);
        floor1.addRoom(rooms.get(0));
        floor1.addRoom(rooms.get(1));
        floor1.addRoom(rooms.get(2));
        Floor floor2 = new Floor(2);
        floor2.addRoom(rooms.get(3));
        floor2.addRoom(rooms.get(4));
        floor2.addRoom(rooms.get(5));
        Floor floor3 = new Floor(3);
        floor3.addRoom(rooms.get(6));
        floor3.addRoom(rooms.get(7));
        hotel.addFloor(floor1);
        hotel.addFloor(floor2);
        hotel.addFloor(floor3);

        Facility pool = new Facility("F-POOL", "Main Pool", "POOL", 40);
        Facility gym = new Facility("F-GYM", "Fitness Center", "GYM", 25);
        Facility breakfastHall = new Facility("F-BREAKFAST", "Breakfast Hall", "FOOD_AND_BEVERAGE", 80);
        hotel.addFacility(pool);
        hotel.addFacility(gym);
        hotel.addFacility(breakfastHall);

        Department housekeeping = new Department("Housekeeping");
        Department engineering = new Department("Engineering");
        hotel.addDepartment(housekeeping);
        hotel.addDepartment(engineering);

        List<Staff> staffList = buildStaff(housekeeping, engineering);
        Staff maria = staffList.get(0);
        Staff jonas = staffList.get(1);
        Staff raj = staffList.get(2);
        Staff lena = staffList.get(3);
        Staff tom = staffList.get(4);

        List<Guest> guests = buildGuests();
        List<GuestStay> stays = buildStays(rooms, guests);
        for (GuestStay stay : stays) {
            hotel.recordStay(stay);
        }

        new AmenityUsage(pool, stays.get(3), LocalDateTime.of(2026, 8, 5, 9, 30))
                .exit(LocalDateTime.of(2026, 8, 5, 10, 45));
        new AmenityUsage(gym, stays.get(3), LocalDateTime.of(2026, 8, 6, 7, 0))
                .exit(LocalDateTime.of(2026, 8, 6, 7, 45));
        new AmenityUsage(pool, stays.get(4), LocalDateTime.of(2026, 8, 2, 16, 0))
                .exit(LocalDateTime.of(2026, 8, 2, 17, 0));
        new AmenityUsage(gym, stays.get(2), LocalDateTime.of(2026, 7, 15, 18, 0))
                .exit(LocalDateTime.of(2026, 7, 15, 19, 0));
        new AmenityUsage(pool, stays.get(0), LocalDateTime.of(2026, 7, 11, 10, 0))
                .exit(LocalDateTime.of(2026, 7, 11, 11, 0));

        MealSession ms1 = new MealSession("MS-2026-08-05-BREAKFAST", breakfastHall, "BREAKFAST");
        new MealAttendance(ms1, stays.get(3), 2, LocalDateTime.of(2026, 8, 5, 8, 10));
        new MealAttendance(ms1, stays.get(4), 1, LocalDateTime.of(2026, 8, 5, 8, 30));
        MealSession ms2 = new MealSession("MS-2026-08-06-BREAKFAST", breakfastHall, "BREAKFAST");
        new MealAttendance(ms2, stays.get(3), 2, LocalDateTime.of(2026, 8, 6, 8, 5));
        MealSession ms3 = new MealSession("MS-2026-07-15-LUNCH", breakfastHall, "LUNCH");
        new MealAttendance(ms3, stays.get(2), 3, LocalDateTime.of(2026, 7, 15, 12, 20));

        buildCleaningTasks(rooms, pool, breakfastHall, maria, jonas);
        buildMaintenanceRequests(rooms, pool, gym, breakfastHall, lena, tom);
        buildInspections(rooms, pool, breakfastHall, maria, raj);

        return hotel;
    }

    /** The 8 rooms in ascending room-number order (index 0 = 101 ... index 7 = 302). */
    public static List<Room> sampleRooms() {
        return buildRooms();
    }

    /**
     * The 5 staff in order [Maria, Jonas, Raj, Lena, Tom], with fresh departments.
     * Fresh cleaning tasks are assigned exactly as in the sample: Jonas 3
     * (CT-1001, CT-1003, CT-1007), Maria 2 (CT-1002, CT-1004), others none.
     */
    public static List<Staff> sampleStaff() {
        List<Staff> staff = buildStaff(new Department("Housekeeping"), new Department("Engineering"));
        List<Room> rooms = buildRooms();
        Facility pool = new Facility("F-POOL", "Main Pool", "POOL", 40);
        Facility breakfastHall = new Facility("F-BREAKFAST", "Breakfast Hall", "FOOD_AND_BEVERAGE", 80);
        buildCleaningTasks(rooms, pool, breakfastHall, staff.get(0), staff.get(1));
        return staff;
    }

    /** The 4 guests in order [Alice, Bob, Carol, Dave]. */
    public static List<Guest> sampleGuests() {
        return buildGuests();
    }

    /** The 7 cleaning tasks, freshly built with fresh rooms/facilities/staff. */
    public static List<CleaningTask> sampleCleaningTasks() {
        List<Room> rooms = buildRooms();
        Facility pool = new Facility("F-POOL", "Main Pool", "POOL", 40);
        Facility breakfastHall = new Facility("F-BREAKFAST", "Breakfast Hall", "FOOD_AND_BEVERAGE", 80);
        List<Staff> staff = buildStaff(new Department("Housekeeping"), new Department("Engineering"));
        return buildCleaningTasks(rooms, pool, breakfastHall, staff.get(0), staff.get(1));
    }

    /** The 4 maintenance requests, freshly built with fresh rooms/facilities/staff. */
    public static List<MaintenanceRequest> sampleMaintenanceRequests() {
        List<Room> rooms = buildRooms();
        Facility pool = new Facility("F-POOL", "Main Pool", "POOL", 40);
        Facility gym = new Facility("F-GYM", "Fitness Center", "GYM", 25);
        Facility breakfastHall = new Facility("F-BREAKFAST", "Breakfast Hall", "FOOD_AND_BEVERAGE", 80);
        List<Staff> staff = buildStaff(new Department("Housekeeping"), new Department("Engineering"));
        return buildMaintenanceRequests(rooms, pool, gym, breakfastHall, staff.get(3), staff.get(4));
    }

    /** The 3 inspections, freshly built with fresh rooms/facilities/staff. */
    public static List<Inspection> sampleInspections() {
        List<Room> rooms = buildRooms();
        Facility pool = new Facility("F-POOL", "Main Pool", "POOL", 40);
        Facility breakfastHall = new Facility("F-BREAKFAST", "Breakfast Hall", "FOOD_AND_BEVERAGE", 80);
        List<Staff> staff = buildStaff(new Department("Housekeeping"), new Department("Engineering"));
        return buildInspections(rooms, pool, breakfastHall, staff.get(0), staff.get(2));
    }

    public static void sendStaffToFixRoomAircon(String roomId, String staffId) {
            sampleRooms().stream().filter(rm -> rm.getSpaceId().equals(roomId))
                         .findFirst().get().requireAvailable();
            sampleStaff().stream().filter(stf -> stf.getStaffId().equals(staffId))
                         .findFirst().get().requireCertifiedFor(Certification.HVAC_LICENSED);
    }

    private static List<Room> buildRooms() {
        Room room101 = new Room("101", 1, "STANDARD", Set.of(), new BigDecimal("120.00"));
        Room room102 = new Room("102", 1, "STANDARD", Set.of("ACCESSIBLE"), new BigDecimal("130.00"));
        room102.setHousekeepingStatus(Room.HousekeepingStatus.VACANT_DIRTY);
        Room room103 = new Room("103", 1, "DELUXE", Set.of("SEA_VIEW", "BALCONY"), new BigDecimal("200.00"));
        Room room201 = new Room("201", 2, "STANDARD", Set.of(), new BigDecimal("120.00"));
        room201.setHousekeepingStatus(Room.HousekeepingStatus.OCCUPIED_DIRTY);
        Room room202 = new Room("202", 2, "DELUXE", Set.of("SEA_VIEW"), new BigDecimal("190.00"));
        Room room203 = new Room("203", 2, "SUITE", Set.of("SEA_VIEW", "BALCONY", "ACCESSIBLE"), new BigDecimal("350.00"));
        room203.setHousekeepingStatus(Room.HousekeepingStatus.VACANT_DIRTY);
        Room room301 = new Room("301", 3, "DELUXE", Set.of("BALCONY"), new BigDecimal("210.00"));
        Room room302 = new Room("302", 3, "SUITE", Set.of("SEA_VIEW"), new BigDecimal("380.00"));
        room302.setHousekeepingStatus(Room.HousekeepingStatus.OUT_OF_ORDER);
        return List.of(room101, room102, room103, room201, room202, room203, room301, room302);
    }

    private static List<Staff> buildStaff(Department housekeeping, Department engineering) {
        Staff maria = new Staff("STAFF-1", "Maria Santos", "Housekeeping Supervisor", housekeeping);
        maria.addCertification(Certification.FIRST_AID);
        maria.addCertification(Certification.FOOD_HANDLER);
        Staff jonas = new Staff("STAFF-2", "Jonas Weber", "Housekeeper", housekeeping);
        jonas.addCertification(Certification.FIRST_AID);
        jonas.setSupervisor(maria);
        Staff raj = new Staff("STAFF-3", "Raj Patel", "Chief Engineer", engineering);
        raj.addCertification(Certification.HVAC_LICENSED);
        raj.addCertification(Certification.FIRST_AID);
        Staff lena = new Staff("STAFF-4", "Lena Fischer", "Maintenance Technician", engineering);
        lena.addCertification(Certification.HVAC_LICENSED);
        lena.setSupervisor(raj);
        Staff tom = new Staff("STAFF-5", "Tom Alvarez", "Maintenance Technician", engineering);
        tom.setSupervisor(raj);
        return List.of(maria, jonas, raj, lena, tom);
    }

    private static List<Guest> buildGuests() {
        Guest alice = new Guest("GUEST-1", "Alice Nguyen", new ContactInfo("alice@example.com", "+1-555-0101"));
        alice.setLoyaltyTier("GOLD");
        Guest bob = new Guest("GUEST-2", "Bob Okafor", new ContactInfo("bob@example.com", "+1-555-0102"));
        Guest carol = new Guest("GUEST-3", "Carol Diaz", new ContactInfo("carol@example.com", "+1-555-0103"));
        carol.setLoyaltyTier("PLATINUM");
        Guest dave = new Guest("GUEST-4", "Dave Kim");
        dave.setLoyaltyTier("SILVER");
        return List.of(alice, bob, carol, dave);
    }

    private static List<GuestStay> buildStays(List<Room> rooms, List<Guest> guests) {
        Guest alice = guests.get(0);
        Guest bob = guests.get(1);
        Guest carol = guests.get(2);
        Guest dave = guests.get(3);

        GuestStay s1001 = new GuestStay("S-1001", alice, rooms.get(1), 2, LocalDate.of(2026, 7, 10));
        s1001.checkOut(LocalDate.of(2026, 7, 13));
        GuestStay s1002 = new GuestStay("S-1002", alice, rooms.get(5), 1, LocalDate.of(2026, 7, 20));
        s1002.checkOut(LocalDate.of(2026, 7, 22));
        GuestStay s1003 = new GuestStay("S-1003", bob, rooms.get(2), 3, LocalDate.of(2026, 7, 15));
        s1003.checkOut(LocalDate.of(2026, 7, 16));
        GuestStay s1004 = new GuestStay("S-1004", carol, rooms.get(3), 2, LocalDate.of(2026, 8, 5));
        GuestStay s1005 = new GuestStay("S-1005", dave, rooms.get(1), 1, LocalDate.of(2026, 8, 1));
        s1005.checkOut(LocalDate.of(2026, 8, 4));
        return List.of(s1001, s1002, s1003, s1004, s1005);
    }

    private static List<CleaningTask> buildCleaningTasks(List<Room> rooms, Facility pool,
                                                         Facility breakfastHall, Staff maria, Staff jonas) {
        CleaningTask ct1001 = new CleaningTask("CT-1001", rooms.get(0), "TURNOVER_CLEANING");
        ct1001.assignTo(jonas);
        ct1001.complete();
        CleaningTask ct1002 = new CleaningTask("CT-1002", rooms.get(2), "TURNOVER_CLEANING");
        ct1002.assignTo(maria);
        ct1002.complete();
        CleaningTask ct1003 = new CleaningTask("CT-1003", rooms.get(3), "DAILY_CLEANING");
        ct1003.assignTo(jonas);
        CleaningTask ct1004 = new CleaningTask("CT-1004", pool, "DEEP_CLEANING");
        ct1004.assignTo(maria);
        CleaningTask ct1005 = new CleaningTask("CT-1005", breakfastHall, "PUBLIC_AREA_CLEANING");
        CleaningTask ct1006 = new CleaningTask("CT-1006", rooms.get(7), "DEEP_CLEANING");
        CleaningTask ct1007 = new CleaningTask("CT-1007", rooms.get(4), "LINEN_CHANGE");
        ct1007.assignTo(jonas);
        return List.of(ct1001, ct1002, ct1003, ct1004, ct1005, ct1006, ct1007);
    }

    private static List<MaintenanceRequest> buildMaintenanceRequests(List<Room> rooms, Facility pool,
                                                                     Facility gym, Facility breakfastHall,
                                                                     Staff lena, Staff tom) {
        MaintenanceRequest mr1001 = new MaintenanceRequest("MR-1001", pool, new HvacIssue("pool_dehumidifier", 24));
        MaintenanceRequest mr1002 = new MaintenanceRequest("MR-1002", rooms.get(7), new ElectricalIssue("wall_outlet", true));
        mr1002.assignTo(lena);
        MaintenanceRequest mr1003 = new MaintenanceRequest("MR-1003", gym, new PlumbingIssue("shower_head", false));
        mr1003.assignTo(tom);
        MaintenanceRequest mr1004 = new MaintenanceRequest("MR-1004", breakfastHall, new StructuralIssue("kitchen_floor", "minor"));
        mr1004.assignTo(tom);
        mr1004.resolve();
        return List.of(mr1001, mr1002, mr1003, mr1004);
    }

    private static List<Inspection> buildInspections(List<Room> rooms, Facility pool,
                                                     Facility breakfastHall, Staff maria, Staff raj) {
        Inspection in2001 = new Inspection("IN-2001", rooms.get(6), "HOUSEKEEPING_QA", maria);
        in2001.recordResult(Inspection.Result.PASS);
        Inspection in2002 = new Inspection("IN-2002", breakfastHall, "FOOD_SAFETY", maria);
        in2002.recordResult(Inspection.Result.PASS);
        Inspection in2003 = new Inspection("IN-2003", pool, "HEALTH_AND_SAFETY", raj);
        in2003.recordResult(Inspection.Result.FAIL);
        return List.of(in2001, in2002, in2003);
    }
}
