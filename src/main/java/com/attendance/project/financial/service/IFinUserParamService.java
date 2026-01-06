package com.attendance.project.financial.service;

import com.attendance.project.financial.domain.FinUserParam;

import java.util.List;

/**
 * 用户参数Service接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface IFinUserParamService 
{
    /**
     * 查询用户参数
     * 
     * @param id 用户参数主键
     * @return 用户参数
     */
    public FinUserParam selectFinUserParamById(Long id);

    /**
     * 查询用户参数列表
     * 
     * @param finUserParam 用户参数
     * @return 用户参数集合
     */
    public List<FinUserParam> selectFinUserParamList(FinUserParam finUserParam);

    /**
     * 新增用户参数
     * 
     * @param finUserParam 用户参数
     * @return 结果
     */
    public int insertFinUserParam(FinUserParam finUserParam);

    public int batchInsertFinUserParam(List<FinUserParam> userParams);

    /**
     * 修改用户参数
     * 
     * @param finUserParam 用户参数
     * @return 结果
     */
    public int updateFinUserParam(FinUserParam finUserParam);

    /**
     * 批量删除用户参数
     * 
     * @param ids 需要删除的用户参数主键集合
     * @return 结果
     */
    public int deleteFinUserParamByIds(String ids);

    /**
     * 删除用户参数信息
     * 
     * @param id 用户参数主键
     * @return 结果
     */
    public int deleteFinUserParamById(Long id);
}
