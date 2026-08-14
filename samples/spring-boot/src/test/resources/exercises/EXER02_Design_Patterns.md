

Define a Notification interface with a send(Registration r) method.
Implement EmailNotification now; stub SmsNotification and PushNotification as no-ops.
Build a NotificationFactory that returns the right implementation based on a channel parameter.
Write a unit test proving RegistrationService never changes when a new channel is added — that's Open/Closed in practice.


# Exercise 2 — Build a Notification Factory for EventHub

**Format:** individual  
**Suggested time:** 90 minutes

EventHub needs to send confirmation emails today. On the roadmap, SMS and push notifications may be added later. The project should make this easy to do without rewriting the registration flow each time.

## Your task

1. Define a `NotificationStrategy` interface with a `send(Registration r)` method. (1pt)
2. Implement `EmailNotificationStrategy`, `SmsNotificationStrategy` and `PushNotificationStrategy`. (3pt)
3. Build a `NotificationFactory` that returns the right implementation based on a `channel` parameter. (1pt)
4. Write a unit test proving `RegistrationService` never changes when a new channel is added. (1pt)

Total: 6pt