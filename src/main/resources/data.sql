INSERT INTO role (name) VALUES ('read');
INSERT INTO role (name) VALUES ('write');

INSERT INTO app_user (email, password, authorities)
VALUES ('developer@account.dev', '$2a$10$y1da7Uk286xbQNxGUVWlNePWq9bY/3aDddA08U41XBIlLherTZv96', ARRAY['manager']);