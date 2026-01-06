package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinParam;
import com.attendance.project.financial.domain.FinParamDetail;

import java.util.List;

/**
 * 财务参数Service接口
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public interface IFinParamService 
{
    /**
     * 查询财务参数
     * 
     * @param id 财务参数主键
     * @return 财务参数
     */
    public FinParam selectFinParamById(Long id);

    /**
     * 查询财务参数列表
     * 
     * @param finParam 财务参数
     * @return 财务参数集合
     */
    public List<FinParam> selectFinParamList(FinParam finParam);

    /**
     * 新增财务参数
     * 
     * @param finParam 财务参数
     * @return 结果
     */
    public int insertFinParam(FinParam finParam);

    /**
     * 修改财务参数
     * 
     * @param finParam 财务参数
     * @return 结果
     */
    public int updateFinParam(FinParam finParam);

    /**
     * 批量删除财务参数
     * 
     * @param ids 需要删除的财务参数主键集合
     * @return 结果
     */
    public int deleteFinParamByIds(String ids);

    /**
     * 删除财务参数信息
     * 
     * @param id 财务参数主键
     * @return 结果
     */
    public int deleteFinParamById(Long id);

    /**
     * 查询财务参数细则
     *
     * @param id 财务参数细则主键
     * @return 财务参数细则
     */
    public FinParamDetail selectFinParamDetailById(Long id);

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

    public int deleteFinParamDetailByIds(String ids);

    public String selectFinParamDetailByCodeKey(FinParamDetail detail);
}
