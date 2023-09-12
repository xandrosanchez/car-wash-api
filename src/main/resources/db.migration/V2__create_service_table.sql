-- Create the 'service' table
CREATE TABLE service (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price DOUBLE PRECISION NOT NULL
);
