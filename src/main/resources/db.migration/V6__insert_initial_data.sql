-- Добавление данных в таблицу "service"
INSERT INTO service (id, name, price) VALUES
    (1, 'Basic Wash', 20.0),
    (2, 'Premium Wash', 35.0),
    (3, 'Interior Detailing', 50.0);

-- Добавление данных в таблицу "customer"
INSERT INTO customer (id, name, phone_number) VALUES
    (1, 'John Doe', '123-456-7890'),
    (2, 'Jane Smith', '987-654-3210');

-- Добавление данных в таблицу "timeslot"
INSERT INTO timeslot (id, service_id, start_time, end_time, available) VALUES
    (1, 1, '2023-09-15 09:00:00', '2023-09-15 10:00:00', true),
    (2, 2, '2023-09-15 10:00:00', '2023-09-15 11:00:00', true),
    (3, 3, '2023-09-15 09:00:00', '2023-09-15 10:00:00', true);