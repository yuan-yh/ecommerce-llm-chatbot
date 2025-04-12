package com.atguigu.system.service.impl;

import java.util.List;
import com.atguigu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atguigu.system.mapper.ChatProjectMapper;
import com.atguigu.guli.ai.domain.ChatProject;
import com.atguigu.system.service.IChatProjectService;

/**
 * 项目配置Service业务层处理
 * 
 * @author lixianfeng
 * @date 2025-04-11
 */
@Service
public class ChatProjectServiceImpl implements IChatProjectService 
{
    @Autowired
    private ChatProjectMapper chatProjectMapper;

    /**
     * 查询项目配置
     * 
     * @param projectId 项目配置主键
     * @return 项目配置
     */
    @Override
    public ChatProject selectChatProjectByProjectId(Long projectId)
    {
        return chatProjectMapper.selectChatProjectByProjectId(projectId);
    }

    /**
     * 查询项目配置列表
     * 
     * @param chatProject 项目配置
     * @return 项目配置
     */
    @Override
    public List<ChatProject> selectChatProjectList(ChatProject chatProject)
    {
        return chatProjectMapper.selectChatProjectList(chatProject);
    }

    /**
     * 新增项目配置
     * 
     * @param chatProject 项目配置
     * @return 结果
     */
    @Override
    public int insertChatProject(ChatProject chatProject)
    {
        chatProject.setCreateTime(DateUtils.getNowDate());
        return chatProjectMapper.insertChatProject(chatProject);
    }

    /**
     * 修改项目配置
     * 
     * @param chatProject 项目配置
     * @return 结果
     */
    @Override
    public int updateChatProject(ChatProject chatProject)
    {
        chatProject.setUpdateTime(DateUtils.getNowDate());
        return chatProjectMapper.updateChatProject(chatProject);
    }

    /**
     * 批量删除项目配置
     * 
     * @param projectIds 需要删除的项目配置主键
     * @return 结果
     */
    @Override
    public int deleteChatProjectByProjectIds(Long[] projectIds)
    {
        return chatProjectMapper.deleteChatProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目配置信息
     * 
     * @param projectId 项目配置主键
     * @return 结果
     */
    @Override
    public int deleteChatProjectByProjectId(Long projectId)
    {
        return chatProjectMapper.deleteChatProjectByProjectId(projectId);
    }
}
