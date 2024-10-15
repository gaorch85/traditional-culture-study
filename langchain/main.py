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


@app.get("/get_theme")
def get_theme():
    theme = service.get_theme()
    return {"theme": theme}


@app.get("/get_intro")
def get_intro():
    intro = service.get_intro()
    return {"introduction": intro}


@app.get("/get_ques")
async def get_ques(request: Request):
    data = await request.json()
    level = data.get("level")
    ques = await service.get_ques(level)
    return {"question": ques}


@app.get("/get_score")
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
