package com.attendance.project.performance.service.impl;

import java.util.*;

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
                        item.setScoreType(category.getScoreType());
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

    @Override
    public void updateCategoriesAndItemsIncrementally(PerfIndProject perfIndProject) {
        Long projectId = perfIndProject.getProjectId();

        // 1. 查询当前数据库中已存在的大类
        PerfIndCategory categoryParam = new PerfIndCategory();
        categoryParam.setProjectId(projectId);
        List<PerfIndCategory> existingCategories = perfIndCategoryMapper.selectPerfIndCategoryList(categoryParam);

        // 2. 创建一个映射，便于查找已存在的大类
        Map<Long, PerfIndCategory> existingCategoryMap = new HashMap<>();
        if (existingCategories != null) {
            for (PerfIndCategory category : existingCategories) {
                existingCategoryMap.put(category.getCategoryId(), category);
            }
        }

        // 3. 处理前端传来的分类列表
        List<PerfIndCategory> newCategories = perfIndProject.getCategories();
        Set<Long> processedCategoryIds = new HashSet<>();

        if (newCategories != null) {
            for (PerfIndCategory newCategory : newCategories) {
                if (newCategory.getCategoryId() != null && existingCategoryMap.containsKey(newCategory.getCategoryId())) {
                    // 更新已存在的大类
                    updateExistingCategory(newCategory);
                    processedCategoryIds.add(newCategory.getCategoryId());
                    // 更新该大类下的小项
                    updateCategoryItemsIncrementally(newCategory);
                } else {
                    // 新增大类
                    newCategory.setProjectId(projectId);
                    perfIndCategoryMapper.insertPerfIndCategory(newCategory);

                    // 新增小项
                    if (newCategory.getItems() != null && !newCategory.getItems().isEmpty()) {
                        for (PerfIndItem newItem : newCategory.getItems()) {
                            newItem.setCategoryId(newCategory.getCategoryId());
                            newItem.setProjectId(projectId);
                            newItem.setScoreType(newCategory.getScoreType());
                            perfIndItemMapper.insertPerfIndItem(newItem);
                        }
                    }
                }
            }
        }

        // 4. 删除未被处理的旧大类（及其小项）
        for (PerfIndCategory existingCategory : existingCategories) {
            if (!processedCategoryIds.contains(existingCategory.getCategoryId())) {
                // 删除该大类下的所有小项
                perfIndItemMapper.deletePerfIndItemByCategoryId(existingCategory.getCategoryId());

                // 删除大类本身
                perfIndCategoryMapper.deletePerfIndCategoryByCategoryId(existingCategory.getCategoryId());
            }
        }
    }

    /**
     * 更新已存在的大类
     */
    private void updateExistingCategory(PerfIndCategory category) {
        perfIndCategoryMapper.updatePerfIndCategory(category);
    }

    /**
     * 增量更新大类下的小项
     */
    private void updateCategoryItemsIncrementally(PerfIndCategory category) {
        Long categoryId = category.getCategoryId();
        Long projectId = category.getProjectId();
        String scoreType = category.getScoreType();
        // 查询当前数据库中已存在的小项
        PerfIndItem itemParam = new PerfIndItem();
        itemParam.setCategoryId(categoryId);
        List<PerfIndItem> existingItems = perfIndItemMapper.selectPerfIndItemList(itemParam);

        // 创建一个映射，便于查找已存在的小项
        Map<Long, PerfIndItem> existingItemMap = new HashMap<>();
        if (existingItems != null) {
            for (PerfIndItem item : existingItems) {
                if (item.getItemId() != null) {
                    existingItemMap.put(item.getItemId(), item);
                }
            }
        }

        // 处理前端传来的小项列表
        List<PerfIndItem> newItems = category.getItems();
        Set<Long> processedItemIds = new HashSet<>();

        if (newItems != null) {
            for (PerfIndItem newItem : newItems) {
                if (newItem.getItemId() != null && existingItemMap.containsKey(newItem.getItemId())) {
                    // 更新已存在的小项
                    newItem.setCategoryId(categoryId);
                    newItem.setProjectId(projectId);
                    newItem.setScoreType(scoreType);
                    perfIndItemMapper.updatePerfIndItem(newItem);
                    processedItemIds.add(newItem.getItemId());
                } else {
                    // 新增小项
                    newItem.setCategoryId(categoryId);
                    newItem.setProjectId(projectId);
                    newItem.setScoreType(scoreType);
                    perfIndItemMapper.insertPerfIndItem(newItem);
                }
            }
        }

        // 删除未被处理的旧小项
        for (PerfIndItem existingItem : existingItems) {
            if (!processedItemIds.contains(existingItem.getItemId())) {
                perfIndItemMapper.deletePerfIndItemByItemId(existingItem.getItemId());
            }
        }
    }

}
