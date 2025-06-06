INSERT INTO conference_room(id, name, status, location, capacity)
VALUES ('13baab4d-ee7f-449e-9e8a-3a92972472b2', 'Conference Room Stars', 'AVAILABLE', 'Tallinn, Kontor 12a, room 12', 25),
       ('28182ccc-b81b-459a-be16-bac00333d246', 'New Room', 'UNDER_CONSTRUCTION', 'TBA', 1);

INSERT INTO conference(id, conference_room_id, start_time, end_time)
VALUES ('aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18', '13baab4d-ee7f-449e-9e8a-3a92972472b2', '2025-11-11 14:30:00', '2025-11-11 18:30:00');