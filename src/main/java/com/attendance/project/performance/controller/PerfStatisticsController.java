package com.attendance.project.performance.controller;

import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.performance.domain.*;
import com.attendance.project.performance.service.IPerfGatherDetailService;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.project.performance.service.IPerfStatisticsOverviewService;
import com.attendance.project.performance.service.IPerfUserParamService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绩效统计Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Controller
@RequestMapping("/performance/statistics")
public class PerfStatisticsController extends BaseController
{
    private String prefix = "performance/statistics";

    @Autowired
    private IPerfStatisticsOverviewService perfStatisticsOverviewService;

    @Autowired
    private IPerfGatherOverviewService perfGatherOverviewService;

    @Autowired
    private IPerfGatherDetailService perfGatherDetailService;

    @Autowired
    private IPerfUserParamService userParamService;

    @RequiresPermissions("perf:stat:view")
    @GetMapping()
    public String overview(ModelMap mmap)
    {
        // 获取本月第一天和今天日期
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        // 转换为字符串格式
        String startDate = firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 传递到页面
        mmap.put("defaultStartDate", startDate);
        mmap.put("defaultEndDate", endDate);

        return prefix + "/statistics";
    }

    /**
     * 查询绩效统计列表
     */
    @RequiresPermissions("perf:stat:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfStatisticsOverview perfStatisticsOverview)
    {
        startPage();
        List<PerfStatisticsOverview> list = perfStatisticsOverviewService.selectPerfStatisticsOverviewList(perfStatisticsOverview);
        return getDataTable(list);
    }

    /**
     * 查询绩效统计列表2 时间段内每日列表
     */
    @RequiresPermissions("perf:stat:view")
    @PostMapping("/list2")
    @ResponseBody
    public TableDataInfo list2(PerfStatisticsOverview perfStatisticsOverview)
    {
        startPage();
        List<PerfStatisticsOverview> list = perfStatisticsOverviewService.selectPerfStatisticsOverviewListAll(perfStatisticsOverview);
        return getDataTable(list);
    }

    /**
     * 导出绩效统计列表
     */
    @RequiresPermissions("perf:stat:export")
    @Log(title = "绩效统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfGatherOverview perfGatherOverview)
    {
        List<PerfGatherOverview> list = perfGatherOverviewService.selectPerfGatherOverviewList(perfGatherOverview);
        ExcelUtil<PerfGatherOverview> util = new ExcelUtil<PerfGatherOverview>(PerfGatherOverview.class);
        return util.exportExcel(list, "绩效统计数据");
    }
    
    /**
     * 进入绩效统计详情页面 - 根据用户ID、部门ID、岗位ID、统计月份查询
     */
    @RequiresPermissions("perf:stat:view")
    @GetMapping("/overviewDetail")
    public String overviewDetail(@RequestParam Long userId,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 ModelMap mmap)
    {
        String gatherDate = startDate+" - "+endDate;
        if (startDate.equals(endDate)) gatherDate = startDate;
        // 根据用户ID和统计月份查找对应的overview记录
        PerfStatisticsOverview queryParam = new PerfStatisticsOverview();
        queryParam.setUserId(userId);
        queryParam.setStartDate(startDate);
        queryParam.setEndDate(endDate);
        List<PerfStatisticsOverview> overviewList = perfStatisticsOverviewService.selectPerfStatisticsOverviewListAll(queryParam);

        // 限制最多3个标签页
        if (overviewList.size() > 3) {
            overviewList = overviewList.subList(0, 3);
        }

        // 根据overview的projectId获取对应的考核项目和详细信息
        List<List<PerfStatisticsDetail>> statisticsDetailList = new ArrayList<>();
        for (PerfStatisticsOverview overview : overviewList) {
            PerfStatisticsDetail detailParam = new PerfStatisticsDetail();
            detailParam.setProjectId(overview.getProjectId());
            detailParam.setUserId(userId);
            detailParam.setStartDate(startDate);
            detailParam.setEndDate(endDate);
            List<PerfStatisticsDetail> details = perfStatisticsOverviewService.selectPerfStatisticsDetailList(detailParam);
            statisticsDetailList.add(details);
        }

        PerfUserParam userParam = new PerfUserParam();
        userParam.setUserId(userId);
        List<PerfUserParam> userParams = userParamService.selectPerfUserParamList(userParam);
        if (userParams.size() > 0) userParam = userParams.get(0);

        mmap.put("overviewList", overviewList);
        mmap.put("overviewDetailList", statisticsDetailList);
        mmap.put("userId", userId);
        mmap.put("gatherDate", gatherDate);
        mmap.put("userParam", userParam);
        return prefix + "/detail";
    }

}
