package com.attendance.project.performance.service.impl;

import java.util.List;
import com.attendance.common.utils.DateUtils;
import com.attendance.project.performance.domain.PerfIndCategory;
import com.attendance.project.performance.domain.PerfIndItem;
import com.attendance.project.performance.mapper.PerfIndCategoryMapper;
import com.attendance.project.performance.mapper.PerfIndItemMapper;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.mapper.PerfIndProjectMapper;
import com.attendance.project.performance.domain.PerfIndProject;
import com.attendance.project.performance.service.IPerfIndProjectService;
import com.attendance.common.utils.text.Convert;

/**
 * 考核项目主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class PerfIndProjectServiceImpl implements IPerfIndProjectService 
{
    @Autowired
    private PerfIndProjectMapper perfIndProjectMapper;

    @Autowired
    private PerfIndCategoryMapper perfIndCategoryMapper;

    @Autowired
    private PerfIndItemMapper perfIndItemMapper;

    /**
     * 查询考核项目主
     * 
     * @param projectId 考核项目主主键
     * @return 考核项目主
     */
    @Override
    public PerfIndProject selectPerfIndProjectByProjectId(Long projectId)
    {
        return perfIndProjectMapper.selectPerfIndProjectByProjectId(projectId);
    }

    /**
     * 查询考核项目主列表
     * 
     * @param perfIndProject 考核项目主
     * @return 考核项目主
     */
    @Override
    public List<PerfIndProject> selectPerfIndProjectList(PerfIndProject perfIndProject)
    {
        return perfIndProjectMapper.selectPerfIndProjectList(perfIndProject);
    }

    /**
     * 新增考核项目主
     * 
     * @param perfIndProject 考核项目主
     * @return 结果
     */
    @Override
    public int insertPerfIndProject(PerfIndProject perfIndProject)
    {
        perfIndProject.setCreateTime(DateUtils.getNowDate());
        return perfIndProjectMapper.insertPerfIndProject(perfIndProject);
    }

    /**
     * 修改考核项目主
     * 
     * @param perfIndProject 考核项目主
     * @return 结果
     */
    @Override
    public int updatePerfIndProject(PerfIndProject perfIndProject)
    {
        perfIndProject.setUpdateTime(DateUtils.getNowDate());
        return perfIndProjectMapper.updatePerfIndProject(perfIndProject);
    }

    /**
     * 批量删除考核项目主
     * 
     * @param projectIds 需要删除的考核项目主主键
     * @return 结果
     */
    @Override
    public int deletePerfIndProjectByProjectIds(String projectIds)
    {
        return perfIndProjectMapper.deletePerfIndProjectByProjectIds(Convert.toStrArray(projectIds));
    }

    /**
     * 删除考核项目主信息
     * 
     * @param projectId 考核项目主主键
     * @return 结果
     */
    @Override
    public int deletePerfIndProjectByProjectId(Long projectId)
    {
        return perfIndProjectMapper.deletePerfIndProjectByProjectId(projectId);
    }

    @Override
    public void saveCategoriesAndItems(PerfIndProject perfIndProject) {
        List<PerfIndCategory> categories = perfIndProject.getCategories();

        if (categories != null && !categories.isEmpty()) {
            for (PerfIndCategory category : categories) {
                // 设置关联的考核项目ID
                category.setProjectId(perfIndProject.getProjectId());
                // 保存大类
                perfIndCategoryMapper.insertPerfIndCategory(category);

                // 保存小项
                List<PerfIndItem> items = category.getItems();
                if (items != null && !items.isEmpty()) {
                    for (PerfIndItem item : items) {
                        // 设置关联的大类ID
                        item.setCategoryId(category.getCategoryId());
                        item.setProjectId(category.getProjectId());
                        // 保存小项
                        perfIndItemMapper.insertPerfIndItem(item);
                    }
                }
            }
        }
    }

    /**
     * 删除现有大类和小项
     */
    @Override
    public void deleteExistingCategoriesAndItems(Long projectId) {
        // 删除所有关联的小项
        perfIndItemMapper.deletePerfIndItemByProjectId(projectId);

        // 删除所有关联的大类
        perfIndCategoryMapper.deletePerfIndCategoryByProjectId(projectId);
    }

    @Override
    public PerfIndProject selectPerfIndProjectWithCategoriesAndItems(Long projectId) {
        PerfIndProject project = selectPerfIndProjectByProjectId(projectId);
        if (project != null) {
            // 查询关联的分类
            PerfIndCategory categoryParam = new PerfIndCategory();
            categoryParam.setProjectId(projectId);
            List<PerfIndCategory> categories = perfIndCategoryMapper.selectPerfIndCategoryList(categoryParam);

            PerfIndItem itemParam = new PerfIndItem();
            // 为每个分类查询关联的小项
            for (PerfIndCategory category : categories) {
                itemParam.setCategoryId(category.getCategoryId());
                List<PerfIndItem> items = perfIndItemMapper.selectPerfIndItemList(itemParam);
                category.setItems(items);
            }

            project.setCategories(categories);
        }
        return project;
    }
}
