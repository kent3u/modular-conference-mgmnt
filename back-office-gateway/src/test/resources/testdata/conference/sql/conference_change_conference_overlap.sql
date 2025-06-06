INSERT INTO conference_room (id, name, status, location, capacity)
VALUES ('2d95af9f-88dc-408e-9bc1-658ef1c53683', 'Sample Room', 'AVAILABLE', 'Viru 1, 3rd floor, room 123', 100);


INSERT INTO conference(id, conference_room_id, start_time, end_time)
VALUES ('aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18', '2d95af9f-88dc-408e-9bc1-658ef1c53683', '2025-11-11 14:30:00', '2025-11-11 16:30:00'),
       ('92ac054c-7eb4-4bd8-8a8d-42477e3a3526', '2d95af9f-88dc-408e-9bc1-658ef1c53683', '2025-11-12 14:30:00', '2025-11-14 16:30:00');