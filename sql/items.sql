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
INSERT INTO items (
        name,
        condition,
        category,
        brand,
        price,
        shipping,
        description
    )
SELECT o.name,
    o.condition_id,
    CASE
        WHEN o.category_name IS NOT NULL THEN (
            SELECT c.id
            FROM categories AS c
            WHERE c.name_all = o.category_name
            LIMIT 1
        )
        ELSE NULL
    END AS category,
    o.brand,
    o.price,
    o.shipping,
    o.description
FROM originals AS o;