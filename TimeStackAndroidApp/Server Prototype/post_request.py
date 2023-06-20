import requests

webhook_url = 'http://localhost:5000/webhook'  # Replace with your server's URL

payload = {
    'message': 'Some action happened',
    'data': 'Additional data related to the action'
}

response = requests.post(webhook_url, json=payload)

print(response.json())
