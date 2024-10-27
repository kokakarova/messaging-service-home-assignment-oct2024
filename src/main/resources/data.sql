INSERT INTO msg_user (id, email, user_name)
VALUES ('6bd3ade7-7daa-4bc7-ba33-3e5879865a8d', 'mock1@email.com', 'mock_user_1'),
       ('2f3197d6-f0d9-480a-9784-2588012e3e73', 'mock2@email.com', 'mock_user_2')
ON CONFLICT DO NOTHING;
INSERT INTO message (id, content, date_sent, read, receiver_id, sender_id)
VALUES ('1ad3ade7-7daa-4bc7-ba33-3e5879865a42', 'mock message from 2 months ago', '2024-08-25T09:30:09.173Z', true,
        '6bd3ade7-7daa-4bc7-ba33-3e5879865a8d', '2f3197d6-f0d9-480a-9784-2588012e3e73'),
       ('2bd3ade7-7daa-4bc7-ba33-3e5879865a53', 'mock message from 1 month ago', '2024-09-20T09:30:09.173Z', true,
        '6bd3ade7-7daa-4bc7-ba33-3e5879865a8d', '2f3197d6-f0d9-480a-9784-2588012e3e73'),
       ('3cd3ade7-7daa-4bc7-ba33-3e5879865a64', 'new message', '2024-10-26T09:30:09.173Z', false,
        '6bd3ade7-7daa-4bc7-ba33-3e5879865a8d', '2f3197d6-f0d9-480a-9784-2588012e3e73')
ON CONFLICT DO NOTHING;