<template>
  <el-container class="chat-page">
    <el-main>
      <message-list :messages="messages" />
    </el-main>
    <el-footer>
      <chat-input @send="handleSendMessage" />
    </el-footer>
  </el-container>
</template>

<script>
import { ref, watch } from 'vue';
import ChatInput from '@/components/ChatInput.vue';
import MessageList from '@/components/MessageList.vue';
import { useChatStore } from '@/stores/chat';

export default {
  components: {
    ChatInput,
    MessageList,
  },
  setup() {
    const chatStore = useChatStore();
    const messages = ref(chatStore.messages);

    // 监听 chatStore 的 messages 变化
    watch(() => chatStore.messages, (newMessages) => {
      messages.value = newMessages;
    });

    const handleSendMessage = async (message) => {
      const chatId = chatStore.chatId; // 如果需要，可以设置为当前会话 ID
      const language = 0; // 设置语言为中文
      const msg = message;

      const messagePayload = {
        chatId,
        language,
        msg,
      };

      // 发送消息并获取响应
      await chatStore.sendMessageToChatGPT(messagePayload);
    };

    return { messages, handleSendMessage };
  },
};

</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.el-main {
  flex: 1;
  padding: 80px 0 100px 0;
}

.el-footer {
  position: absolute;
  bottom: 20px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}
</style>
