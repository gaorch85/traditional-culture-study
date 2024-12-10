import os
from collections import deque
from langchain.agents import create_react_agent, AgentExecutor, AgentOutputParser
from langchain.memory import ConversationBufferMemory
from langchain_community.document_loaders import PyPDFLoader, Docx2txtLoader, TextLoader
from langchain_community.vectorstores import FAISS
from langchain_core.agents import AgentAction, AgentFinish
from langchain_core.messages import HumanMessage, AIMessage
from langchain_core.prompts import PromptTemplate
from langchain_core.tools import Tool
from typing import Union
import re
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_text_splitters import RecursiveCharacterTextSplitter

from utils.modelchoice import get_openai_chat_model


class Retrieval:
    def __init__(self, embedding_model_dir: str):
        print("initial retrieval")
        self.vector_store = self.init_vector_store(embedding_model_dir)
        self.retriever = self.vector_store.as_retriever(
            search_type="similarity", search_kwargs={"k": 3}
        )
        self.last_docs = None
        print("finish init retrieval")

    def init_vector_store(self, embedding_model_dir):
        print("loading vector_store")
        embeddings = HuggingFaceEmbeddings(
            model_name=embedding_model_dir, model_kwargs={"device": "cpu"}
        )
        print("created embedding")
        vectorstore = FAISS.load_local(
            "/home/vivy/class_hw/se/project/langchain/function/vectorstore",
            embeddings,
            allow_dangerous_deserialization=True,
        )
        print("finish loading vector_store")
        return vectorstore

    def get_similar_docs(self, target_text: str):
        references = self.retriever.invoke(target_text)
        self.format_references(references)
        return references

    def format_references(self, references):
        self.last_docs = "\n## 参考资料:\n"
        for doc in references:
            source = doc.metadata["source"].split("_", 1)[-1]
            content = doc.page_content
            self.last_docs += f" - **{source}:**  \n`{content}`"

    def is_have_references(self):
        return self.last_docs is not None

    def get_references(self):
        ans = self.last_docs
        self.last_docs = None
        return ans


class MyAgentOutputParser(AgentOutputParser):
    def parse(self, llm_output: str) -> Union[AgentAction, AgentFinish]:
        if "AI:" in llm_output:
            return AgentFinish(
                return_values={"output": llm_output.split("AI:")[-1].strip()},
                log=llm_output,
            )

        regex = r"Action: (.*?)[\n]*Action Input: (.*)"
        match = re.search(regex, llm_output, re.DOTALL)
        if match:
            action = match.group(1).strip()
            action_input = match.group(2).strip()
            return AgentAction(tool=action, tool_input=action_input, log=llm_output)

        raise ValueError(f"Could not parse LLM output: `{llm_output}`")


class CultureGuide:
    def __init__(self, history: list[str], embedding_model_dir: str):

        self.chat_model = get_openai_chat_model(0.3)
        self.retrieval = Retrieval(embedding_model_dir)
        self.tools = None
        self.agent_prompt = None
        self.agent_output_parser = MyAgentOutputParser()
        self.agent = None
        self.memory = ConversationBufferMemory(
            memory_key="chat_history", return_messages=True
        )
        self.agent_executor = None

        self.load_memory(history)
        self.init_tools()
        self.init_agent_prompt()
        self.init_agent()
        self.init_agent_executor()

    def init_tools(self):
        tool = Tool(
            name="LocalSearch",
            description="Useful for when you need to answer questions about the local knowledge base.",
            func=self.retrieval.get_similar_docs,
        )
        self.tools = [tool]

    def init_agent_prompt(self):
        prompt_template_str: str = """

        You are a guide for traditional Chinese culture. You can\n
        answer the question about traditional Chinese culture, \n
        and when user ask a question, you can use the following tools\n
        if you think it is useful.\n
        {tools}

        Remember to consider the chat history when formulating your response,\n
        and refer to it if relevant to the current question.\n
        Chat History:
        {chat_history}

        When users ask for any information, you should always prioritize using\n
        tools to get related information unless you think you really don't need\n
        them(like programming).Make sure your answer be Chinese\n

        When user ask you to make a learing guide or route, you should consider\n
        user's knowledge background, which indicates what user have learned.\n

        When you use the tool, you should carefully provide the most relevant\n
        sentence or phrase that closely matches the desired information,\n
        as the accuracy of this match significantly impacts the effectiveness
        of the retrieval. You will only adopt the information you retrieve if\n
        it is truly useful to the results.\n
        To use a tool, please use the following format:\n

        Thought: Do I need to use a tool? Yes\n
        Action: the action to take, should be one of [{tool_names}]\n
        Action Input: the input to the action\n
        Observation: the result of the action\n

        if you do not need to use a tool, you MUST use the format:\n
        Thought: Do I need to use a tool? No\n
        AI: [your response here]\n

        Important: If the user questions or challenges any information from\n
        the chat history, you should thoroughly reconsider your approach.\n
        This may involve using tools again to solve the problem, even if\n
        you've used them before. Always prioritize providing the most accurate
        and up-to-date information.\n
        Begin!

        The follwing input combine the user's question and background.
        input: {input}
        {agent_scratchpad}
        """
        self.agent_prompt = PromptTemplate.from_template(prompt_template_str)

    def init_agent(self):
        self.agent = create_react_agent(
            llm=self.chat_model,
            tools=self.tools,
            prompt=self.agent_prompt,
            output_parser=self.agent_output_parser,
        )

    def load_memory(self, history: list[str]):
        for i in range(0, len(history), 2):
            if i + 1 < len(history):
                human_message = history[i]
                ai_message = history[i + 1]
                self.memory.chat_memory.add_message(HumanMessage(content=human_message))
                self.memory.chat_memory.add_message(AIMessage(content=ai_message))
            else:
                self.memory.chat_memory.add_message(HumanMessage(content=history[i]))

    def init_agent_executor(self):
        self.agent_executor = AgentExecutor.from_agent_and_tools(
            agent=self.agent,
            tools=self.tools,
            memory=self.memory,
            verbose=True,
            max_iterations=5,
            handle_parsing_errors=True,
        )

    async def get_answer(self, question: str, background):
        background = "Here is the background info:" + background
        combined_input = {"input": f"{question} {background}"}
        ai_response_started = False
        window = deque(maxlen=4)
        async for event in self.agent_executor.astream_events(
            input=combined_input,
            version="v2",
        ):
            kind = event["event"]

            if kind == "on_chat_model_stream":
                content = event["data"]["chunk"].content
                if content:
                    if not ai_response_started:
                        for char in content:
                            if ai_response_started:
                                yield char
                            else:
                                window.append(char)
                                if "".join(window) == "AI: ":
                                    ai_response_started = True
                    else:
                        yield content

        # Get references
        if self.retrieval.is_have_references():
            references = self.retrieval.get_references()
            yield references
