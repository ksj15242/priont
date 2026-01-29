import json
import os

import utils.date_utils as date_utils
import db.repository as repository

def _create_priont_price_data(date_end):
    date_start = date_utils.get_prev_business_date(base_date = date_end)

    dates = date_utils.get_prev_business_dates(date=date_end)
    date_to_idx = {date:i for i, date in enumerate(dates)}

    rows = repository.get_price_info(date_start, date_end)

    categories = {}
    for row in rows:
        category = row['category_id']
        categories.setdefault(category, []).append(row)

    for category_id, category_rows in categories.items():
        data = {}

        for row in category_rows:
            key = (row['product_id'], row['variety_id'], row['grade_id'])

            if key not in data:
                data[key] = {
                    'id' : row['category_id']+row['product_id']+row['variety_id']+row['grade_id'],
                    'category' : row['category_name'],
                    'name' : row['product_name'],
                    'variety' : row['variety_name'],
                    'unit' : row['unit_name'],
                    'grade' : row['grade_name'],
                    'regions' : {}
                }

            item = data[key]

            region = row['region_id']
            if region not in item['regions']:
                item['regions'][region] = {
                    'name' : row['region_name'],
                    'dates':dates,
                    'prices':[None]*len(dates)
                }

            region_data = item['regions'][region]
            region_data['prices'][date_to_idx[row['date']]] = row['price']

        params = {"data": []}
        for item in data.values():
            item['regions'] = list(item['regions'].values())
            params['data'].append(item)

        path = f'db/json/{date_end}'
        os.makedirs(path, exist_ok=True)

        file_name = f'{path}/priont_{category_id}.json'
        with open(file_name, "w", encoding="utf-8") as f:
            json.dump(params, f, ensure_ascii=False, indent=4)
        
        print(f'Saved : {file_name}')

def _create_priont_rank_data(date_end):
    date_start_7 = date_utils.get_prev_business_date(base_date=date_end, days=7)
    date_start_30 = date_utils.get_prev_business_date(base_date=date_end)
    dates = {'weekly':date_start_7, 'monthly':date_start_30}
    
    params = {
        'data':{
            'weekly':{
                "increase":[],
                "decrease":[],
            }, 
            'monthly':{
                "increase":[],
                "decrease":[],
            }
        }
    }

    for day, date_start in dates.items():
        rows = repository.get_rank_info(date_start, date_end)
        inc_row = rows['desc']
        dec_row = rows['asc']

        for row in inc_row:
            data = {
                "id" : row['category_id']+row['product_id']+row['variety_id']+row['grade_id'],
                'category' : row['category_name'],
                'name' : row['product_name'],
                'variety' : row['variety_name'],
                'unit' : row['unit_name'],
                'grade' : row['grade_name'],
                'price' : row['price'],
                'rate' : str(row['rate'])
            }
            params['data'][day]['increase'].append(data)

        for row in dec_row:
            data = {
                "id" : row['category_id']+row['product_id']+row['variety_id']+row['grade_id'],
                'category' : row['category_name'],
                'name' : row['product_name'],
                'variety' : row['variety_name'],
                'unit' : row['unit_name'],
                'grade' : row['grade_name'],
                'price' : row['price'],
                'rate' : str(row['rate'])
            }
            params['data'][day]['decrease'].append(data)

    path = f'db/json/{date_end}'
    os.makedirs(path, exist_ok=True)

    file_name = f'{path}/priont_rank.json'
    with open(file_name, "w", encoding="utf-8") as f:
        json.dump(params, f, ensure_ascii=False, indent=4)
    
    print(f'Saved : {file_name}')

def create_priont_data(date_end):
    _create_priont_price_data(date_end)
    _create_priont_rank_data(date_end)