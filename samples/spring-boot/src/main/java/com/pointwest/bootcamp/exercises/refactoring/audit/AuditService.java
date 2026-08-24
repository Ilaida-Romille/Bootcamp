package com.pointwest.bootcamp.exercises.refactoring.audit;

import java.util.List;

import com.pointwest.bootcamp.exercises.refactoring.AuditEntry;

public interface AuditService {
    void logRegistration(String stayId, String guestId, String spaceId, int partySize);

    List<AuditEntry> getAuditEntries();
}
