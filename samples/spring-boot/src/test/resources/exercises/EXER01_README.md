# Exercise 1 — Refactor the Guest Stay Registration God Class

**Format:** individual or pairs  
**Suggested time:** 90 minutes

You are given a working but tangled `GuestStayRegistrationManager`. It performs various unrelated responsibilities all in one class.

The test suite is green at the start. Refactor the implementation
without changing its externally observable behavior.

## Start here

Read:

- `src/main/java/com/hotel/exercises/refactoring/GuestStayRegistrationManager.java`
- `src/test/java/com/hotel/exercises/refactoring/GuestStayRegistrationManagerContractTest.java`

Run only this exercise from:

```bash
mvnw -Dtest=GuestStayRegistrationManagerContractTest test
```

## Your task

1. Identify the responsibilities currently mixed into
   `GuestStayRegistrationManager`.
2. Extract meaningful responsibilities behind interfaces and concrete
   implementations.
3. Rewire the manager to depend on those interfaces through constructor
   injection. Use plain Java. Spring is optiona.
4. Do not modify the supplied characterization tests. Keep them green
   throughout the refactoring; add separate focused tests where useful.
5. Complete `DESIGN_NOTES.md` with one concise paragraph explaining which SOLID
   principles your extractions improve and why.

You may choose the number, names, and package structure of extracted
collaborators. You may also add focused unit tests for them. 

## Stable external contract

The supplied tests intentionally depend only on these public operations:

```java
GuestStayRegistrationManager.inMemory(Clock clock)
GuestStayRegistrationManager.inMemory()
manager.register(Guest guest, Room room, int partySize, LocalDate checkInDate)
manager.findStay(String stayId)
manager.sentEmails()
manager.auditEntries()
```

Keep those signatures and their behavior intact. `inMemory(Clock)` is the
exercise composition root: after refactoring, it may construct whichever
concrete collaborators you design. Your constructor-injected implementation can
remain an internal detail, so the static harness does not prescribe interface
or class names.

`AuditEntry` and `SentEmail` are also contract records used to observe the
workflow. They are not hints about how persistence, notification, or auditing
must be implemented internally.
