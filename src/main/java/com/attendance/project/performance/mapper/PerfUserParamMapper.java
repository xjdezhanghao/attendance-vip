package com.attendance.project.performance.mapper;

import com.attendance.project.performance.domain.PerfUserParam;

import java.util.List;

/**
 * 用户参数Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public interface PerfUserParamMapper
{
    /**
     * 查询用户参数
     * 
     * @param id 用户参数主键
     * @return 用户参数
     */
    public PerfUserParam selectPerfUserParamById(Long id);

    /**
     * 查询用户参数列表
     * 
     * @param perfUserParam 用户参数
     * @return 用户参数集合
     */
    public List<PerfUserParam> selectPerfUserParamList(PerfUserParam perfUserParam);

    public List<PerfUserParam> selectPerfUserParamListOnly(PerfUserParam perfUserParam);


    /**
     * 新增用户参数
     * 
     * @param perfUserParam 用户参数
     * @return 结果
     */
    public int insertPerfUserParam(PerfUserParam perfUserParam);

    public int batchInsertPerfUserParam(List<PerfUserParam> userParams);

    /**
     * 修改用户参数
     * 
     * @param perfUserParam 用户参数
     * @return 结果
     */
    public int updatePerfUserParam(PerfUserParam perfUserParam);

    /**
     * 删除用户参数
     * 
     * @param id 用户参数主键
     * @return 结果
     */
    public int deletePerfUserParamById(Long id);

    /**
     * 批量删除用户参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerfUserParamByIds(String[] ids);
}
