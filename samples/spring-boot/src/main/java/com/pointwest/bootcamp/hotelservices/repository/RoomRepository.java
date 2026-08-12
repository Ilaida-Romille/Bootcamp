package com.pointwest.bootcamp.hotelservices.repository;

import org.springframework.data.repository.CrudRepository;

import com.pointwest.bootcamp.hotelservices.model.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {
    
}
