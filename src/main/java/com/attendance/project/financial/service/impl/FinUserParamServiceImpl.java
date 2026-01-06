package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinUserParam;
import com.attendance.project.financial.mapper.FinUserParamMapper;
import com.attendance.project.financial.service.IFinUserParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户参数Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
@Service
public class FinUserParamServiceImpl implements IFinUserParamService
{
    @Autowired
    private FinUserParamMapper finUserParamMapper;

    /**
     * 查询用户参数
     * 
     * @param id 用户参数主键
     * @return 用户参数
     */
    @Override
    public FinUserParam selectFinUserParamById(Long id)
    {
        return finUserParamMapper.selectFinUserParamById(id);
    }

    /**
     * 查询用户参数列表
     * 
     * @param finUserParam 用户参数
     * @return 用户参数
     */
    @Override
    public List<FinUserParam> selectFinUserParamList(FinUserParam finUserParam)
    {
        return finUserParamMapper.selectFinUserParamList(finUserParam);
    }

    /**
     * 新增用户参数
     * 
     * @param finUserParam 用户参数
     * @return 结果
     */
    @Override
    public int insertFinUserParam(FinUserParam finUserParam)
    {
        finUserParam.setCreateTime(DateUtils.getNowDate());
        return finUserParamMapper.insertFinUserParam(finUserParam);
    }

    @Override
    public int batchInsertFinUserParam(List<FinUserParam> userParams) {
        return finUserParamMapper.batchInsertFinUserParam(userParams);
    }

    /**
     * 修改用户参数
     * 
     * @param finUserParam 用户参数
     * @return 结果
     */
    @Override
    public int updateFinUserParam(FinUserParam finUserParam)
    {
        return finUserParamMapper.updateFinUserParam(finUserParam);
    }

    /**
     * 批量删除用户参数
     * 
     * @param ids 需要删除的用户参数主键
     * @return 结果
     */
    @Override
    public int deleteFinUserParamByIds(String ids)
    {
        return finUserParamMapper.deleteFinUserParamByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户参数信息
     * 
     * @param id 用户参数主键
     * @return 结果
     */
    @Override
    public int deleteFinUserParamById(Long id)
    {
        return finUserParamMapper.deleteFinUserParamById(id);
    }
}
