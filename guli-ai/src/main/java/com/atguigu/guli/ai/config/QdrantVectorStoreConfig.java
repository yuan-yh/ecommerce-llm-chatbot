package com.atguigu.guli.ai.config;

import com.atguigu.guli.ai.constant.SystemConstant;
import io.qdrant.client.QdrantClient;
import org.springframework.ai.autoconfigure.vectorstore.qdrant.QdrantVectorStoreProperties;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 为了解决：
 * Parameter 0 of method vectorStore in org.springframework.ai.autoconfigure.vectorstore.qdrant.QdrantVectorStoreAutoConfiguration required a single bean, but 2 were found:
 * 	- ollamaEmbeddingModel: defined by method 'ollamaEmbeddingModel' in class path resource [org/springframework/ai/autoconfigure/ollama/OllamaAutoConfiguration.class]
 * 	- openAiEmbeddingModel: defined by method 'openAiEmbeddingModel' in class path resource [org/springframework/ai/autoconfigure/openai/OpenAiAutoConfiguration.class]
 * 	手动配置向量数据库的初始化
 */
@Configuration
public class QdrantVectorStoreConfig {

    @Autowired
    QdrantClient qdrantClient;

    @Autowired
    QdrantVectorStoreProperties properties;

    @Bean
    public QdrantVectorStore openAiVectorStore(OpenAiEmbeddingModel openAiEmbeddingModel){ // 不同的大模型使用的维度是不同的
        return new QdrantVectorStore(qdrantClient, SystemConstant.VECTOR_STORE_OPENAI, openAiEmbeddingModel, properties.isInitializeSchema());
    }

    @Bean
    public QdrantVectorStore ollamaVectorStore(OllamaEmbeddingModel ollamaEmbeddingModel){ // ollama本地模型
        return new QdrantVectorStore(qdrantClient, SystemConstant.VECTOR_STORE_OLLAMA, ollamaEmbeddingModel, properties.isInitializeSchema());
    }
}
