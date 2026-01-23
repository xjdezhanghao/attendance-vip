package com.attendance.project.performance.mapper;

import com.attendance.framework.aspectj.lang.annotation.DataScope;
import com.attendance.project.performance.domain.PerfStatisticsOverview;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewList(PerfStatisticsOverview perfStatisticsOverview);

    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAll(PerfStatisticsOverview perfStatisticsOverview);

    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAllDate(PerfStatisticsOverview perfStatisticsOverview);

    public PerfStatisticsOverview selectPerfStatisticsOverviewByOverviewId(Long overviewId);

    public BigDecimal selectCompanyAvgScore(Map<String, Object> params);

    public List<Map<String, Object>> selectTopUsers(Map<String, Object> params);

    public List<Map<String, Object>> selectCompanyTrend(Map<String, Object> params);

    public List<Map<String, Object>> selectTopDeptAverages(Map<String, Object> params);

    public List<Map<String, Object>> selectDeptTrend(Map<String, Object> params);

    public List<Map<String, Object>> selectRadarScores(Map<String, Object> params);
}
