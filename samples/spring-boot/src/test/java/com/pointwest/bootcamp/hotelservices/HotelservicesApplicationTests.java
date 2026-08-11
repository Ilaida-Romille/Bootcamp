package com.pointwest.bootcamp.hotelservices;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pointwest.bootcamp.hotelservices.controller.FacilityController;
import com.pointwest.bootcamp.hotelservices.controller.RoomController;
import com.pointwest.bootcamp.hotelservices.dto.FacilityDto;
import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.dto.RoomDto.HousekeepingStatus;

@SpringBootTest
class HotelservicesApplicationTests {

	@Autowired
	private RoomController roomController;

	@Autowired
	private FacilityController facilityController;

	@Test
	void roomListByHousekeepingStatus() {
		List<RoomDto> rooms = roomController.listByHousekeepingStatus(HousekeepingStatus.OCCUPIED_CLEAN);

		assertNotNull(rooms);
	}

	@Test 
	void facilityList() {
		List<FacilityDto> facilities = facilityController.listFacilities();

		assertNotNull(facilities);
	}


}
