from function.CultureGuide import CultureGuide
import asyncio


async def ask(question):
    chat = CultureGuide([], "/home/vivy/class_hw/se/simpleGpt/m3e-base")
    async for answer in chat.get_answer(
        question,
        "user have learned Chinese traditional festivals",
    ):
        print(answer, end="")


if __name__ == "__main__":
    question = (
        "introduce yourself, give me a route to learn Chinese traditional cultures"
    )
    asyncio.run(ask(question))
