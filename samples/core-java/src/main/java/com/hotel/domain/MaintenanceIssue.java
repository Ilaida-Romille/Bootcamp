package com.hotel.domain;

/** Closed set of facility problems, routed by type in the pattern-matching-switch koans. */
public sealed interface MaintenanceIssue
        permits PlumbingIssue, ElectricalIssue, HvacIssue, StructuralIssue {
}
