--
-- Drop existing objects
--

DROP SEQUENCE IF EXISTS "order".order_id_seq CASCADE;
DROP TABLE IF EXISTS "order".order CASCADE;

DROP SEQUENCE IF EXISTS "order".order_status_hist_id_seq CASCADE;
DROP TABLE IF EXISTS "order".order_status_hist CASCADE;

DROP SEQUENCE IF EXISTS "order".order_item_id_seq CASCADE;
DROP TABLE IF EXISTS "order".order_item CASCADE;

--
-- Order
--

CREATE SEQUENCE "order".order_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "order".order
(
  "id"                              integer                 NOT NULL   DEFAULT nextval('order.order_id_seq'::regclass),
  -- Unique key used as a business key for the purchase workflow
  "key"                             uuid                    NOT NULL, 
  "account"                         integer                 NOT NULL,   
  -- Reference to the cart instance used during the checkout operation
  "cart"							              integer                 NOT NULL,  
  "total_price"                     numeric(20,6)           NOT NULL,
  "total_price_excluding_tax"       numeric(20,6)           NOT NULL,
  "total_tax"                       numeric(20,6)           NOT NULL,
  "currency"                        text                    NOT NULL,
  "created_on"                      timestamp               NOT NULL,
  "status"                          character varying(64)   NOT NULL,
  "status_updated_on"               timestamp               NOT NULL,
  "delivery_method"                 character varying(64)   NOT NULL,
  "payment_method"                  character varying(64)   NOT NULL,
  -- User friendly reference code for support (unique per user account)
  "reference_number"                character varying,
  CONSTRAINT pk_order PRIMARY KEY (id),
  CONSTRAINT uq_order_key UNIQUE ("key"),
  CONSTRAINT fk_order_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT fk_order_cart FOREIGN KEY ("cart")
      REFERENCES "order".cart (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
);

--
-- Order status history
--

CREATE SEQUENCE "order".order_status_hist_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "order".order_status_hist
(
  "id"                    integer                 NOT NULL   DEFAULT nextval('order.order_status_hist_id_seq'::regclass),
  "order"                 integer                 NOT NULL,  
  "status"                character varying(64)   NOT NULL,
  "status_updated_on"     timestamp               NOT NULL,
  -- Optional user key. If the property is updated by the system without user interaction,
  -- the value is set to null
  "status_updated_by"     integer,
  CONSTRAINT pk_order_status_hist PRIMARY KEY (id),
  CONSTRAINT fk_order_status_hist_order FOREIGN KEY ("order")
      REFERENCES "order".order (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Order item
--

CREATE SEQUENCE "order".order_item_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "order".order_item
(
  "id"              integer                 NOT NULL   DEFAULT nextval('order.order_item_id_seq'::regclass),
  "order"           integer                 NOT NULL,
  -- Invoice line number
  "index"           integer					NOT NULL,
  -- Item type: Bundle, Value-Added-Service (VAS), API, Catalogue Asset
  "type"			character varying(64)   NOT NULL,
  "product"         uuid                    NOT NULL,
  "pricing_model"   uuid                    NOT NULL,
  "discount_code"	character varying(64)   NULL,
  CONSTRAINT pk_order_item PRIMARY KEY (id),
  CONSTRAINT fk_order_item_order FOREIGN KEY ("order")
      REFERENCES "order".order (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);
