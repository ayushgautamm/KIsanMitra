import requests
import json

# Test the API
url = "http://127.0.0.1:8000/predict"
data = {
    "year": 2024,
    "area": 10.0,
    "production": 1000.0,
    "yield_per_ha": 5.0,
    "fertilizer": 50.0,
    "irrigation": 20.0,
    "crop": "Rice",
    "season": "kharif"
}

try:
    response = requests.post(url, json=data)
    print(f"Status Code: {response.status_code}")
    print(f"Response: {response.json()}")
except Exception as e:
    print(f"Error: {e}")













