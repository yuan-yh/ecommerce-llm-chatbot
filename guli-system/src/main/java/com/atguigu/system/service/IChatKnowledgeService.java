package com.atguigu.system.service;

import java.util.List;
import com.atguigu.system.domain.ChatKnowledge;
import org.springframework.web.multipart.MultipartFile;

/**
 * 知识库管理Service接口
 * 
 * @author lixianfeng
 * @date 2025-04-11
 */
public interface IChatKnowledgeService 
{
    /**
     * 查询知识库管理
     * 
     * @param knowledgeId 知识库管理主键
     * @return 知识库管理
     */
    public ChatKnowledge selectChatKnowledgeByKnowledgeId(Long knowledgeId);

    /**
     * 查询知识库管理列表
     * 
     * @param chatKnowledge 知识库管理
     * @return 知识库管理集合
     */
    public List<ChatKnowledge> selectChatKnowledgeList(ChatKnowledge chatKnowledge);

    /**
     * 新增知识库管理
     * 
     * @param chatKnowledge 知识库管理
     * @return 结果
     */
    public int insertChatKnowledge(ChatKnowledge chatKnowledge);

    /**
     * 修改知识库管理
     * 
     * @param chatKnowledge 知识库管理
     * @return 结果
     */
    public int updateChatKnowledge(ChatKnowledge chatKnowledge);

    /**
     * 批量删除知识库管理
     * 
     * @param knowledgeIds 需要删除的知识库管理主键集合
     * @return 结果
     */
    public int deleteChatKnowledgeByKnowledgeIds(Long[] knowledgeIds);

    /**
     * 删除知识库管理信息
     * 
     * @param knowledgeId 知识库管理主键
     * @return 结果
     */
    public int deleteChatKnowledgeByKnowledgeId(Long knowledgeId);

    void upload(ChatKnowledge chatKnowledge, MultipartFile file);
}
