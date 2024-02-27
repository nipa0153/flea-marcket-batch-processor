DROP TABLE IF EXISTS originals;
CREATE TABLE IF NOT EXISTS originals(
    id integer unique not null,
    name text,
    condition_id integer,
    category_name text,
    brand text,
    price double precision,
    shipping int,
    description text,
    primary key(id)
);
-- 以下はpsqlにて実施
\ COPY originals (
    id,
    name,
    condition_id,
    category_name,
    brand,
    price,
    shipping,
    description
)
FROM '/Users/sakaidashigeaki/src/github.com/ShigeakiSakaida/flea-marcket-batch-processor/flea-marcket-batch-processor/train 2.tsv' WITH (FORMAT csv, DELIMITER E'\t', HEADER true);