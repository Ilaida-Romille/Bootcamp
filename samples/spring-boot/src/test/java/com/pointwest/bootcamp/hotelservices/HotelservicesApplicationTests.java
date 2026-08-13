package com.pointwest.bootcamp.hotelservices;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.pointwest.bootcamp.hotelservices.controller.FacilityController;
import com.pointwest.bootcamp.hotelservices.controller.RoomController;
import com.pointwest.bootcamp.hotelservices.dto.FacilityDto;
import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.model.Room;
import com.pointwest.bootcamp.hotelservices.repository.RoomRepository;
import com.pointwest.bootcamp.hotelservices.repository.summary.RoomStaySummary;

@Sql("/testdata.sql")
@SpringBootTest
class HotelservicesApplicationTests {

	@Autowired
	private RoomController roomController;

	@Autowired
	private FacilityController facilityController;

	@Autowired 
	private RoomRepository roomRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setupBeforeEachTest() {
		// Room room1 = new Room("1", 1, "studio", 2, Room.HousekeepingStatus.VACANT_CLEAN);
		// roomRepository.save(room1);
	}

	@AfterEach
	void cleanupAfterEachTest() {
		// Delete child rows first to satisfy the guest_stay.room_id foreign key.
		jdbcTemplate.update("DELETE FROM guest_stay");

		jdbcTemplate.update("DELETE FROM space");
	}

	@Test
	void roomGuestStaySummary() {
		List<RoomStaySummary> roomStays = roomRepository.findRoomStaySummary();

		roomStays.forEach(rs -> {
			String roomStayStr = String.format("Room: %s, Stays: %d", rs.getRoomNumber(), rs.getStayCount());
			System.out.println(roomStayStr);
	    });
	}

	@Test
	void searchRoomByName() {
		List<Room> foundRooms = roomRepository.findByNameContainingIgnoreCaseOrRoomTypeContainingIgnoreCaseOrderByRoomNumberAsc("Standard", "Standard");

		System.out.println("Found: " + foundRooms.size());
		foundRooms.stream().forEach(room -> System.out.println(room.getName()));
	}

	@Test
	void roomListByHousekeepingStatus() {
		List<RoomDto> rooms = roomController.listRooms();
		assertNotNull(rooms);
	}

	@Test 
	void facilityList() {
		List<FacilityDto> facilities = facilityController.listFacilities();
		assertNotNull(facilities);
	}


}
