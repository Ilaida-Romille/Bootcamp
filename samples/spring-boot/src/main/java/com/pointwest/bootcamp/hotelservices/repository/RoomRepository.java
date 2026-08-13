package com.pointwest.bootcamp.hotelservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pointwest.bootcamp.hotelservices.model.Room;
import com.pointwest.bootcamp.hotelservices.repository.summary.RoomStaySummary;

public interface RoomRepository extends CrudRepository<Room, Long> {

        @Query("""
        SELECT
            r.roomNumber AS roomNumber,
            COUNT(stay) AS stayCount
        FROM Room r
        LEFT JOIN r.stays stay
        GROUP BY r.spaceId, r.roomNumber
        ORDER BY COUNT(stay) DESC, r.roomNumber ASC
        """)
    List<RoomStaySummary> findRoomStaySummary();
}
