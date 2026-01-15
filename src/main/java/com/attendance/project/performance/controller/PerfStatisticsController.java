package com.attendance.project.performance.controller;

import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.performance.domain.PerfGatherDetail;
import com.attendance.project.performance.domain.PerfGatherOverview;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.service.IPerfGatherDetailService;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.project.performance.service.IPerfUserParamService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private IPerfGatherOverviewService perfGatherOverviewService;

    @Autowired
    private IPerfGatherDetailService perfGatherDetailService;

    @Autowired
    private IPerfUserParamService userParamService;

    @RequiresPermissions("perf:stat:view")
    @GetMapping()
    public String overview()
    {
        return prefix + "/statistics";
    }

    /**
     * 查询绩效统计列表
     */
    @RequiresPermissions("perf:stat:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfGatherOverview perfGatherOverview)
    {
        startPage();
        List<PerfGatherOverview> list = perfGatherOverviewService.selectPerfGatherOverviewList(perfGatherOverview);
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
     * 新增绩效统计
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存绩效统计
     */
    @RequiresPermissions("perf:stat:add")
    @Log(title = "绩效统计", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PerfGatherOverview perfGatherOverview)
    {
        return toAjax(perfGatherOverviewService.insertPerfGatherOverview(perfGatherOverview));
    }

    /**
     * 进入绩效采集详情页面 - 根据用户ID、部门ID、岗位ID、采集月份查询
     */
    @RequiresPermissions("perf:stat:edit")
    @GetMapping("/overviewGather")
    public String overviewGather(@RequestParam Long userId,
                                 @RequestParam String gatherDate,
                                 ModelMap mmap)
    {
        // 根据用户ID和采集月份查找对应的overview记录
        PerfGatherOverview queryParam = new PerfGatherOverview();
        queryParam.setUserId(userId);
        queryParam.setGatherDate(gatherDate);
        List<PerfGatherOverview> overviewList = perfGatherOverviewService.selectPerfGatherOverviewListAll(queryParam);

        // 限制最多3个标签页
        if (overviewList.size() > 3) {
            overviewList = overviewList.subList(0, 3);
        }

        // 根据overview的projectId获取对应的考核项目和详细信息
        List<List<PerfGatherDetail>> gatherDetailsList = new ArrayList<>();
        for (PerfGatherOverview overview : overviewList) {
            PerfGatherDetail detailParam = new PerfGatherDetail();
            detailParam.setProjectId(overview.getProjectId());
            //detailParam.setOverviewId(overview.getOverviewId());
            List<PerfGatherDetail> details = perfGatherDetailService.selectPerfGatherDetailList(detailParam);
            gatherDetailsList.add(details);
        }

        PerfUserParam userParam = new PerfUserParam();
        userParam.setUserId(userId);
        List<PerfUserParam> userParams = userParamService.selectPerfUserParamList(userParam);
        if (userParams.size() > 0) userParam = userParams.get(0);

        mmap.put("overviewList", overviewList);
        mmap.put("gatherDetailList", gatherDetailsList);
        mmap.put("userId", userId);
        mmap.put("gatherDate", gatherDate);
        mmap.put("userParam", userParam);
        return prefix + "/detail";
    }

}
