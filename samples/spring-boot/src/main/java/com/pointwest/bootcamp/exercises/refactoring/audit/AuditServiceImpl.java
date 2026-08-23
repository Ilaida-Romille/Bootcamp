package com.pointwest.bootcamp.exercises.refactoring.audit;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.pointwest.bootcamp.exercises.refactoring.AuditEntry;

public class AuditServiceImpl implements AuditService {
    private final Clock clock;
    private final List<AuditEntry> auditEntries = new ArrayList<>();

    public AuditServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void logRegistration(String stayId, String guestId, String spaceId, int partySize) {
        String details = "%s guest=%s room=%s party=%d"
                .formatted(stayId, guestId, spaceId, partySize);
        auditEntries.add(new AuditEntry(Instant.now(clock), "STAY_REGISTERED", details));
    }

    @Override
    public List<AuditEntry> getAuditEntries() {
        return List.copyOf(auditEntries);
    }
}
