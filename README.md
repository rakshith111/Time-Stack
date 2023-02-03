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
- POST /receive
    - Request body: JSON object containing a key - "message" and a value - the message
    - Response body: "OK" or "ERROR"
- GET /dump
    - Response body: JSON object containing all messages
## Screenshots
![image](https://user-images.githubusercontent.com/36219488/216549961-0db7e0b8-ca1a-4ffc-918a-a5e1e0ba895c.png)
