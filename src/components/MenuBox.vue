<template>
  <div class="menu-box">
    <div class="icon-box">
      <div class="icon-column1">
        <!-- <div class="zhedie" @click="toggleSidebar" v-if="!isHidden">
          <ArrowLeft />
        </div> -->
        <!-- <div class="zhedie" @click="toggleSidebar" v-if="isHidden">
          <ArrowRight /> -->
        <el-button type="primary" @click="createNewChat">添加会话</el-button>
        <!-- </div> -->
      </div>
      <!-- <div class="icon-column2">
        <div class="xinduihua" @click="createNewChat">
          <Edit />
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
import { ArrowLeft, Edit, ArrowRight } from "@element-plus/icons-vue";
import { useChatStore } from "@/stores/chat"; // 引入 chat store

export default {
  components: {
    ArrowLeft,
    Edit,
    ArrowRight,
  },
  props: {
    isHidden: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    toggleSidebar() {
      this.$emit("toggle-sidebar");
    },
    async createNewChat() {
      const chatStore = useChatStore(); // 获取 chat store
      try {
        await chatStore.createNewChat(); // 调用 createNewChat 方法
      } catch (error) {
        console.error("创建新聊天失败:", error);
      }
    },
  },
};
</script>

<style scoped>
.menu-box {
  background-color: antiquewhite
}

.icon-box {
  min-width: 80px;
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.icon-column1,
.icon-column2 {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;

}

.icon-column2 {
  justify-content: flex-end;
}

.zhedie,
.xinduihua {
  width: 25px;
  height: 25px;
  cursor: pointer;

  border-radius: 50%;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.zhedie:hover,
xinduihua:hover {
  transform: scale(1.1);
}

.zhedie:active {
  transform: scale(1);
}
</style>
