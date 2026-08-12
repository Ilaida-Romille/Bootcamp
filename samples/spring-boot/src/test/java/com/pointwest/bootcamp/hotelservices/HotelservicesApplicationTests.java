package com.pointwest.bootcamp.hotelservices;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pointwest.bootcamp.hotelservices.controller.FacilityController;
import com.pointwest.bootcamp.hotelservices.controller.RoomController;
import com.pointwest.bootcamp.hotelservices.dto.FacilityDto;
import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.model.Room;
import com.pointwest.bootcamp.hotelservices.repository.RoomRepository;

@SpringBootTest
class HotelservicesApplicationTests {

	@Autowired
	private RoomController roomController;

	@Autowired
	private FacilityController facilityController;

	@Autowired
	private RoomRepository roomRepository;

	@BeforeEach
	void setupBeforeEachTest() {
		Room room1 = new Room("1", 1, "studio", 2, Room.HousekeepingStatus.VACANT_CLEAN);
		roomRepository.save(room1);
	}

	@AfterEach
	void cleanupAfterEachTest() {
		roomRepository.deleteAll();
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
