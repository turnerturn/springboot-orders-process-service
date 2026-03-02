-- Create recipes table (replaces menu_items)
CREATE TABLE recipes (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    terminal_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT
);

ALTER TABLE recipes ADD CONSTRAINT fk_recipes_terminal FOREIGN KEY (terminal_id) REFERENCES terminals (id);
