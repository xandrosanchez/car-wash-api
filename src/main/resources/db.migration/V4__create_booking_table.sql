-- Create the 'booking' table
CREATE TABLE booking (
    id BIGINT PRIMARY KEY,
    service_id BIGINT REFERENCES service(id),
    customer_id BIGINT REFERENCES customer(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);
