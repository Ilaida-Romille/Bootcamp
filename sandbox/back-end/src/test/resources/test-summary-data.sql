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
INSERT INTO sessions (session_id, title, description, location, agenda_id) VALUES
    ('SES-101', 'Keynote: Java 21', 'Opening speech', 'Hall A', 'AG-100'),
    ('SES-102', 'Morning Coffee Break', 'Networking & Coffee', 'Lobby', 'AG-100'),
    ('SES-103', 'Spring Boot 3 Deep Dive', 'Technical Session', 'Hall B', 'AG-100'),
    ('SES-104', 'Cloud Native Architecture', 'Technical Session', 'Hall A', 'AG-100'),
    ('SES-105', 'Lunch Break', 'Catered Lunch', 'Dining Area', 'AG-100');

INSERT INTO presentation_sessions (session_id, speaker) VALUES
    ('SES-101', 'Dr. Alice Johnson'),
    ('SES-103', 'Bob Smith'),
    ('SES-104', 'Charlie Davis');

INSERT INTO break_sessions (session_id, break_type) VALUES
    ('SES-102', 'Networking'),
    ('SES-105', 'Catered Lunch');

-- ==================== EVENT 2000: 5 SESSIONS ====================
INSERT INTO sessions (session_id, title, description, location, agenda_id) VALUES
    ('SES-201', 'AI Keynote', 'Generative AI Tech', 'Auditorium', 'AG-200'),
    ('SES-202', 'Tea Break', 'Short Break', 'Lobby', 'AG-200'),
    ('SES-203', 'LLMs in Production', 'Technical Talk', 'Room 1', 'AG-200'),
    ('SES-204', 'AI Ethics Panel', 'Discussion', 'Room 2', 'AG-200'),
    ('SES-205', 'Networking Drinks', 'Evening Drinks', 'Rooftop', 'AG-200');

INSERT INTO presentation_sessions (session_id, speaker) VALUES
    ('SES-201', 'Eva AI Lead'),
    ('SES-203', 'Frank Engineer'),
    ('SES-204', 'Grace Policy Specialist');

INSERT INTO break_sessions (session_id, break_type) VALUES
    ('SES-202', 'Refreshments'),
    ('SES-205', 'Networking');

