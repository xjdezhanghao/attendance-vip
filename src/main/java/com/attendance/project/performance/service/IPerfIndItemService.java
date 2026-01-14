package com.attendance.project.performance.service;

import java.util.List;
import com.attendance.project.performance.domain.PerfIndItem;

/**
 * 考核小项Service接口
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public interface IPerfIndItemService 
{
    /**
     * 查询考核小项
     * 
     * @param itemId 考核小项主键
     * @return 考核小项
     */
    public PerfIndItem selectPerfIndItemByItemId(Long itemId);

    /**
     * 查询考核小项列表
     * 
     * @param perfIndItem 考核小项
     * @return 考核小项集合
     */
    public List<PerfIndItem> selectPerfIndItemList(PerfIndItem perfIndItem);

    /**
     * 新增考核小项
     * 
     * @param perfIndItem 考核小项
     * @return 结果
     */
    public int insertPerfIndItem(PerfIndItem perfIndItem);

    /**
     * 修改考核小项
     * 
     * @param perfIndItem 考核小项
     * @return 结果
     */
    public int updatePerfIndItem(PerfIndItem perfIndItem);
    public int updatePerfIndItemByCategory(PerfIndItem perfIndItem);

    /**
     * 批量删除考核小项
     * 
     * @param itemIds 需要删除的考核小项主键集合
     * @return 结果
     */
    public int deletePerfIndItemByItemIds(String itemIds);

    /**
     * 删除考核小项信息
     * 
     * @param itemId 考核小项主键
     * @return 结果
     */
    public int deletePerfIndItemByItemId(Long itemId);
}
