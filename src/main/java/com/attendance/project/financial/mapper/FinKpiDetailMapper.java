package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinKpiDetail;

import java.util.List;

/**
 * 绩效详情Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public interface FinKpiDetailMapper 
{
    /**
     * 查询绩效详情
     * 
     * @param id 绩效详情主键
     * @return 绩效详情
     */
    public FinKpiDetail selectFinKpiDetailById(Long id);

    /**
     * 查询绩效详情列表
     * 
     * @param finKpiDetail 绩效详情
     * @return 绩效详情集合
     */
    public List<FinKpiDetail> selectFinKpiDetailList(FinKpiDetail finKpiDetail);

    /**
     * 新增绩效详情
     * 
     * @param finKpiDetail 绩效详情
     * @return 结果
     */
    public int insertFinKpiDetail(FinKpiDetail finKpiDetail);

    /**
     * 修改绩效详情
     * 
     * @param finKpiDetail 绩效详情
     * @return 结果
     */
    public int updateFinKpiDetail(FinKpiDetail finKpiDetail);

    /**
     * 删除绩效详情
     * 
     * @param id 绩效详情主键
     * @return 结果
     */
    public int deleteFinKpiDetailById(Long id);

    /**
     * 批量删除绩效详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinKpiDetailByIds(String[] ids);

    public int batchInsertFinKpiDetail(List<FinKpiDetail> details);

    public int deleteFinKpiDetailByPids(String[] pids);
}
