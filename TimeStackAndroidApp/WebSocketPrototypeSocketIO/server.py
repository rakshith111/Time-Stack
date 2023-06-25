import eventlet
import socketio

sio = socketio.Server()
app = socketio.WSGIApp(sio)

@sio.event
def connect(sid, environ):
    sio.emit('my_message', {'data': 'Server response 1'}, room=sid)
    print('connect', sid)

@sio.event
def my_message(sid, data):
    print('message', data)
    if data['data'] == 'Hello Server':
        sio.emit('my_message', {'data': 'Server response 2'}, room=sid)

@sio.event
def disconnect(sid):
    print('disconnect', sid)

if __name__ == '__main__':
    eventlet.wsgi.server(eventlet.listen(('192.168.0.108', 8000)), app)
