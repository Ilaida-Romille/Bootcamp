package com.pointwest.bootcamp.eventhubri.identity.enums;

// FIX: ER diagram lists 6 role names; original enum was missing 2.
public enum RoleType {
    PLATFORM_OWNER,
    ORGANIZER_ADMIN,
    ORGANIZER_MEMBER,
    ATTENDEE,
    CHECKIN_STAFF,
    PLATFORM_SUPPORT
}
