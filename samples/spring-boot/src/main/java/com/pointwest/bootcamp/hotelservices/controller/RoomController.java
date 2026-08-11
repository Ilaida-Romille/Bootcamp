package com.pointwest.bootcamp.hotelservices.controller;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.dto.RoomDto.HousekeepingStatus;
import com.pointwest.bootcamp.hotelservices.service.RoomService;

@Controller
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	public List<RoomDto> listRooms() {
		return roomService.listRooms();
	}

	public List<RoomDto> listByHousekeepingStatus(HousekeepingStatus status) {
		return roomService.listByHousekeepingStatus(status);
	}
}
