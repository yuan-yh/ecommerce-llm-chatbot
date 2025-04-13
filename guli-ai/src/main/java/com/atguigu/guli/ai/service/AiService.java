package com.atguigu.guli.ai.service;

import com.atguigu.common.core.domain.model.LoginUser;
import com.atguigu.common.utils.SecurityUtils;
import com.atguigu.guli.ai.constant.SystemConstant;
import com.atguigu.system.domain.ChatKnowledge;
import com.atguigu.guli.ai.util.FileUtil;
import com.atguigu.system.mapper.ChatKnowledgeMapper;
import com.atguigu.system.mapper.ChatProjectMapper;
import com.atguigu.system.domain.ChatProject;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Ai操作的service，可以编写接口及实现类
 * 我这里直接写实现类
 */
@Service
public class AiService {

    @Autowired
    private ChatKnowledgeMapper chatKnowledgeMapper;

    @Autowired
    private ChatProjectMapper chatProjectMapper;

    @Autowired
    private QdrantVectorStore openAiVectorStore;
    @Autowired
    private QdrantVectorStore ollamaVectorStore;


    public void upload(ChatKnowledge chatKnowledge, MultipartFile file) {
        // 从文件中把内容取出来：通过工具类
        String content = FileUtil.getContentFromFile(file);

        // save in the local db 本地数据库 (mySQL)
        chatKnowledge.setFileName(file.getOriginalFilename());
        chatKnowledge.setContent(content);
        // 获取登录用户信息。
        LoginUser loginUser = SecurityUtils.getLoginUser();
        chatKnowledge.setUserId(loginUser.getUserId());
        chatKnowledge.setCreateBy(loginUser.getUsername());
        chatKnowledge.setCreateTime(new Date());
        this.chatKnowledgeMapper.insertChatKnowledge(chatKnowledge);

        // 保存知识库到向量数据库：projectId knowledgId content内容
        // 根据projectId查询项目（模型的类型）
        ChatProject chatProject = this.chatProjectMapper.selectChatProjectByProjectId(chatKnowledge.getProjectId());

        // save in the vector db (qdrant)
        if (chatProject.getType().equals(SystemConstant.MODEL_TYPE_OPENAI)) {
            this.openAiVectorStore.add(List.of(new Document(chatKnowledge.getContent(), Map.of("projectId", chatKnowledge.getProjectId().toString(), "knowldgeId", chatKnowledge.getKnowledgeId().toString()))));
        }
        else if (chatProject.getType().equals(SystemConstant.MODEL_TYPE_OLLAMA)) {
            this.ollamaVectorStore.add(List.of(new Document(chatKnowledge.getContent(), Map.of("projectId", chatKnowledge.getProjectId().toString(), "knowldgeId", chatKnowledge.getKnowledgeId().toString()))));
        }
    }

    /**
     * 删除知识库
     */
    public void delete(String projectId, String knowledgeId) {
        // DELETE the file in mysql
        this.chatKnowledgeMapper.deleteChatKnowledgeByKnowledgeId(Long.parseLong(knowledgeId));

        // 根据projectId查询项目（模型的类型）
        ChatProject chatProject = this.chatProjectMapper.selectChatProjectByProjectId(Long.valueOf(projectId));

        // DELETE the file in the vector db (qdrant)
        // Create the filter expression
        String filterExpression = String.format("projectId == '%s' && knowldgeId == '%s'", projectId, knowledgeId);
        if (chatProject.getType().equals(SystemConstant.MODEL_TYPE_OPENAI)) {
            // Get all documents matching the filter
            List<Document> documents = this.openAiVectorStore.similaritySearch(SearchRequest.query("")
                    .withTopK(Integer.MAX_VALUE)
                    .withFilterExpression(filterExpression));

            // Extract document IDs and delete them
            List<String> documentIds = documents.stream()
                    .map(Document::getId)
                    .toList();

            if (!documentIds.isEmpty() && this.openAiVectorStore != null) {
                this.openAiVectorStore.delete(documentIds);
            }
        }
        else if (chatProject.getType().equals(SystemConstant.MODEL_TYPE_OLLAMA)) {
            // Find the single document matching projectId and knowledgeId
            List<Document> documents = this.ollamaVectorStore.similaritySearch(SearchRequest.query("")
                    .withTopK(Integer.MAX_VALUE)
                    .withFilterExpression(filterExpression));

            if (!documents.isEmpty() && this.ollamaVectorStore != null) {
                this.ollamaVectorStore.delete(List.of(documents.get(0).getId()));
            }
        }
    }
}