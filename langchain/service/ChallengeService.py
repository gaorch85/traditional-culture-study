import json
import asyncio
import requests
from fastapi import WebSocketDisconnect
from function.DailyChallenge import challenge_generator


class ChallengeService:
    def __init__(self):
        """Init the challenge generator."""
        self.challenge = challenge_generator()

    async def get_theme(self):
        self.theme = asyncio.run(self.challenge.get_theme())

    async def get_intro(self):
        intro = asyncio.run(self.challenge.get_intro(self.theme))

    async def get_ques(self):
        ques = asyncio.run(self.challenge.get_ques("esay"))

    async def get_score(self):
        
