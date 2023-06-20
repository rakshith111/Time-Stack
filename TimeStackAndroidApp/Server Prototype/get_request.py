import requests

url = 'http://localhost:5000/send_req'  # Replace with your server's URL

response = requests.get(url)

if response.status_code == 200:
    data = response.json()
    # Process the received data
    # ... your logic here ...
else:
    print('GET request failed with status code:', response.status_code)
