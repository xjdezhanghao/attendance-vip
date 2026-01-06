package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinOvertimeDetail;

import java.util.List;

/**
 * 加班申请详情Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-14
 */
public interface FinOvertimeDetailMapper 
{
    /**
     * 查询加班申请详情
     * 
     * @param id 加班申请详情主键
     * @return 加班申请详情
     */
    public FinOvertimeDetail selectFinOvertimeDetailById(Long id);

    /**
     * 查询加班申请详情列表
     * 
     * @param finOvertimeDetail 加班申请详情
     * @return 加班申请详情集合
     */
    public List<FinOvertimeDetail> selectFinOvertimeDetailList(FinOvertimeDetail finOvertimeDetail);

    public List<FinOvertimeDetail> selectFinOvertimeDetailListSuper(FinOvertimeDetail finOvertimeDetail);

    /**
     * 新增加班申请详情
     * 
     * @param finOvertimeDetail 加班申请详情
     * @return 结果
     */
    public int insertFinOvertimeDetail(FinOvertimeDetail finOvertimeDetail);

    /**
     * 修改加班申请详情
     * 
     * @param finOvertimeDetail 加班申请详情
     * @return 结果
     */
    public int updateFinOvertimeDetail(FinOvertimeDetail finOvertimeDetail);

    public int deleteFinOvertimeDetailById(Long id);

    /**
     * 批量删除加班申请详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinOvertimeDetailByIds(String[] ids);

    public int deleteFinOvertimeDetailByOids(String[] oids);

    public int batchInsertFinOvertimeDetail(List<FinOvertimeDetail> details);
}
