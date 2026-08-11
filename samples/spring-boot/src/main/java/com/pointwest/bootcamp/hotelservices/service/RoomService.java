package com.pointwest.bootcamp.hotelservices.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.hotelservices.dto.RoomDto;
import com.pointwest.bootcamp.hotelservices.model.Room.HousekeepingStatus;
import com.pointwest.bootcamp.hotelservices.repository.RoomRepository;

@Service
public class RoomService {

	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<RoomDto> listRooms() {
		return roomRepository.findAll().stream()
				.map(RoomDto::new)
				.collect(Collectors.toList());
	}

	public List<RoomDto> listByHousekeepingStatus(RoomDto.HousekeepingStatus status) {
		HousekeepingStatus domainStatus = HousekeepingStatus.valueOf(status.name());
		return roomRepository.findByHousekeepingStatus(domainStatus).stream()
				.map(RoomDto::new)
				.collect(Collectors.toList());
	}
}
