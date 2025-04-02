Objective: Large Model Application

In this case, client - Spring AI (Java), LangChain (Python) -> call large models' interfaces -> chat -> render the content on the frontend webpages

Step 1. 大模型
- deploy large model in the local environment using Ollama
- transformer - 大模型行的部署&微調
- HuggingFace - 大模型行的部署&微調

Step 1.2 Client-side
- UI: openWebUI

Step 1.3 向量數據庫
- 因爲本地部署不聯網，所以需要外挂知識庫，本地知識庫 (RAG)

MongoDB
- save the context of each chat to ensure a smooth discussion
- when sending through Ollama API, send both current input from user and the chat history between user and assistant
- https://github.com/ollama/ollama/blob/main/docs/api.md

Setup:
- Model Selected due to VARM limitation: https://ollama.com/library/qwen2.5:0.5b
```ollama run qwen2.5:0.5b```
- OpenWeb UI framework in Docker
```docker run -d -p 3000:8080 --add-host=host.docker.internal:host-gateway -v open-webui:/app/backend/data --name open-webui --restart always ghcr.io/open-webui/open-webui:main```
- Ollama API Document - REST - web JS
`https://github.com/ollama/ollama/blob/main/docs/api.md`
- Postman API Test
```
POST
localhost:11434/api/chat
Body (raw JSON):
{
  "model": "qwen2.5:0.5b",
  "messages": [
    {
      "role": "user",
      "content": "劉德華是誰?"
    },
    {
        "role": "assistant",
        "content": "劉德華，全名刘立新，是中國著名的詩人、詞作家。他的生平和作品詳情在不同版本的书籍中有所描述。1. **原名和出生地：** 刘立新，又稱為劉德華，出生于江苏省南京市。2. **主要成就：** 他以其詩作和詞詠著而聞，作品被多次獲得了國際wide的賞評，如美國《國家文學獎》、世界華語文學最高榮譽的“華爾街詩壇”等。他的歌曲在世界廣泛流传。3. **教育背景：** 他在南京師範大學文學院國語系學士。4. **表演经历：** 刘德華的創作風格深受古典文学和現代主義的影響，他以其獨特的詞句和詩情寫就了许多經典性的歌曲和詩作。"
    },
    {
      "role": "user",
      "content": "他有什麽代表作?"
    }
  ], 
  "stream": false
}
```