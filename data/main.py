import asyncio

from api import client, config
from utils import json_utils, date_utils

async def main():
    try:
        print("Starting fetch process...")
        await client.run_fetch_process(config.DateType.INITIAL)
        
        print("Creating JSON data...")
        json_utils.create_priont_data(date_utils.today())
        
        print("All processes completed successfully!")

    except Exception as e:
        print(f"Critical error in main: {e}")

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\nProcess interrupted by user.")