package com.attendance.project.financial.mapper;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.financial.domain.FinKpi;

import java.util.List;

/**
 * 绩效Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
public interface FinKpiMapper 
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
    @DataScope(deptAlias = "d", userAlias = "u")
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
     * 删除绩效
     * 
     * @param id 绩效主键
     * @return 结果
     */
    public int deleteFinKpiById(Long id);

    /**
     * 批量删除绩效
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinKpiByIds(String[] ids);
}
