-- Create product_authorizations table
CREATE TABLE product_authorizations (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    supplier VARCHAR(255),
    loading_control VARCHAR(255),
    destination VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT
);

ALTER TABLE product_authorizations ADD CONSTRAINT fk_product_auth_product FOREIGN KEY (product_id) REFERENCES products (id);
