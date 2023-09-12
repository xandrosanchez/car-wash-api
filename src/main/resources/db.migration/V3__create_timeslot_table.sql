-- Create the 'timeslot' table
CREATE TABLE timeslot (
    id SERIAL PRIMARY KEY,
    service_id BIGINT REFERENCES service(id),
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    available BOOLEAN
);
