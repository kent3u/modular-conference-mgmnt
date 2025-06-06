INSERT INTO conference_room (id, name, status, location, capacity)
VALUES ('2d95af9f-88dc-408e-9bc1-658ef1c53683', 'Sample Room', 'AVAILABLE', 'Viru 1, 3rd floor, room 123', 100);

INSERT INTO conference(id, conference_room_id, start_time, end_time)
VALUES ('490a4c38-91c7-4264-9161-a4dc544d58e8', '2d95af9f-88dc-408e-9bc1-658ef1c53683', '2025-11-11 14:30:00', '2025-11-11 16:30:00');