drop table if exists refresh_tokens;
drop table if exists order_ingredients;
drop table if exists orders;
drop table if exists ingredients;
drop table if exists users;

CREATE TABLE users
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE ingredients
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(255)   NOT NULL,
    type          VARCHAR(100)   NOT NULL,

    proteins      INTEGER  NOT NULL,
    fat           INTEGER  NOT NULL,
    carbohydrates INTEGER  NOT NULL,
    calories      INTEGER  NOT NULL,

    price         NUMERIC(10, 2) NOT NULL,

    image         VARCHAR(1000) NOT NULL,
    image_large   VARCHAR(1000) NOT NULL,
    image_mobile  VARCHAR(1000) NOT NULL
);

-- ✅ sequence для номера заказа
CREATE SEQUENCE IF NOT EXISTS order_number_seq
    START WITH 1
    INCREMENT BY 1
    NO CYCLE;

CREATE TABLE orders
(
    id         UUID PRIMARY KEY,
    user_id    UUID REFERENCES users (id) ON DELETE SET NULL,

    status     VARCHAR(50) NOT NULL,
    name       VARCHAR(255),

    -- ✅ number генерится БД автоматически
    number     INTEGER NOT NULL UNIQUE DEFAULT nextval('order_number_seq'),

    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE TABLE order_ingredients
(
    order_id      UUID REFERENCES orders (id) ON DELETE CASCADE,
    ingredient_id UUID REFERENCES ingredients (id) ON DELETE RESTRICT,
    quantity      INTEGER NOT NULL,

    PRIMARY KEY (order_id, ingredient_id)
);

CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY,
    token      VARCHAR(512) NOT NULL UNIQUE,
    user_id    UUID NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL
);


