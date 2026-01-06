package com.attendance.project.performance.mapper;

import java.util.List;
import com.attendance.project.performance.domain.PerfIndCategory;

/**
 * 考核大类Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface PerfIndCategoryMapper 
{
    /**
     * 查询考核大类
     * 
     * @param categoryId 考核大类主键
     * @return 考核大类
     */
    public PerfIndCategory selectPerfIndCategoryByCategoryId(Long categoryId);

    /**
     * 查询考核大类列表
     * 
     * @param perfIndCategory 考核大类
     * @return 考核大类集合
     */
    public List<PerfIndCategory> selectPerfIndCategoryList(PerfIndCategory perfIndCategory);

    /**
     * 新增考核大类
     * 
     * @param perfIndCategory 考核大类
     * @return 结果
     */
    public int insertPerfIndCategory(PerfIndCategory perfIndCategory);

    /**
     * 修改考核大类
     * 
     * @param perfIndCategory 考核大类
     * @return 结果
     */
    public int updatePerfIndCategory(PerfIndCategory perfIndCategory);

    /**
     * 删除考核大类
     * 
     * @param categoryId 考核大类主键
     * @return 结果
     */
    public int deletePerfIndCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除考核大类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerfIndCategoryByCategoryIds(String[] categoryIds);

    public int deletePerfIndCategoryByProjectId(Long projectId);
}
