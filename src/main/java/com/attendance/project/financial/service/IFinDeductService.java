package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinDeduct;
import com.attendance.project.financial.domain.FinDeductDetail;

import java.util.List;

/**
 * 扣除Service接口
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
public interface IFinDeductService 
{
    /**
     * 查询扣除
     * 
     * @param id 扣除主键
     * @return 扣除
     */
    public FinDeduct selectFinDeductById(Long id);

    /**
     * 查询扣除列表
     * 
     * @param finDeduct 扣除
     * @return 扣除集合
     */
    public List<FinDeduct> selectFinDeductList(FinDeduct finDeduct);

    /**
     * 新增扣除
     * 
     * @param finDeduct 扣除
     * @return 结果
     */
    public int insertFinDeduct(FinDeduct finDeduct);

    /**
     * 修改扣除
     * 
     * @param finDeduct 扣除
     * @return 结果
     */
    public int updateFinDeduct(FinDeduct finDeduct);

    /**
     * 批量删除扣除
     * 
     * @param ids 需要删除的扣除主键集合
     * @return 结果
     */
    public int deleteFinDeductByIds(String ids);

    /**
     * 删除扣除信息
     * 
     * @param id 扣除主键
     * @return 结果
     */
    public int deleteFinDeductById(Long id);

    public int batchInsertFinDeductDetail(List<FinDeductDetail> details);

    public List<FinDeductDetail> selectFinDeductDetailList(FinDeductDetail finDeductDetail);

    public int updateFinDeductDetail(FinDeductDetail finDeductDetail);
}
