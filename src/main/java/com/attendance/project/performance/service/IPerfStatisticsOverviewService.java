package com.attendance.project.performance.service;

import com.attendance.project.performance.domain.PerfStatisticsOverview;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 绩效统计主Service接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface IPerfStatisticsOverviewService
{
    /**
     * 查询绩效统计主
     * 
     * @param overviewId 绩效统计主主键
     * @return 绩效统计主
     */
    public PerfStatisticsOverview selectPerfStatisticsOverviewByOverviewId(Long overviewId);

    /**
     * 查询绩效统计主列表
     * 
     * @param perfStatisticsOverview 绩效统计主
     * @return 绩效统计主集合
     */
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewList(PerfStatisticsOverview perfStatisticsOverview);

    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAll(PerfStatisticsOverview perfStatisticsOverview);


}
