package com.hotel.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A hotel floor hosting its own rooms; the one-to-many nested collection that
 * drives the flatMap koans. */
public class Floor {

    private final int number;
    private final List<Room> rooms = new ArrayList<>();

    public Floor(int number) {
        this.number = number;
        System.out.println("[Floor] floor " + number + " opened");
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public int getNumber() { return number; }
    public List<Room> getRooms() { return Collections.unmodifiableList(rooms); }
}
