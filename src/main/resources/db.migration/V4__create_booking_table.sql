-- Create the 'booking' table
CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    service_id BIGINT REFERENCES service(id),
    customer_id BIGINT REFERENCES customer(id),
    startTime TIMESTAMP NOT NULL,
    endTime TIMESTAMP NOT NULL
);
