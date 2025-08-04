    CREATE TABLE IF NOT EXISTS refresh_tokens (
        id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
        token VARCHAR(255) NOT NULL UNIQUE,
        expiration_date DATETIME NOT NULL,
        user_id INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );