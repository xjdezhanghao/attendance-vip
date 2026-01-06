package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinKpi;
import com.attendance.project.financial.domain.FinKpiDetail;

import java.util.List;

/**
 * 绩效Service接口
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public interface IFinKpiService 
{
    /**
     * 查询绩效
     * 
     * @param id 绩效主键
     * @return 绩效
     */
    public FinKpi selectFinKpiById(Long id);

    /**
     * 查询绩效列表
     * 
     * @param finKpi 绩效
     * @return 绩效集合
     */
    public List<FinKpi> selectFinKpiList(FinKpi finKpi);

    /**
     * 新增绩效
     * 
     * @param finKpi 绩效
     * @return 结果
     */
    public int insertFinKpi(FinKpi finKpi);

    /**
     * 修改绩效
     * 
     * @param finKpi 绩效
     * @return 结果
     */
    public int updateFinKpi(FinKpi finKpi);

    /**
     * 批量删除绩效
     * 
     * @param ids 需要删除的绩效主键集合
     * @return 结果
     */
    public int deleteFinKpiByIds(String ids);

    /**
     * 删除绩效信息
     * 
     * @param id 绩效主键
     * @return 结果
     */
    public int deleteFinKpiById(Long id);

    public int batchInsertFinKpiDetail(List<FinKpiDetail> details);

    public List<FinKpiDetail> selectFinKpiDetailList(FinKpiDetail finKpiDetail);

    public int updateFinKpiDetail(FinKpiDetail finKpiDetail);
}
