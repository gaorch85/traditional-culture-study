<template>
  <div class="chat-outer-container">
    <div class="chat-container" ref="chatHistoryContainer">
      <div
        v-for="(chat, index) in chatHistory"
        :key="index"
        :class="['chat-item', { 'chat-person': chat.isPerson, 'chat-bot': !chat.isPerson }]"
      >
        <div class="chat-content-wrapper">
          <img v-if="!chat.isPerson" class="chat-icon" src="../assets/ChatGPT.png" alt="bot-icon" />
          <div class="chat-content">
            <MarkdownContainer :markdownContent="chat.content" />
          </div>
        </div>
      </div>
    </div>
    
    <button class="scroll-to-bottom-btn" @click="scrollToBottom" v-show="showScrollButton">
      ↓
    </button>

    <!-- <el-button @click="scrollToBottom" class="icon-button">
      <el-icon><Bottom /></el-icon>
    </el-button> -->
  </div>
</template>

  
  <script>
  import MarkdownContainer from '@/components/MarkdownContainer.vue';
  
  export default {
    data() {
      return {
        showScrollButton: false,
      };
    },

    mounted() {
      this.scrollToBottom();
      this.$refs.chatHistoryContainer.addEventListener('scroll', this.handleScroll);
    },

    beforeUnmount() {
      this.$refs.chatHistoryContainer.removeEventListener('scroll', this.handleScroll);
    },



    

    methods: {
      scrollToBottom() {
        const container = this.$refs.chatHistoryContainer;
        container.scrollTop = container.scrollHeight;
      },

      handleScroll() {
        const container = this.$refs.chatHistoryContainer;
        const scrollBottom = container.scrollHeight - container.scrollTop - container.clientHeight;
        this.showScrollButton = scrollBottom > 100;
      },
    },

    props: {
      chatHistory: {
        type: Array,
        required: true,
      },
    },
    components: {
      MarkdownContainer,
    },
    watch: {
      chatHistory: {
          handler() {
            setTimeout(() => {
              this.scrollToBottom();
            }, 100);
        },
        deep: true
        },
    },

  };
  </script>
  
<style scoped>

.chat-outer-container {
  position: relative;
  height: 100%;
}

.chat-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px;
  height: 100%;
  overflow-y: auto;
}

.chat-item {
  display: flex;
}

.chat-content-wrapper {
  display: flex;
  align-items: flex-start;
  
}

.chat-person {
  display: flex;
  justify-content: flex-end;
  
}

.chat-person .chat-content-wrapper {
  margin-left: auto;
  max-width: 80%;
  display: flex;
  justify-content: flex-end;
}

.chat-bot {
  justify-content: flex-start;
  width: 100%;
}

.chat-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.chat-content {
  background-color: #f8f9fa;
  padding: 10px;
  border-radius: 10px;
  box-sizing: border-box;
  width: 100%;
}

.scroll-to-bottom-btn {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.scroll-to-bottom-btn:hover {
  opacity: 1;
}

</style>
