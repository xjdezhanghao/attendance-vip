package com.attendance.project.performance.service.impl;

import java.util.List;
import com.attendance.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.mapper.PerfGatherDetailMapper;
import com.attendance.project.performance.domain.PerfGatherDetail;
import com.attendance.project.performance.service.IPerfGatherDetailService;
import com.attendance.common.utils.text.Convert;

/**
 * 绩效小项采集Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class PerfGatherDetailServiceImpl implements IPerfGatherDetailService 
{
    @Autowired
    private PerfGatherDetailMapper perfGatherDetailMapper;

    /**
     * 查询绩效小项采集
     * 
     * @param detailId 绩效小项采集主键
     * @return 绩效小项采集
     */
    @Override
    public PerfGatherDetail selectPerfGatherDetailByDetailId(Long detailId)
    {
        return perfGatherDetailMapper.selectPerfGatherDetailByDetailId(detailId);
    }

    /**
     * 查询绩效小项采集列表
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 绩效小项采集
     */
    @Override
    public List<PerfGatherDetail> selectPerfGatherDetailList(PerfGatherDetail perfGatherDetail)
    {
        return perfGatherDetailMapper.selectPerfGatherDetailList(perfGatherDetail);
    }

    /**
     * 新增绩效小项采集
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 结果
     */
    @Override
    public int insertPerfGatherDetail(PerfGatherDetail perfGatherDetail)
    {
        perfGatherDetail.setCreateTime(DateUtils.getNowDate());
        return perfGatherDetailMapper.insertPerfGatherDetail(perfGatherDetail);
    }

    /**
     * 修改绩效小项采集
     * 
     * @param perfGatherDetail 绩效小项采集
     * @return 结果
     */
    @Override
    public int updatePerfGatherDetail(PerfGatherDetail perfGatherDetail)
    {
        perfGatherDetail.setUpdateTime(DateUtils.getNowDate());
        return perfGatherDetailMapper.updatePerfGatherDetail(perfGatherDetail);
    }

    /**
     * 批量删除绩效小项采集
     * 
     * @param detailIds 需要删除的绩效小项采集主键
     * @return 结果
     */
    @Override
    public int deletePerfGatherDetailByDetailIds(String detailIds)
    {
        return perfGatherDetailMapper.deletePerfGatherDetailByDetailIds(Convert.toStrArray(detailIds));
    }

    /**
     * 删除绩效小项采集信息
     * 
     * @param detailId 绩效小项采集主键
     * @return 结果
     */
    @Override
    public int deletePerfGatherDetailByDetailId(Long detailId)
    {
        return perfGatherDetailMapper.deletePerfGatherDetailByDetailId(detailId);
    }
}
