CREATE TABLE crypto.public.currencies
(
    id        INTEGER PRIMARY KEY,
    symbol    VARCHAR(32) UNIQUE,
    price_usd DECIMAL
);

CREATE TABLE crypto.public.users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(32),
    symbol   VARCHAR(32),
    price    DECIMAL
);