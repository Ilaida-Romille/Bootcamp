-- Clean up database state prior to inserting test data
DELETE FROM session_selections;
DELETE FROM registrations;
DELETE FROM presentation_sessions;
DELETE FROM break_sessions;
DELETE FROM sessions;
DELETE FROM events;
DELETE FROM agendas;

-- Insert Agendas
INSERT INTO agendas (agenda_id, description) VALUES
    ('AG-100', 'Tech Summit Main Agenda'),
    ('AG-200', 'AI Expo Main Agenda');

-- Insert Events (referenced by agenda_id)
INSERT INTO events 
    (event_id, title, description, status, capacity, start_date_time, end_date_time, agenda_id)
VALUES
    (1000, 'Tech Summit 2026', 'Annual Developer Conference', 'PUBLISHED', 500, '2026-09-01 09:00:00', '2026-09-01 17:00:00', 'AG-100'),
    (2000, 'AI Horizons 2026', 'Artificial Intelligence Showcase', 'PUBLISHED', 300, '2026-10-10 09:00:00', '2026-10-10 17:00:00', 'AG-200');

-- ==================== EVENT 1000: 5 SESSIONS ====================
-- Parent table: sessions
INSERT INTO sessions (session_id, title, description, location, agenda_id) VALUES
    ('SES-101', 'Keynote: Java 21', 'Opening speech', 'Hall A', 'AG-100'),
    ('SES-102', 'Morning Coffee Break', 'Networking & Coffee', 'Lobby', 'AG-100'),
    ('SES-103', 'Spring Boot 3 Deep Dive', 'Technical Session', 'Hall B', 'AG-100'),
    ('SES-104', 'Cloud Native Architecture', 'Technical Session', 'Hall A', 'AG-100'),
    ('SES-105', 'Lunch Break', 'Catered Lunch', 'Dining Area', 'AG-100');

-- Joined subclass tables (presentation_sessions & break_sessions)
INSERT INTO presentation_sessions (session_id, speaker) VALUES
    ('SES-101', 'Dr. Alice Johnson'),
    ('SES-103', 'Bob Smith'),
    ('SES-104', 'Charlie Davis');

INSERT INTO break_sessions (session_id, break_type) VALUES
    ('SES-102', 'Networking'),
    ('SES-105', 'Catered Lunch');

-- ==================== EVENT 2000: 5 SESSIONS ====================
-- Parent table: sessions
INSERT INTO sessions (session_id, title, description, location, agenda_id) VALUES
    ('SES-201', 'AI Keynote', 'Generative AI Tech', 'Auditorium', 'AG-200'),
    ('SES-202', 'Tea Break', 'Short Break', 'Lobby', 'AG-200'),
    ('SES-203', 'LLMs in Production', 'Technical Talk', 'Room 1', 'AG-200'),
    ('SES-204', 'AI Ethics Panel', 'Discussion', 'Room 2', 'AG-200'),
    ('SES-205', 'Networking Drinks', 'Evening Drinks', 'Rooftop', 'AG-200');

-- Joined subclass tables
INSERT INTO presentation_sessions (session_id, speaker) VALUES
    ('SES-201', 'Eva AI Lead'),
    ('SES-203', 'Frank Engineer'),
    ('SES-204', 'Grace Policy Specialist');

INSERT INTO break_sessions (session_id, break_type) VALUES
    ('SES-202', 'Refreshments'),
    ('SES-205', 'Networking');

-- ==================== REGISTRATIONS & SELECTIONS (Event 1000) ====================
-- Registrations for Event 1000
INSERT INTO registrations (registration_id, attendee_id, event_id, status, registered_at, dietary_restrictions) VALUES
    ('REG-101', 'ATT-001', 1000, 'CONFIRMED', '2026-08-01 10:00:00', 'Vegan'),
    ('REG-102', 'ATT-002', 1000, 'CONFIRMED', '2026-08-02 11:00:00', 'None'),
    ('REG-103', 'ATT-003', 1000, 'CONFIRMED', '2026-08-03 12:00:00', 'Gluten Free');

-- Session Selections for Event 1000
-- SES-101 has 3 selections
-- SES-103 has 2 selections
-- SES-102 has 1 selection
-- SES-104 has 1 selection
INSERT INTO session_selections (selection_id, session_id, registration_id, selected_at) VALUES
    ('SEL-001', 'SES-101', 'REG-101', '2026-08-01 10:05:00'),
    ('SEL-002', 'SES-103', 'REG-101', '2026-08-01 10:06:00'),
    ('SEL-003', 'SES-101', 'REG-102', '2026-08-02 11:05:00'),
    ('SEL-004', 'SES-102', 'REG-102', '2026-08-02 11:06:00'),
    ('SEL-005', 'SES-103', 'REG-102', '2026-08-02 11:07:00'),
    ('SEL-006', 'SES-101', 'REG-103', '2026-08-03 12:05:00'),
    ('SEL-007', 'SES-104', 'REG-103', '2026-08-03 12:06:00');