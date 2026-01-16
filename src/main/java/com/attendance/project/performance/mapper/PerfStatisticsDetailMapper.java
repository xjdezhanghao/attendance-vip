package com.attendance.project.performance.mapper;

import com.attendance.project.performance.domain.PerfStatisticsDetail;
import com.attendance.project.performance.domain.PerfStatisticsDetail;
import com.attendance.project.performance.domain.PerfStatisticsOverview;

import java.util.List;

/**
 * 绩效小项统计Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface PerfStatisticsDetailMapper
{

    /**
     * 查询绩效小项统计列表
     * 
     * @param perfStatisticsDetail 绩效小项统计
     * @return 绩效小项统计集合
     */
    public List<PerfStatisticsDetail> selectPerfStatisticsDetailList(PerfStatisticsDetail perfStatisticsDetail);
    
}
