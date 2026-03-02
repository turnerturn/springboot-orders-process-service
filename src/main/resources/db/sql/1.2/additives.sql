-- Create additives table
CREATE TABLE additives (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT
);

ALTER TABLE additives ADD CONSTRAINT fk_additives_product FOREIGN KEY (product_id) REFERENCES products (id);
