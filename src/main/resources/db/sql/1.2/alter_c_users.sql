-- Drop merchant_id from c_users
ALTER TABLE c_users DROP CONSTRAINT IF EXISTS fk_users_merchant;
ALTER TABLE c_users DROP COLUMN IF EXISTS merchant_id;
