from datetime import datetime, timedelta

# ==========================================================
# Date generation for API parameters and Data processing
# ==========================================================

def today():
    return datetime.now().strftime("%Y-%m-%d")

def tomorrow():
    return (datetime.now()+timedelta(days=1)).strftime("%Y-%m-%d")

def get_prev_business_date(days=30):
    dates = get_prev_business_dates(days)

    return dates[0] if dates else None

def get_prev_business_dates(days):
    SATURDAY = 5
    cur_date = datetime.today()
    dates = []

    if cur_date.weekday()<SATURDAY:
        dates.append(cur_date.strftime("%Y-%m-%d"))
    
    while len(dates)<days:
        cur_date -= timedelta(days=1)

        if cur_date.weekday()<SATURDAY:
            dates.append(cur_date.strftime("%Y-%m-%d"))
    
    return sorted(dates)