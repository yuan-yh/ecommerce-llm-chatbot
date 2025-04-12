package com.atguigu.system.service.impl;

import java.util.List;
import com.atguigu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atguigu.system.mapper.ChatKnowledgeMapper;
import com.atguigu.system.domain.ChatKnowledge;
import com.atguigu.system.service.IChatKnowledgeService;

/**
 * 知识库管理Service业务层处理
 *
 */
@Service
public class ChatKnowledgeServiceImpl implements IChatKnowledgeService 
{
    @Autowired
    private ChatKnowledgeMapper chatKnowledgeMapper;

//    @Autowired
//    private AiService aiService;

    /**
     * 查询知识库管理
     * 
     * @param knowledgeId 知识库管理主键
     * @return 知识库管理
     */
    @Override
    public ChatKnowledge selectChatKnowledgeByKnowledgeId(Long knowledgeId)
    {
        return chatKnowledgeMapper.selectChatKnowledgeByKnowledgeId(knowledgeId);
    }

    /**
     * 查询知识库管理列表
     * 
     * @param chatKnowledge 知识库管理
     * @return 知识库管理
     */
    @Override
    public List<ChatKnowledge> selectChatKnowledgeList(ChatKnowledge chatKnowledge)
    {
        return chatKnowledgeMapper.selectChatKnowledgeList(chatKnowledge);
    }

    /**
     * 新增知识库管理
     * 
     * @param chatKnowledge 知识库管理
     * @return 结果
     */
    @Override
    public int insertChatKnowledge(ChatKnowledge chatKnowledge)
    {
        chatKnowledge.setCreateTime(DateUtils.getNowDate());
        return chatKnowledgeMapper.insertChatKnowledge(chatKnowledge);
    }

    /**
     * 修改知识库管理
     * 
     * @param chatKnowledge 知识库管理
     * @return 结果
     */
    @Override
    public int updateChatKnowledge(ChatKnowledge chatKnowledge)
    {
        chatKnowledge.setUpdateTime(DateUtils.getNowDate());
        return chatKnowledgeMapper.updateChatKnowledge(chatKnowledge);
    }

    /**
     * 批量删除知识库管理
     * 
     * @param knowledgeIds 需要删除的知识库管理主键
     * @return 结果
     */
    @Override
    public int deleteChatKnowledgeByKnowledgeIds(Long[] knowledgeIds)
    {
        return chatKnowledgeMapper.deleteChatKnowledgeByKnowledgeIds(knowledgeIds);
    }

    /**
     * 删除知识库管理信息
     * 
     * @param knowledgeId 知识库管理主键
     * @return 结果
     */
    @Override
    public int deleteChatKnowledgeByKnowledgeId(Long knowledgeId)
    {
        return chatKnowledgeMapper.deleteChatKnowledgeByKnowledgeId(knowledgeId);
    }

//    @Transactional
//    @Override
//    public void upload(ChatKnowledge chatKnowledge, MultipartFile file) {
//        String content = FileUtil.getContentFromFile(file);
//
//        // save in the local db (mySQL)
//        chatKnowledge.setFileName(file.getOriginalFilename());
//        chatKnowledge.setContent(content);
//        this.chatKnowledgeMapper.insertChatKnowledge(chatKnowledge);
//
//        // save in the vector db (qdrant)
//        this.aiService.saveKnowledge(chatKnowledge);
//    }
}
