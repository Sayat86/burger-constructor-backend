CREATE TABLE users
(
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE ingredients
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255)   NOT NULL,
    type          VARCHAR(100)   NOT NULL,

    proteins      NUMERIC(6, 2)  NOT NULL,
    fat           NUMERIC(6, 2)  NOT NULL,
    carbohydrates NUMERIC(6, 2)  NOT NULL,
    calories      INTEGER        NOT NULL,

    price         NUMERIC(10, 2) NOT NULL,

    image         TEXT,
    image_large   TEXT,
    image_mobile  TEXT
);

CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      REFERENCES users (id) ON DELETE SET NULL,

    status     VARCHAR(50) NOT NULL,
    name       VARCHAR(255),

    number     INTEGER     NOT NULL UNIQUE,

    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE TABLE order_ingredients
(
    order_id      BIGINT REFERENCES orders (id) ON DELETE CASCADE,
    ingredient_id BIGINT REFERENCES ingredients (id) ON DELETE RESTRICT,

    PRIMARY KEY (order_id, ingredient_id)
);
