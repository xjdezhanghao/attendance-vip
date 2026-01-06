package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinKpi;
import com.attendance.project.financial.domain.FinKpiDetail;
import com.attendance.project.financial.mapper.FinKpiDetailMapper;
import com.attendance.project.financial.mapper.FinKpiMapper;
import com.attendance.project.financial.service.IFinKpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 绩效Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
@Service
public class FinKpiServiceImpl implements IFinKpiService
{
    @Autowired
    private FinKpiMapper finKpiMapper;

    @Autowired
    private FinKpiDetailMapper finKpiDetailMapper;

    /**
     * 查询绩效
     * 
     * @param id 绩效主键
     * @return 绩效
     */
    @Override
    public FinKpi selectFinKpiById(Long id)
    {
        return finKpiMapper.selectFinKpiById(id);
    }

    /**
     * 查询绩效列表
     * 
     * @param finKpi 绩效
     * @return 绩效
     */
    @Override
    public List<FinKpi> selectFinKpiList(FinKpi finKpi)
    {
        return finKpiMapper.selectFinKpiList(finKpi);
    }

    /**
     * 新增绩效
     * 
     * @param finKpi 绩效
     * @return 结果
     */
    @Override
    public int insertFinKpi(FinKpi finKpi)
    {
        finKpi.setCreateTime(DateUtils.getNowDate());
        return finKpiMapper.insertFinKpi(finKpi);
    }

    /**
     * 修改绩效
     * 
     * @param finKpi 绩效
     * @return 结果
     */
    @Override
    public int updateFinKpi(FinKpi finKpi)
    {
        return finKpiMapper.updateFinKpi(finKpi);
    }

    /**
     * 批量删除绩效
     * 
     * @param ids 需要删除的绩效主键
     * @return 结果
     */
    @Override
    public int deleteFinKpiByIds(String ids)
    {
        finKpiDetailMapper.deleteFinKpiDetailByPids(Convert.toStrArray(ids));
        return finKpiMapper.deleteFinKpiByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除绩效信息
     * 
     * @param id 绩效主键
     * @return 结果
     */
    @Override
    public int deleteFinKpiById(Long id)
    {
        return finKpiMapper.deleteFinKpiById(id);
    }

    @Override
    public int batchInsertFinKpiDetail(List<FinKpiDetail> details) {
        return finKpiDetailMapper.batchInsertFinKpiDetail(details);
    }

    @Override
    public List<FinKpiDetail> selectFinKpiDetailList(FinKpiDetail finKpiDetail) {
        return finKpiDetailMapper.selectFinKpiDetailList(finKpiDetail);
    }

    @Override
    public int updateFinKpiDetail(FinKpiDetail finKpiDetail) {
        return finKpiDetailMapper.updateFinKpiDetail(finKpiDetail);
    }
}
