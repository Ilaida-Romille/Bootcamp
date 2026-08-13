
INSERT INTO space
    (dtype, space_id, name, status, room_number, floor, room_type,
     max_occupancy, housekeeping_status)
VALUES
    ('Room', 1001, 'Room 101', 0, '101', 1, 'Standard', 2, 2),
    ('Room', 1002, 'Room 102', 0, '102', 1, 'Standard', 2, 1),
    ('Room', 1003, 'Room 103', 0, '103', 1, 'Deluxe',   4, 1),
    ('Room', 1004, 'Room 201', 0, '201', 2, 'Standard', 2, 2),
    ('Room', 1005, 'Room 202', 0, '202', 2, 'Suite',    6, 0),
    ('Room', 1006, 'Room 203', 0, '203', 2, 'Deluxe',   4, 0),
    ('Room', 1007, 'Room 301', 0, '301', 3, 'Standard', 2, 0),
    ('Room', 1008, 'Room 302', 0, '302', 3, 'Penthouse', 8, 0);

INSERT INTO guest_stay
    (stay_id, room_id, party_size, check_in_date, check_out_date, active)
VALUES
    (2001, 1001, 2, '2026-01-02', '2026-01-04', FALSE),
    (2002, 1001, 1, '2026-02-10', '2026-02-12', FALSE),
    (2003, 1001, 2, '2026-03-15', NULL,         TRUE),
    (2004, 1002, 2, '2026-01-20', '2026-01-23', FALSE),
    (2005, 1002, 1, '2026-04-05', NULL,         TRUE),
    (2006, 1003, 4, '2026-05-11', '2026-05-14', FALSE),
    (2007, 1004, 2, '2026-02-01', '2026-02-03', FALSE),
    (2008, 1004, 2, '2026-03-08', '2026-03-10', FALSE),
    (2009, 1004, 1, '2026-06-17', NULL,         TRUE);
