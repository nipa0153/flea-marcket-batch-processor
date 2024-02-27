-- category テーブルの作成
DROP TABLE IF EXISTS temp_category;
DROP TABLE IF EXISTS categories;
CREATE TABLE IF NOT EXISTS categories (
    id serial PRIMARY KEY,
    name text,
    parent_id integer,
    name_all text
);
-- 一時テーブルの作成
-- category_nameがNULLでないレコードについて、各階層を抽出
CREATE TEMP TABLE temp_categories AS
SELECT NULLIF(split_part(category_name, '/', 1), '') AS parent,
    NULLIF(split_part(category_name, '/', 2), '') AS child,
    NULLIF(split_part(category_name, '/', 3), '') AS grandchild
FROM originals
WHERE category_name IS NOT NULL;
-- 1階層目（親）のカテゴリを挿入
INSERT INTO categories (name)
SELECT DISTINCT parent
FROM temp_categories
WHERE parent IS NOT NULL;
-- 2階層目（子）のカテゴリを挿入
-- 親カテゴリが存在するものだけを選択
INSERT INTO categories (name, parent_id)
SELECT DISTINCT child,
    c.id
FROM temp_categories AS tc
    JOIN categories AS c ON tc.parent = c.name
WHERE child IS NOT NULL;
-- 3階層目（孫）のカテゴリを挿入
-- 子カテゴリと親カテゴリが存在するものだけを選択
INSERT INTO categories (name, parent_id, name_all)
SELECT DISTINCT tc.grandchild,
    c1.id,
    tc.parent || '/' || tc.child || '/' || tc.grandchild
FROM temp_categories AS tc
    JOIN categories AS c1 ON tc.child = c1.name
    JOIN categories AS c2 ON tc.parent = c2.name
    AND c1.parent_id = c2.id
WHERE tc.grandchild IS NOT NULL;
-- 一時テーブルの削除
DROP TABLE temp_categories;