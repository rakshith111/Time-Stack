###############################################################################
#
# The MIT License (MIT)
#
# Copyright (c) typedef int GmbH
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#
###############################################################################

import asyncio
import json
import copy
from multiprocessing import Pool

from autobahn.asyncio.websocket import WebSocketServerProtocol, \
    WebSocketServerFactory


class SlowSquareServerProtocol(WebSocketServerProtocol):
   def __init__(self):
         super().__init__()
         self.value = 3
         self.copy=copy.deepcopy(self.value)
         self.onOpen()

   async def onOpen(self):
      
      asyncio.ensure_future(self.valuealt())
      

   async def slowsquare(self, x):
      if x > 1000:
          raise Exception("number too large")
      else:
         await asyncio.sleep(1)

         self.value=self.value+1
         print(self.value,"value in slowsquare")
         return x * x

   async def onMessage(self, payload, isBinary):
      if not isBinary:
         x = json.loads(payload.decode('utf8'))
         try:
            res = await self.slowsquare(x)
            print(self.value,"value in onMessage")
            
         except Exception as e:
            self.sendClose(1000, "Exception raised: {0}".format(e))
         else:
            self.sendMessage(json.dumps(res).encode('utf8'))

   async def valuealt(self):
      print(self.value,"value in valuealt")
      print(self.copy,"copy in valuealt")
      if self.value%4==0:
         print("value is divisible by 4")
         self.sendMessage(("high").encode('utf8'))



if __name__ == '__main__':

   factory = WebSocketServerFactory("ws://192.168.1.2:9000")
   factory.protocol = SlowSquareServerProtocol

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
