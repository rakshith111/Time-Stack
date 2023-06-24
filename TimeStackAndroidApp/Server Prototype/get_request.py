import requests

url = ''  # Replace with your server's URL

response = requests.get(url)

if response.status_code == 200:
    data = response.json()
    print(data)
else:
    print('GET request failed with status code:', response.status_code)