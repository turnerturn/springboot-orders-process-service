-- Add terminal_id to queues and drop franchise_id
ALTER TABLE queues ADD COLUMN terminal_id UUID;
ALTER TABLE queues ADD CONSTRAINT fk_queues_terminal FOREIGN KEY (terminal_id) REFERENCES terminals (id);
ALTER TABLE queues DROP CONSTRAINT IF EXISTS fk_queues_franchise;
ALTER TABLE queues DROP COLUMN IF EXISTS franchise_id;
