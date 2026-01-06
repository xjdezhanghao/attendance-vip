package com.attendance.project.financial.mapper;

import com.attendance.project.financial.domain.FinParam;

import java.util.List;

/**
 * 财务参数Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
public interface FinParamMapper 
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
     * 删除财务参数
     * 
     * @param id 财务参数主键
     * @return 结果
     */
    public int deleteFinParamById(Long id);

    /**
     * 批量删除财务参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinParamByIds(String[] ids);
}
