package com.attendance.project.performance.statistics.mapper;

import java.util.List;

import com.attendance.project.performance.statistics.dto.StatisticsDetailQuery;
import com.attendance.project.performance.statistics.dto.StatisticsDetailRow;
import com.attendance.project.performance.statistics.dto.StatisticsOverviewQuery;
import com.attendance.project.performance.statistics.dto.StatisticsProjectSummaryRow;
import com.attendance.project.performance.statistics.vo.StatisticsOverviewVO;
import com.attendance.project.performance.statistics.vo.StatisticsProjectOverviewVO;

/**
 * 绩效统计Mapper
 */
public interface PerfStatisticsMapper {
    List<StatisticsOverviewVO> selectStatisticsOverviewList(StatisticsOverviewQuery query);

    List<StatisticsOverviewVO> selectStatisticsDailyList(StatisticsOverviewQuery query);

    List<StatisticsProjectOverviewVO> selectDailyProjectOverviewList(StatisticsDetailQuery query);

    List<StatisticsProjectSummaryRow> selectRangeProjectSummaryList(StatisticsDetailQuery query);

    List<StatisticsDetailRow> selectRangeDetailRows(StatisticsDetailQuery query);
}
