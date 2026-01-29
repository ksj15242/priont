import asyncio
import aiohttp
import os

from api import config
from utils import date_utils, file_utils, data_utils
from db import repository

def _get_request_params(date_type):
    if date_type==config.DateType.INITIAL:
        start_date = date_utils.get_prev_business_date()
        end_date = date_utils.today()
    elif date_type==config.DateType.DAILY:
        start_date = date_utils.today()
        end_date = date_utils.tomorrow()

    products_info = repository.get_products_info()
        
    params_list = []
    for product in products_info:
        params = {
            **config.all_params,
            'p_startday': start_date,
            'p_endday': end_date,
            'p_itemcategorycode': product['category'],
            'p_itemcode': product['product'],
            'p_kindcode': product['variety']
        }

        params_list.append(params)
    
    return params_list

def _generate_product_id(param):
    return f"{param['p_itemcategorycode']}_{param['p_itemcode']}_{param['p_kindcode']}"

def _extract_error_code(data):
    if isinstance(data, list):
        return data[0]

    return data.get("error_code")

async def _request_data(session, param, semaphore):
    product_id = _generate_product_id(param)

    async with semaphore:
        try:
            async with session.get(config.URL, params=param) as response:
                if response.status!=200:
                    print(f"HTTP Error : {response.status} in {product_id}")
                    return None, product_id

                result = await response.json(content_type=None)
                data = result.get('data')

                error_code = _extract_error_code(data)
                if error_code in config.REQUEST_ERROR_CODES:
                    msg = f"{config.REQUEST_ERROR_CODES[error_code]}"
                    print(f"[API Error] {msg} in {product_id}")

                    return None, product_id
                
                return result, product_id
                
        except Exception as e:
            print(f"Exception occured : {e} in {product_id}")

            return None, product_id

async def run_fetch_process(date_type):
    if not isinstance(date_type, config.DateType):
        raise TypeError(f"Expected config.DateType, but got {type(date_type).__name__}: {repr(date_type)}")
    
    semaphore = asyncio.Semaphore(config.SEMAPHORE_LIMIT)

    async with aiohttp.ClientSession() as session:
        all_params = _get_request_params(date_type)

        print("Session Start...")
        print(f"Request {len(all_params)} items...")

        tasks = [
            _request_data(session, param, semaphore)
            for param in all_params
        ]

        results = await asyncio.gather(*tasks)
    
        print("Session End...")

        all_rows = []
        for data, product_id in results:
            if data is None:
                continue

            if date_type==config.DateType.INITIAL:
                save_path = os.path.join(config.SAVE_FILE_PATH, date_utils.today(), f"{product_id}.json")
                file_utils.save_initial_data_to_json(data, save_path)

            elif date_type==config.DateType.DAILY:
                rows = data_utils.get_db_rows(data)
                all_rows.extend(rows)
        
        if date_type==config.DateType.INITIAL:
            rows = repository.fetch_initial_rows_from_files()
            repository.insert_products_data(rows)
        elif date_type==config.DateType.DAILY:
            repository.insert_products_data(all_rows)