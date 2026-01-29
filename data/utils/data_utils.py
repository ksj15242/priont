import pandas as pd

# ==========================================================
# ['condition']['item']에서 품목 공통 데이터 제공
# ==========================================================
def _get_products_metadata(data):
    return pd.json_normalize(data['condition']['item'])

# ==========================================================
# ['data']['item']에서 지역별 품목 가격 정보 제공
# 지역별 여러 유통업체 존재
# ==========================================================
def _get_products_data(data):
    return pd.json_normalize(data['data']['item'])

# ==========================================================
# 필요한 품목 공통 정보 추출
# ==========================================================
def _extract_meta_info(df_meta_info):
    required_columns = ('p_itemcode', 'p_kindcode', 'p_productrankcode')

    for column in required_columns:
        if column not in df_meta_info.columns:
            raise ValueError(f"Column '{column}' not found in metadata.")

    if df_meta_info.empty:
        raise ValueError("Metadata dataframe is empty. Cannot extract IDs.")

    row = df_meta_info.iloc[0]

    return {
        'product_id': row['p_itemcode'],
        'variety_id': row['p_kindcode'],
        'rank_id': row['p_productrankcode']
    }

# ==========================================================
# countyname='평균'은 전국 평균을 의미 
# 특정 날짜에 전국 평균이 존재한다는 말은 최소 1개 이상
# 지역 데이터가 존재한다는 의미이므로 조회할 날짜에 포함 
# ==========================================================
def _get_valid_dates(df):
    return df[df['countyname'] == '평균'][['yyyy', 'regday']]

# ==========================================================
# DB에 매핑할 지역코드, '평년'(사용X) 및 '의정부'(데이터X) 제외
# '평균'에 해당하는 code(0000)는 custom
# ==========================================================
def _get_region_code_map():
    regions_code = {
        '평균':'0000', '서울':'1101', '부산':'2100', '대구':'2200', '인천':'2300',
        '광주':'2401', '대전':'2501', '울산':'2601', '수원':'3111', '강릉':'3214',
        '춘천':'3211', '청주':'3311', '전주':'3511', '포항':'3711', '제주':'3911',
        '순천':'3613', '안동':'3714', '창원':'3814', '용인':'3145', '세종':'2701',
        '성남':'3112', '고양':'3138', '천안':'3411', '김해':'3818'
    }

    return regions_code

# ==========================================================
# price 항목 데이터 클리닝
# ==========================================================
def _preprocess_prices(df):
    if 'price' not in df.columns:
        raise ValueError("DataFrame must contain a 'price' column.")

    df_valid = df[df['price']!='-'].copy()
    df_valid['price'] = df_valid['price'].str.replace(',', '', regex=False).astype(int)

    return df_valid

# ==========================================================
# 최종적으로 DB에 넣을 형식으로 데이터 반환
# ==========================================================
def get_db_rows(data):
    df_meta_info = _get_products_metadata(data)
    df_data = _get_products_data(data)

    meta_info = _extract_meta_info(df_meta_info)
    valid_dates = _get_valid_dates(df_data)
    region_map = _get_region_code_map()

    df = _preprocess_prices(df_data)
    price_mean = (
        df
        .groupby(['countyname', 'yyyy', 'regday'])['price']
        .mean()
    )

    rows = []
    for region_name, region_code in region_map.items():
        for year, regday in zip(valid_dates['yyyy'], valid_dates['regday']):
            key = (region_name, year, regday)

            if key not in price_mean.index:
                continue

            price = int(price_mean.loc[key])
            date = f"{year}-{regday.replace('/','-')}"

            rows.append((
                meta_info['product_id'],
                meta_info['variety_id'],
                region_code,
                meta_info['rank_id'],
                date,
                price
            ))

    return rows