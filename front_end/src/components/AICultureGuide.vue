<template>
  <div class="ai-culture-guide">
    <!-- 悬浮按钮 -->
    <el-button
      class="floating-btn"
      type="primary"
      :icon="ChatDotRound"
      circle
      @click="toggleDialog"
    ></el-button>

    <!-- 对话抽屉 -->
    <el-drawer
      v-model="dialogVisible"
      title="AI文化向导"
      direction="rtl"
      size="30%"
    >
      <div class="chat-container">
        <div class="chat-messages">
          <ChatHistory :chatHistory="chatHistory" />
        </div>
        <div class="chat-input">
          <el-input
            v-model="inputString"
            placeholder="询问AI文化向导相关问题..."
            clearable
            type="textarea"
            @keydown.enter.prevent="getResponse"
            :autosize="{ minRows: 2, maxRows: 4 }"
          ></el-input>
          <el-button type="primary" round :disabled="!inputString.trim()" @click="getResponse">发送</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ChatDotRound } from '@element-plus/icons-vue'
import ChatHistory from './ChatHistory.vue'
import { ElMessage } from 'element-plus'
import { api_listChatHistory, api_getResponse, api_getUserId } from '@/api/chat'

const dialogVisible = ref(false)
const inputString = ref('')
const aiResponse = ref('')
const chatHistory = ref([])
const userId = ref('0')
const isGPTthinking = ref(false)
const websocketUrl = process.env.VUE_APP_WEBSOCKET_URL + '/get'
const ws = ref(null)

const toggleDialog = () => {
  dialogVisible.value = !dialogVisible.value
}

const getResponse = async () => {
  if (inputString.value.trim()) {
    initWebsocket()
    const response = await api_getResponse({content: inputString.value})
    if(response.data.code === 200) {
      chatHistory.value.push({ content: inputString.value, isPerson: true })
      inputString.value = ''
      aiResponse.value = '向导正在思考您的问题...'
      isGPTthinking.value = true
      chatHistory.value.push({ content: aiResponse.value, isPerson: false })
    } else {
      ElMessage.error('网络异常')
    }
  } else {
    ElMessage.error('不能发送空消息')
  }
}

const initWebsocket = () => {
  if (ws.value) {
    ws.value.close()
  }
  ws.value = new WebSocket(websocketUrl)

  ws.value.onopen = () => {
    console.log('WebSocket connection opened')
    sendWebsocket(userId)
  }

  ws.value.onmessage = (event) => {
    if(isGPTthinking.value) {
      isGPTthinking.value = false
      chatHistory.value[chatHistory.value.length - 1].content = ''
    }
    aiResponse.value += event.data // 追加收到的字符
    chatHistory.value[chatHistory.value.length - 1].content = aiResponse.value
  }

  ws.value.onclose = () => {
    console.log('WebSocket connection closed')
    aiResponse.value = ''
  }

  ws.value.onerror = (error) => {
    console.error('WebSocket error:', error)
  }
}

const sendWebsocket = (data) => {
  if(ws.value && ws.value.readyState === WebSocket.OPEN) {
    ws.value.send(data)
  } else {
    console.log('websocket不存在或未连接，无法发送数据')
  }
}

onMounted(async () => {
  const response = await api_listChatHistory()
  if(response.data.code === 200) {
    chatHistory.value = response.data.data
  } else {
    ElMessage.error('网络异常')
  }
  response = await api_getUserId()
  if(response.data.code === 200) {
    userId.value = response.data.data
  } else {
    ElMessage.error('网络异常')
  }
  
})

// 监听对话框关闭，关闭WebSocket连接
watch(dialogVisible, (newVal) => {
  if (!newVal && ws.value) {
    ws.value.close()
  }
})
</script>

<style scoped>
.ai-culture-guide {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 1000;
}

.floating-btn {
  font-size: 24px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.chat-input {
  display: flex;
  align-items: flex-end;
  padding: 20px;
  border-top: 1px solid #e0e0e0;
}

.chat-input .el-input {
  flex: 1;
  margin-right: 10px;
}

.chat-input .el-button {
  flex-shrink: 0;
}
</style>