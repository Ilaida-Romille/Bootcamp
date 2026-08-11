package com.pointwest.bootcamp.hotelservices.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.hotelservices.dto.FacilityDto;
import com.pointwest.bootcamp.hotelservices.repository.FacilityRepository;

@Service
public class FacilityService {

	private final FacilityRepository facilityRepository;

	public FacilityService(FacilityRepository facilityRepository) {
		this.facilityRepository = facilityRepository;
	}

	public List<FacilityDto> listFacilities() {
		return facilityRepository.findAll().stream()
				.map(FacilityDto::new)
				.collect(Collectors.toList());
	}

	public List<FacilityDto> listByCategory(String category) {
		return facilityRepository.findByCategory(category).stream()
				.map(FacilityDto::new)
				.collect(Collectors.toList());
	}
}
