import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

//创建 MockAdapter 实例
// const mock = new MockAdapter(axios);

// // 模拟会话列表接口
// mock.onGet('/api/ai/list-chat').reply(200, [
//   {
//     chatId: 1,
//     projectId: 0,
//     userId: 1,
//     title: "聊天记录 1",
//     createTime: "2024-10-06T12:00:00Z",
//     editing: false,
//   },
//   {
//     chatId: 2,
//     projectId: 0,
//     userId: 2,
//     title: "聊天记录 2",
//     createTime: "2024-10-05T12:00:00Z",
//     editing: false,
//   },
//   {
//     chatId: 3,
//     projectId: 0,
//     userId: 3,
//     title: "聊天记录 3",
//     createTime: "2024-10-06T08:00:00Z",
//     editing: false,
//   },
// ]);

// // 模拟发送消息接口
// mock.onPost('/api/ai/chat-stream').reply(200, `
// 当然，下面是一个简单的 Java "Hello World" 程序：

// \`\`\`java
// public class HelloWorld {
//     public static void main(String[] args) {
//         System.out.println("Hello World");
//     }
// }
// \`\`\`

// 将以上代码保存到一个名为 HelloWorld.java 的文件中。然后在命令行或终端中使用以下步骤进行编译和运行:

// 1. 编译：javac HelloWorld.java
// 2. 运行：java HelloWorld
// `);
// // 模拟创建新的会话接口
// mock.onPost('/api/ai/create-chat').reply(200, {
//   chatId: 4,
//   title: "新聊天记录",
//   createTime: "2024-10-07T12:00:00Z",
// });

// // 模拟修改会话标题接口
// mock.onPost('/api/ai/update-chat').reply(200, { success: true });

// // 模拟保存消息接口
// mock.onPost('/api/ai/save-msg').reply(200, { success: true });

// // 模拟查询会话中的消息接口
// mock.onGet('/api/ai/list-msg').reply(200, [
//   { messageId: 1, chatId: 1, text: "Hello", sender: "user" },
//   { messageId: 2, chatId: 1, text: "Hi there!", sender: "chatgpt" },
// ]);

// // 模拟删除会话接口
// mock.onGet('/api/ai/delete-chat').reply(200, { success: true });

// // 模拟模型列表接口
// mock.onGet('/api/chat/project').reply(200, {
//   "total": 4,
//   "rows": [
//       {
//           "createBy": "",
//           "createTime": "2024-06-28 14:35:05",
//           "updateBy": "",
//           "updateTime": null,
//           "remark": null,
//           "projectId": 114,
//           "projectName": "小谷同学",
//           "type": "ollama",
//           "model": "qwen2.5:7b"
//       },
//       {
//           "createBy": "",
//           "createTime": "2024-07-01 10:50:25",
//           "updateBy": "",
//           "updateTime": null,
//           "remark": null,
//           "projectId": 117,
//           "projectName": "谷粒商城",
//           "type": "ollama",
//           "model": "qwen2.5:7b"
//       },
//       {
//           "createBy": "",
//           "createTime": "2024-07-01 10:50:50",
//           "updateBy": "",
//           "updateTime": null,
//           "remark": null,
//           "projectId": 118,
//           "projectName": "谷粒随享",
//           "type": "openai",
//           "model": "gpt-3.5-turbo"
//       },
//       {
//           "createBy": "",
//           "createTime": "2024-07-01 16:27:02",
//           "updateBy": "",
//           "updateTime": null,
//           "remark": null,
//           "projectId": 119,
//           "projectName": "在线教育",
//           "type": "openai",
//           "model": "gpt-3.5-turbo"
//       }
//   ],
//   "code": 200,
//   "msg": "查询成功"
// });

// 发送消息到 ChatGPT
export const sendMessage = (message) => {
  return axios.post('/api/ai/chat-stream', message);
};

// 创建新的会话
export const createChat = (chatVo) => {
  return axios.post('/api/ai/create-chat', chatVo);
};

// 修改会话标题
export const updateChat = (chatVo) => {
  return axios.post('/api/ai/update-chat', chatVo);
};

// 保存消息
export const saveMessage = (messageVo) => {
  return axios.post('/api/ai/save-msg', messageVo);
};

// 查询会话列表
export const listChats = (projectId, userId) => {
  return axios.get('/api/ai/list-chat', {
    params: { projectId, userId },
  });
};

// 查询会话中的消息
export const listMessages = (chatId) => {
  return axios.get('/api/ai/list-msg', {
    params: { chatId },
  });
};

// 删除会话
export const deleteChat = (projectId, chatId) => {
  return axios.get('/api/ai/delete-chat', {
    params: { projectId, chatId },
  });
};

// 模型列表
export const fetchProjects = async () => {
  const response = await axios.get('/api/chat/project');
  return response.data; // 假设返回的数据是项目列表
};
