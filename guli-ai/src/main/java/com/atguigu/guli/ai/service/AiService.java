package com.atguigu.guli.ai.service;

import com.atguigu.guli.ai.domain.ChatKnowledge;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Ai操作的service，可以编写接口及实现类
 * 我这里直接写实现类
 */
@Service
public class AiService {

    @Autowired
//    private VectorStore vectorStore;
    private QdrantVectorStore openAiVectorStore;
    private QdrantVectorStore ollamaVectorStore;

    public void saveKnowledge(ChatKnowledge chatKnowledge) {
        this.ollamaVectorStore.add(List.of(new Document(chatKnowledge.getContent(), Map.of("projectId", chatKnowledge.getProjectId(), "knowldgeId", chatKnowledge.getKnowledgeId()))));
    }
}