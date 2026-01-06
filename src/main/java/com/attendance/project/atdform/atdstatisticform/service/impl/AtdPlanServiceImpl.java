package com.attendance.project.atdform.atdstatisticform.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.mapper.AtdPlanMapper;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 排班计划Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-10
 */
@Service
public class AtdPlanServiceImpl implements IAtdPlanService
{
    @Autowired
    private AtdPlanMapper atdPlanMapper;

    /**
     * 查询排班计划
     * 
     * @param id 排班计划主键
     * @return 排班计划
     */
    @Override
    public AtdPlan selectAtdPlanById(Long id)
    {
        return atdPlanMapper.selectAtdPlanById(id);
    }

    /**
     * 查询排班计划列表
     * 
     * @param atdPlan 排班计划
     * @return 排班计划
     */
    @Override
    public List<AtdPlan> selectAtdPlanList(AtdPlan atdPlan)
    {
        return atdPlanMapper.selectAtdPlanList(atdPlan);
    }

    @Override
    public List<Map<String, Object>> selectAtdPlanMapList(AtdPlan atdPlan) {
        return atdPlanMapper.selectAtdPlanMapList(atdPlan);
    }

    /**
     * 新增排班计划
     * 
     * @param atdPlan 排班计划
     * @return 结果
     */
    @Override
    public int insertAtdPlan(AtdPlan atdPlan)
    {
        atdPlan.setCreateTime(DateUtils.getNowDate());
        return atdPlanMapper.insertAtdPlan(atdPlan);
    }

    /**
     * 修改排班计划
     * 
     * @param atdPlan 排班计划
     * @return 结果
     */
    @Override
    public int updateAtdPlan(AtdPlan atdPlan)
    {
        return atdPlanMapper.updateAtdPlan(atdPlan);
    }

    /**
     * 批量删除排班计划
     * 
     * @param ids 需要删除的排班计划主键
     * @return 结果
     */
    @Override
    public int deleteAtdPlanByIds(String ids)
    {
        return atdPlanMapper.deleteAtdPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除排班计划信息
     * 
     * @param id 排班计划主键
     * @return 结果
     */
    @Override
    public int deleteAtdPlanById(Long id)
    {
        return atdPlanMapper.deleteAtdPlanById(id);
    }

    @Override
    public int deleteAtdPlanByUserDates(AtdPlan atdPlan) {
        return atdPlanMapper.deleteAtdPlanByUserDates(atdPlan);
    }

    @Override
    public int batchInsertAtdPlan(List<AtdPlan> list) {
        return atdPlanMapper.batchInsertAtdPlan(list);
    }

    @Override
    public int selectAtdPlanCount(AtdPlan atdPlan) {
        return atdPlanMapper.selectAtdPlanCount(atdPlan);
    }
}
