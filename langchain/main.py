from fastapi import FastAPI, WebSocket, Request
import uvicorn
from service.service import Service
from dotenv import load_dotenv

# 加载环境变量
load_dotenv()

app = FastAPI()

service = Service()


@app.websocket("/ws")
async def spring(websocket: WebSocket):
    await websocket.accept()
    await service.ask(websocket)


@app.websocket("/get")
async def client(websocket: WebSocket):
    await websocket.accept()
    await service.get(websocket)


@app.post("/get_info")
async def get_info():
    theme = service.get_theme()
    intro = await service.get_intro()
    ques = await service.get_ques("easy")
    return {"theme": theme, "introduction": intro, "question": ques}


@app.post("/get_score")
async def get_score(request: Request):
    data = await request.json()
    ques = data.get("qusetion")
    answer = data.get("answer")
    dict = await service.get_score(ques, answer)
    score = dict["score"]
    reason = dict["reason"]
    return {"score": score, "reason": reason}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080)
