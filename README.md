# Time-Stack

## Prototype Goal

### Create a web application that allows users to post messages to a server and retrieve all messages posted to the server.
## Server Side (Windows)
- POST Request to server with a JSON object containing a message
    - Server stores the message in a database
    - Server saves the time the message was received
- GET Request to server to retrieve all messages
    - View all the messages in QtUi

## Client Side (Android)
- POST Request to server with a JSON object containing a message
    - Receive a response from the server ("OK" or "ERROR")
- GET Request to server to retrieve all messages
    - View all the messages in a list

## Server Side (Windows) setup
- pip install -r requirements.txt
- python ServerUi.py for the server UI
- python Server.py for the server

## API methods (Server Side)
- POST /message
    - Request body: JSON object containing a message
    - Response body: "OK" or "ERROR"
- GET /messages
    - Response body: JSON object containing all messages
