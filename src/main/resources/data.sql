INSERT INTO msg_user (user_id, email, user_name)
VALUES ('6bd3ade7-7daa-4bc7-ba33-3e5879865a8d', 'mock1@email.com', 'mock_user_1') ON CONFLICT DO NOTHING;
INSERT INTO msg_user (user_id, email, user_name)
VALUES ('2f3197d6-f0d9-480a-9784-2588012e3e73', 'mock2@email.com', 'mock_user_2') ON CONFLICT DO NOTHING;