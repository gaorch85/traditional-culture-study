"""DailyChallenge.py, implement the generator for the theme of daily challenge."""

from utils.InfoGenerator import info_generator
import hashlib
import asyncio
from datetime import datetime


class challenge_generator:
    """The generator for the daily challenge."""

    def __init__(self):
        """Set the themes list."""
        self.info = info_generator()
        self.themes = [
            "中华武术" "唐诗",
            "宋词",
            "书法",
            "国画",
            "围棋",
            "京剧",
            "昆曲",
            "中医",
            "风水学",
            "中国传统节日",
            "儒家思想",
            "道教",
            "佛教",
            "唐三彩",
            "古筝",
            "琵琶",
            "太极拳",
            "面塑",
            "剪纸",
            "年画",
            "龙舟",
            "舞狮",
            "舞龙",
            "水墨画",
            "中华传统建筑",
            "元曲",
            "明清小说",
            "书法艺术",
            "汉字",
            "青铜铸造工艺",
            "瓷器",
            "古典家具",
            "茶道",
            "酒文化",
            "中华家庭伦理",
            "中国传统礼仪",
            "四书五经",
            "信天游",
            "陕北说书",
            "杂技",
            "祖宗崇拜",
            "香文化",
            "丝绸文化",
            "陶瓷",
            "漆器",
        ]

    def get_theme(self):
        """Get the theme according to the current time."""
        current_time = datetime.now()
        date_str = current_time.strftime("%Y%m%d")
        hash_object = hashlib.md5()
        hash_object.update(date_str.encode("utf-8"))
        index = int(hash_object.hexdigest(), 16) % len(self.themes)
        self.theme = self.themes[index]
        return self.theme

    def extract_ques(self, question):
        start_index = question.find("Content: ")
        content = question[start_index + len("Content: ") :].strip()
        return content

    def extract_judge(self, judge):
        score_index = judge.find("Score:")
        judge_index = judge.find("Judge:")
        right_answer_index = judge.find("Right answer:")
        score = judge[score_index + len("Score:") : judge_index].strip()
        judge = judge[judge_index + len("Reason:") : right_answer_index].strip()

        dict = {"score": int(score), "reason": judge}
        return dict

    async def get_intro(self):
        """Get the introduction about the current theme."""
        intro = ""
        async for str in self.info.get_introduction(self.theme):
            intro += str
        return intro

    async def get_ques(self, level):
        """Get the problem associated with the current theme."""
        question = ""
        async for str in self.info.get_question(self.theme, level):
            question += str
        question = self.extract_ques(question)
        return question

    async def get_score(self, question, answer):
        """Get the score about the user input."""
        judge = ""
        async for str in self.info.get_score(question, answer):
            judge += str

        return self.extract_judge(judge)


def test():
    challenge = challenge_generator()
    theme = challenge.get_theme()
    print(theme)
    intro = asyncio.run(challenge.get_intro())
    print(intro)
    answer = "sorry, i don't konw"
    problem = asyncio.run(challenge.get_ques("very hard"))
    print(problem)
    dict = asyncio.run(challenge.get_score(problem, answer))
    print(dict["score"])
    print(dict["reason"])


if __name__ == "__main__":
    test()
