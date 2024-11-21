create schema if not exists functionSchema;

CREATE TABLE functionSchema.points
(
    id SERIAL PRIMARY KEY,
    x  DOUBLE PRECISION,
    y  DOUBLE PRECISION
);

CREATE TABLE functionSchema.functions
(
    hash_id     BIGINT PRIMARY KEY,
    name        VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_function_points FOREIGN KEY (hash_id) REFERENCES functionSchema.points (id) ON DELETE CASCADE
);
