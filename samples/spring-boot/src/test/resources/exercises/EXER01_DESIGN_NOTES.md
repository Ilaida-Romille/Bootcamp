# Design notes

In one concise paragraph, explain which responsibilities you extracted, which
SOLID principles those boundaries improve, and one trade-off in your design.

<!-- Answer -->

I extracted the audit, notification, stay, and room-capacity logic into separate classes so the GuestStayRegistrationManager can mainly handle the registration flow. This follows the Single Responsibility Principle since each class now has a more focused job, and the use of interfaces also follows the Dependency Inversion Principle because the manager depends on abstractions instead of the actual implementations. The main trade-off is that there are now more files to manage, which can feel like extra code for a small feature, but it makes the code easier to understand and change later.
