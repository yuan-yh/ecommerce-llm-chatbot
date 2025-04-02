<template>
  <!-- <div class="message-list"> -->
  <!-- <div v-for="(message, index) in messages" :key="index" class="message-item" :class="message.sender">
      <div class="message-content" v-html="message.text"></div>
      <span v-if="message.isTyping" class="cursor">_</span>
    </div> -->
  <!-- 添加光标 -->
  <ul class="message-list" ref="messaggListRef">
    <li :class="message.sender === 'user' ? 'ask' : 'answer'" v-for="(message, index) in messages" :key="index">
      <div :class="message.sender === 'user' ? 'askIcon' : 'answerIcon'">
        <el-icon v-if="message.sender === 'user'">
          <User />
        </el-icon>
        <el-icon v-else>
          <img v-if="message.text == '努力思考中，请稍后'" src="../assets/loading.gif" alt="" width="70px"
            style="background-color: transparent;">
          <Service v-else />
        </el-icon>
      </div>
      <div :class="message.sender === 'user' ? 'askContent' : 'answerContent'" v-html="message.text">
      </div>
      <span v-if="message.isTyping" class="cursor">_</span>
    </li>
  </ul>
  <!-- </div> -->
</template>

<script>
import { ChatLineRound, Bottom, ChatSquare, User, Link, Position, Service } from "@element-plus/icons-vue"
import { onMounted, watch, ref } from "vue";
export default {
  //   props: {
  //     messages: {
  //       type: Array,
  //       default: () => [],
  //     },
  //   },

};
</script>


<script setup>
const messaggListRef = ref()
const scrollTop = ref()
const props = defineProps(["messages"])

onMounted(() => {
  setInterval(() => {
    scrollTop.value = messaggListRef.value.scrollHeight
  }, 300);
})

watch(scrollTop, () => {
  messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
})


// watch
</script>

<style scoped>
.message-list {
  max-height: 100%;
  overflow-y: auto;
  padding: 10px;
}

.user .message-content {
  background-color: #d1eaff;
}

.cursor {
  display: inline-block;
  animation: blink 1s step-end infinite;
  /* 添加闪烁动画 */
  position: absolute;
  /* 绝对定位光标 */
  bottom: 5px;
  /* 光标相对于消息底部的位置 */
  right: 10px;
  /* 光标相对于消息右侧的位置 */
}

.code-block {
  background-color: #f5f5f5;
  /* 背景颜色 */
  border-radius: 5px;
  /* 圆角边框 */
  padding: 10px;
  /* 内边距 */
  margin: 10px 0;
  /* 外边距 */
  overflow: auto;
  /* 允许滚动 */
}

@keyframes blink {

  0%,
  100% {
    opacity: 1;
  }

  50% {
    opacity: 0;
  }
}

/* 响应式布局：适配不同屏幕 */
@media (max-width: 768px) {
  .message-content {
    max-width: 80%;
    font-size: 14px;
  }
}
</style>

<style lang="less" scoped>
// ul {
//   height: 65%;
//   overflow: auto;
//   padding-left: 0;
// }

li {
  margin-bottom: 20px;
}

li.ask {
  display: flex;
  flex-direction: row-reverse;
  text-align: right;

  .askIcon {
    margin-left: 20px;
    font-size: 30px;
  }

  .askContent {
    border: 1px solid #aaa;
    border-radius: 20px;
    padding: 20px 20px 0 20px;
    background-color: #eee;
    line-height: 20px;
  }
}

li.answer {
  display: flex;
  text-align: left;

  .answerIcon {
    margin-right: 20px;
    font-size: 30px;

    :deep(.el-loading-spinner) {
      left: -5px;
    }
  }

  .answerContent {
    border: 1px solid #aaa;
    border-radius: 20px;
    padding: 20px;
    background-color: #eee;
    line-height: 25px;
  }
}
</style>