-- ==================== REGISTRATIONS FOR EVENT 1000 (100 Registrations) ====================
INSERT INTO registrations (registration_id, attendee_id, event_id, status, registered_at, dietary_restrictions) VALUES
    ('REG-101', 'ATT-001', 1000, 'CONFIRMED', '2026-08-01 10:00:00', 'Vegan'),
    ('REG-102', 'ATT-002', 1000, 'CONFIRMED', '2026-08-01 10:05:00', 'None'),
    ('REG-103', 'ATT-003', 1000, 'CONFIRMED', '2026-08-01 10:10:00', 'Gluten Free'),
    ('REG-104', 'ATT-004', 1000, 'CONFIRMED', '2026-08-01 10:15:00', 'None'),
    ('REG-105', 'ATT-005', 1000, 'CONFIRMED', '2026-08-01 10:20:00', 'Vegetarian'),
    ('REG-106', 'ATT-006', 1000, 'CONFIRMED', '2026-08-01 10:25:00', 'None'),
    ('REG-107', 'ATT-007', 1000, 'CONFIRMED', '2026-08-01 10:30:00', 'Nut Allergy'),
    ('REG-108', 'ATT-008', 1000, 'CONFIRMED', '2026-08-01 10:35:00', 'None'),
    ('REG-109', 'ATT-009', 1000, 'CONFIRMED', '2026-08-01 10:40:00', 'Halal'),
    ('REG-110', 'ATT-010', 1000, 'CONFIRMED', '2026-08-01 10:45:00', 'None'),
    ('REG-111', 'ATT-011', 1000, 'CONFIRMED', '2026-08-01 10:50:00', 'None'),
    ('REG-112', 'ATT-012', 1000, 'CONFIRMED', '2026-08-01 10:55:00', 'Vegan'),
    ('REG-113', 'ATT-013', 1000, 'CONFIRMED', '2026-08-01 11:00:00', 'None'),
    ('REG-114', 'ATT-014', 1000, 'CONFIRMED', '2026-08-01 11:05:00', 'Dairy Free'),
    ('REG-115', 'ATT-015', 1000, 'CONFIRMED', '2026-08-01 11:10:00', 'None'),
    ('REG-116', 'ATT-016', 1000, 'CONFIRMED', '2026-08-01 11:15:00', 'Kosher'),
    ('REG-117', 'ATT-017', 1000, 'CONFIRMED', '2026-08-01 11:20:00', 'None'),
    ('REG-118', 'ATT-018', 1000, 'CONFIRMED', '2026-08-01 11:25:00', 'None'),
    ('REG-119', 'ATT-019', 1000, 'CONFIRMED', '2026-08-01 11:30:00', 'Vegetarian'),
    ('REG-120', 'ATT-020', 1000, 'CONFIRMED', '2026-08-01 11:35:00', 'None'),
    ('REG-121', 'ATT-021', 1000, 'CONFIRMED', '2026-08-01 11:40:00', 'None'),
    ('REG-122', 'ATT-022', 1000, 'CONFIRMED', '2026-08-01 11:45:00', 'None'),
    ('REG-123', 'ATT-023', 1000, 'CONFIRMED', '2026-08-01 11:50:00', 'None'),
    ('REG-124', 'ATT-024', 1000, 'CONFIRMED', '2026-08-01 11:55:00', 'Vegan'),
    ('REG-125', 'ATT-025', 1000, 'CONFIRMED', '2026-08-01 12:00:00', 'None'),
    ('REG-126', 'ATT-026', 1000, 'CONFIRMED', '2026-08-01 12:05:00', 'None'),
    ('REG-127', 'ATT-027', 1000, 'CONFIRMED', '2026-08-01 12:10:00', 'None'),
    ('REG-128', 'ATT-028', 1000, 'CONFIRMED', '2026-08-01 12:15:00', 'None'),
    ('REG-129', 'ATT-029', 1000, 'CONFIRMED', '2026-08-01 12:20:00', 'Gluten Free'),
    ('REG-130', 'ATT-030', 1000, 'CONFIRMED', '2026-08-01 12:25:00', 'None'),
    ('REG-131', 'ATT-031', 1000, 'CONFIRMED', '2026-08-01 12:30:00', 'None'),
    ('REG-132', 'ATT-032', 1000, 'CONFIRMED', '2026-08-01 12:35:00', 'None'),
    ('REG-133', 'ATT-033', 1000, 'CONFIRMED', '2026-08-01 12:40:00', 'None'),
    ('REG-134', 'ATT-034', 1000, 'CONFIRMED', '2026-08-01 12:45:00', 'None'),
    ('REG-135', 'ATT-035', 1000, 'CONFIRMED', '2026-08-01 12:50:00', 'None'),
    ('REG-136', 'ATT-036', 1000, 'CONFIRMED', '2026-08-01 12:55:00', 'None'),
    ('REG-137', 'ATT-037', 1000, 'CONFIRMED', '2026-08-01 13:00:00', 'Vegetarian'),
    ('REG-138', 'ATT-038', 1000, 'CONFIRMED', '2026-08-01 13:05:00', 'None'),
    ('REG-139', 'ATT-039', 1000, 'CONFIRMED', '2026-08-01 13:10:00', 'None'),
    ('REG-140', 'ATT-040', 1000, 'CONFIRMED', '2026-08-01 13:15:00', 'None'),
    ('REG-141', 'ATT-041', 1000, 'CONFIRMED', '2026-08-01 13:20:00', 'None'),
    ('REG-142', 'ATT-042', 1000, 'CONFIRMED', '2026-08-01 13:25:00', 'None'),
    ('REG-143', 'ATT-043', 1000, 'CONFIRMED', '2026-08-01 13:30:00', 'Vegan'),
    ('REG-144', 'ATT-044', 1000, 'CONFIRMED', '2026-08-01 13:35:00', 'None'),
    ('REG-145', 'ATT-045', 1000, 'CONFIRMED', '2026-08-01 13:40:00', 'None'),
    ('REG-146', 'ATT-046', 1000, 'CONFIRMED', '2026-08-01 13:45:00', 'None'),
    ('REG-147', 'ATT-047', 1000, 'CONFIRMED', '2026-08-01 13:50:00', 'None'),
    ('REG-148', 'ATT-048', 1000, 'CONFIRMED', '2026-08-01 13:55:00', 'None'),
    ('REG-149', 'ATT-049', 1000, 'CONFIRMED', '2026-08-01 14:00:00', 'None'),
    ('REG-150', 'ATT-050', 1000, 'CONFIRMED', '2026-08-01 14:05:00', 'Nut Allergy'),
    ('REG-151', 'ATT-051', 1000, 'CONFIRMED', '2026-08-01 14:10:00', 'None'),
    ('REG-152', 'ATT-052', 1000, 'CONFIRMED', '2026-08-01 14:15:00', 'None'),
    ('REG-153', 'ATT-053', 1000, 'CONFIRMED', '2026-08-01 14:20:00', 'None'),
    ('REG-154', 'ATT-054', 1000, 'CONFIRMED', '2026-08-01 14:25:00', 'None'),
    ('REG-155', 'ATT-055', 1000, 'CONFIRMED', '2026-08-01 14:30:00', 'None'),
    ('REG-156', 'ATT-056', 1000, 'CONFIRMED', '2026-08-01 14:35:00', 'None'),
    ('REG-157', 'ATT-057', 1000, 'CONFIRMED', '2026-08-01 14:40:00', 'None'),
    ('REG-158', 'ATT-058', 1000, 'CONFIRMED', '2026-08-01 14:45:00', 'None'),
    ('REG-159', 'ATT-059', 1000, 'CONFIRMED', '2026-08-01 14:50:00', 'None'),
    ('REG-160', 'ATT-060', 1000, 'CONFIRMED', '2026-08-01 14:55:00', 'None'),
    ('REG-161', 'ATT-061', 1000, 'CONFIRMED', '2026-08-01 15:00:00', 'None'),
    ('REG-162', 'ATT-062', 1000, 'CONFIRMED', '2026-08-01 15:05:00', 'None'),
    ('REG-163', 'ATT-063', 1000, 'CONFIRMED', '2026-08-01 15:10:00', 'None'),
    ('REG-164', 'ATT-064', 1000, 'CONFIRMED', '2026-08-01 15:15:00', 'None'),
    ('REG-165', 'ATT-065', 1000, 'CONFIRMED', '2026-08-01 15:20:00', 'None'),
    ('REG-166', 'ATT-066', 1000, 'CONFIRMED', '2026-08-01 15:25:00', 'None'),
    ('REG-167', 'ATT-067', 1000, 'CONFIRMED', '2026-08-01 15:30:00', 'None'),
    ('REG-168', 'ATT-068', 1000, 'CONFIRMED', '2026-08-01 15:35:00', 'None'),
    ('REG-169', 'ATT-069', 1000, 'CONFIRMED', '2026-08-01 15:40:00', 'None'),
    ('REG-170', 'ATT-070', 1000, 'CONFIRMED', '2026-08-01 15:45:00', 'None'),
    ('REG-171', 'ATT-071', 1000, 'CONFIRMED', '2026-08-01 15:50:00', 'None'),
    ('REG-172', 'ATT-072', 1000, 'CONFIRMED', '2026-08-01 15:55:00', 'None'),
    ('REG-173', 'ATT-073', 1000, 'CONFIRMED', '2026-08-01 16:00:00', 'None'),
    ('REG-174', 'ATT-074', 1000, 'CONFIRMED', '2026-08-01 16:05:00', 'None'),
    ('REG-175', 'ATT-075', 1000, 'CONFIRMED', '2026-08-01 16:10:00', 'None'),
    ('REG-176', 'ATT-076', 1000, 'CONFIRMED', '2026-08-01 16:15:00', 'None'),
    ('REG-177', 'ATT-077', 1000, 'CONFIRMED', '2026-08-01 16:20:00', 'None'),
    ('REG-178', 'ATT-078', 1000, 'CONFIRMED', '2026-08-01 16:25:00', 'None'),
    ('REG-179', 'ATT-079', 1000, 'CONFIRMED', '2026-08-01 16:30:00', 'None'),
    ('REG-180', 'ATT-080', 1000, 'CONFIRMED', '2026-08-01 16:35:00', 'None'),
    ('REG-181', 'ATT-081', 1000, 'CONFIRMED', '2026-08-01 16:40:00', 'None'),
    ('REG-182', 'ATT-082', 1000, 'CONFIRMED', '2026-08-01 16:45:00', 'None'),
    ('REG-183', 'ATT-083', 1000, 'CONFIRMED', '2026-08-01 16:50:00', 'None'),
    ('REG-184', 'ATT-084', 1000, 'CONFIRMED', '2026-08-01 16:55:00', 'None'),
    ('REG-185', 'ATT-085', 1000, 'CONFIRMED', '2026-08-01 17:00:00', 'None'),
    ('REG-186', 'ATT-086', 1000, 'CONFIRMED', '2026-08-01 17:05:00', 'None'),
    ('REG-187', 'ATT-087', 1000, 'CONFIRMED', '2026-08-01 17:10:00', 'None'),
    ('REG-188', 'ATT-088', 1000, 'CONFIRMED', '2026-08-01 17:15:00', 'None'),
    ('REG-189', 'ATT-089', 1000, 'CONFIRMED', '2026-08-01 17:20:00', 'None'),
    ('REG-190', 'ATT-090', 1000, 'CONFIRMED', '2026-08-01 17:25:00', 'None'),
    ('REG-191', 'ATT-091', 1000, 'CONFIRMED', '2026-08-01 17:30:00', 'None'),
    ('REG-192', 'ATT-092', 1000, 'CONFIRMED', '2026-08-01 17:35:00', 'None'),
    ('REG-193', 'ATT-093', 1000, 'CONFIRMED', '2026-08-01 17:40:00', 'None'),
    ('REG-194', 'ATT-094', 1000, 'CONFIRMED', '2026-08-01 17:45:00', 'None'),
    ('REG-195', 'ATT-095', 1000, 'CONFIRMED', '2026-08-01 17:50:00', 'None'),
    ('REG-196', 'ATT-096', 1000, 'CONFIRMED', '2026-08-01 17:55:00', 'None'),
    ('REG-197', 'ATT-097', 1000, 'CONFIRMED', '2026-08-01 18:00:00', 'None'),
    ('REG-198', 'ATT-098', 1000, 'CONFIRMED', '2026-08-01 18:05:00', 'None'),
    ('REG-199', 'ATT-099', 1000, 'CONFIRMED', '2026-08-01 18:10:00', 'None'),
    ('REG-200', 'ATT-100', 1000, 'CONFIRMED', '2026-08-01 18:15:00', 'None');

