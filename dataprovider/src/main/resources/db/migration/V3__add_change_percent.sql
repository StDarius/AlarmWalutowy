ALTER TABLE rate_history
    ADD COLUMN IF NOT EXISTS change_percent NUMERIC(18,6) NULL;