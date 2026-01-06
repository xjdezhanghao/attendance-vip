package com.attendance.project.atdform.atdstatisticform.controller;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.atdform.atdstatisticform.domain.AtdHoliday;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;
import com.attendance.project.atdform.atdstatisticform.service.IAtdHolidayService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdStatisticFormService;
import com.attendance.project.business.domain.AtdRecord;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.system.config.service.IConfigService;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.domain.UserRole;
import com.attendance.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排班计划Controller
 *
 * @author ruoyi
 * @date 2025-01-10
 */
@Controller
@RequestMapping("/atdform/plan")
public class AtdPlanController extends BaseController
{
    private String prefix = "atdform/plan";

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IAtdPlanService atdPlanService;

    @Autowired
    private IAtdRecordService atdRecordService;

    @Autowired
    private IAtdStatisticFormService atdStatisticFormService;

    @Autowired
    private IAtdHolidayService atdHolidayService;

    @Autowired
    private IConfigService configService;

    @GetMapping()
    public String plan(ModelMap mmap)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "";
        String endDate = "";
        Integer curday = calendar.get(Calendar.DAY_OF_MONTH);
        if (curday > 20) {
            calendar.set(Calendar.DATE, 21);
            startDate = df.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 20);
            endDate = df.format(calendar.getTime());
        } else {
            calendar.set(Calendar.DATE, 20);
            endDate = df.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 21);
            startDate = df.format(calendar.getTime());
        }
        mmap.put("start", startDate);
        mmap.put("end", endDate);
        boolean canEdit = false;
        Long userId = getSysUser().getUserId();
        List<UserRole> userRoles = userService.selectUserRoleByUserId(userId);
        for (int i=0; i<userRoles.size(); i++){
            Long roleId = userRoles.get(i).getRoleId();
            if (roleId == 1 || roleId == 2 || roleId == 103 || roleId == 104 || roleId == 106){
                canEdit = true;
                break;
            }
        }
        mmap.put("canEdit", canEdit);
        return prefix + "/plan";
    }

    /**
     * 查询排班计划列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AtdPlan atdPlan) throws Exception
    {
        String startDate = atdPlan.getStartDate();
        String endDate = atdPlan.getEndDate();
        String deptname = atdPlan.getDeptName();
        String username = atdPlan.getUserName();
        //查询时间为空获取上月21和本月20
        //获取当前日期年月日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if ((startDate == null || "".equals(startDate)) && (endDate == null || "".equals(endDate))){
            Integer curday = calendar.get(Calendar.DAY_OF_MONTH);
            if (curday > 20) {
                calendar.set(Calendar.DATE, 21);
                startDate = df.format(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 20);
                endDate = df.format(calendar.getTime());
            } else {
                calendar.set(Calendar.DATE, 20);
                endDate = df.format(calendar.getTime());
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DATE, 21);
                startDate = df.format(calendar.getTime());
            }
        }
        /*if (startDate == null || "".equals(startDate)) startDate = df.format(new Date());
        if (endDate == null || "".equals(endDate)) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            endDate = df.format(calendar.getTime());
        }*/
        //1 形成时间列表
        List<String> timeList = DateUtils.getBetweenDate(startDate, endDate, "yyyy-MM-dd");
        AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
        atdStatisticForm.setStarttime(startDate);
        atdStatisticForm.setEndtime(endDate);
        atdStatisticForm.setDeptname(deptname);
        atdStatisticForm.setUsername(username);
        //2 查出所有姓名
        // List<String>  names = atdStatisticFormService.selectNames(atdStatisticForm);
        List<Integer> ids = atdStatisticFormService.selectIds(atdStatisticForm);

        //3 查询数据库数据
        startPage();

        //4 把所有人考勤赋值  对每个人的考勤形成字符串
        List<String> resultList = new ArrayList<>();
        String dult0 = " " + "," + " " + "," + "星期";
        for (String time : timeList) {
            Date date_time = df.parse(time);
            String week = DateUtils.dateToWeek(date_time);
            dult0 = dult0 + "," + week;//星期
        }

        resultList.add(dult0);
        //for (String name : names)
        AtdPlan param = new AtdPlan();
        param.setDeptId(getSysUser().getDeptId());
        param.setUserIdList(ids);
        param.setDateList(timeList);
        param.setDeptName(deptname);
        param.setUserName(username);
        List<Map<String, Object>> maps = atdPlanService.selectAtdPlanMapList(param);
        String ignIds = configService.selectConfigByKey("atd.ignore.ids");
        for (Map<String, Object> map : maps) {
            Long userId = (Long)map.get("user_id");
            if (!ids.contains(userId.intValue())) continue;
            String userName = map.get("user_name").toString();
            if (ignIds.indexOf(("#"+userId+"#")) >= 0) continue;
            //初始化字符串
            String dult1 = userId + "," + userName + "," + "排班";
            for (String time : timeList) {
                if ("1".equals(map.get(time))){
                    dult1 += ",白";
                } else if ("2".equals(map.get(time))) {
                    dult1 += ",倒";
                } else if ("3".equals(map.get(time))){
                    dult1 += ",另";
                } else {
                    dult1 += ",";
                }
            }
            resultList.add(dult1);
        }

        //如果查询时间为空   默认查询本月的
        return getDataTable(resultList);
    }

    /**
     * 导出排班计划列表
     */
    @Log(title = "排班计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AtdPlan atdPlan)
    {
        List<AtdPlan> list = atdPlanService.selectAtdPlanList(atdPlan);
        ExcelUtil<AtdPlan> util = new ExcelUtil<AtdPlan>(AtdPlan.class);
        return util.exportExcel(list, "排班计划数据");
    }

    /**
     * 新增排班计划
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存排班计划
     */
    @Log(title = "排班计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AtdPlan atdPlan)
    {
        return toAjax(atdPlanService.insertAtdPlan(atdPlan));
    }

    /**
     * 修改排班计划
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        AtdPlan atdPlan = atdPlanService.selectAtdPlanById(id);
        mmap.put("atdPlan", atdPlan);
        return prefix + "/edit";
    }

    /**
     * 修改保存排班计划
     */
    @Log(title = "排班计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AtdPlan atdPlan)
    {
        return toAjax(atdPlanService.updateAtdPlan(atdPlan));
    }

    /**
     * 删除排班计划
     */
    @Log(title = "排班计划", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(atdPlanService.deleteAtdPlanByIds(ids));
    }

    /**
     * 处理排班计划
     */
    @PostMapping( "/process")
    @ResponseBody
    public AjaxResult processAtdPlan(AtdPlan atdPlan, ModelMap mmap) throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AtdPlan param = new AtdPlan();
        param.setUserId(atdPlan.getUserId());
        param.setPlanDate(atdPlan.getPlanDate());
        List<AtdPlan> oldAtdPlans = atdPlanService.selectAtdPlanList(param);
        int result = 0;
        User atdUser = userService.selectUserById(atdPlan.getUserId());
        atdPlan.setUserName(atdUser.getUserName());
        atdPlan.setDeptId(atdUser.getDept().getDeptId());
        atdPlan.setDeptName(atdUser.getDept().getDeptName());

        if (oldAtdPlans.size() > 0){
            //修改现有
            AtdPlan oldAtd = oldAtdPlans.get(0);
            atdPlan.setId(oldAtd.getId());
            String oldEnable = oldAtd.getEnable();
            String enable = atdPlan.getEnable();
            if (!enable.equals(oldEnable)){
                String oldRemark = oldAtd.getRemark();
                String oldEnableStr = getPlanEnableType(oldEnable);
                String enableStr = getPlanEnableType(enable);
                if (oldRemark != null && !"".equals(oldRemark)){
                    if (oldRemark.length()>=120){
                        oldRemark = oldRemark.substring(oldRemark.indexOf("；")+1);
                    }
                    atdPlan.setRemark(oldRemark+"；"+oldEnableStr+"变更为"+enableStr);
                }else{
                    atdPlan.setRemark(enableStr);
                }
                result = atdPlanService.updateAtdPlan(atdPlan);
            }
        } else {
            //新增
            if ("1".equals(atdPlan.getEnable()) || "2".equals(atdPlan.getEnable()) || "3".equals(atdPlan.getEnable())){
                atdPlan.setCreateBy(getLoginName());
                atdPlan.setCreateTime(new Date());
                String remark = getPlanEnableType(atdPlan.getEnable());
                atdPlan.setRemark(remark);
                result = atdPlanService.insertAtdPlan(atdPlan);
            }
        }
        //获取关联时间 主要为今明两天
        calendar.setTime(sdf.parse(atdPlan.getPlanDate()));
        //calendar.add(Calendar.DAY_OF_YEAR, -1); // 减去一天
        //Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // 加上两天
        Date tommorow = calendar.getTime();
        //昨日倒班今日未安排自动上班记录查询参数，原本不记录出勤，需按新计划变更
        AtdRecord find20Atd = new AtdRecord();
        find20Atd.setUserid(atdPlan.getUserId());
        find20Atd.setAtdtime(sdf.parse(atdPlan.getPlanDate()));
        find20Atd.setAtdtag("1");
        find20Atd.setAtddesc("倒班");
        find20Atd.setAtdstatus("0");
        List<AtdRecord> t20Atds = new ArrayList<>();
        if (!"0".equals(atdPlan.getEnable()) && !"3".equals(atdPlan.getEnable())){
            t20Atds = atdRecordService.selectAtdRecordList(find20Atd);
            if (t20Atds.size() > 0){
                AtdRecord t20Atd = t20Atds.get(0);
                AtdStatisticForm t20Asf = new AtdStatisticForm();
                t20Asf.setId(t20Atd.getId().longValue());
                t20Asf.setAtdstatus("3"); //计划变更标识
                atdStatisticFormService.updateAtdStatisticForm(t20Asf);
            }
        }
        //更改倒班和其他安排关联考勤记录
        if ("2".equals(atdPlan.getEnable())){
            //查看昨天、明天的计划安排
            AtdRecord findAtdParam = new AtdRecord();
            findAtdParam.setUserid(atdPlan.getUserId());
            findAtdParam.setAtdtime(sdf.parse(atdPlan.getPlanDate()));
            findAtdParam.setAtdtag("2");
            //今日考勤变更
            List<AtdRecord> todayAtds = atdRecordService.selectAtdRecordList(findAtdParam);
            if (todayAtds.size() > 0){
                //今天正常下班
                AtdRecord todayXiaban = todayAtds.get(0);
                if ("2".equals(todayXiaban.getAtdstatus())){
                    Integer todayXiabanId = todayXiaban.getId();
                    AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
                    atdStatisticForm.setId(todayXiabanId.longValue());
                    //atdStatisticForm.setAtddesckj("倒班");
                    atdStatisticForm.setAtddesc("倒班");
                    atdStatisticForm.setAtdstatus("3");
                    atdStatisticForm.setOperatename("计划变更");
                    atdStatisticForm.setOperatetime(sdf2.format(new Date()));
                    atdStatisticFormService.updateAtdStatisticForm(atdStatisticForm);
                }
            } else {
                AtdStatisticForm daobanAtd = new AtdStatisticForm();
                daobanAtd.setUserid(atdPlan.getUserId());
                daobanAtd.setAtdtime(sdf.parse(atdPlan.getPlanDate()));
                daobanAtd.setAtdtag("2");
                Date t2xiaban = sdf.parse(atdPlan.getPlanDate());
                t2xiaban.setHours(23);
                t2xiaban.setMinutes(59);
                t2xiaban.setSeconds(59);
                daobanAtd.setCreateTime(t2xiaban);
                daobanAtd.setKjcreateTime(t2xiaban);
                //daobanAtd.setAtdstatus("0");
                //daobanAtd.setAtddesckj("倒班");
                daobanAtd.setAtddesc("倒班");
                daobanAtd.setOperatename("计划变更");
                daobanAtd.setOperatetime(sdf2.format(new Date()));
                atdStatisticFormService.insertAtdStatisticForm(daobanAtd);
            }
            //明天上班
            findAtdParam.setUserid(atdPlan.getUserId());
            findAtdParam.setAtdtime(sdf.parse(sdf.format(tommorow)));
            findAtdParam.setAtdtag("1");
            List<AtdRecord> tommorowAtds = atdRecordService.selectAtdRecordList(findAtdParam);
            AtdPlan findTomPlan = new AtdPlan();
            findTomPlan.setPlanDate(sdf.format(tommorow));
            findTomPlan.setUserId(atdPlan.getUserId());
            List<AtdPlan> tomPlans = atdPlanService.selectAtdPlanList(findTomPlan);
            //明天是否已排班
            boolean hasPlaned = false;
            if (tomPlans.size() > 0){
                AtdPlan tomPlan = tomPlans.get(0);
                if (!"0".equals(tomPlan.getEnable())){
                    hasPlaned = true;
                }
            }
            if (tommorowAtds.size() > 0){
                AtdRecord tommorowShb = tommorowAtds.get(0);
                if ("1".equals(tommorowShb.getAtdstatus())){
                    Integer tommorowShbId = tommorowShb.getId();
                    AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
                    atdStatisticForm.setId(tommorowShbId.longValue());
                    //atdStatisticForm.setAtddesckj("倒班");
                    atdStatisticForm.setAtddesc("倒班");
                    atdStatisticForm.setAtdstatus("3");
                    if (!hasPlaned) atdStatisticForm.setAtdstatus("0");
                    atdStatisticForm.setOperatename("计划变更");
                    atdStatisticForm.setOperatetime(sdf2.format(new Date()));
                    atdStatisticFormService.updateAtdStatisticForm(atdStatisticForm);
                }
            } else {
                if (tommorow.compareTo(new Date()) <= 0){
                    AtdStatisticForm daobanAtd = new AtdStatisticForm();
                    daobanAtd.setUserid(atdPlan.getUserId());
                    daobanAtd.setAtdtime(sdf.parse(sdf.format(tommorow)));
                    daobanAtd.setAtdtag("1");
                    daobanAtd.setKjcreateTime(sdf.parse(sdf.format(tommorow)));
                    daobanAtd.setAtdstatus("3");
                    if (!hasPlaned) daobanAtd.setAtdstatus("0");
                    //daobanAtd.setAtddesckj("倒班");
                    daobanAtd.setAtddesc("倒班");
                    daobanAtd.setOperatename("计划变更");
                    daobanAtd.setOperatetime(sdf2.format(new Date()));
                    atdStatisticFormService.insertAtdStatisticForm(daobanAtd);
                }
            }
            findAtdParam.setAtdtag("2");
            findAtdParam.setAtdstatus("2");
            List<AtdRecord> tommorowAtds2 = atdRecordService.selectAtdRecordList(findAtdParam);
            if (tommorowAtds2.size() > 0){
                if (!hasPlaned) {
                    AtdRecord tommorowXb = tommorowAtds2.get(0);
                    Date midTime = new Date();
                    midTime.setTime(tommorow.getTime());
                    midTime.setHours(8);
                    midTime.setMinutes(30);
                    if (tommorowXb.getCreateTime().compareTo(midTime) >= 0){
                        AtdStatisticForm updateXb = new AtdStatisticForm();
                        updateXb.setId(tommorowXb.getId().longValue());
                        updateXb.setAtddesc("倒班");
                        updateXb.setAtdstatus("3");
                        updateXb.setOperatename("计划变更");
                        updateXb.setOperatetime(sdf2.format(new Date()));
                        atdStatisticFormService.updateAtdStatisticForm(updateXb);
                    }
                }
            }
        } else if ("3".equals(atdPlan.getEnable())){
            AtdRecord findAtdParam = new AtdRecord();
            findAtdParam.setUserid(atdPlan.getUserId());
            findAtdParam.setAtdtime(sdf.parse(atdPlan.getPlanDate()));
            List<AtdRecord> todayAtds = atdRecordService.selectAtdRecordList(findAtdParam);
            for (int i=0; i<todayAtds.size(); i++){
                AtdRecord todayAtd = todayAtds.get(i);
                if ("2".equals(todayAtd.getAtdstatus()) || "1".equals(todayAtd.getAtdstatus())){
                    Integer todayAtdId = todayAtd.getId();
                    AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
                    atdStatisticForm.setId(todayAtdId.longValue());
                    //atdStatisticForm.setAtddesckj("其他安排");
                    atdStatisticForm.setAtddesc("其他安排");
                    atdStatisticForm.setAtdstatus("3");
                    atdStatisticForm.setOperatename("计划变更");
                    atdStatisticForm.setOperatetime(sdf2.format(new Date()));
                    atdStatisticFormService.updateAtdStatisticForm(atdStatisticForm);
                }
            }
        } else {
            //变为未安排或行政班，同时有自动生成下班记录，则删除该记录
            find20Atd.setAtdstatus(null);
            find20Atd.setAtdtag("2");
            find20Atd.setAtddesc("倒班");
            t20Atds = atdRecordService.selectAtdRecordList(find20Atd);
            if (t20Atds.size() > 0){
                atdRecordService.deleteAtdRecordByAtd(t20Atds.get(0));
            }
        }

        //自动生成后续安排 √
        Date today = sdf.parse(sdf.format(new Date()));
        Date planDate = sdf.parse(atdPlan.getPlanDate());
        if (atdPlan.getAutoGenerate() && planDate.compareTo(today) >= 0){
            //长白查询节假日
            List<String> xiubans = new ArrayList<>();
            List<String> shangbans = new ArrayList<>();
            if ("1".equals(atdPlan.getEnable())){
                AtdHoliday holiday = new AtdHoliday();
                holiday.setYear(atdPlan.getPlanDate().split("-")[0]);
                holiday.setType("xiuban");
                xiubans = atdHolidayService.selectAtdHolidayStringList(holiday);
                holiday.setType("3");
                shangbans = atdHolidayService.selectAtdHolidayStringList(holiday);
            }
            //删除之前排班
            calendar.setTime(sdf.parse(atdPlan.getPlanDate()));
            if (sdf.parse(atdPlan.getEndDate()).compareTo(calendar.getTime())<=0 || atdPlan.getEndDate() == null || "".equals(atdPlan.getEndDate())) return AjaxResult.success(result);
            List<String> timeList = DateUtils.getBetweenDate(atdPlan.getPlanDate(), atdPlan.getEndDate(), "yyyy-MM-dd");
            List<String> behindTimeList = timeList.subList(1, timeList.size());
           if ("1".equals(atdPlan.getEnable()) || "2".equals(atdPlan.getEnable()) || "0".equals(atdPlan.getEnable())){
               atdPlan.setDateList(behindTimeList);
               atdPlanService.deleteAtdPlanByUserDates(atdPlan);
           }
            //批量新增后续排班
            if ("1".equals(atdPlan.getEnable()) || "2".equals(atdPlan.getEnable())){
                List<AtdPlan> newAtdPlanList = new ArrayList<>();
                for (int i=0; i<timeList.size(); i++){
                    AtdPlan newAtdPlan = new AtdPlan();
                    String dateStr = timeList.get(i);
                    newAtdPlan.setPlanDate(dateStr);
                    newAtdPlan.setUserId(atdPlan.getUserId());
                    newAtdPlan.setUserName(atdPlan.getUserName());
                    newAtdPlan.setDeptId(atdPlan.getDeptId());
                    newAtdPlan.setDeptName(atdPlan.getDeptName());
                    newAtdPlan.setCreateBy(getLoginName());
                    newAtdPlan.setCreateTime(new Date());
                    if (i == 0) continue; //当日已排班
                    //倒班上二休一即三天排一次
                    if ("2".equals(atdPlan.getEnable())){
                        if (i%3==0){
                            newAtdPlan.setEnable("2");
                            newAtdPlan.setRemark(getPlanEnableType("2"));
                            newAtdPlanList.add(newAtdPlan);
                        } else {
                            continue;
                        }
                    }
                    //长白安排非休班非周末
                    if ("1".equals(atdPlan.getEnable())){
                        if (shangbans.contains(dateStr)){
                            newAtdPlan.setEnable("1");
                            newAtdPlan.setRemark(getPlanEnableType("1"));
                            newAtdPlanList.add(newAtdPlan);
                        } else {
                            Date thisDate = sdf.parse(dateStr);
                            calendar.setTime(thisDate);
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            if (!(xiubans.contains(dateStr) || dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)){
                                newAtdPlan.setEnable("1");
                                newAtdPlan.setRemark(getPlanEnableType("1"));
                                newAtdPlanList.add(newAtdPlan);
                            }
                        }
                    }
                }
                if (newAtdPlanList.size() > 0){
                    atdPlanService.batchInsertAtdPlan(newAtdPlanList);
                }
            }
        }

        return AjaxResult.success(result);
    }

    String getPlanEnableType(String enable){
        String result = "未安排";
        if("0".equals(enable)){
            result = "未安排";
        }else if("1".equals(enable)){
            result = "行政班";
        }else if("2".equals(enable)){
            result = "倒班";
        }else if("3".equals(enable)){
            result = "其他安排";
        }
        return result;
    }
}
