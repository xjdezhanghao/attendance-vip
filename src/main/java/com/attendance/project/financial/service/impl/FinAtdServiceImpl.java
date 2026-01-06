package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinAtd;
import com.attendance.project.financial.mapper.FinAtdMapper;
import com.attendance.project.financial.service.IFinAtdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考勤Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
@Service
public class FinAtdServiceImpl implements IFinAtdService
{
    @Autowired
    private FinAtdMapper finAtdMapper;


    /**
     * 查询考勤
     * 
     * @param id 考勤主键
     * @return 考勤
     */
    @Override
    public FinAtd selectFinAtdById(Long id)
    {
        return finAtdMapper.selectFinAtdById(id);
    }

    /**
     * 查询考勤列表
     * 
     * @param finAtd 考勤
     * @return 考勤
     */
    @Override
    public List<FinAtd> selectFinAtdList(FinAtd finAtd)
    {
        return finAtdMapper.selectFinAtdList(finAtd);
    }

    /**
     * 新增考勤
     * 
     * @param finAtd 考勤
     * @return 结果
     */
    @Override
    public int insertFinAtd(FinAtd finAtd)
    {
        finAtd.setCreateTime(DateUtils.getNowDate());
        return finAtdMapper.insertFinAtd(finAtd);
    }

    @Override
    public int updateFinAtd(FinAtd finAtd) {
        return finAtdMapper.updateFinAtd(finAtd);
    }

    /**
     * 批量删除考勤
     * 
     * @param ids 需要删除的考勤主键
     * @return 结果
     */
    @Override
    public int deleteFinAtdByIds(String ids)
    {
        return finAtdMapper.deleteFinAtdByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤信息
     * 
     * @param id 考勤主键
     * @return 结果
     */
    @Override
    public int deleteFinAtdById(Long id)
    {
        return finAtdMapper.deleteFinAtdById(id);
    }

}
