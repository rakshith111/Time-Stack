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
    print(response_message)
    return jsonify({'success': True})

@app.route('/send_req', methods=['GET'])
def send_payload():
    # Retrieve data from the query parameters or other sources
    event = request.args.get('event')
    payload = request.args.get('payload')

    # Perform any additional processing or actions based on the event and payload
    # ... your logic here ...

    # Send the payload to the webhook endpoint
    response = requests.post('http://192.168.0.104', json={'message': payload})

    return jsonify({'success': True, 'response': response.json()})

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=8000)
