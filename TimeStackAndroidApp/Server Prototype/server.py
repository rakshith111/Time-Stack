from flask import Flask, request, jsonify
import requests
app = Flask(__name__)

@app.route('/webhook', methods=['POST'])
def webhook():
    data = request.get_json()

    # Process the received message
    # ... your logic here ...

    # Prepare the response message
    response_message = "Received: " + data['message']

    # Send the response message
    # ... your code to send the message ...
    data = {
        'message':'success'
    }
    print(response_message)
    return jsonify(data)

@app.route('/data', methods=['GET'])
def get_data():
    # Retrieve data from a source (e.g., database, file, API)
    data = {
        'name': 'John Doe',
        'age': 30,
        'city': 'New York'
    }
    
    # Return the data as a JSON response
    return jsonify(data)

if __name__ == '__main__':
    app.run(host='', port=8000)
