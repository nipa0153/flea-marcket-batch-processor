DROP TABLE IF EXISTS items;
CREATE TABLE IF NOT EXISTS items(
    id serial not null,
    name text,
    condition integer,
    category integer,
    brand text,
    price double precision,
    shipping integer,
    description text,
    primary key(id)
);