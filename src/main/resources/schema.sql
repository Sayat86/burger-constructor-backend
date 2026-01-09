
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
    calories      INTEGER        NOT NULL,

    price         NUMERIC(10, 2) NOT NULL,

    image         VARCHAR(1000) NOT NULL,
    image_large   VARCHAR(1000) NOT NULL,
    image_mobile  VARCHAR(1000) NOT NULL
);

CREATE TABLE orders
(
    id         UUID PRIMARY KEY,
    user_id    BIGINT      REFERENCES users (id) ON DELETE SET NULL,

    status     VARCHAR(50) NOT NULL,
    name       VARCHAR(255),

    number     INTEGER     NOT NULL UNIQUE,

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
