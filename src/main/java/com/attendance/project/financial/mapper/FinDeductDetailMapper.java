package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinDeductDetail;

import java.util.List;

/**
 * 扣除详情Mapper接口
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
public interface FinDeductDetailMapper 
{
    /**
     * 查询扣除详情
     * 
     * @param id 扣除详情主键
     * @return 扣除详情
     */
    public FinDeductDetail selectFinDeductDetailById(Long id);

    /**
     * 查询扣除详情列表
     * 
     * @param finDeductDetail 扣除详情
     * @return 扣除详情集合
     */
    public List<FinDeductDetail> selectFinDeductDetailList(FinDeductDetail finDeductDetail);

    /**
     * 新增扣除详情
     * 
     * @param finDeductDetail 扣除详情
     * @return 结果
     */
    public int insertFinDeductDetail(FinDeductDetail finDeductDetail);

    /**
     * 修改扣除详情
     * 
     * @param finDeductDetail 扣除详情
     * @return 结果
     */
    public int updateFinDeductDetail(FinDeductDetail finDeductDetail);

    /**
     * 删除扣除详情
     * 
     * @param id 扣除详情主键
     * @return 结果
     */
    public int deleteFinDeductDetailById(Long id);

    /**
     * 批量删除扣除详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinDeductDetailByIds(String[] ids);

    public int batchInsertFinDeductDetail(List<FinDeductDetail> details);

    public int deleteFinDeductDetailByPids(String[] pids);
}
