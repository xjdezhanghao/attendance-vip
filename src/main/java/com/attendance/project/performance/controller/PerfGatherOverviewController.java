package com.attendance.project.performance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attendance.project.performance.domain.PerfGatherDetail;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.service.IPerfGatherDetailService;
import com.attendance.project.performance.service.IPerfUserParamService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.project.performance.domain.PerfGatherOverview;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.web.page.TableDataInfo;

/**
 * 绩效采集主Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Controller
@RequestMapping("/performance/gather")
public class PerfGatherOverviewController extends BaseController
{
    private String prefix = "performance/gather";

    @Autowired
    private IPerfGatherOverviewService perfGatherOverviewService;

    @Autowired
    private IPerfGatherDetailService perfGatherDetailService;

    @Autowired
    private IPerfUserParamService userParamService;

    @RequiresPermissions("perf:gather:view")
    @GetMapping()
    public String overview()
    {
        return prefix + "/overview";
    }

    /**
     * 查询绩效采集主列表
     */
    @RequiresPermissions("perf:gather:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfGatherOverview perfGatherOverview)
    {
        startPage();
        List<PerfGatherOverview> list = perfGatherOverviewService.selectPerfGatherOverviewList(perfGatherOverview);
        return getDataTable(list);
    }

    /**
     * 导出绩效采集主列表
     */
    @RequiresPermissions("perf:gather:export")
    @Log(title = "绩效采集主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfGatherOverview perfGatherOverview)
    {
        List<PerfGatherOverview> list = perfGatherOverviewService.selectPerfGatherOverviewList(perfGatherOverview);
        ExcelUtil<PerfGatherOverview> util = new ExcelUtil<PerfGatherOverview>(PerfGatherOverview.class);
        return util.exportExcel(list, "绩效采集主数据");
    }

    /**
     * 新增绩效采集主
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存绩效采集主
     */
    @RequiresPermissions("perf:gather:add")
    @Log(title = "绩效采集主", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PerfGatherOverview perfGatherOverview)
    {
        return toAjax(perfGatherOverviewService.insertPerfGatherOverview(perfGatherOverview));
    }

    /**
     * 进入绩效采集详情页面 - 根据用户ID、部门ID、岗位ID、采集月份查询
     */
    @RequiresPermissions("perf:gather:edit")
    @GetMapping("/overviewGather")
    public String overviewGather(@RequestParam Long userId,
                                 @RequestParam String gatherMonth,
                                 ModelMap mmap)
    {
        // 根据用户ID和采集月份查找对应的overview记录
        PerfGatherOverview queryParam = new PerfGatherOverview();
        queryParam.setUserId(userId);
        queryParam.setGatherMonth(gatherMonth);
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
        mmap.put("gatherMonth", gatherMonth);
        mmap.put("userParam", userParam);
        return prefix + "/detail";
    }

    /**
     * 修改保存绩效采集主
     */
    @RequiresPermissions("perf:gather:edit")
    @Log(title = "绩效采集", businessType = BusinessType.UPDATE)
    @PostMapping("/saveDetails")
    @ResponseBody
    public AjaxResult saveDetails(HttpServletRequest request) {
        try {
            // 解析请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();

            // 存储每个overviewId对应的数据
            Map<Long, Map<Long, Long>> allScores = new HashMap<>();
            Map<Long, Map<Long, String>> allRemarks = new HashMap<>();
            Map<Long, Map<Long, String>> allImagePaths = new HashMap<>();
            Map<Long, String> overallRemarks = new HashMap<>();

            // 解析评分数据 - scores[overviewId][itemId]
            for (String paramName : parameterMap.keySet()) {
                if (paramName.startsWith("scores[")) {
                    // 使用正则表达式解析参数名格式：scores[overviewId][itemId]
                    String regex = "scores\\[([0-9]+)\\]\\[([0-9]+)\\]";
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher matcher = pattern.matcher(paramName);
                    if (matcher.matches()) {
                        Long overviewId = Long.parseLong(matcher.group(1));
                        Long itemId = Long.parseLong(matcher.group(2));
                        String[] values = parameterMap.get(paramName);
                        if (values.length > 0 && !values[0].isEmpty()) {
                            allScores.computeIfAbsent(overviewId, k -> new HashMap<>())
                                    .put(itemId, Long.valueOf(values[0]));
                        }
                    }
                } else if (paramName.startsWith("remarks[")) {
                    // 使用正则表达式解析参数名格式：remarks[overviewId][itemId]
                    String regex = "remarks\\[([0-9]+)\\]\\[([0-9]+)\\]";
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher matcher = pattern.matcher(paramName);
                    if (matcher.matches()) {
                        Long overviewId = Long.parseLong(matcher.group(1));
                        Long itemId = Long.parseLong(matcher.group(2));
                        String[] values = parameterMap.get(paramName);
                        if (values.length > 0) {
                            allRemarks.computeIfAbsent(overviewId, k -> new HashMap<>())
                                    .put(itemId, values[0]);
                        }
                    }
                } else if (paramName.startsWith("imagePaths[")) {
                    // 使用正则表达式解析参数名格式：imagePaths[overviewId][itemId]
                    String regex = "imagePaths\\[([0-9]+)\\]\\[([0-9]+)\\]";
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher matcher = pattern.matcher(paramName);
                    if (matcher.matches()) {
                        Long overviewId = Long.parseLong(matcher.group(1));
                        Long itemId = Long.parseLong(matcher.group(2));
                        String[] values = parameterMap.get(paramName);
                        if (values.length > 0) {
                            allImagePaths.computeIfAbsent(overviewId, k -> new HashMap<>())
                                    .put(itemId, values[0]);
                        }
                    }
                } else if (paramName.startsWith("overallRemarks[")) {
                    // 使用正则表达式解析参数名格式：overallRemarks[overviewId]
                    String regex = "overallRemarks\\[([0-9]+)\\]";
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher matcher = pattern.matcher(paramName);
                    if (matcher.matches()) {
                        Long overviewId = Long.parseLong(matcher.group(1));
                        String[] values = parameterMap.get(paramName);
                        if (values.length > 0) {
                            overallRemarks.put(overviewId, values[0]);
                        }
                    }
                }
            }

            // 更新每个overview的备注
            for (Map.Entry<Long, String> entry : overallRemarks.entrySet()) {
                Long overviewId = entry.getKey();
                PerfGatherOverview overview = new PerfGatherOverview();
                overview.setOverviewId(overviewId);
                overview.setRemark(entry.getValue());
                perfGatherOverviewService.updatePerfGatherOverview(overview);
            }

            // 批量更新每个overview下的考核项
            for (Long overviewId : allScores.keySet()) {
                Map<Long, Long> scores = allScores.get(overviewId);
                Map<Long, String> remarks = allRemarks.get(overviewId);
                Map<Long, String> imagePaths = allImagePaths.get(overviewId);

                // 确保 Map 不为 null，如果为 null 则创建空的 HashMap
                if (scores == null) scores = new HashMap<>();
                if (remarks == null) remarks = new HashMap<>();
                if (imagePaths == null) imagePaths = new HashMap<>();

                perfGatherOverviewService.updateScoresAndRemarks(overviewId, scores, remarks, imagePaths);
            }

            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("保存绩效采集评分失败", e);
            return AjaxResult.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除绩效采集主
     */
    @RequiresPermissions("perf:gather:remove")
    @Log(title = "绩效采集主", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(perfGatherOverviewService.deletePerfGatherOverviewByOverviewIds(ids));
    }
}
