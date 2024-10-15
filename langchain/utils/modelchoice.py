def get_openai_chat_model(temperature=0.7):
    from langchain_openai import ChatOpenAI

    chat_model_openai = ChatOpenAI(
        api_key="sk-UmqnxxRXO3avSFtQjhYobVT2Lpmz7suXQhbySB1Rl9VEUeSg",
        base_url="https://api.chatanywhere.tech",
        model="gpt-4o-mini",
        temperature=temperature,
        max_tokens=None,
        timeout=None,
        max_retries=2,
    )
    return chat_model_openai


def get_qianfan_chat_model():
    # 导包，baidu qianfan
    from langchain_community.chat_models import QianfanChatEndpoint

    # 生成 baiduqianfan chat_model
    chat_model_qianfan = QianfanChatEndpoint(
        temperature=0.8,
        model="ernie-bot-turbo",
        verbose=True,
    )
    return chat_model_qianfan


def get_spark_chat_model():
    from langchain_community.chat_models import ChatSparkLLM

    chat_model_spark = ChatSparkLLM(
        temperature=0.8,
    )
    return chat_model_spark


def get_zhipu_chat_model(temperature=0.7):
    from langchain_community.chat_models import ChatZhipuAI

    chat_model_zhipuai = ChatZhipuAI(
        api_key="897260a0fd332dcaaaa7c00b22d22091.HQtEoSqNdwdnINB4",
        model="glm-4",
        temperature=temperature,
    )
    return chat_model_zhipuai


def get_tongyi_chat_model():
    from langchain_community.chat_models.tongyi import ChatTongyi

    chat_model_tongyiqwen = ChatTongyi(
        temperature=0.8,
    )
    return chat_model_tongyiqwen
