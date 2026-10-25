DROP DATABASE IF EXISTS delivery_db;
CREATE DATABASE delivery_db;
USE delivery_db;

-- Таблиця клієнтів
CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(250) NOT NULL
);

-- Таблиця кур'єрів
CREATE TABLE courier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL
);

-- Таблиця відправлень
CREATE TABLE shipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(250) NOT NULL,
    weight DOUBLE NOT NULL,
    service_level DOUBLE NOT NULL,
    sensitivity DOUBLE NOT NULL
);

-- Таблиця замовлень доставок
CREATE TABLE delivery_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    courier_id BIGINT NOT NULL,
    shipment_id BIGINT NOT NULL,
    state VARCHAR(30) NOT NULL,
    f1 DOUBLE NOT NULL,
    f2 DOUBLE NOT NULL,
    f3 DOUBLE NOT NULL,
    integral_score DOUBLE NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (courier_id) REFERENCES courier(id),
    FOREIGN KEY (shipment_id) REFERENCES shipment(id)
);

-- Початкові дані
INSERT INTO client (full_name, phone, address) VALUES
    ('Шевченко Тарас Григорович', '+380501112233', 'м. Полтава, вул. Соборності, 12'),
    ('Коваленко Олена Олексіївна', '+380674445566', 'м. Полтава, вул. Європейська, 45'),
    ('Мельник Дмитро Ігорович', '+380937778899', 'м. Полтава, просп. Миру, 8');

INSERT INTO courier (full_name, phone, vehicle_type) VALUES
    ('Бондаренко Андрій Сергійович', '+380509998877', 'Моторолер'),
    ('Ткаченко Ігор Володимирович', '+380671112244', 'Легковий автомобіль'),
    ('Кравченко Ольга Миколаївна', '+380933334455', 'Електровелосипед');

INSERT INTO shipment (description, weight, service_level, sensitivity) VALUES
    ('Документи (договір №104)', 0.5, 0.90, 0.20),
    ('Електроніка (ноутбук ASUS)', 3.2, 0.75, 0.85),
    ('Медикаменти (термінова аптечка)', 1.1, 0.95, 0.90);

INSERT INTO delivery_order (client_id, courier_id, shipment_id, state, f1, f2, f3, integral_score) VALUES
    (1, 1, 1, 'NEW', 0.80, 0.90, 0.20, 0.685),
    (2, 2, 2, 'ACCEPTED', 0.60, 0.75, 0.85, 0.715),
    (3, 3, 3, 'IN_TRANSIT', 0.95, 0.95, 0.90, 0.9375);