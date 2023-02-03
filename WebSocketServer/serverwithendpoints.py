import asyncio
from autobahn.asyncio.websocket import WebSocketServerProtocol, WebSocketServerFactory


class ServerProtocol(WebSocketServerProtocol):
    data = {}

    async def onMessage(self, payload, isBinary):
        if isBinary:
            print("Binary message received: {0} bytes".format(len(payload)))
        else:
            message = payload.decode('utf8')
            endpoint, payload = message.split(" ", 1)
            print("Text message received: {0}".format(message))
            print("Endpoint: {0}".format(endpoint))
            print("Payload: {0}".format(payload))

            if endpoint == "mydata":
                self.data = eval(payload)
                print("type of data:", type(self.data))
                print("Data received:", self.data)
                self.sendMessage("Data received".encode('utf8'))
            elif endpoint == "senddata":
                print("Sending data:", self.data)
                self.sendMessage(str(self.data).encode('utf8'))
            else:
                print("Unknown endpoint")


if __name__ == '__main__':
    factory = WebSocketServerFactory()
    factory.protocol = ServerProtocol

    loop = asyncio.get_event_loop()
    coro = loop.create_server(factory, '192.168.1.2', 9000)
    server = loop.run_until_complete(coro)

    try:
        loop.run_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.close()
        loop.close()

