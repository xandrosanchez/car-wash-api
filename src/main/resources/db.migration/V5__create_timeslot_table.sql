-- Create the 'timeslot' table
CREATE TABLE timeslot (
    id BIGINT PRIMARY KEY,
    service_id BIGINT REFERENCES service(id),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    available BOOLEAN
);
