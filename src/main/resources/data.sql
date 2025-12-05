-- Create admin_request table if it doesn't exist
CREATE TABLE IF NOT EXISTS admin_request (
    id BIGSERIAL PRIMARY KEY,
    business_name VARCHAR(100) NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    business_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Insert 5 customer users
INSERT INTO customer_user (name, phone, email, password_hash, created_at)
SELECT 'User 1', '9876543210', 'user1@gmail.com', '$2a$10$dummy.hash.for.password', NOW()
WHERE NOT EXISTS (SELECT 1 FROM customer_user WHERE email = 'user1@gmail.com');

INSERT INTO customer_user (name, phone, email, password_hash, created_at)
SELECT 'User 2', '9876543211', 'user2@gmail.com', '$2a$10$dummy.hash.for.password', NOW()
WHERE NOT EXISTS (SELECT 1 FROM customer_user WHERE email = 'user2@gmail.com');

INSERT INTO customer_user (name, phone, email, password_hash, created_at)
SELECT 'User 3', '9876543212', 'user3@gmail.com', '$2a$10$dummy.hash.for.password', NOW()
WHERE NOT EXISTS (SELECT 1 FROM customer_user WHERE email = 'user3@gmail.com');

INSERT INTO customer_user (name, phone, email, password_hash, created_at)
SELECT 'User 4', '9876543213', 'user4@gmail.com', '$2a$10$dummy.hash.for.password', NOW()
WHERE NOT EXISTS (SELECT 1 FROM customer_user WHERE email = 'user4@gmail.com');

INSERT INTO customer_user (name, phone, email, password_hash, created_at)
SELECT 'User 5', '9876543214', 'user5@gmail.com', '$2a$10$dummy.hash.for.password', NOW()
WHERE NOT EXISTS (SELECT 1 FROM customer_user WHERE email = 'user5@gmail.com');

-- Insert 2 admin users
INSERT INTO admin_user (username, password_hash, email, created_at)
SELECT 'admin1', '$2a$10$dummy.hash.for.admin1', 'admin1@test.com', NOW()
WHERE NOT EXISTS (SELECT 1 FROM admin_user WHERE username = 'admin1');

INSERT INTO admin_user (username, password_hash, email, created_at)
SELECT 'admin2', '$2a$10$dummy.hash.for.admin2', 'admin2@test.com', NOW()
WHERE NOT EXISTS (SELECT 1 FROM admin_user WHERE username = 'admin2');