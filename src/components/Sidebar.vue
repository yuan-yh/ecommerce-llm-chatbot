<template>
  <div class="sidebar-box" :class="isHidden.value ? 'sidebar-hidden' : 'sidebar-show'">
    <MenuBox :isHidden="isHidden" @toggle-sidebar="toggleSidebar" />
    <div class="sidebar" v-if="!isHidden">
      <div v-for="(group, date) in groupedChats" :key="date">
        <div class="date-label">{{ date }}</div>
        <div v-for="chat in group" :key="chat.chatId" @click="selectMenu(chat.chatId)"
          :class="{ active: activeMenu === chat.chatId }" class="sidebar-item">
          <template v-if="activeMenu === chat.chatId && chat.editing">
            <input v-model="chat.title" class="edit-input" @blur="stopEditing(chat)"
              @keyup.enter="updateChatTitle(chat)" />
          </template>
          <template v-else>
            {{ chat.title }}
            <div v-if="activeMenu === chat.chatId" class="action-icons">
              <EditPen @click.stop="startEditing(chat)" />
              <Delete @click.stop="deleteChat(chat)" />
            </div>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch, computed, onMounted, onBeforeUnmount } from 'vue';
import { EditPen, Delete } from "@element-plus/icons-vue";
import MenuBox from "./MenuBox.vue";
import { listChats, updateChat, deleteChat as deleteChatApi } from "@/api/chat";
import { useChatStore } from '@/stores/chat';

export default {
  components: {
    EditPen,
    Delete,
    MenuBox,
  },
  setup() {
    const isHidden = ref(false);
    const activeMenu = ref(null);
    const chatStore = useChatStore();
    const chats = ref(chatStore.chatList);
    const projectId = ref(chatStore.projectId);
    const chatId = ref(chatStore.chatId);

    // 监听 chatStore 的 chatList 变化
    watch(() => chatStore.chatList, (newChatList) => {
      chats.value = newChatList;

      activeMenu.value = newChatList[0].chatId
    });

    const groupedChats = computed(() => {
      const groups = {};
      chats.value.forEach((chat) => {
        const dateLabel = getDateLabel(chat.createTime);
        if (!groups[dateLabel]) {
          groups[dateLabel] = [];
        }
        groups[dateLabel].push(chat);
      });

      const sortedGroups = {};
      const todayChats = groups["今天"] || [];
      if (todayChats.length > 0) sortedGroups["今天"] = todayChats;

      Object.keys(groups)
        .filter((date) => date !== "今天")
        .forEach((date) => {
          sortedGroups[date] = groups[date];
        });

      return sortedGroups;
    });

    const toggleSidebar = () => {
      isHidden.value = !isHidden.value;
    };

    const fetchChats = async () => {
      try {
        await chatStore.fetchChatList();
        if (chats.value.length > 0) {
          activeMenu.value = chats.value[0].chatId; // 默认选择第一个会话

          chatStore.fetchMessages(activeMenu.value)

          console.log("activeMenu initialized to:", activeMenu.value); // 调试用
        }
      } catch (error) {
        console.error("获取会话列表失败:", error);
      }
    };

    const selectMenu = async (chatId) => {
      activeMenu.value = chatId;
      chatStore.chatId = chatId;
      await chatStore.fetchMessages(chatId);
    };

    const startEditing = (chat) => {
      activeMenu.value = chat.chatId;
      chat.editing = true;
    };

    const stopEditing = (chat) => {
      chat.editing = false;
      updateChatTitle(chat);
    };

    const updateChatTitle = async (chat) => {
      try {
        await updateChat({ projectId: projectId.value, chatId: chat.chatId, title: chat.title });
        chat.editing = false;
        activeMenu.value = chat.chatId;
      } catch (error) {
        console.error("更新会话标题失败:", error);
      }
    };

    const deleteChat = async (chat) => {
      try {
        await deleteChatApi(projectId.value, chat.chatId);
        chats.value = chats.value.filter((c) => c.chatId !== chat.chatId);
        activeMenu.value = chats.value[0].chatId
      } catch (error) {
        console.error("删除会话失败:", error);
      }
    };

    const getDateLabel = (createTime) => {
      const now = new Date();
      const chatDate = new Date(createTime);
      const diffInDays = Math.floor((now - chatDate) / (1000 * 3600 * 24));

      if (diffInDays === 0) {
        return "今天";
      } else if (diffInDays === 1) {
        return "昨天";
      } else {
        return `${diffInDays}天前`;
      }
    };

    const checkWindowSize = () => {
      isHidden.value = window.innerWidth <= 768;
    };

    onMounted(() => {
      checkWindowSize();
      window.addEventListener('resize', checkWindowSize);
      fetchChats();
    });

    onBeforeUnmount(() => {
      window.removeEventListener('resize', checkWindowSize);
    });

    return {
      isHidden,
      activeMenu,
      groupedChats,
      toggleSidebar,
      selectMenu,
      startEditing,
      stopEditing,
      updateChatTitle,
      deleteChat,
    };
  },
};
</script>



<style scoped>
.date-label {
  font-weight: bold;
  font-size: 14px;
  margin: 10px 0 5px 0;
  padding: 5px 20px;
}

.icon-box {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.icon-column1 {
  flex: 1;
  display: flex;
  align-items: center;
}

.icon-column2 {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.sidebar-box {
  height: 100vh;
  overflow-y: auto;
}

.sidebar-show {
  border-right: 1px solid #dcdcdc;
  background-color: #fffdfd;
}

.sidebar-hide {
  border-right: 0;
  background-color: #fff;
}

.menu-box {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.zhedie,
.xinduihua {
  width: 25px;
  height: 25px;
}

.sidebar {
  padding: 0;
  margin: 20px 0;
  width: 250px;
}

.sidebar-item {
  padding: 10px 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.sidebar-item:hover {
  background-color: #f5f5f5;

}

.active {
  background-color: #d0d0d0;
  color: blue;
}

.edit-input {
  flex: 1;
  margin-right: 10px;
  border: 1px solid #dcdcdc;
  border-radius: 4px;
  padding: 5px;
}

.action-icons {
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.action-icons svg {
  margin-left: 5px;
  cursor: pointer;
  width: 16px;
  /* 根据需要调整宽度 */
  height: 16px;
  /* 根据需要调整高度 */
}

.action-icons svg:hover {
  transform: scale(1.1);
}
</style>
