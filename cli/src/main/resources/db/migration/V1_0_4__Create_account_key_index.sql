DROP INDEX IF EXISTS web.idx_account_key;

CREATE UNIQUE INDEX idx_account_key ON web.account USING btree (key);
