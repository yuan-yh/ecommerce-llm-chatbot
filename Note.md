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