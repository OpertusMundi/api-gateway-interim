--
-- Drop existing objects
--

DROP SEQUENCE IF EXISTS "web".account_asset_id_seq CASCADE;
DROP TABLE IF EXISTS "web".account_asset CASCADE;

DROP SEQUENCE IF EXISTS "web".account_sub_id_seq CASCADE;
DROP TABLE IF EXISTS "web".account_subscription CASCADE;

-- User assets and subscriptions (Table is not updatable)
--
-- When a new version of an asset is added, this table is searched based on parent PID. If purchased_on + update_interval > today,
-- a new row is added with the asset = <new PID>. All other fields are copied from the parent record.
--
-- If a user purchase a new version with additional years of updates, a new row is added. Updated versions update the latest order.

CREATE SEQUENCE "web".account_asset_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "web".account_asset
(
  "id"                              integer                 NOT NULL   DEFAULT nextval('web.account_asset_id_seq'::regclass),
  -- Asset owner
  "account"                         integer                 NOT NULL,   
  -- Order of purchase. For updated assets the order is copied
  "order"							integer					NOT NULL,
  -- Asset PID (asset version derives from the PID)
  "asset"							character varying(64)   NOT NULL,
  -- Copied from the order
  "purchased_on"					timestamp               NOT NULL,
  -- Date record added to user profile. On a new purchase, purchased_on == added_on. On version update, added_on is the date of the 
  -- new asset release date
  "added_on"						timestamp               NOT NULL,
  -- Copied from the order/cart item. For updated assets, price values are set to 0
  "total_price"                     numeric(20,6)           NOT NULL,
  "total_price_excluding_tax"       numeric(20,6)           NOT NULL,
  "total_tax"                       numeric(20,6)           NOT NULL,
  -- Update inteval (months)
  "update_interval"					integer					NOT NULL,
  -- Asset source: PURCHASED (new asset), UPDATE (version update of existing asset)
  "source"							character varying(32)	NOT NULL,
  CONSTRAINT pk_account_asset PRIMARY KEY (id),
    CONSTRAINT fk_account_asset_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_asset_order FOREIGN KEY ("order")
      REFERENCES "order".order (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE "web".account_sub_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "web".account_subscription
(
  "id"                              integer                 NOT NULL   DEFAULT nextval('web.account_sub_id_seq'::regclass),
  -- Subscription owner
  "account"                         integer                 NOT NULL,   
  -- Order of purchase
  "order"							integer					NOT NULL,
  -- Asset PID (asset version derives from the PID)
  "asset"							character varying(64)   NOT NULL,
  -- Always set to the initial subscription interval for both initial purchase and asset updates
  "start_date"						timestamp               NOT NULL,
  "end_date"						timestamp               NOT NULL,
  -- Subscription duration in months
  "duration"						integer					NOT NULL,
  -- Date record added to user profile. On a new purchase, purchased_on == added_on. On version update, added_on is the date of the 
  -- new asset release date
  "added_on"						timestamp               NOT NULL,
  -- Copied from the order/cart item. For updated assets, price values are set to 0
  "total_price"                     numeric(20,6)           NOT NULL,
  "total_price_excluding_tax"       numeric(20,6)           NOT NULL,
  "total_tax"                       numeric(20,6)           NOT NULL,
  -- Asset source: PURCHASED (new asset), RENEWED (update subscription), UPDATE (version update of existing asset)
  "source"							character varying(32)	NOT NULL,
  -- Renewed subscription
  "renewal"							integer					NULL,
  CONSTRAINT pk_account_sub PRIMARY KEY (id),
  CONSTRAINT fk_account_sub_renewal FOREIGN KEY ("renewal")
      REFERENCES web.account_subscription (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT fk_account_sub_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_sub_order FOREIGN KEY ("order")
      REFERENCES "order".order (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);
