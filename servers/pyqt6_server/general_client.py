import websocket
import threading
import socket

def on_message(ws, message):
    print("Received message:", message)

def on_error(ws, error):
    print("Error:", error)

def on_close(ws, close_status_code, close_msg):
    print("Connection closed")

def on_open(ws):
    def send_message():
        while True:
            message = input("Enter a message (or 'exit' to quit): ")
            if message.lower() == "exit":
                ws.close()
                break
            ws.send(message)

    threading.Thread(target=send_message).start()

def get_local_ip()->None:
    '''

    A function that returns the local ip address.
    It makes a connection to google's dns server and returns the local ip address.    

    '''
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        return local_ip
    except:
        return None
    
if __name__ == "__main__":
    websocket.enableTrace(True)
    local_ip=get_local_ip()
    print(f"Local ip: {local_ip}")
    ws = websocket.WebSocketApp(f"ws://{local_ip}:8888/socket",
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close)
    ws.on_open = on_open

    ws.run_forever()
