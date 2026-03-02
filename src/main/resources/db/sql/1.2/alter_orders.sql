-- Add new fields to orders: terminal_id, recipe_id, volume, destination_id
-- Drop old fields: franchise_id, queue_id stays (kept for queue management)
ALTER TABLE orders ADD COLUMN terminal_id UUID;
ALTER TABLE orders ADD COLUMN recipe_id UUID;
ALTER TABLE orders ADD COLUMN volume DOUBLE PRECISION NOT NULL DEFAULT 0;
ALTER TABLE orders ADD COLUMN destination_id VARCHAR(255);
ALTER TABLE orders ADD CONSTRAINT fk_orders_terminal FOREIGN KEY (terminal_id) REFERENCES terminals (id);
ALTER TABLE orders ADD CONSTRAINT fk_orders_recipe FOREIGN KEY (recipe_id) REFERENCES recipes (id);
ALTER TABLE orders DROP CONSTRAINT IF EXISTS fk_orders_franchise;
ALTER TABLE orders DROP COLUMN IF EXISTS franchise_id;
