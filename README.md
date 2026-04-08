# SafetyAlertSystem

JAVA - 21.0.8
Spring Boot - 3.2.2
Maven - 3.9.11
Twilio - 11.0.0

## DATABASE QUERIES
## POSTGRES - 14.20

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
    blood_group VARCHAR(20),
    verified BOOLEAN
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

TABLE : alert_locations

CREATE TABLE alert_locations (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    accuracy DOUBLE PRECISION,
    source VARCHAR(50)
);

TABLE : alerts

CREATE TABLE alerts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_alert_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    status VARCHAR(50) DEFAULT 'CREATED',
    triggered_at TIMESTAMP WITH TIME ZONE NOT NULL,
    triggered_mode VARCHAR(50) NOT NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT fk_alert_location
        FOREIGN KEY (location_id)
        REFERENCES alert_locations(id)
        ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

TABLE : call_attempt

CREATE TABLE call_logs (
    id BIGSERIAL PRIMARY KEY,
    alert_id BIGINT,
    user_id BIGINT,
    phone_number VARCHAR(20),
    attempt_count INT NOT NULL DEFAULT 0,
    call_sid VARCHAR(64),
    status VARCHAR(20) NOT NULL DEFAULT 'INITIATED',
    duration INT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

TABLE : Otp

CREATE TABLE otp (
    id BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(15) NOT NULL,
    otp_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    attempts INT DEFAULT 0,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    message_sid character varying(255),
    delivery_status VARCHAR (20),
    retry_count INTEGER
);