-- ==================== SESSION SELECTIONS (100 Selections Total) ====================
-- Breakdown across 5 sessions:
-- SES-101 (Keynote: Java 21)       = 40 selections (SEL-001 to SEL-040)
-- SES-103 (Spring Boot Deep Dive)  = 30 selections (SEL-041 to SEL-070)
-- SES-104 (Cloud Native Arch)     = 15 selections (SEL-071 to SEL-085)
-- SES-102 (Morning Coffee Break)   = 10 selections (SEL-086 to SEL-095)
-- SES-105 (Lunch Break)            =  5 selections (SEL-096 to SEL-100)

INSERT INTO session_selections (selection_id, session_id, registration_id, selected_at) VALUES
    -- SES-101 (40)
    ('SEL-001', 'SES-101', 'REG-101', '2026-08-01 10:05:00'),
    ('SEL-002', 'SES-101', 'REG-102', '2026-08-01 10:06:00'),
    ('SEL-003', 'SES-101', 'REG-103', '2026-08-01 10:11:00'),
    ('SEL-004', 'SES-101', 'REG-104', '2026-08-01 10:16:00'),
    ('SEL-005', 'SES-101', 'REG-105', '2026-08-01 10:21:00'),
    ('SEL-006', 'SES-101', 'REG-106', '2026-08-01 10:26:00'),
    ('SEL-007', 'SES-101', 'REG-107', '2026-08-01 10:31:00'),
    ('SEL-008', 'SES-101', 'REG-108', '2026-08-01 10:36:00'),
    ('SEL-009', 'SES-101', 'REG-109', '2026-08-01 10:41:00'),
    ('SEL-010', 'SES-101', 'REG-110', '2026-08-01 10:46:00'),
    ('SEL-011', 'SES-101', 'REG-111', '2026-08-01 10:51:00'),
    ('SEL-012', 'SES-101', 'REG-112', '2026-08-01 10:56:00'),
    ('SEL-013', 'SES-101', 'REG-113', '2026-08-01 11:01:00'),
    ('SEL-014', 'SES-101', 'REG-114', '2026-08-01 11:06:00'),
    ('SEL-015', 'SES-101', 'REG-115', '2026-08-01 11:11:00'),
    ('SEL-016', 'SES-101', 'REG-116', '2026-08-01 11:16:00'),
    ('SEL-017', 'SES-101', 'REG-117', '2026-08-01 11:21:00'),
    ('SEL-018', 'SES-101', 'REG-118', '2026-08-01 11:26:00'),
    ('SEL-019', 'SES-101', 'REG-119', '2026-08-01 11:31:00'),
    ('SEL-020', 'SES-101', 'REG-120', '2026-08-01 11:36:00'),
    ('SEL-021', 'SES-101', 'REG-121', '2026-08-01 11:41:00'),
    ('SEL-022', 'SES-101', 'REG-122', '2026-08-01 11:46:00'),
    ('SEL-023', 'SES-101', 'REG-123', '2026-08-01 11:51:00'),
    ('SEL-024', 'SES-101', 'REG-124', '2026-08-01 11:56:00'),
    ('SEL-025', 'SES-101', 'REG-125', '2026-08-01 12:01:00'),
    ('SEL-026', 'SES-101', 'REG-126', '2026-08-01 12:06:00'),
    ('SEL-027', 'SES-101', 'REG-127', '2026-08-01 12:11:00'),
    ('SEL-028', 'SES-101', 'REG-128', '2026-08-01 12:16:00'),
    ('SEL-029', 'SES-101', 'REG-129', '2026-08-01 12:21:00'),
    ('SEL-030', 'SES-101', 'REG-130', '2026-08-01 12:26:00'),
    ('SEL-031', 'SES-101', 'REG-131', '2026-08-01 12:31:00'),
    ('SEL-032', 'SES-101', 'REG-132', '2026-08-01 12:36:00'),
    ('SEL-033', 'SES-101', 'REG-133', '2026-08-01 12:41:00'),
    ('SEL-034', 'SES-101', 'REG-134', '2026-08-01 12:46:00'),
    ('SEL-035', 'SES-101', 'REG-135', '2026-08-01 12:51:00'),
    ('SEL-036', 'SES-101', 'REG-136', '2026-08-01 12:56:00'),
    ('SEL-037', 'SES-101', 'REG-137', '2026-08-01 13:01:00'),
    ('SEL-038', 'SES-101', 'REG-138', '2026-08-01 13:06:00'),
    ('SEL-039', 'SES-101', 'REG-139', '2026-08-01 13:11:00'),
    ('SEL-040', 'SES-101', 'REG-140', '2026-08-01 13:16:00'),

    -- SES-103 (30)
    ('SEL-041', 'SES-103', 'REG-141', '2026-08-01 13:21:00'),
    ('SEL-042', 'SES-103', 'REG-142', '2026-08-01 13:26:00'),
    ('SEL-043', 'SES-103', 'REG-143', '2026-08-01 13:31:00'),
    ('SEL-044', 'SES-103', 'REG-144', '2026-08-01 13:36:00'),
    ('SEL-045', 'SES-103', 'REG-145', '2026-08-01 13:41:00'),
    ('SEL-046', 'SES-103', 'REG-146', '2026-08-01 13:46:00'),
    ('SEL-047', 'SES-103', 'REG-147', '2026-08-01 13:51:00'),
    ('SEL-048', 'SES-103', 'REG-148', '2026-08-01 13:56:00'),
    ('SEL-049', 'SES-103', 'REG-149', '2026-08-01 14:01:00'),
    ('SEL-050', 'SES-103', 'REG-150', '2026-08-01 14:06:00'),
    ('SEL-051', 'SES-103', 'REG-151', '2026-08-01 14:11:00'),
    ('SEL-052', 'SES-103', 'REG-152', '2026-08-01 14:16:00'),
    ('SEL-053', 'SES-103', 'REG-153', '2026-08-01 14:21:00'),
    ('SEL-054', 'SES-103', 'REG-154', '2026-08-01 14:26:00'),
    ('SEL-055', 'SES-103', 'REG-155', '2026-08-01 14:31:00'),
    ('SEL-056', 'SES-103', 'REG-156', '2026-08-01 14:36:00'),
    ('SEL-057', 'SES-103', 'REG-157', '2026-08-01 14:41:00'),
    ('SEL-058', 'SES-103', 'REG-158', '2026-08-01 14:46:00'),
    ('SEL-059', 'SES-103', 'REG-159', '2026-08-01 14:51:00'),
    ('SEL-060', 'SES-103', 'REG-160', '2026-08-01 14:56:00'),
    ('SEL-061', 'SES-103', 'REG-161', '2026-08-01 15:01:00'),
    ('SEL-062', 'SES-103', 'REG-162', '2026-08-01 15:06:00'),
    ('SEL-063', 'SES-103', 'REG-163', '2026-08-01 15:11:00'),
    ('SEL-064', 'SES-103', 'REG-164', '2026-08-01 15:16:00'),
    ('SEL-065', 'SES-103', 'REG-165', '2026-08-01 15:21:00'),
    ('SEL-066', 'SES-103', 'REG-166', '2026-08-01 15:26:00'),
    ('SEL-067', 'SES-103', 'REG-167', '2026-08-01 15:31:00'),
    ('SEL-068', 'SES-103', 'REG-168', '2026-08-01 15:36:00'),
    ('SEL-069', 'SES-103', 'REG-169', '2026-08-01 15:41:00'),
    ('SEL-070', 'SES-103', 'REG-170', '2026-08-01 15:46:00'),

    -- SES-104 (15)
    ('SEL-071', 'SES-104', 'REG-171', '2026-08-01 15:51:00'),
    ('SEL-072', 'SES-104', 'REG-172', '2026-08-01 15:56:00'),
    ('SEL-073', 'SES-104', 'REG-173', '2026-08-01 16:01:00'),
    ('SEL-074', 'SES-104', 'REG-174', '2026-08-01 16:06:00'),
    ('SEL-075', 'SES-104', 'REG-175', '2026-08-01 16:11:00'),
    ('SEL-076', 'SES-104', 'REG-176', '2026-08-01 16:16:00'),
    ('SEL-077', 'SES-104', 'REG-177', '2026-08-01 16:21:00'),
    ('SEL-078', 'SES-104', 'REG-178', '2026-08-01 16:26:00'),
    ('SEL-079', 'SES-104', 'REG-179', '2026-08-01 16:31:00'),
    ('SEL-080', 'SES-104', 'REG-180', '2026-08-01 16:36:00'),
    ('SEL-081', 'SES-104', 'REG-181', '2026-08-01 16:41:00'),
    ('SEL-082', 'SES-104', 'REG-182', '2026-08-01 16:46:00'),
    ('SEL-083', 'SES-104', 'REG-183', '2026-08-01 16:51:00'),
    ('SEL-084', 'SES-104', 'REG-184', '2026-08-01 16:56:00'),
    ('SEL-085', 'SES-104', 'REG-185', '2026-08-01 17:01:00'),

    -- SES-102 (10)
    ('SEL-086', 'SES-102', 'REG-186', '2026-08-01 17:06:00'),
    ('SEL-087', 'SES-102', 'REG-187', '2026-08-01 17:11:00'),
    ('SEL-088', 'SES-102', 'REG-188', '2026-08-01 17:16:00'),
    ('SEL-089', 'SES-102', 'REG-189', '2026-08-01 17:21:00'),
    ('SEL-090', 'SES-102', 'REG-190', '2026-08-01 17:26:00'),
    ('SEL-091', 'SES-102', 'REG-191', '2026-08-01 17:31:00'),
    ('SEL-092', 'SES-102', 'REG-192', '2026-08-01 17:36:00'),
    ('SEL-093', 'SES-102', 'REG-193', '2026-08-01 17:41:00'),
    ('SEL-094', 'SES-102', 'REG-194', '2026-08-01 17:46:00'),
    ('SEL-095', 'SES-102', 'REG-195', '2026-08-01 17:51:00'),

    -- SES-105 (5)
    ('SEL-096', 'SES-105', 'REG-196', '2026-08-01 17:56:00'),
    ('SEL-097', 'SES-105', 'REG-197', '2026-08-01 18:01:00'),
    ('SEL-098', 'SES-105', 'REG-198', '2026-08-01 18:06:00'),
    ('SEL-099', 'SES-105', 'REG-199', '2026-08-01 18:11:00'),
    ('SEL-100', 'SES-105', 'REG-200', '2026-08-01 18:16:00');