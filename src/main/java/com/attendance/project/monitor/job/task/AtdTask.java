package com.attendance.project.monitor.job.task;

import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;
import com.attendance.project.atdform.atdstatisticform.service.IAtdHolidayService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdStatisticFormService;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.system.config.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Component("atdTask")
public class AtdTask
{
    @Autowired
    private IAtdHolidayService atdHolidayService;

    @Autowired
    private IAtdRecordService atdRecordService;

    @Autowired
    private IAtdPlanService atdPlanService;

    @Autowired
    private IAtdStatisticFormService atdStatisticFormService;

    @Autowired
    private IConfigService configService;

    public void getHoliday() {
        DateFormat df = new SimpleDateFormat("yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String year = df.format(new Date());
        AtdHoliday param = new AtdHoliday();
        param.setYear(year);
        List<AtdHoliday> atdHolidays = atdHolidayService.selectAtdHolidayList(param);
        if (atdHolidays.size() <= 0){
            String url = configService.selectConfigByKey("atd.holiday.url");
            String key = configService.selectConfigByKey("atd.holiday.key");
            atdHolidayService.getHolidayByYear(url, key, year);
            System.out.println("获取节假日数据 - " + df2.format(new Date()));
        }
    }

    //更新倒班考勤信息
    public void updateType2AtdRecords() throws Exception {
        //找出昨天计划与实际考勤情况。更新今日考勤情况
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        //获取昨今两天计划类型
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 减去一天
        Date yesterday = calendar.getTime();
        String yesterdayStr = sdf.format(yesterday);
        String todayStr = sdf.format(today);
        //查询计划
        AtdPlan atdPlan = new AtdPlan();
        atdPlan.setPlanDate(todayStr);
        //今天根据昨天倒班计划自动生成上下班记录
        List<AtdPlan> todayPlans = atdPlanService.selectAtdPlanList(atdPlan);
        //查询昨日考勤记录
        AtdRecord findParamOld = new AtdRecord();
        findParamOld.setAtdtime(sdf.parse(yesterdayStr));
       //更新昨日计划参数
        atdPlan.setPlanDate(yesterdayStr);
        atdPlan.setEnable("2");
        for (int i=0; i<todayPlans.size(); i++){
            AtdPlan todayPlan = todayPlans.get(i);
            Long userid = todayPlan.getUserId();
            atdPlan.setUserId(userid);
            List<AtdPlan> yesterdayPlans = atdPlanService.selectAtdPlanList(atdPlan);
            findParamOld.setUserid(userid);
            List<AtdRecord> yesterdayAtds =  atdRecordService.selectAtdRecordList(findParamOld);
            //生成自动考勤记录
            AtdStatisticForm daobanAtd = new AtdStatisticForm();
            daobanAtd.setUserid(userid);
            daobanAtd.setAtdtime(sdf.parse(sdf.format(today)));
            daobanAtd.setAtdtag("1");
            daobanAtd.setKjcreateTime(today);
            //daobanAtd.setAtdstatus("1");
            //daobanAtd.setAtddesckj("倒班");
            daobanAtd.setAtddesc("倒班");//系统自动生成与倒班相关的第二标识
            daobanAtd.setOperatename("系统");
            daobanAtd.setOperatetime(sdf2.format(new Date()));
            //今日上下班记录清空，查询参数
            AtdRecord findTodayAtd = new AtdRecord();
            findTodayAtd.setUserid(userid);
            findTodayAtd.setAtdtime(sdf.parse(todayStr));
            findTodayAtd.setAtdtag("1");
            //昨天倒班且有考勤记录
            if (yesterdayPlans.size() > 0 && yesterdayAtds.size() > 0){
                //删除已有记录
                List<AtdRecord> todayAtds = atdRecordService.selectAtdRecordList(findTodayAtd);
                if (todayAtds.size() > 0) {
                    atdRecordService.deleteAtdRecordByAtd(todayAtds.get(0));
                }
                //当天自动生成上班记录
                if ("0".equals(todayPlan.getEnable())){
                    daobanAtd.setAtdstatus("0"); //倒班第二天未安排
                }
                atdStatisticFormService.insertAtdStatisticForm(daobanAtd);
            }
            //当天倒班自动生成下班记录
            if ("2".equals(todayPlan.getEnable())){
                //删除已有记录
                findTodayAtd.setAtdtag("2");
                List<AtdRecord> todayAtds = atdRecordService.selectAtdRecordList(findTodayAtd);
                if (todayAtds.size() > 0) {
                    atdRecordService.deleteAtdRecordByAtd(todayAtds.get(0));
                }
                daobanAtd.setAtdtag("2");
                daobanAtd.setAtdstatus(null);
                Date t2xiaban = sdf.parse(todayStr);
                t2xiaban.setHours(23);
                t2xiaban.setMinutes(59);
                t2xiaban.setSeconds(59);
                daobanAtd.setKjcreateTime(t2xiaban);
                atdStatisticFormService.insertAtdStatisticForm(daobanAtd);
            }
        }

        //查看昨日倒班计划并给今日未排班情况自动生成上班记录
        atdPlan.setUserId(null);
        List<AtdPlan> yesterdayPlans = atdPlanService.selectAtdPlanList(atdPlan);
        for (int i=0; i<yesterdayPlans.size(); i++){
            AtdPlan yesterdayPlan = yesterdayPlans.get(i);
            findParamOld.setUserid(yesterdayPlan.getUserId());
            List<AtdRecord> yesterdayAtds =  atdRecordService.selectAtdRecordList(findParamOld);
            //查看今日有无安排
            atdPlan.setUserId(yesterdayPlan.getUserId());
            atdPlan.setPlanDate(todayStr);
            atdPlan.setEnable(null);
            List<AtdPlan> todayUserPlans = atdPlanService.selectAtdPlanList(atdPlan);
            if (yesterdayAtds.size() > 0 && todayUserPlans.size() <= 0){
                //删除已有记录
                AtdRecord findTodayAtd = new AtdRecord();
                findTodayAtd.setUserid(yesterdayPlan.getUserId());
                findTodayAtd.setAtdtime(sdf.parse(todayStr));
                findTodayAtd.setAtdtag("1");
                List<AtdRecord> todayAtds = atdRecordService.selectAtdRecordList(findTodayAtd);
                if (todayAtds.size() > 0) {
                    atdRecordService.deleteAtdRecordByAtd(todayAtds.get(0));
                }
                //生成自动考勤记录
                AtdStatisticForm daobanAtd = new AtdStatisticForm();
                daobanAtd.setUserid(yesterdayPlan.getUserId());
                daobanAtd.setAtdtime(sdf.parse(sdf.format(today)));
                daobanAtd.setAtdtag("1");
                daobanAtd.setKjcreateTime(today);
                daobanAtd.setAtdstatus("0"); //倒班第二天未安排
                //daobanAtd.setAtddesckj("倒班");
                daobanAtd.setAtddesc("倒班"); //自动生成倒班相关标识
                daobanAtd.setOperatename("系统");
                daobanAtd.setOperatetime(sdf2.format(new Date()));
                atdStatisticFormService.insertAtdStatisticForm(daobanAtd);
            }
        }

    }

}
