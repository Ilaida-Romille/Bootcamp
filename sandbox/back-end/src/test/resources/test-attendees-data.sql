-- Clean up database state prior to inserting attendee test data
DELETE FROM session_selections;
DELETE FROM registrations;
DELETE FROM attendees;
DELETE FROM organizers;
DELETE FROM users;

-- Parent table: users
INSERT INTO users (user_id, name, email) VALUES
    ('USR-001', 'John Doe', 'john.doe@example.com'),
    ('USR-002', 'Jane Smith', 'jane.smith@example.com'),
    ('USR-003', 'Johnny Appleseed', 'johnny@example.com');

-- Subclass table: attendees
INSERT INTO attendees (user_id, attendee_id) VALUES
    ('USR-001', 'ATT-001'),
    ('USR-002', 'ATT-002'),
    ('USR-003', 'ATT-003');