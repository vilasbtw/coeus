ALTER TABLE users
ADD COLUMN account_expired BIT(1) DEFAULT b'0',
ADD COLUMN account_locked BIT(1) DEFAULT b'0',
ADD COLUMN credentials_expired BIT(1) DEFAULT b'0',
ADD COLUMN enabled BIT(1) DEFAULT 1;
