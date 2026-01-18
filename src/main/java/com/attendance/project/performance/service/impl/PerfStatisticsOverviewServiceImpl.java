package com.attendance.project.performance.service.impl;

import com.attendance.project.performance.domain.*;
import com.attendance.project.performance.mapper.*;
import com.attendance.project.performance.service.IPerfStatisticsOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 绩效采集主Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class PerfStatisticsOverviewServiceImpl implements IPerfStatisticsOverviewService
{
    @Autowired
    private PerfGatherOverviewMapper perfGatherOverviewMapper;

    @Autowired
    private PerfIndProjectMapper perfIndProjectMapper;

    @Autowired
    private PerfIndItemMapper  perfIndItemMapper;

    @Autowired
    private PerfUserParamMapper perfUserParamMapper;

    @Autowired
    private PerfStatisticsOverviewMapper perfStatisticsOverviewMapper;

    @Autowired
    private PerfStatisticsDetailMapper perfStatisticsDetailMapper;

    @Autowired
    private PerfUserPostMapper perfUserPostMapper;

    @Autowired
    private PerfPostMapper perfPostMapper;

    @Override
    public PerfStatisticsOverview selectPerfStatisticsOverviewByOverviewId(Long overviewId) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewByOverviewId(overviewId);
    }

    @Override
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewList(PerfStatisticsOverview perfStatisticsOverview) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewList(perfStatisticsOverview);
    }

    @Override
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAll(PerfStatisticsOverview perfStatisticsOverview) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewListAll(perfStatisticsOverview);
    }

    @Override
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAllDate(PerfStatisticsOverview perfStatisticsOverview) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewListAllDate(perfStatisticsOverview);
    }

    @Override
    public List<PerfStatisticsDetail> selectPerfStatisticsDetailList(PerfStatisticsDetail perfStatisticsDetail) {
        return perfStatisticsDetailMapper.selectPerfStatisticsDetailList(perfStatisticsDetail);
    }

    @Override
    public Map<String, Object> getDefaultStatistics(String startDate, String endDate, Integer topN, String trendGranularity) {
        LocalDate today = LocalDate.now();
        LocalDate start = startDate == null || startDate.isBlank()
            ? today.minusDays(29)
            : LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = endDate == null || endDate.isBlank()
            ? today
            : LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        if (end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        int normalizedTopN = normalizeTopN(topN);
        Map<String, Object> baseParams = new HashMap<>();
        baseParams.put("startDate", start.toString());
        baseParams.put("endDate", end.toString());
        baseParams.put("topN", normalizedTopN);

        BigDecimal currentAvg = defaultScore(perfStatisticsOverviewMapper.selectCompanyAvgScore(baseParams));
        LocalDate prevStart = start.minusDays(30);
        LocalDate prevEnd = end.minusDays(30);
        Map<String, Object> prevParams = new HashMap<>();
        prevParams.put("startDate", prevStart.toString());
        prevParams.put("endDate", prevEnd.toString());
        BigDecimal prevAvg = defaultScore(perfStatisticsOverviewMapper.selectCompanyAvgScore(prevParams));

        BigDecimal momDelta = currentAvg.subtract(prevAvg);
        BigDecimal momDeltaPct = prevAvg.compareTo(BigDecimal.ZERO) == 0
            ? null
            : momDelta.multiply(BigDecimal.valueOf(100))
                .divide(prevAvg, 2, RoundingMode.HALF_UP);

        List<Map<String, Object>> topUsers = buildTopUsers(perfStatisticsOverviewMapper.selectTopUsers(baseParams));
        List<Map<String, Object>> companyTrend = perfStatisticsOverviewMapper.selectCompanyTrend(baseParams);
        List<Map<String, Object>> topDeptAverages = perfStatisticsOverviewMapper.selectTopDeptAverages(baseParams);

        List<Long> deptIds = new ArrayList<>();
        for (Map<String, Object> dept : topDeptAverages) {
            Object deptId = dept.get("dept_id");
            if (deptId instanceof Number) {
                deptIds.add(((Number) deptId).longValue());
            }
        }

        List<Map<String, Object>> deptTrend = deptIds.isEmpty()
            ? Collections.emptyList()
            : perfStatisticsOverviewMapper.selectDeptTrend(buildDeptTrendParams(baseParams, deptIds));
        List<Map<String, Object>> radarScores = perfStatisticsOverviewMapper.selectRadarScores(baseParams);

        List<String> dates = buildDateRange(start, end);
        Map<String, BigDecimal> companyTrendMap = buildDateScoreMap(companyTrend, "gather_date", "avg_score");
        Map<Long, Map<String, BigDecimal>> deptTrendMap = buildDeptTrendMap(deptTrend);

        Map<String, Object> trend = new LinkedHashMap<>();
        trend.put("dates", dates);
        trend.put("companyAvg", buildSeries(dates, companyTrendMap));
        trend.put("topDepts", buildDeptSeries(dates, topDeptAverages, deptTrendMap));

        Map<String, Object> radar = new LinkedHashMap<>();
        radar.put("categories", buildRadarCategories(radarScores));
        radar.put("values", buildRadarValues(radarScores));

        Map<String, Object> gauge = new LinkedHashMap<>();
        gauge.put("companyAvgScore", scaleScore(currentAvg));
        gauge.put("companyAvgIndex", scaleScore(currentAvg));
        gauge.put("prevPeriodAvgScore", scaleScore(prevAvg));
        gauge.put("momDelta", scaleScore(momDelta));
        gauge.put("momDeltaPct", momDeltaPct == null ? null : momDeltaPct.doubleValue());

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("startDate", start.toString());
        meta.put("endDate", end.toString());
        meta.put("topN", normalizedTopN);
        meta.put("baseIndex", 100);
        meta.put("gauge", Map.of("min", 0, "max", 160, "bands", List.of(60, 80, 110, 150)));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("meta", meta);
        response.put("gauge", gauge);
        response.put("topUsers", topUsers);
        response.put("trend", trend);
        response.put("radar", radar);
        return response;
    }

    private int normalizeTopN(Integer topN) {
        int target = topN == null ? 10 : topN;
        if (target < 5) {
            return 5;
        }
        if (target > 50) {
            return 50;
        }
        return target;
    }

    private BigDecimal defaultScore(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Double scaleScore(BigDecimal value) {
        return value == null ? null : value.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private Map<String, Object> buildDeptTrendParams(Map<String, Object> baseParams, List<Long> deptIds) {
        Map<String, Object> params = new HashMap<>(baseParams);
        params.put("deptIds", deptIds);
        return params;
    }

    private List<String> buildDateRange(LocalDate start, LocalDate end) {
        List<String> dates = new ArrayList<>();
        LocalDate cursor = start;
        while (!cursor.isAfter(end)) {
            dates.add(cursor.toString());
            cursor = cursor.plusDays(1);
        }
        return dates;
    }

    private Map<String, BigDecimal> buildDateScoreMap(List<Map<String, Object>> rows, String dateKey, String scoreKey) {
        Map<String, BigDecimal> map = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object date = row.get(dateKey);
            Object score = row.get(scoreKey);
            if (date != null) {
                map.put(date.toString(), score instanceof BigDecimal ? (BigDecimal) score : null);
            }
        }
        return map;
    }

    private Map<Long, Map<String, BigDecimal>> buildDeptTrendMap(List<Map<String, Object>> rows) {
        Map<Long, Map<String, BigDecimal>> result = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Object deptId = row.get("dept_id");
            Object date = row.get("gather_date");
            Object score = row.get("avg_score");
            if (!(deptId instanceof Number) || date == null) {
                continue;
            }
            long id = ((Number) deptId).longValue();
            result.computeIfAbsent(id, key -> new HashMap<>())
                .put(date.toString(), score instanceof BigDecimal ? (BigDecimal) score : null);
        }
        return result;
    }

    private List<Double> buildSeries(List<String> dates, Map<String, BigDecimal> valueMap) {
        List<Double> series = new ArrayList<>(dates.size());
        for (String date : dates) {
            BigDecimal value = valueMap.get(date);
            series.add(value == null ? null : value.setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        return series;
    }

    private List<Map<String, Object>> buildDeptSeries(List<String> dates, List<Map<String, Object>> depts,
                                                      Map<Long, Map<String, BigDecimal>> trendMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> dept : depts) {
            Object deptIdObj = dept.get("dept_id");
            Object deptNameObj = dept.get("dept_name");
            if (!(deptIdObj instanceof Number)) {
                continue;
            }
            long deptId = ((Number) deptIdObj).longValue();
            Map<String, Object> deptEntry = new LinkedHashMap<>();
            deptEntry.put("deptId", deptId);
            deptEntry.put("deptName", deptNameObj);
            Map<String, BigDecimal> scores = trendMap.getOrDefault(deptId, Collections.emptyMap());
            deptEntry.put("avg", buildSeries(dates, scores));
            result.add(deptEntry);
        }
        return result;
    }

    private List<String> buildRadarCategories(List<Map<String, Object>> radarRows) {
        List<String> categories = new ArrayList<>();
        for (Map<String, Object> row : radarRows) {
            Object name = row.get("category_name");
            categories.add(name == null ? "" : name.toString());
        }
        return categories;
    }

    private List<Double> buildRadarValues(List<Map<String, Object>> radarRows) {
        List<Double> values = new ArrayList<>();
        for (Map<String, Object> row : radarRows) {
            Object score = row.get("score");
            if (score instanceof BigDecimal) {
                values.add(((BigDecimal) score).setScale(2, RoundingMode.HALF_UP).doubleValue());
            } else if (score instanceof Number) {
                values.add(((Number) score).doubleValue());
            } else {
                values.add(null);
            }
        }
        return values;
    }

    private List<Map<String, Object>> buildTopUsers(List<Map<String, Object>> rows) {
        List<Map<String, Object>> topUsers = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Map<String, Object> user = new LinkedHashMap<>();
            user.put("userId", row.get("user_id"));
            user.put("userName", row.get("user_name"));
            user.put("deptId", row.get("dept_id"));
            user.put("deptName", row.get("dept_name"));
            user.put("score", row.get("score"));
            topUsers.add(user);
        }
        return topUsers;
    }
}
