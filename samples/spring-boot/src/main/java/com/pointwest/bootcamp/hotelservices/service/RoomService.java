package com.pointwest.bootcamp.hotelservices.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.repository.RoomRepository;

@Service
public class RoomService {

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<RoomDto> listRooms() {
		List<RoomDto> roomDtoList = new ArrayList<>();

		roomRepository.findAll().forEach(room -> roomDtoList.add(new RoomDto(room)));
		
				return roomDtoList;
	}

	// public List<RoomDto> listByHousekeepingStatus(RoomDto.HousekeepingStatus status) {
	// 	HousekeepingStatus domainStatus = HousekeepingStatus.valueOf(status.name());
	// 	return roomRepository.findByHousekeepingStatus(domainStatus).stream()
	// 			.map(RoomDto::new)
	// 			.collect(Collectors.toList());
	// }
}
