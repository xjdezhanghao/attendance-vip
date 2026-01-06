package com.attendance.project.business.controller;

import com.attendance.framework.config.WxConfig;
import com.attendance.framework.shiro.token.CustomToken;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.system.post.service.IPostService;
import com.attendance.project.system.role.service.IRoleService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/business/attendance")
public class AttendanceController extends BaseController {

    private String prefix = "business/attendance";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private IAtdRecordService atdRecordService;

    @Autowired
    private IAtdPlanService atdPlanService;

    /**
     * 获取用户统计考勤记录
     */
    @ApiOperation("获取用户统计考勤记录")
    @PostMapping("/statistic")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getStatisticAtdRecord(@RequestBody AtdRecord atdRecord)
    {
        atdRecord.setStartDate(setIntDate(atdRecord.getStartDate()));
        atdRecord.setEndDate(setIntDate(atdRecord.getEndDate()));
        Long userid = atdRecord.getUserid();
        User sysUser = getSysUser();
        if (sysUser == null){
            //单点登录
            sysUser = userService.selectUserById(userid);
            CustomToken token = new CustomToken(sysUser.getLoginName());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }

        PageHelper.startPage(atdRecord.getPageNum(), atdRecord.getPageSize(), "u.user_id");
        AjaxResult result = new AjaxResult();
        List<AtdRecord> atdRecords = atdRecordService.getStatisticAtdRecord(atdRecord);
        result.put("recordList", atdRecords);
        return result;
    }

    /**
     * 获取用户考勤记录
     */
    @ApiOperation("获取用户考勤记录")
    @PostMapping("/statisticUserAtd")
    @ResponseBody
    @CrossOrigin
    public AjaxResult statisticUserAtd(@RequestBody AtdRecord atdRecord)
    {
        atdRecord.setStartDate(setIntDate(atdRecord.getStartDate()));
        atdRecord.setEndDate(setIntDate(atdRecord.getEndDate()));
        AjaxResult result = new AjaxResult();
        PageHelper.startPage(atdRecord.getPageNum(), atdRecord.getPageSize(), "atdtime,atdtag");
        List<AtdRecord> atdRecords = atdRecordService.selectAtdRecordList(atdRecord);
        result.put("recordList", atdRecords);
        return result;
    }

