INSERT INTO participant (id, email, gender, first_name, last_name, date_of_birth)
VALUES ('a83faf56-5940-4f5e-ad19-4e9f4d33c232', 'test@test.ee', 1, 'Jane', 'Doe', '1990-10-10');

INSERT INTO conference_room (id, name, status, location, capacity)
VALUES ('2d95af9f-88dc-408e-9bc1-658ef1c53683', 'Sample Room', 'AVAILABLE', 'Viru 1, 3rd floor, room 123', 2);

INSERT INTO conference (id, conference_room_id, start_time, end_time)
VALUES ('aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18', '2d95af9f-88dc-408e-9bc1-658ef1c53683', '2025-11-11 14:30:00', '2025-11-11 16:30:00');

INSERT INTO registration (id, conference_participant_code, conference_id, participant_id)
VALUES ('f18842dd-daa3-44d5-a3b5-d70e78756364', 'CCE12g', 'aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18', 'a83faf56-5940-4f5e-ad19-4e9f4d33c232');
