package com.pointwest.bootcamp.hotelservices.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.pointwest.bootcamp.hotelservices.model.Facility;
import com.pointwest.bootcamp.hotelservices.model.Space;

@Repository
public class FacilityRepository {

	private final List<Facility> facilitys = new ArrayList<>();

	public FacilityRepository() {
		// Hard-coded data
		Facility facility8 = new Facility("302", "FitnessBros", "Gym", 10, Space.Status.OPERATIONAL);
		facility8.setSpaceId(8L);
		facilitys.add(facility8);
	}

	public List<Facility> findAll() {
		return new ArrayList<>(facilitys);
	}

	public Optional<Facility> findById(Long id) {
		if (id == null || id < 1 || id > facilitys.size()) {
			return Optional.empty();
		}
		return Optional.of(facilitys.get(id.intValue() - 1));
	}

	public List<Facility> findByCategory(String category) {
		return facilitys.stream()
				.filter(facility -> facility.getCategory().equals(category))
				.collect(Collectors.toList());
	}

	public Facility save(Facility facility) {
		// For simplicity, just return the facility (in a real implementation, this would update the list)
		return facility;
	}
}
