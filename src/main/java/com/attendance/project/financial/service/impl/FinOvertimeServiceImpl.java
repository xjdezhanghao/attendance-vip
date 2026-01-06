package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.FinOvertime;
import com.attendance.project.financial.domain.FinOvertimeDetail;
import com.attendance.project.financial.mapper.FinOvertimeDetailMapper;
import com.attendance.project.financial.mapper.FinOvertimeMapper;
import com.attendance.project.financial.service.IFinOvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 加班申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-08
 */
@Service
public class FinOvertimeServiceImpl implements IFinOvertimeService
{
    @Autowired
    private FinOvertimeMapper finOvertimeMapper;

    @Autowired
    private FinOvertimeDetailMapper finOvertimeDetailMapper;

    /**
     * 查询加班申请
     * 
     * @param id 加班申请主键
     * @return 加班申请
     */
    @Override
    public FinOvertime selectFinOvertimeById(Long id)
    {
        return finOvertimeMapper.selectFinOvertimeById(id);
    }

    /**
     * 查询加班申请列表
     * 
     * @param finOvertime 加班申请
     * @return 加班申请
     */
    @Override
    public List<FinOvertime> selectFinOvertimeList(FinOvertime finOvertime)
    {
        return finOvertimeMapper.selectFinOvertimeList(finOvertime);
    }

    /**
     * 新增加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    @Override
    public int insertFinOvertime(FinOvertime finOvertime)
    {
        finOvertime.setCreateTime(DateUtils.getNowDate());
        return finOvertimeMapper.insertFinOvertime(finOvertime);
    }

    /**
     * 修改加班申请
     * 
     * @param finOvertime 加班申请
     * @return 结果
     */
    @Override
    public int updateFinOvertime(FinOvertime finOvertime)
    {
        return finOvertimeMapper.updateFinOvertime(finOvertime);
    }

    /**
     * 批量删除加班申请
     * 
     * @param ids 需要删除的加班申请主键
     * @return 结果
     */
    @Override
    public int deleteFinOvertimeByIds(String ids)
    {
        finOvertimeDetailMapper.deleteFinOvertimeDetailByOids(Convert.toStrArray(ids));
        return finOvertimeMapper.deleteFinOvertimeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除加班申请信息
     * 
     * @param id 加班申请主键
     * @return 结果
     */
    @Override
    public int deleteFinOvertimeById(Long id)
    {
        return finOvertimeMapper.deleteFinOvertimeById(id);
    }

    @Override
    public int batchInsertFinOvertimeDetail(List<FinOvertimeDetail> details) {
        return finOvertimeDetailMapper.batchInsertFinOvertimeDetail(details);
    }

    @Override
    public List<FinOvertimeDetail> selectFinOvertimeDetailList(FinOvertimeDetail finOvertimeDetail) {
        return finOvertimeDetailMapper.selectFinOvertimeDetailList(finOvertimeDetail);
    }

    @Override
    public List<FinOvertimeDetail> selectFinOvertimeDetailListSuper(FinOvertimeDetail finOvertimeDetail) {
        return finOvertimeDetailMapper.selectFinOvertimeDetailListSuper(finOvertimeDetail);
    }

    @Override
    public int updateFinOvertimeDetail(FinOvertimeDetail finOvertimeDetail) {
        return finOvertimeDetailMapper.updateFinOvertimeDetail(finOvertimeDetail);
    }
}
