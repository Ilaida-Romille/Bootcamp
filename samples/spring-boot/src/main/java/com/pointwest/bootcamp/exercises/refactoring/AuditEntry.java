package com.pointwest.bootcamp.exercises.refactoring;

import java.time.Instant;

/** An externally observable audit event produced by the exercise workflow. */
public record AuditEntry(Instant occurredAt, String action, String details) {
}
