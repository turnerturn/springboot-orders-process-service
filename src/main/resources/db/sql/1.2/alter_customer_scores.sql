-- Add terminal_id to customer_scores and drop franchise_id
ALTER TABLE customer_scores ADD COLUMN terminal_id UUID;
ALTER TABLE customer_scores ADD CONSTRAINT fk_customer_scores_terminal FOREIGN KEY (terminal_id) REFERENCES terminals (id);
ALTER TABLE customer_scores DROP CONSTRAINT IF EXISTS fk_franchise;
ALTER TABLE customer_scores DROP COLUMN IF EXISTS franchise_id;
