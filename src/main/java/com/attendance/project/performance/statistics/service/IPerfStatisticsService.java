package com.attendance.project.performance.statistics.service;

import java.util.List;

import com.attendance.project.performance.statistics.dto.StatisticsDetailQuery;
import com.attendance.project.performance.statistics.dto.StatisticsOverviewQuery;
import com.attendance.project.performance.statistics.vo.StatisticsDetailView;
import com.attendance.project.performance.statistics.vo.StatisticsOverviewVO;

/**
 * 绩效统计Service接口
 */
public interface IPerfStatisticsService {
    List<StatisticsOverviewVO> selectStatisticsOverviewList(StatisticsOverviewQuery query);

    List<StatisticsOverviewVO> selectStatisticsDailyList(StatisticsOverviewQuery query);

    StatisticsDetailView buildDailyDetail(StatisticsDetailQuery query);

    StatisticsDetailView buildRangeDetail(StatisticsDetailQuery query);
}
