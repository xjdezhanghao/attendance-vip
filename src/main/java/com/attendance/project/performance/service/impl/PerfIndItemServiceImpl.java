package com.attendance.project.performance.service.impl;

import java.util.List;
import com.attendance.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.mapper.PerfIndItemMapper;
import com.attendance.project.performance.domain.PerfIndItem;
import com.attendance.project.performance.service.IPerfIndItemService;
import com.attendance.common.utils.text.Convert;

/**
 * 考核小项Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class PerfIndItemServiceImpl implements IPerfIndItemService 
{
    @Autowired
    private PerfIndItemMapper perfIndItemMapper;

    /**
     * 查询考核小项
     * 
     * @param itemId 考核小项主键
     * @return 考核小项
     */
    @Override
    public PerfIndItem selectPerfIndItemByItemId(Long itemId)
    {
        return perfIndItemMapper.selectPerfIndItemByItemId(itemId);
    }

    /**
     * 查询考核小项列表
     * 
     * @param perfIndItem 考核小项
     * @return 考核小项
     */
    @Override
    public List<PerfIndItem> selectPerfIndItemList(PerfIndItem perfIndItem)
    {
        return perfIndItemMapper.selectPerfIndItemList(perfIndItem);
    }

    /**
     * 新增考核小项
     * 
     * @param perfIndItem 考核小项
     * @return 结果
     */
    @Override
    public int insertPerfIndItem(PerfIndItem perfIndItem)
    {
        perfIndItem.setCreateTime(DateUtils.getNowDate());
        return perfIndItemMapper.insertPerfIndItem(perfIndItem);
    }

    /**
     * 修改考核小项
     * 
     * @param perfIndItem 考核小项
     * @return 结果
     */
    @Override
    public int updatePerfIndItem(PerfIndItem perfIndItem)
    {
        return perfIndItemMapper.updatePerfIndItem(perfIndItem);
    }

    /**
     * 批量删除考核小项
     * 
     * @param itemIds 需要删除的考核小项主键
     * @return 结果
     */
    @Override
    public int deletePerfIndItemByItemIds(String itemIds)
    {
        return perfIndItemMapper.deletePerfIndItemByItemIds(Convert.toStrArray(itemIds));
    }

    /**
     * 删除考核小项信息
     * 
     * @param itemId 考核小项主键
     * @return 结果
     */
    @Override
    public int deletePerfIndItemByItemId(Long itemId)
    {
        return perfIndItemMapper.deletePerfIndItemByItemId(itemId);
    }
}
