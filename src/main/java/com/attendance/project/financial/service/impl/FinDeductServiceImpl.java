package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinDeduct;
import com.attendance.project.financial.domain.FinDeductDetail;
import com.attendance.project.financial.mapper.FinDeductDetailMapper;
import com.attendance.project.financial.mapper.FinDeductMapper;
import com.attendance.project.financial.service.IFinDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 扣除Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
@Service
public class FinDeductServiceImpl implements IFinDeductService
{
    @Autowired
    private FinDeductMapper finDeductMapper;

    @Autowired
    private FinDeductDetailMapper finDeductDetailMapper;

    /**
     * 查询扣除
     * 
     * @param id 扣除主键
     * @return 扣除
     */
    @Override
    public FinDeduct selectFinDeductById(Long id)
    {
        return finDeductMapper.selectFinDeductById(id);
    }

    /**
     * 查询扣除列表
     * 
     * @param finDeduct 扣除
     * @return 扣除
     */
    @Override
    public List<FinDeduct> selectFinDeductList(FinDeduct finDeduct)
    {
        return finDeductMapper.selectFinDeductList(finDeduct);
    }

    /**
     * 新增扣除
     * 
     * @param finDeduct 扣除
     * @return 结果
     */
    @Override
    public int insertFinDeduct(FinDeduct finDeduct)
    {
        finDeduct.setCreateTime(DateUtils.getNowDate());
        return finDeductMapper.insertFinDeduct(finDeduct);
    }

    /**
     * 修改扣除
     * 
     * @param finDeduct 扣除
     * @return 结果
     */
    @Override
    public int updateFinDeduct(FinDeduct finDeduct)
    {
        return finDeductMapper.updateFinDeduct(finDeduct);
    }

    /**
     * 批量删除扣除
     * 
     * @param ids 需要删除的扣除主键
     * @return 结果
     */
    @Override
    public int deleteFinDeductByIds(String ids)
    {
        finDeductDetailMapper.deleteFinDeductDetailByPids(Convert.toStrArray(ids));
        return finDeductMapper.deleteFinDeductByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除扣除信息
     * 
     * @param id 扣除主键
     * @return 结果
     */
    @Override
    public int deleteFinDeductById(Long id)
    {
        return finDeductMapper.deleteFinDeductById(id);
    }

    @Override
    public int batchInsertFinDeductDetail(List<FinDeductDetail> details) {
        return finDeductDetailMapper.batchInsertFinDeductDetail(details);
    }

    @Override
    public List<FinDeductDetail> selectFinDeductDetailList(FinDeductDetail finDeductDetail) {
        return finDeductDetailMapper.selectFinDeductDetailList(finDeductDetail);
    }

    @Override
    public int updateFinDeductDetail(FinDeductDetail finDeductDetail) {
        return finDeductDetailMapper.updateFinDeductDetail(finDeductDetail);
    }
}
