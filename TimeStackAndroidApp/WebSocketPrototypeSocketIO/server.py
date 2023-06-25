import socketio

# create a Socket.IO server
sio = socketio.Server()

# wrap with a WSGI application
app = socketio.WSGIApp(sio)
  # a Flask, Django, etc. application
app = socketio.WSGIApp(sio, app)

static_files = {
    '/': {'filename': 'latency.html', 'content_type': 'text/plain'},
}

sio = socketio.Server()
app = socketio.WSGIApp(sio, static_files=static_files)
@sio.event
def my_event(sid, data):
    pass

@sio.on('my custom event')
def another_event(sid, data):
    pass