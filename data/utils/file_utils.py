import json
import os

def save_initial_data_to_json(data, save_path):
    try:
        os.makedirs(os.path.dirname(save_path), exist_ok=True)

        with open(save_path, "w", encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=4)

        print(f"Successfully saved to: {save_path}")
        return True
    
    except Exception as e:
        print(f"[{e}] Save failed to {save_path}")
        return False
    
def load_json(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        return json.load(f)