# SafetyAlertSystem

JAVA - 21.0.8
Spring Boot - 3.2.2
Maven - 3.9.11
Twilio - 11.0.0

# DATABASE QUERIES
# POSTGRES - 14.20

DATABASE NAME : SafetyAlertSystem

TABLE : addresses

CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(255),
    county VARCHAR(255),
    eircode VARCHAR(255),
    address_type SMALLINT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

TABLE : users

CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    gender VARCHAR(10) NOT NULL,
    date_of_birth date NOT NULL,
    blood_group VARCHAR(20)
);

TABLE : emergency_contacts

CREATE TABLE emergency_contacts (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    relationship VARCHAR(50),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

TABLE : phone_numbers

CREATE TABLE phone_numbers (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(15) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_phone_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

TABLE : medical_records

CREATE TABLE medical_records (
    id SERIAL PRIMARY KEY,
    allergies VARCHAR(100),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user_medical
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);