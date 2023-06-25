import socketio

sio = socketio.Client()

@sio.event
def connect():
    print('connection established')
    sio.emit('my_message', {'data': 'Hello Server'})  # Send the first message

@sio.event
def my_message(data):
    print('message received with', data)

    sio.emit('my_message', {'data': 'Hello again'})  # Send the second message

@sio.event
def disconnect():
    print('disconnected from server')

sio.connect('http://192.168.0.108:8000')
sio.wait()
