package com.attendance.project.system.line.mapper;

import com.attendance.project.system.line.domain.FlowLine;

import java.util.List;

/**
 * 线性流程Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
public interface FlowLineMapper 
{
    /**
     * 查询线性流程
     * 
     * @param id 线性流程主键
     * @return 线性流程
     */
    public FlowLine selectFlowLineById(Long id);

    /**
     * 查询线性流程列表
     * 
     * @param flowLine 线性流程
     * @return 线性流程集合
     */
    public List<FlowLine> selectFlowLineList(FlowLine flowLine);

    /**
     * 新增线性流程
     * 
     * @param flowLine 线性流程
     * @return 结果
     */
    public int insertFlowLine(FlowLine flowLine);

    /**
     * 修改线性流程
     * 
     * @param flowLine 线性流程
     * @return 结果
     */
    public int updateFlowLine(FlowLine flowLine);

    /**
     * 删除线性流程
     * 
     * @param id 线性流程主键
     * @return 结果
     */
    public int deleteFlowLineById(Long id);

    /**
     * 批量删除线性流程
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFlowLineByIds(String[] ids);
}
