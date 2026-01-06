package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinUserParam;

import java.util.List;

/**
 * 用户参数Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface FinUserParamMapper 
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
     * 删除用户参数
     * 
     * @param id 用户参数主键
     * @return 结果
     */
    public int deleteFinUserParamById(Long id);

    /**
     * 批量删除用户参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinUserParamByIds(String[] ids);
}
