package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinOvertime;
import com.attendance.project.financial.domain.FinOvertimeDetail;

import java.util.List;

/**
 * 加班申请Service接口
 * 
 * @author ruoyi
 * @date 2024-11-08
 */
public interface IFinOvertimeService 
{
    /**
     * 查询加班申请
     * 
     * @param id 加班申请主键
     * @return 加班申请
     */
    public FinOvertime selectFinOvertimeById(Long id);

    /**
     * 查询加班申请列表
     * 
     * @param finOvertime 加班申请
     * @return 加班申请集合
     */
    public List<FinOvertime> selectFinOvertimeList(FinOvertime finOvertime);

    /**
     * 新增加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    public int insertFinOvertime(FinOvertime finOvertime);

    /**
     * 修改加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    public int updateFinOvertime(FinOvertime finOvertime);

    /**
     * 批量删除加班申请
     * 
     * @param ids 需要删除的加班申请主键集合
     * @return 结果
     */
    public int deleteFinOvertimeByIds(String ids);

    /**
     * 删除加班申请信息
     * 
     * @param id 加班申请主键
     * @return 结果
     */
    public int deleteFinOvertimeById(Long id);

    public int batchInsertFinOvertimeDetail(List<FinOvertimeDetail> details);

    public List<FinOvertimeDetail> selectFinOvertimeDetailList(FinOvertimeDetail finOvertimeDetail);

    public List<FinOvertimeDetail> selectFinOvertimeDetailListSuper(FinOvertimeDetail finOvertimeDetail);

    public int updateFinOvertimeDetail(FinOvertimeDetail finOvertimeDetail);

}
