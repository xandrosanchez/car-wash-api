-- Create the 'service' table
CREATE TABLE service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price DOUBLE PRECISION NOT NULL
);
