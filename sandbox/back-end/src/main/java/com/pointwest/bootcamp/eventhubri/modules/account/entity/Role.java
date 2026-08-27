package com.pointwest.bootcamp.eventhubri.modules.account.entity;

import java.util.Set;

import static com.pointwest.bootcamp.eventhubri.modules.account.entity.Privilege.*;

/** User role, persisted as a string column on {@link AppUser}. */
public enum Role {

    PLATFORM_ADMIN(Set.of(MANAGE_PLATFORM_RATES, VIEW_ALL_ORGANIZATIONS, VIEW_ALL_INVOICES)),
    ORGANIZER_ADMIN(Set.of(MANAGE_EVENTS, MANAGE_STAFF, VIEW_ORGANIZATION_INVOICES, SEND_NOTIFICATIONS, MANAGE_AGENDA,
            CHECK_IN_ATTENDEES)),
    ORGANIZER_STAFF(Set.of(MANAGE_AGENDA, CHECK_IN_ATTENDEES, SEND_NOTIFICATIONS)),
    ATTENDEE(Set.of(REGISTER_FOR_EVENT, MANAGE_OWN_PROFILE));

    private final Set<Privilege> privileges;

    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }
}
