from flask import Flask, render_template
from flask_socketio import SocketIO
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

@app.route('/socket.io/')
def socketio_route():
    # handle the WebSocket connection here
    pass

@socketio.on('connect')
def handle_connect():
    print('Client connected')

@socketio.on('disconnect')
def handle_disconnect():
    print('Client disconnected')

@socketio.on('message')
def handle_message(data):
    print('Received message:', data)
    emit('message', {'response': 'Message received!'})

if __name__ == '__main__':
    socketio.run(app,  host = '192.168.0.108', port = 8000)