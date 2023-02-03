## Alternatively
## use the following command to communicate with the server

## python -m websockets ws://192.168.1.2:9000
### Basicserver example
```

Connected to ws://192.168.1.2:9000.
> 7 # send the number 
< 49 # receive the number
> 4 # send the number 
< 16 # receive the number
>
```
### Server with endpoint

```
Connected to ws://192.168.1.2:9000.
> mydata {"somekey":"someval"}
< Data received
> senddata "something"
#
```
#### The server will fail after requesting the endpoint "senddata" with the following error:
```

Traceback (most recent call last):
  File "C:\Users\AORUS\.conda\envs\timestack\Lib\asyncio\events.py", line 80, in _run 
    self._context.run(self._callback, *self._args)
  File "C:\Users\AORUS\.conda\envs\timestack\Lib\site-packages\autobahn\asyncio\websocket.py", line 105, in process
    self._dataReceived(data)
  File "C:\Users\AORUS\.conda\envs\timestack\Lib\site-packages\autobahn\websocket\protocol.py", line 1242, in _dataReceived
    self.data += data
TypeError: unsupported operand type(s) for +=: 'dict' and 'bytearray'
```