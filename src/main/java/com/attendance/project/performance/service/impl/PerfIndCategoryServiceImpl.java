package com.attendance.project.performance.service.impl;

import java.util.List;
import com.attendance.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.mapper.PerfIndCategoryMapper;
import com.attendance.project.performance.domain.PerfIndCategory;
import com.attendance.project.performance.service.IPerfIndCategoryService;
import com.attendance.common.utils.text.Convert;

/**
 * 考核大类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class PerfIndCategoryServiceImpl implements IPerfIndCategoryService 
{
    @Autowired
    private PerfIndCategoryMapper perfIndCategoryMapper;

    /**
     * 查询考核大类
     * 
     * @param categoryId 考核大类主键
     * @return 考核大类
     */
    @Override
    public PerfIndCategory selectPerfIndCategoryByCategoryId(Long categoryId)
    {
        return perfIndCategoryMapper.selectPerfIndCategoryByCategoryId(categoryId);
    }

    /**
     * 查询考核大类列表
     * 
     * @param perfIndCategory 考核大类
     * @return 考核大类
     */
    @Override
    public List<PerfIndCategory> selectPerfIndCategoryList(PerfIndCategory perfIndCategory)
    {
        return perfIndCategoryMapper.selectPerfIndCategoryList(perfIndCategory);
    }

    /**
     * 新增考核大类
     * 
     * @param perfIndCategory 考核大类
     * @return 结果
     */
    @Override
    public int insertPerfIndCategory(PerfIndCategory perfIndCategory)
    {
        perfIndCategory.setCreateTime(DateUtils.getNowDate());
        return perfIndCategoryMapper.insertPerfIndCategory(perfIndCategory);
    }

    /**
     * 修改考核大类
     * 
     * @param perfIndCategory 考核大类
     * @return 结果
     */
    @Override
    public int updatePerfIndCategory(PerfIndCategory perfIndCategory)
    {
        return perfIndCategoryMapper.updatePerfIndCategory(perfIndCategory);
    }

    /**
     * 批量删除考核大类
     * 
     * @param categoryIds 需要删除的考核大类主键
     * @return 结果
     */
    @Override
    public int deletePerfIndCategoryByCategoryIds(String categoryIds)
    {
        return perfIndCategoryMapper.deletePerfIndCategoryByCategoryIds(Convert.toStrArray(categoryIds));
    }

    /**
     * 删除考核大类信息
     * 
     * @param categoryId 考核大类主键
     * @return 结果
     */
    @Override
    public int deletePerfIndCategoryByCategoryId(Long categoryId)
    {
        return perfIndCategoryMapper.deletePerfIndCategoryByCategoryId(categoryId);
    }
}
