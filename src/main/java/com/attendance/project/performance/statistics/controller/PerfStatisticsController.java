package com.attendance.project.performance.statistics.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.attendance.common.utils.StringUtils;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.service.IPerfUserParamService;
import com.attendance.project.performance.statistics.dto.StatisticsDetailQuery;
import com.attendance.project.performance.statistics.dto.StatisticsOverviewQuery;
import com.attendance.project.performance.statistics.service.IPerfStatisticsService;
import com.attendance.project.performance.statistics.vo.StatisticsDetailView;
import com.attendance.project.performance.statistics.vo.StatisticsOverviewVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 绩效统计Controller
 */
@Controller
@RequestMapping("/performance/statistics")
public class PerfStatisticsController extends BaseController {
    private static final String PREFIX = "performance/statistics";

    @Autowired
    private IPerfStatisticsService statisticsService;

    @Autowired
    private IPerfUserParamService userParamService;

    @RequiresPermissions("perf:stat:view")
    @GetMapping({"", "/", "/overview"})
    public String overview(ModelMap mmap) {
        setDefaultDates(mmap);
        mmap.put("dailyMode", false);
        return PREFIX + "/overview";
    }

    @RequiresPermissions("perf:stat:view")
    @GetMapping("/overview/daily")
    public String overviewDaily(@RequestParam Long userId,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                @RequestParam(required = false) String userName,
                                @RequestParam(required = false) Long postId,
                                @RequestParam(required = false) Long deptId,
                                @RequestParam(required = false) String returnParams,
                                @RequestParam(required = false) Integer pageNum,
                                @RequestParam(required = false) Integer pageSize,
                                @RequestParam(required = false) String sortName,
                                @RequestParam(required = false) String sortOrder,
                                ModelMap mmap) {
        mmap.put("dailyMode", true);
        mmap.put("userId", userId);
        mmap.put("userName", userName);
        mmap.put("postId", postId);
        mmap.put("deptId", deptId);
        mmap.put("startDate", startDate);
        mmap.put("endDate", endDate);
        mmap.put("returnParams", returnParams);
        mmap.put("pageNum", pageNum);
        mmap.put("pageSize", pageSize);
        mmap.put("sortName", sortName);
        mmap.put("sortOrder", sortOrder);
        return PREFIX + "/overview";
    }

    @RequiresPermissions("perf:stat:view")
    @PostMapping("/overview/list")
    @ResponseBody
    public TableDataInfo list(StatisticsOverviewQuery query) {
        startPage();
        List<StatisticsOverviewVO> list = statisticsService.selectStatisticsOverviewList(query);
        return getDataTable(list);
    }

    @RequiresPermissions("perf:stat:view")
    @PostMapping("/overview/dailyList")
    @ResponseBody
    public TableDataInfo dailyList(StatisticsOverviewQuery query) {
        startPage();
        List<StatisticsOverviewVO> list = statisticsService.selectStatisticsDailyList(query);
        return getDataTable(list);
    }

    @RequiresPermissions("perf:stat:view")
    @GetMapping("/detail")
    public String detail(@RequestParam Long userId,
                         @RequestParam(required = false) String gatherDate,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate,
                         ModelMap mmap) {
        StatisticsDetailQuery query = new StatisticsDetailQuery();
        query.setUserId(userId);
        query.setGatherDate(gatherDate);
        query.setStartDate(startDate);
        query.setEndDate(endDate);

        StatisticsDetailView detailView;
        String detailMode;
        if (StringUtils.isNotBlank(gatherDate)) {
            detailView = statisticsService.buildDailyDetail(query);
            detailMode = "day";
        } else {
            detailView = statisticsService.buildRangeDetail(query);
            detailMode = "range";
        }

        PerfUserParam userParam = new PerfUserParam();
        userParam.setUserId(userId);
        List<PerfUserParam> userParams = userParamService.selectPerfUserParamList(userParam);
        if (!userParams.isEmpty()) {
            userParam = userParams.get(0);
        }

        mmap.put("overviewList", detailView.getOverviewList());
        mmap.put("gatherDetailList", detailView.getDetailList());
        mmap.put("userParam", userParam);
        mmap.put("userId", userId);
        mmap.put("gatherDate", gatherDate);
        mmap.put("startDate", startDate);
        mmap.put("endDate", endDate);
        mmap.put("detailMode", detailMode);
        return PREFIX + "/detail";
    }

    private void setDefaultDates(ModelMap mmap) {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mmap.put("startDate", firstDay.format(formatter));
        mmap.put("endDate", today.format(formatter));
    }
}
