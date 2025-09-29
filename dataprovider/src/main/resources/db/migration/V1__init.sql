
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  currency VARCHAR(10) NOT NULL,
  threshold_percent NUMERIC(10,4) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS rate_history (
  id BIGSERIAL PRIMARY KEY,
  base_currency VARCHAR(10) NOT NULL,
  quote_currency VARCHAR(10) NOT NULL,
  rate NUMERIC(18,6) NOT NULL,
  change_percent NUMERIC(10,4),
  timestamp TIMESTAMP NOT NULL
);
