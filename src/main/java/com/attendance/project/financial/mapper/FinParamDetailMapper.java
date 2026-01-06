package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinParamDetail;

import java.util.List;

/**
 * 财务参数细则Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public interface FinParamDetailMapper 
{
    /**
     * 查询财务参数细则
     * 
     * @param id 财务参数细则主键
     * @return 财务参数细则
     */
    public FinParamDetail selectFinParamDetailById(Long id);

    public String selectFinParamDetailByCodeKey(FinParamDetail detail);

    /**
     * 查询财务参数细则列表
     * 
     * @param finParamDetail 财务参数细则
     * @return 财务参数细则集合
     */
    public List<FinParamDetail> selectFinParamDetailList(FinParamDetail finParamDetail);

    /**
     * 新增财务参数细则
     * 
     * @param finParamDetail 财务参数细则
     * @return 结果
     */
    public int insertFinParamDetail(FinParamDetail finParamDetail);

    /**
     * 修改财务参数细则
     * 
     * @param finParamDetail 财务参数细则
     * @return 结果
     */
    public int updateFinParamDetail(FinParamDetail finParamDetail);

    /**
     * 删除财务参数细则
     * 
     * @param id 财务参数细则主键
     * @return 结果
     */
    public int deleteFinParamDetailById(Long id);

    /**
     * 批量删除财务参数细则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinParamDetailByIds(String[] ids);
}
