-- Create terminals table (replaces franchises)
CREATE TABLE terminals (
    id UUID PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    contact_details VARCHAR(255) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    number_of_queues INT NOT NULL,
    max_queue_size INT NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT
);
