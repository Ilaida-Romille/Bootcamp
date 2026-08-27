package com.pointwest.bootcamp.eventhubri.identity.enums;

// FIX: aligned to the ER diagram's organizer_members.status values
// (INVITED, ACTIVE, REMOVED). DEACTIVATED/REJECTED were not in the schema doc.
public enum MembershipStatus {
    INVITED,
    ACTIVE,
    REMOVED
}
