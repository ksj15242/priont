import os

from dotenv import load_dotenv

# REFERENCE PAGE
# "https://www.kamis.or.kr/customer/reference/openapi_list.do?action=detail&boardno=17"

load_dotenv()

API_KEY = os.getenv("API_KEY")
ID = os.getenv("ID")
URL = os.getenv("URL")

SEMAPHORE_LIMIT = 10

SAVE_FILE_PATH = "db/30days"

class DateType:
    INITIAL = "initial"
    DAILY = "daily"

class RANKTYPE:
    ASC = "ASC"
    DESC = "DESC"

REQUEST_ERROR_CODES = {
    '001' : 'No data',
    '200' : 'Wrong parameters',
    '900' : 'Unauthenticated request'
}

COMMON_PARAMS = {
    # CERTI_KEY
    "p_cert_key" : API_KEY,

    # CERTI_ID
    "p_cert_id" : ID,

    # return type (json, xml)
    "p_returntype" : "json", 
}

all_params = {
    **COMMON_PARAMS,

    # date (YYYY-MM-DD)
    "p_startday" : "", 
    "p_endday" : "",

    # region code (default:All)
    # "p_countrycode" : "", 

    # category code
    "p_itemcategorycode" : "",

    # product code
    "p_itemcode" : "",

    # variety code
    "p_kindcode" : "",

    # rank code
    "p_productrankcode" : "",

    # unit(kg) (default:N)
    # "p_convert_kg_yn" : ""
}