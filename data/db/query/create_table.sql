-- =============================
-- CATEGORY
-- =============================
CREATE TABLE IF NOT EXISTS category (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

-- =============================
-- PRODUCT
-- =============================
CREATE TABLE IF NOT EXISTS product (
    id TEXT PRIMARY KEY,
    category_id TEXT NOT NULL,
    name TEXT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- =============================
-- VARIETY
-- =============================
CREATE TABLE IF NOT EXISTS variety (
    product_id TEXT NOT NULL,
    id TEXT NOT NULL,
    name TEXT NOT NULL,
    unit TEXT NOT NULL,
    PRIMARY KEY (product_id, id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- =============================
-- COUNTRY
-- =============================
CREATE TABLE IF NOT EXISTS region (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

-- =============================
-- RANK
-- =============================
CREATE TABLE IF NOT EXISTS grade (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

-- =============================
-- PRICE
-- =============================
CREATE TABLE IF NOT EXISTS price (
    product_id TEXT NOT NULL,
    variety_id TEXT NOT NULL,
    region_id TEXT NOT NULL,
    grade_id TEXT NOT NULL,
    date TEXT NOT NULL,
    price INTEGER NOT NULL,

    PRIMARY KEY (
        product_id,
        variety_id,
        region_id,
        grade_id,
        date
    ),

    FOREIGN KEY (product_id, variety_id)
        REFERENCES variety(product_id, id),

    FOREIGN KEY (region_id)
        REFERENCES region(id),

    FOREIGN KEY (grade_id)
        REFERENCES grade(id)
);

-- =============================
-- INDEXES
-- =============================
CREATE INDEX IF NOT EXISTS idx_price_search
ON price (product_id, variety_id, region_id, grade_id, date);