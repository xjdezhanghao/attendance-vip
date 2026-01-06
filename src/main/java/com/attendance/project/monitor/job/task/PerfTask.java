package com.attendance.project.monitor.job.task;

import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;
import com.attendance.project.atdform.atdstatisticform.service.IAtdHolidayService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdStatisticFormService;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.monitor.server.domain.Sys;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.project.system.config.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度测试
 * 
 * @author june
 */
@Component("perfTask")
public class PerfTask
{
    @Autowired
    private IPerfGatherOverviewService perfGatherOverviewService;

    /**
     * 每日0点12点自动生成当月考核采集记录
     */
    //@Scheduled(cron = "0 0 12,0 * * ?")
    public void generateGatherRecords() {
        System.out.println("开始生成当月考核采集记录");
        perfGatherOverviewService.generateMonthlyGatherRecords();
        System.out.println("当月考核采集记录生成完成");
    }

}
