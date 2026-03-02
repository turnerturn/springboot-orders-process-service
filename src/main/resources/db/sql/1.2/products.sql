-- Create products table
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    supplier VARCHAR(255),
    loading_control VARCHAR(255),
    recipe_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT
);

ALTER TABLE products ADD CONSTRAINT fk_products_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id);
