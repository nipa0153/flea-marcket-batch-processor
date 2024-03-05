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
-- 一時テーブルの作成
CREATE TEMP TABLE temp_category AS
SELECT id,
    COALESCE(
        NULLIF(split_part(category_name, '/', 1), ''),
        '未分類'
    ) AS parent,
    COALESCE(
        NULLIF(split_part(category_name, '/', 2), ''),
        '未分類'
    ) AS child,
    COALESCE(
        NULLIF(split_part(category_name, '/', 3), ''),
        '未分類'
    ) AS grandchild
FROM original;
-- カテゴリマッピングのための一時テーブルを作成
WITH category_mapping AS (
    SELECT o.id AS original_id,
        c.id AS category_id
    FROM temp_category tc
        JOIN original o ON tc.id = o.id
        JOIN category c ON (
            tc.parent || '/' || tc.child || '/' || tc.grandchild = c.name_all
            OR (
                o.category_name IS NULL
                AND c.name = '未分類'
            )
        )
) -- itemsテーブルへのデータ挿入
INSERT INTO items (
        name,
        condition,
        category,
        brand,
        price,
        stock,
        shipping,
        description
    )
SELECT o.name,
    o.condition_id,
    cm.category_id,
    o.brand,
    o.price,
    NULL AS stock,
    -- stockはNULLか適切なデフォルト値
    o.shipping,
    o.description
FROM original o
    LEFT JOIN category_mapping cm ON o.id = cm.original_id;
-- 一時テーブルの削除
DROP TABLE temp_category;
-- INSERT INTO items (
--         name,
--         condition,
--         category,
--         brand,
--         price,
--         shipping,
--         description
--     )
-- SELECT o.name,
--     o.condition_id,
--     CASE
--         WHEN o.category_name IS NOT NULL THEN (
--             SELECT c.id
--             FROM categories AS c
--             WHERE c.name_all = o.category_name
--             LIMIT 1
--         )
--         ELSE NULL
--     END AS category,
--     o.brand,
--     o.price,
--     o.shipping,
--     o.description
-- FROM originals AS o;
--さいしゅう
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
    COALESCE(
        (
            SELECT c.id
            FROM categories AS c
            WHERE -- category_nameから最初の3階層を抽出して比較
                c.name_all = CONCAT(
                    NULLIF(split_part(o.category_name, '/', 1), ''),
                    '/',
                    NULLIF(split_part(o.category_name, '/', 2), ''),
                    '/',
                    NULLIF(split_part(o.category_name, '/', 3), '')
                )
            LIMIT 1
        ), -- カテゴリが見つからない場合はNULLを挿入
        NULL
    ) AS category,
    o.brand,
    o.price,
    o.shipping,
    o.description
FROM originals AS o;