<template>
  <div class="chat-input">
    <div class="input-area"
         contenteditable="true"
         ref="messageInput"
         @input="updateMessage"
         @keyup.enter="sendMessage"
         :placeholder="isSending ? '正在发送...' : '输入消息...'"></div>
    <div class="send-icon" :class="isSending?'disabled':''" @click="sendMessage"  :disabled="isSending">
      <el-icon><Promotion /></el-icon>
    </div>
  </div>
</template>

<script>
import { Promotion } from "@element-plus/icons-vue";
import { useChatStore } from '@/stores/chat'; // 引入 chatStore

export default {
  components: {
    Promotion,
  },
  data() {
    return {
      message: '',
    };
  },
  computed: {
    isSending() {
      return useChatStore().isSending; // 从 store 获取状态
    },
  },
  methods: {
    updateMessage(event) {
      this.message = event.target.innerText;
    },
    sendMessage() {
      if (this.message.trim() && !this.isSending) { // 判断是否正在发送
        this.$emit('send', this.message);
        this.message = '';
        this.$refs.messageInput.innerText = '';
      }
    },
  },
};
</script>


<style scoped>
.chat-input {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 10px;
  border-radius: 24px; /* 整体圆角 */
  border: 1px solid #ddd;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

.input-area {
  flex: 1;
  padding: 10px 40px 10px 16px; /* 右边留出空间给图标 */
  border-radius: 24px;
  background-color: #ffffff;
  font-size: 16px;
  line-height: 1.5;
  min-height: 40px;
  max-height: 120px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-wrap: break-word;
  outline: none; /* 移除输入框默认的边框 */
}

.input-area[contenteditable]:empty:before {
  content: attr(placeholder);
  color: #aaa;
}

.send-icon {
  position: absolute;
  right: 10px;
  cursor: pointer;
  font-size: 24px;
  padding: 8px;
  background-color: #409EFF;
  border-radius: 50%;
  color: #fff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.send-icon:hover {
  background-color: #66b1ff;
  transform: scale(1.1);
}

.send-icon.disabled {
  background-color: #ccc;
}
.send-icon.disabled:hover {
  background-color: #ccc;
  transform: scale(1);
}
.send-icon:active {
  background-color: #3a8ee6;
  transform: scale(1);
}

@media (max-width: 768px) {
  .chat-input {
    padding: 8px;
  }

  .input-area {
    padding: 8px 32px 8px 12px;
    font-size: 14px;
  }

  .send-icon {
    font-size: 20px;
    right: 8px;
  }
}
</style>
