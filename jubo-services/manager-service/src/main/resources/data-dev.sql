INSERT IGNORE INTO oj_role (name, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('ROLE_USER', 1, '2020-07-01 00:00:00', 1, '2020-07-01 00:00:00');

INSERT INTO oj_church (id, name, member_num, created_by, created_date, last_modified_by, last_modified_date)
VALUES (unhex(replace('dbe07414-49d1-11e6-b7a7-0242ac140002', '-', '')), 'MyChurch', 20,
1, '2020-07-01 00:00:00', 1, '2020-07-01 00:00:00');

INSERT INTO oj_jubo (id, church_id, title, start_date, end_date, view_count, deleted, created_by, created_date,
last_modified_by, last_modified_date)
VALUES (1, unhex(replace('dbe07414-49d1-11e6-b7a7-0242ac140002', '-', '')), '2020년 7월 5일 주보', '2020-07-05 00:00:00',
'2020-07-11 00:00:00', 0, 'N', 1, '2020-07-04 00:00:00', 1, '2020-07-04 00:00:00');

INSERT INTO oj_attachment (id, path, origin_name, file_type, created_by, created_date, last_modified_by,
last_modified_date)
VALUES (unhex(replace('a0fd7051-c82e-11ea-a901-0242ac120003', '-', '')), '/test/upload/path/testUUID1.jpg',
'testFile1.jpg', 'IMAGE', 1, '2020-07-04 00:00:00', 1, '2020-07-04 00:00:00');

--INSERT INTO oj_attachment (id, path, origin_name, file_type, created_by, created_date, last_modified_by,
--last_modified_date)
--VALUES (unhex(replace('9d6533ac-c83b-11ea-a901-0242ac120003', '-', '')), '/test/upload/path/testUUID2.jpg',
--'testFile2.jpg', 'IMAGE', 1, '2020-07-04 00:00:00', 1, '2020-07-04 00:00:00');
