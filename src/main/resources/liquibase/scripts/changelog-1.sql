-- liquibase formatted sql

-- changeset SavelyDomnikov:1

CREATE TABLE sock (
    id BIGSERIAL PRIMARY KEY,
    color VARCHAR(50) NOT NULL,
    cotton_part INT NOT NULL CHECK ( cotton_part >= 0 and cotton_part <= 100 ),
    quantity INT NOT NULL CHECK ( quantity > 0 ),
    UNIQUE (color, cotton_part)
)