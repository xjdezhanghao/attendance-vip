package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinParam;
import com.attendance.project.financial.domain.FinParamDetail;
import com.attendance.project.financial.mapper.FinParamDetailMapper;
import com.attendance.project.financial.mapper.FinParamMapper;
import com.attendance.project.financial.service.IFinParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 财务参数Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-21
 */
@Service
public class FinParamServiceImpl implements IFinParamService
{
    @Autowired
    private FinParamMapper finParamMapper;

    @Autowired
    private FinParamDetailMapper finParamDetailMapper;


    /**
     * 查询财务参数
     * 
     * @param id 财务参数主键
     * @return 财务参数
     */
    @Override
    public FinParam selectFinParamById(Long id)
    {
        return finParamMapper.selectFinParamById(id);
    }

    /**
     * 查询财务参数列表
     * 
     * @param finParam 财务参数
     * @return 财务参数
     */
    @Override
    public List<FinParam> selectFinParamList(FinParam finParam)
    {
        return finParamMapper.selectFinParamList(finParam);
    }

    /**
     * 新增财务参数
     * 
     * @param finParam 财务参数
     * @return 结果
     */
    @Override
    public int insertFinParam(FinParam finParam)
    {
        finParam.setCreateTime(DateUtils.getNowDate());
        return finParamMapper.insertFinParam(finParam);
    }

    /**
     * 修改财务参数
     * 
     * @param finParam 财务参数
     * @return 结果
     */
    @Override
    public int updateFinParam(FinParam finParam)
    {
        return finParamMapper.updateFinParam(finParam);
    }

    /**
     * 批量删除财务参数
     * 
     * @param ids 需要删除的财务参数主键
     * @return 结果
     */
    @Override
    public int deleteFinParamByIds(String ids)
    {
        return finParamMapper.deleteFinParamByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除财务参数信息
     * 
     * @param id 财务参数主键
     * @return 结果
     */
    @Override
    public int deleteFinParamById(Long id)
    {
        return finParamMapper.deleteFinParamById(id);
    }

    /**
     * 查询财务参数细则
     *
     * @param id 财务参数细则主键
     * @return 财务参数细则
     */
    @Override
    public FinParamDetail selectFinParamDetailById(Long id)
    {
        return finParamDetailMapper.selectFinParamDetailById(id);
    }

    /**
     * 查询财务参数细则列表
     *
     * @param finParamDetail 财务参数细则
     * @return 财务参数细则
     */
    @Override
    public List<FinParamDetail> selectFinParamDetailList(FinParamDetail finParamDetail)
    {
        return finParamDetailMapper.selectFinParamDetailList(finParamDetail);
    }

    /**
     * 新增财务参数细则
     *
     * @param finParamDetail 财务参数细则
     * @return 结果
     */
    @Override
    public int insertFinParamDetail(FinParamDetail finParamDetail)
    {
        finParamDetail.setCreateTime(DateUtils.getNowDate());
        return finParamDetailMapper.insertFinParamDetail(finParamDetail);
    }

    /**
     * 修改财务参数细则
     *
     * @param finParamDetail 财务参数细则
     * @return 结果
     */
    @Override
    public int updateFinParamDetail(FinParamDetail finParamDetail)
    {
        return finParamDetailMapper.updateFinParamDetail(finParamDetail);
    }

    @Override
    public int deleteFinParamDetailByIds(String ids)
    {
        return finParamDetailMapper.deleteFinParamDetailByIds(Convert.toStrArray(ids));
    }

    @Override
    public String selectFinParamDetailByCodeKey(FinParamDetail detail) {
        return finParamDetailMapper.selectFinParamDetailByCodeKey(detail);
    }
}
