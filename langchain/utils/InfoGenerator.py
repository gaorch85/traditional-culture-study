from langchain_core.prompts import PromptTemplate
from utils.modelchoice import get_openai_chat_model, get_zhipu_chat_model


class info_generator:
    def __init__(self):
        self.chat_model = get_zhipu_chat_model()

    def intro_model(self):
        prompt_template_str: str = """

        You are a bot who is knowledgeable about traditional Chinese culture,\n
        the user will give you a theme about Chinese culture,
        and you should give a 200 words introduction about the theme.\n
        Theme:
        {theme}
        Please ensure that, the language of your answer should match the
        language of the question. If the question is in Chinese, respond \n
        in Chinese; if the question is in another language,\n
        use that language for the description.

        """
        self.prompt = PromptTemplate.from_template(prompt_template_str)
        self.update_chain()

    def question_model(self):
        prompt_template_str: str = """

        You are a bot who is knowledgeable about traditional Chinese culture,\n
        the user will give you a theme about Chinese culture,
        and you should give a problem about the theme, which will test the
        user's knowledge. The problem should fit the level of  user\n
        Theme:
        {theme}
        Level:
        {level}
        Please ensure that, the language of your answer should match the
        language of the question. If the question is in Chinese, respond \n
        in Chinese; if the question is in another language,\n
        use that language for the description.
        Ensure your output is in the follwing format:
        'Question Theme: ...
         Content: ...
        '
        """
        self.prompt = PromptTemplate.from_template(prompt_template_str)
        self.update_chain()

    def judge_model(self):
        prompt_template_str: str = """

        You are a bot who is knowledgeable about traditional Chinese culture.\n
        You wiil be given a question about one of the traditional Chinese\n
        culture and the user answers for that question.\n
        Then you should judge user's answer and gvie a score, which range from\n
        0 to 100.Note you should use loose criteria to judge.\n
        Your judge will be shown to user\n
        Note that you should call user 'you'\n
        Question:
        {question}
        Answer:
        {answer}
        Please ensure that, the language of your answer should match the
        language of the question. If the question is in Chinese, respond \n
        in Chinese; if the question is in another language,\n
        use that language for the description.
        Ensure your output is in the follwing format:
        'Score: ...
         Judge: ...
         Right answer: ...
        '
        """
        self.prompt = PromptTemplate.from_template(prompt_template_str)
        self.update_chain()

    def update_chain(self):
        self.conversation_chain = self.prompt | self.chat_model

    async def get_introduction(self, theme):
        self.intro_model()
        async for chunk in self.conversation_chain.astream(input={"theme": theme}):
            yield chunk.content

    async def get_question(self, theme, level):
        """
        Get the question about the given theme.

        The level should be easy, moderateor hard.
        """
        self.question_model()
        self.conversation_chain = self.prompt | self.chat_model
        async for chunk in self.conversation_chain.astream(
            input={"theme": theme, "level": level}
        ):
            yield chunk.content

    async def get_score(self, question, answer):
        self.judge_model()
        self.conversation_chain = self.prompt | self.chat_model
        async for chunk in self.conversation_chain.astream(
            input={"question": question, "answer": answer}
        ):
            yield chunk.content
