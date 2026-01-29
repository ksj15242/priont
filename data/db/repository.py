import sqlite3
import os

from api import config
from utils import data_utils, date_utils, file_utils

_PRODUCTS_INFO_QUERY = """
    SELECT p.category_id AS category, p.id AS product, v.id AS variety
    FROM product AS p
    JOIN variety AS v
    ON p.id = v.product_id
"""

_PRICE_INFO_QUERY = """
    SELECT
        pro.category_id AS category_id,
        cat.name AS category_name,
        pro.id AS product_id,
        pro.name AS product_name,
        var.id AS variety_id,
        var.name AS variety_name,
        var.unit AS unit_name,
        pri.grade_id,
        gra.name AS grade_name,
        pri.region_id,
        reg.name AS region_name,
        pri.date,
        pri.price
    FROM product pro
    JOIN category cat
        ON pro.category_id = cat.id
    JOIN variety var
        ON pro.id = var.product_id 
    JOIN price pri
        ON pro.id = pri.product_id 
        AND var.id = pri.variety_id
    JOIN grade gra
        ON pri.grade_id = gra.id
    JOIN region reg
        ON pri.region_id = reg.id

    WHERE pri.date BETWEEN :date_start AND :date_end
    ORDER BY pro.category_id, product_id, variety_id, grade_id, region_id, date
"""

# ==========================================================
# 상승률/하락률 TOP3 품목 정보 조회
# ==========================================================
def _get_rank_info_query(rank_type):
    if rank_type not in (config.RANKTYPE.ASC, config.RANKTYPE.DESC):
        raise ValueError(f"get_rank_info_query Error : order must be {config.RANKTYPE.ASC} or {config.RANKTYPE.DESC}")

    return f"""
        SELECT 
            pro.category_id,
            cat.name AS category_name,
            p1.product_id,
            pro.name AS product_name,
            p1.variety_id,
            var.name AS variety_name,
            var.unit AS unit_name,
            p1.grade_id,
            gra.name AS grade_name,
            p1.price,
            p2.price_avg,
            ROUND((p1.price - p2.price_avg) * 100.0 / p2.price_avg, 2) AS rate
        FROM price p1
        JOIN (
            SELECT
                product_id,
                variety_id,
                grade_id,
                region_id,
                CAST(ROUND(AVG(price),0) AS INTEGER) AS price_avg
            FROM price
            WHERE 
                date BETWEEN :date_start AND :date_end
                AND region_id = '0000'
            GROUP BY product_id, variety_id, grade_id
        ) p2
        ON 
            p1.product_id = p2.product_id
            AND p1.variety_id = p2.variety_id
            AND p1.grade_id = p2.grade_id
            AND p1.region_id = p2.region_id

        JOIN product pro
        ON p1.product_id = pro.id

        JOIN category cat
        ON pro.category_id = cat.id

        JOIN variety var
        ON p1.product_id = var.product_id AND p1.variety_id = var.id

        JOIN grade gra
        ON p1.grade_id = gra.id

        WHERE
            p1.date = :date_end
        ORDER BY rate {rank_type}
        LIMIT 3
    """

# ==========================================================
# 초기 파일로 저장한 데이터 DB에 사용할 row로 제공
# ==========================================================
def fetch_initial_rows_from_files():
    file_path = f"{config.SAVE_FILE_PATH}/{date_utils.today()}"
    file_names = [f for f in os.listdir(file_path) if f.endswith('.json')]
    
    all_rows = []
    for file in file_names:
        full_path = os.path.join(file_path, file)
        data = file_utils.load_json(full_path)
        rows = data_utils.get_db_rows(data)
        all_rows.extend(rows)
        
    return all_rows

# ==========================================================
# DB에 데이터 저장
# ==========================================================
def insert_products_data(rows):
    if not rows:
        return

    with sqlite3.connect('db/priont.db') as con:
        cur = con.cursor()
        cur.executemany("""
            INSERT OR IGNORE INTO price (product_id, variety_id, region_id, grade_id, date, price) 
                    VALUES(?, ?, ?, ?, ?, ?)
        """, rows)
    con.commit()

    print(f"Successfully inserted {len(rows)} rows.")

# ==========================================================
# API 요청할 품목 조회
# ==========================================================
def get_products_info():
    with sqlite3.connect('db/priont.db') as con:
        con.row_factory = sqlite3.Row
        cur = con.cursor()
        query = _PRODUCTS_INFO_QUERY
        cur.execute(query)
        
        return cur.fetchall()

# ==========================================================
# 날짜가 start~end에 해당하는 데이터 조회
# ==========================================================
def get_price_info(date_start, date_end):
    params = {
        "date_start" : date_start,
        "date_end" : date_end
    }

    with sqlite3.connect('db/priont.db') as conn:
        conn.row_factory = sqlite3.Row
        cur = conn.cursor()
        query = _PRICE_INFO_QUERY
        cur.execute(query, params)
    
        return cur.fetchall()

# ==========================================================
# priont_rank.json 데이터
# ==========================================================
def get_rank_info(date_start, date_end):
    queryAsc = _get_rank_info_query(config.RANKTYPE.ASC)
    queryDESC = _get_rank_info_query(config.RANKTYPE.DESC)

    params = {
        "date_start" : date_start,
        "date_end" : date_end
    }

    with sqlite3.connect('db/priont.db') as conn:
        conn.row_factory = sqlite3.Row
        cur = conn.cursor()

        cur.execute(queryAsc, params)
        asc_rows = cur.fetchall()

        cur.execute(queryDESC, params)
        desc_rows = cur.fetchall()

    return {
        "asc" : asc_rows,
        "desc" : desc_rows
    }