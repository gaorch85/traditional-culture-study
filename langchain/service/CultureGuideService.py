import json
import asyncio
from fastapi import WebSocketDisconnect
from function.CultureGuide import CultureGuide


class CultureGuideService:
    def __init__(self):
        self.chat = None

    async def work(self, preData, websocket, sockets):
        history = []
        for message in preData["lastMessages"]:
            history.append(message["content"])
        backintro = preData["backintro"]
        testdir = "./m3e-base"
        self.chat = CultureGuide(history, testdir)

        while True:
            try:
                text_data = await websocket.receive_text()
                msg = json.loads(text_data)
                question = msg["content"]

                # 处理问题并返回回答
                skt = None
                conversationId = str(msg["conversationId"])
                while skt == None:
                    skt = await sockets.get(conversationId)
                    await asyncio.sleep(0.1)

                reply = ""
                async for chunk in self.chat.get_answer(question, backintro):
                    await skt.send_text(chunk)
                    await asyncio.sleep(0.0001)
                    reply += chunk

                await skt.close()
                await websocket.send_text(reply)
                await sockets.delete(conversationId)

            except WebSocketDisconnect:
                break
