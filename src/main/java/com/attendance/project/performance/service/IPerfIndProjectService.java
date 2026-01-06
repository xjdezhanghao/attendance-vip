package com.attendance.project.performance.service;

import java.util.List;
import com.attendance.project.performance.domain.PerfIndProject;

/**
 * 考核项目主Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface IPerfIndProjectService 
{
    /**
     * 查询考核项目主
     * 
     * @param projectId 考核项目主主键
     * @return 考核项目主
     */
    public PerfIndProject selectPerfIndProjectByProjectId(Long projectId);

    /**
     * 查询考核项目主列表
     * 
     * @param perfIndProject 考核项目主
     * @return 考核项目主集合
     */
    public List<PerfIndProject> selectPerfIndProjectList(PerfIndProject perfIndProject);

    /**
     * 新增考核项目主
     * 
     * @param perfIndProject 考核项目主
     * @return 结果
     */
    public int insertPerfIndProject(PerfIndProject perfIndProject);

    /**
     * 修改考核项目主
     * 
     * @param perfIndProject 考核项目主
     * @return 结果
     */
    public int updatePerfIndProject(PerfIndProject perfIndProject);

    /**
     * 批量删除考核项目主
     * 
     * @param projectIds 需要删除的考核项目主主键集合
     * @return 结果
     */
    public int deletePerfIndProjectByProjectIds(String projectIds);

    /**
     * 删除考核项目主信息
     * 
     * @param projectId 考核项目主主键
     * @return 结果
     */
    public int deletePerfIndProjectByProjectId(Long projectId);

    public void saveCategoriesAndItems(PerfIndProject perfIndProject);

    public void deleteExistingCategoriesAndItems(Long projectId);

    /**
     * 根据项目ID查询考核项目及其关联的分类和小项
     */
    PerfIndProject selectPerfIndProjectWithCategoriesAndItems(Long projectId);


}
