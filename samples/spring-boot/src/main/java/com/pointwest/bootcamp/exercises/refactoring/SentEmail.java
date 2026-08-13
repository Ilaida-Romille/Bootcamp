package com.pointwest.bootcamp.exercises.refactoring;

/** An email captured by the in-memory exercise composition. */
public record SentEmail(String recipient, String subject, String body) {
}
