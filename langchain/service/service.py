import asyncio
import json
from .CultureGuideService import CultureGuideService
from utils.hashmap import AsyncDict
from function.DailyChallenge import challenge_generator

sockets = AsyncDict()


class Service:
    def __init__(self):
        self.chat = None
        self.chllenge = challenge_generator()

    async def get(self, websocket):
        conversationId = await websocket.receive_text()
        await sockets.set(conversationId, websocket)
        # 保持连接打开状态
        while True:
            await asyncio.sleep(1)

    async def ask(self, websocket):
        text_data = await websocket.receive_text()
        preData = json.loads(text_data)

        self.chat = CultureGuideService()

        await self.chat.work(preData, websocket, sockets)

    def get_theme(self):
        theme = self.chllenge.get_theme()
        return theme

    def get_intro(self):
        intro = asyncio.run(self.chllenge.get_intro())
        return intro

    async def get_ques(self, level):
        ques = await self.chllenge.get_ques(level)
        return ques

    async def get_score(self, question, answer):
        dict = await self.chllenge.get_score(question, answer)
        return dict
