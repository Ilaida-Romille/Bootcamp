package com.pointwest.bootcamp.hotelservices.controller;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.pointwest.bootcamp.hotelservices.dto.FacilityDto;
import com.pointwest.bootcamp.hotelservices.service.FacilityService;

@Controller
public class FacilityController {

	private final FacilityService facilityService;

	public FacilityController(FacilityService facilityService) {
		this.facilityService = facilityService;
	}

	public List<FacilityDto> listFacilities() {
		System.out.println("List Facilities");
		return facilityService.listFacilities();
	}

	public List<FacilityDto> listByCategory(String category) {
		return facilityService.listByCategory(category);
	}
}
