package com.attendance.project.system.line.service;

import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.system.line.domain.BusLine;
import com.attendance.project.system.line.domain.FlowLine;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.user.domain.User;

import java.util.List;

/**
 * 线性流程Service接口
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
public interface IFlowLineService 
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
     * 批量删除线性流程
     * 
     * @param ids 需要删除的线性流程主键集合
     * @return 结果
     */
    public int deleteFlowLineByIds(String ids);

    /**
     * 删除线性流程信息
     * 
     * @param id 线性流程主键
     * @return 结果
     */
    public int deleteFlowLineById(Long id);

    public int insertRecLine(RecLine recLine);

    public int updateRecLine(RecLine recLine);

    public List<RecLine> selectRecLineList(RecLine recLine);

    public AjaxResult operateInfo(User curUser, RecLine recLine);

    public List<BusLine> selectBusLineList(BusLine busLine);

    public List<FlowLine> selectFlowLineListByPostAndBusiness(BusLine busLine, User user);

    public int deleteRecLineByRecId(Long id);
}
