package com.attendance.project.atdform.atdstatisticform.service;

import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;

import java.util.List;
import java.util.Map;

/**
 * 排班计划Service接口
 * 
 * @author ruoyi
 * @date 2025-01-10
 */
public interface IAtdPlanService 
{
    /**
     * 查询排班计划
     * 
     * @param id 排班计划主键
     * @return 排班计划
     */
    public AtdPlan selectAtdPlanById(Long id);

    /**
     * 查询排班计划列表
     * 
     * @param atdPlan 排班计划
     * @return 排班计划集合
     */
    public List<AtdPlan> selectAtdPlanList(AtdPlan atdPlan);

    public List<Map<String, Object>> selectAtdPlanMapList(AtdPlan atdPlan);

    /**
     * 新增排班计划
     * 
     * @param atdPlan 排班计划
     * @return 结果
     */
    public int insertAtdPlan(AtdPlan atdPlan);

    /**
     * 修改排班计划
     * 
     * @param atdPlan 排班计划
     * @return 结果
     */
    public int updateAtdPlan(AtdPlan atdPlan);

    /**
     * 批量删除排班计划
     * 
     * @param ids 需要删除的排班计划主键集合
     * @return 结果
     */
    public int deleteAtdPlanByIds(String ids);

    /**
     * 删除排班计划信息
     * 
     * @param id 排班计划主键
     * @return 结果
     */
    public int deleteAtdPlanById(Long id);

    public int deleteAtdPlanByUserDates(AtdPlan atdPlan);

    public int batchInsertAtdPlan(List<AtdPlan> list);

    public int selectAtdPlanCount(AtdPlan atdPlan);

}