    /**
     * 获取用户考勤记录
     */
    @ApiOperation("获取用户考勤记录")
    @PostMapping("/getUserAtdRecord")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserAtdRecord(@RequestBody AtdRecord atdRecord)
    {
        AjaxResult result = new AjaxResult();
        Integer year = Integer.valueOf(atdRecord.getSelyear());
        Integer month = Integer.valueOf(atdRecord.getSelmonth());
        Integer date = Integer.valueOf(atdRecord.getSeldate());
        Date paramDate = new Date(year-1900,month-1,date);
        atdRecord.setAtdtime(paramDate);
        List<AtdRecord> atdRecords = atdRecordService.selectAtdRecordList(atdRecord);
        AtdRecord record = null;
        if (atdRecords.size() > 0){
            record = atdRecords.get(0);
        }
        result.put("record", record);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0; i<atdRecords.size(); i++){
            AtdRecord tmp = atdRecords.get(i);
            if ("1".equals(tmp.getAtdtag())){
                result.put("start", df.format(tmp.getCreateTime()));
            }else if ("2".equals(tmp.getAtdtag())){
                result.put("end", df.format(tmp.getCreateTime()));
            }
        }
        return result;
    }

    /**
     * 获取用户月考勤记录
     */
    @ApiOperation("获取用户月考勤记录")
    @PostMapping("/getUserMonthAtd")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserMonthAtd(@RequestBody AtdRecord atdRecord)
    {
        AjaxResult result = new AjaxResult();
        Map<String, String> dateMap = new HashMap<String, String>();
        List<AtdRecord> atdRecords = atdRecordService.getUserMonthAtd(atdRecord);
        for(int i=0; i<atdRecords.size(); i++){
            AtdRecord atd = atdRecords.get(i);
            Integer atdDate = atd.getAtdtime().getDate();
            dateMap.put(String.valueOf(atdDate), "1");
        }
        result.put("dateMap", dateMap);
        return result;
    }

    /**
     * 获取用户月考勤记录
     */
    @ApiOperation("获取用户周考勤记录")
    @PostMapping("/getUserWeekAtd")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserWeekAtd(@RequestBody AtdRecord atdRecord)
    {
        AjaxResult result = new AjaxResult();
        Map<String, String> dateMap = new HashMap<String, String>();
        List<AtdRecord> atdRecords = atdRecordService.getUserWeekAtd(atdRecord);
        for(int i=0; i<atdRecords.size(); i++){
            AtdRecord atd = atdRecords.get(i);
            Integer atdDate = atd.getAtdtime().getDate();
            dateMap.put(String.valueOf(atdDate), "1");
        }
        result.put("thisDateMap", dateMap);
        return result;
    }

    /**
     * 获取用户日考勤记录
     */
    @ApiOperation("获取用户日考勤记录")
    @PostMapping("/getUserDayAtd")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserDayAtd(@RequestBody AtdRecord atdRecord)
    {
        AjaxResult result = new AjaxResult();
        Map<String, String> dateMap = new HashMap<String, String>();
        List<AtdRecord> atdRecords = atdRecordService.getUserWeekAtd(atdRecord);
        for(int i=0; i<atdRecords.size(); i++){
            AtdRecord atd = atdRecords.get(i);
            Integer atdDate = atd.getAtdtime().getDate();
            dateMap.put(String.valueOf(atdDate), "1");
        }
        result.put("thisDateMap", dateMap);
        return result;
    }

    /**
     * 保存签到记录
     */
    @ApiOperation("保存签到记录")
    @PostMapping("/saveRecord")
    @ResponseBody
    @CrossOrigin
    public AjaxResult saveRecord(@RequestBody AtdRecord atdRecord)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer year = Integer.valueOf(atdRecord.getSelyear());
        Integer month = Integer.valueOf(atdRecord.getSelmonth());
        Integer date = Integer.valueOf(atdRecord.getSeldate());
        Date paramDate = new Date(year-1900,month-1,date);
        //创建对比时间
        Date tmpDate = new Date(year-1900,month-1,date);
        tmpDate.setHours(8);tmpDate.setMinutes(31);
        Long startTime = tmpDate.getTime();
        tmpDate.setHours(16);tmpDate.setMinutes(9);
        Long endTime = tmpDate.getTime();
        tmpDate.setHours(13);tmpDate.setMinutes(30);
        Long midTime = tmpDate.getTime();
        tmpDate.setHours(8);tmpDate.setMinutes(30);
        Long endTime2 = tmpDate.getTime();
        //获取用户id及签到类型
        Long userid = atdRecord.getUserid();
        String atdTag = atdRecord.getAtdtag();
        Date atdtime = paramDate;
        //获取昨今两天计划类型
        calendar.setTime(atdtime);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 减去一天
        Date yesterday = calendar.getTime();
        String yesterdayStr = sdf.format(yesterday);
        String todayStr = sdf.format(atdtime);
        AtdPlan atdPlan = new AtdPlan();
        atdPlan.setUserId(userid);
        atdPlan.setPlanDate(yesterdayStr);
        String yesterdayType = "0";
        List<AtdPlan> yesterdayPlans = atdPlanService.selectAtdPlanList(atdPlan);
        if (yesterdayPlans.size() > 0){
            yesterdayType = yesterdayPlans.get(0).getEnable();
        }
        atdPlan.setPlanDate(todayStr);
        List<AtdPlan> todayPlans = atdPlanService.selectAtdPlanList(atdPlan);
        String todayType = "0";
        if (todayPlans.size() > 0){
            todayType = todayPlans.get(0).getEnable();
        }
        //获取昨天实际出勤情况
        boolean yesterdayStatus = true;
        AtdRecord findParamOld = new AtdRecord();
        findParamOld.setAtdtime(yesterday);
        findParamOld.setUserid(userid);
        List<AtdRecord> oldAtds = atdRecordService.selectAtdRecordList(findParamOld);
        if (oldAtds.size() <= 0){
            yesterdayStatus = false;
        }
        //实际签到时间
        Date createTime = new Date();
        atdRecord.setCreateTime(createTime);
        Long nowTime = createTime.getTime();
        atdRecord.setAtdtime(paramDate);
        //当天签到判断迟到早退
        if(createTime.getYear() == year-1900 && createTime.getMonth() == month-1 && createTime.getDate() == date){
            if ("1".equals(atdRecord.getAtdtag())){ //上班情况
                //昨天不是正常倒班
                if (!("2".equals(yesterdayType) && yesterdayStatus)){
                    //今天已排班
                    if (!"3".equals(todayType)){
                        if(nowTime > startTime){
                            atdRecord.setAtdstatus("1");
                        }
                    }
                }
            } else if("2".equals(atdRecord.getAtdtag())) { //下班情况
                //今天已排班
                if (!("0".equals(todayType) && "2".equals(yesterdayType) && yesterdayStatus)){
                    if (!"3".equals(todayType) && !"2".equals(todayType)){
                        if(nowTime < endTime){
                            atdRecord.setAtdstatus("2");
                        }
                    }
                } else { //昨天倒班正常且今天未排班（应有下班记录）
                    if(nowTime < endTime2){
                        atdRecord.setAtdstatus("2");
                    }
                }
            }
        } else {
            atdRecord.setCreateTime(paramDate);
            atdRecord.setAtdstatus(atdRecord.getAtdtag());
            atdRecord.setAtddesc("日期异常");
        }
        AjaxResult result = new AjaxResult();
        /*String atddesc = atdRecord.getAtddesc();
        if (atddesc != null){
            atdRecord.setAtddesc(atddesc.trim());
        }*/
        //查看是否已经保存
        AtdRecord findParam = new AtdRecord();
        findParam.setAtdtime(paramDate);
        findParam.setUserid(userid);
        findParam.setAtdtag(atdTag);
        List<AtdRecord> findList = atdRecordService.selectAtdRecordList(findParam);
        if (findList.size() == 0){
            Integer count = atdRecordService.insertAtdRecord(atdRecord);
        }
        result.put("recordid", atdRecord.getId());
        result.put("createTime", createTime);
        return result;
    }

    /**
     * 更新签到记录
     */
    @ApiOperation("更新签到记录")
    @PostMapping("/updateRecord")
    @ResponseBody
    @CrossOrigin
    public AjaxResult updateRecord(@RequestBody AtdRecord atdRecord)
    {
        AjaxResult result = new AjaxResult();
        String atddesc = atdRecord.getAtddesc();
        if (atddesc != null){
            atdRecord.setAtddesc(atddesc.trim());
        }
        Integer count = atdRecordService.updateAtdRecord(atdRecord);
        result.put("recordid", count);
        return result;
    }

    Date setIntDate(Date oldDate){
        oldDate.setHours(0);
        return oldDate;
    }
}
