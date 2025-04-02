<template>
  <el-container style="height: 100vh;">
    <el-container>
      <!-- 侧边栏 -->
      <sidebar />

      <!-- 主内容区 -->
      <el-main class="main-content">
          <!-- 顶部导航栏 -->
        <top-nav />

        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import TopNav from './components/TopNav.vue';
import Sidebar from './components/Sidebar.vue';
import { useChatStore } from '@/stores/chat'; // 引入 Chat Store
export default {
  components: {
    TopNav,
    Sidebar,
  },
  setup() {
    const chatStore = useChatStore(); // 使用 Chat Store
    chatStore.initUUID(); // 初始化用户 UUID
  },
};
</script>

<style scoped>
.main-content {
  position: relative;
  padding: 0;
}
</style>
