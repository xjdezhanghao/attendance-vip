package com.attendance.project.monitor.job.controller;

import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.monitor.job.task.PerfTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 绩效采集定时任务控制器
 *
 * @author june
 */
@Controller
@RequestMapping("/monitor/job/perfTask")
public class PerfTaskController extends BaseController {

    @Autowired
    private PerfTask perfTask;

    /**
     * 手动触发生成考核采集记录
     */
    @Log(title = "绩效采集", businessType = BusinessType.OTHER)
    @PostMapping("/generateGatherRecords")
    @ResponseBody
    public AjaxResult generateGatherRecords() {
        try {
            perfTask.callGenerateGatherRecords();
            return AjaxResult.success("生成考核采集记录成功");
        } catch (Exception e) {
            return AjaxResult.error("生成考核采集记录失败: " + e.getMessage());
        }
    }
}