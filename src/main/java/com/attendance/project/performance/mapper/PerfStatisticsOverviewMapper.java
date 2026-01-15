package com.attendance.project.performance.mapper;

import com.attendance.project.performance.domain.PerfStatisticsOverview;

import java.util.List;

/**
 * 绩效统计Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface PerfStatisticsOverviewMapper
{

    /**
     * 查询绩效统计主列表
     * 
     * @param perfStatisticsOverview 绩效统计主
     * @return 绩效统计主集合
     */
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewList(PerfStatisticsOverview perfStatisticsOverview);

    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAll(PerfStatisticsOverview perfStatisticsOverview);

    public PerfStatisticsOverview selectPerfStatisticsOverviewByOverviewId(Long overviewId);
}
