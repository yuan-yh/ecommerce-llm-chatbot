import { defineStore } from 'pinia';
import { sendMessage, createChat, listMessages, listChats, saveMessage } from '@/api/chat'; // 引入 listChats API
import Prism from 'prismjs'; // 确保已安装并导入 Prism.js
import { v4 as uuidv4 } from 'uuid'; // 确保已安装并导入uuid库

export const useChatStore = defineStore('chat', {
  state: () => ({
    messages: [], // 当前会话的消息列表
    projectId: 114,
    chatId: 5, // 当前会话 ID
    chatList: [], // 会话列表
    isSending: false, // 消息发送状态
    uuid: '', // 用户唯一标识
  }),
  actions: {
    uuidToNumber(uuid) {
      let number = 0;
      for (let i = 0; i < uuid.length && i < 6; i++) {
        const hexValue = uuid[i];
        let digit = 0;
        if (/[0-9a-fA-F]/.test(hexValue)) {
          digit = parseInt(hexValue, 16);
        }
        number = number * 16 + digit;
      }
      return number % 1000000; // 取模得到 6 位数字
    },
    // 初始化时检查UUID是否存在，如果没有则生成新的UUID
    initUUID() {
      let storedUUID = localStorage.getItem('user_uuid');
      if (!storedUUID) {
        storedUUID = uuidv4(); // 生成新的UUID
        localStorage.setItem('user_uuid', this.uuidToNumber(storedUUID));
      }
      this.uuid = storedUUID;
    },

    // 获取会话列表
    async fetchChatList() {
      try {
        const response = await listChats(this.projectId,this.uuid);
        console.log(response.data);
        
        this.chatList = response.data.map(item => {
          item.chatId = BigInt(item.chatId).toString()
          return item;
        }); // 假设 API 返回会话列表
        
      } catch (error) {
        console.error('获取会话列表失败:', error);
      }
    },

    // 选择会话，切换当前会话
    async selectChat(chatId) {
      this.chatId = chatId;
      await this.fetchMessages(chatId); // 加载选中会话的消息
    },

    // 格式化消息
    formatMessage(message) {
      const codeBlockMatches = [];
      let formattedMessage = message.replace(/```(?:\s*(\w+))?\n([\s\S]*?)```/g, (match, language, codeContent, index) => {
        const lang = (language || 'plaintext').trim();
        codeBlockMatches.push({ codeContent, lang });
        return `__CODE_BLOCK_${codeBlockMatches.length - 1}__`;
      });
      formattedMessage = formattedMessage.replace(/\n/g, '<br>');

      codeBlockMatches.forEach((block, index) => {
        const codeBlockHTML = `
          <div class="code-block">
            <pre><code class="language-${block.lang}">${block.codeContent}</code></pre>
          </div>
        `;
        formattedMessage = formattedMessage.replace(`__CODE_BLOCK_${index}__`, codeBlockHTML);
      });

      console.log(formattedMessage);
      return formattedMessage;
    },

    // 添加消息
    addMessage(message) {
      message.text = this.formatMessage(message.text);
      this.messages.push(message);
    },

    // 发送消息到ChatGPT
    async sendMessageToChatGPT(userMessage) {
      this.addMessage({ sender: 'user', text: userMessage.msg });
      this.addMessage({ sender: 'chatgpt', text: "努力思考中，请稍后" });
     
      this.isSending = true;
    
      try {
        const response = await sendMessage({
          projectId: this.projectId,
          language: 0,
          ...userMessage,
        });
        this.messages.splice(this.messages.length - 1,1);
    
        const chatGptMessage = response.data;
        const formattedMessage = this.formatMessage(chatGptMessage);
        const chatGptReply = { sender: 'chatgpt', text: '', isTyping: true, isThinking: true };
        this.addMessage(chatGptReply);

        saveMessage({
            "chatId": this.chatId,
            "content":chatGptMessage
        })

        const codeRegex = /```(\w+)?\n([\s\S]*?)```/g;
        let lastIndex = 0;
        let match;

        while ((match = codeRegex.exec(formattedMessage)) !== null) {
          const [fullMatch, language, codeContent] = match;
          const nonCodeText = formattedMessage.slice(lastIndex, match.index);
          if (nonCodeText) {
            await this.outputMessagePart(nonCodeText, chatGptReply);
          }

          chatGptReply.text += `<pre><code class="language-${language || 'plaintext'}">`;
          this.messages = [...this.messages];
          await this.outputMessagePart(codeContent, chatGptReply);
          chatGptReply.text += `</code></pre>`;
          this.messages = [...this.messages];

          lastIndex = codeRegex.lastIndex;
        }

        if (lastIndex < formattedMessage.length) {
          const remainingText = formattedMessage.slice(lastIndex);
          await this.outputMessagePart(remainingText, chatGptReply);
        }

        setTimeout(() => {
          Prism.highlightAll();
        }, 0);

        chatGptReply.isThinking = false;
        chatGptReply.isTyping = false;
        this.isSending = false;
        this.messages = [...this.messages];



        return chatGptMessage;
      } catch (error) {
        console.error('发送消息时出错:', error);
        const errorMessage = '抱歉，我现在无法响应。';
        this.addMessage({ sender: 'chatgpt', text: errorMessage, isTyping: false });
        this.isSending = false;
        return errorMessage;
      }
    },

    async outputMessagePart(part, chatGptReply) {
      let buffer = '';
      for (const char of part) {
        buffer += char;
        chatGptReply.text = buffer + '_';
        this.messages = [...this.messages];
        await new Promise(resolve => setTimeout(resolve, 50));
        chatGptReply.text = buffer;
        this.messages = [...this.messages];
      }
    },

    // 创建新会话
    async createNewChat() {
      try {
        const response = await createChat({
          userId:this.uuid,
          projectId: this.projectId,
          title: '新会话' + Math.random().toString(36).substring(2, 7),
        });
        this.messages = [];
        this.chatId = response.data;
        this.chatId = BigInt(response.data).toString()
        console.log(this.chatId);
        
        
        await this.fetchChatList(); // 创建新会话后更新会话列表
      } catch (error) {
        console.error('创建新聊天失败:', error);
      }
    },

    // 获取指定会话的消息
    async fetchMessages(chatId) {
      try {
        const response = await listMessages(chatId);
        this.messages = response.data.map(msg => ({
          ...msg,
          text: this.formatMessage(msg.content),
          sender: msg.type ===0?'user':'chatgpt'
        }));
      } catch (error) {
        console.error('获取消息失败:', error);
      }
    },
  },
});
