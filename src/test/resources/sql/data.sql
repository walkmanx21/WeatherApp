INSERT INTO Users (id, login, password)
VALUES (1, 'walkmanx21', 'walkmanx21'),
       (2, 'walkmanx22', 'walkmanx22'),
       (3, 'walkmanx23', 'walkmanx23'),
       (4, 'walkmanx24', 'walkmanx24'),
       (5, 'walkmanx25', 'walkmanx25');

INSERT INTO Sessions (id, user_id, expires_at)
VALUES ('123e4567-e89b-12d3-a456-426655440001', 1, '2025-08-06 11:41:52.442544'),
       ('123e4567-e89b-12d3-a456-426655440002', 2, '2025-08-06 12:41:52.442544'),
       ('123e4567-e89b-12d3-a456-426655440003', 3, '2025-08-06 13:41:52.442544'),
       ('123e4567-e89b-12d3-a456-426655440004', 4, '2025-08-06 14:41:52.442544'),
       ('123e4567-e89b-12d3-a456-426655440005', 5, '2025-08-06 15:41:52.442544');