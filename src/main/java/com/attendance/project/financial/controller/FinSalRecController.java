package com.attendance.project.financial.controller;

import com.attendance.common.constant.Constants;
import com.attendance.common.exception.UtilException;
import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.StringUtils;
import com.attendance.common.utils.file.FileUploadUtils;
import com.attendance.common.utils.file.FileUtils;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.config.AttendanceConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.atdform.atdstatisticform.domain.AtdPlan;
import com.attendance.project.atdform.atdstatisticform.domain.AtdStatisticForm;
import com.attendance.project.atdform.atdstatisticform.service.IAtdPlanService;
import com.attendance.project.atdform.atdstatisticform.service.IAtdStatisticFormService;
import com.attendance.project.financial.domain.*;
import com.attendance.project.financial.service.*;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工资记录Controller
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
@Controller
@RequestMapping("/financial/record")
public class FinSalRecController extends BaseController
{
    private String prefix = "financial/record";

    @Autowired
    private IFinSalRecService finSalRecService;

    @Autowired
    private IFlowLineService flowLineService;

    @Autowired
    private IFinParamService finParamService;

    @Autowired
    private IFinOvertimeService finOvertimeService;

    @Autowired
    private IFinKpiService finKpiService;

    @Autowired
    private IFinDeductService finDeductService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IAtdStatisticFormService atdStatisticFormService;

    @Autowired
    private IAtdPlanService atdPlanService;

    private Workbook wb;

    @RequiresPermissions("fin:rec:view")
    @GetMapping()
    public String rec()
    {
        return prefix + "/record";
    }

    /**
     * 查询工资记录列表
     */
    @RequiresPermissions("fin:rec:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinSalRec finSalRec)
    {
        startPage();
        List<FinSalRec> list = finSalRecService.selectFinSalRecList(finSalRec);
        return getDataTable(list);
    }

    /**
     * 导出工资记录列表
     */
    //@RequiresPermissions("fin:rec:export")
    @Log(title = "工资记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export/{id}")
    @ResponseBody
    public AjaxResult export(@PathVariable("id") Long id, FinSalRec finSalRec)
    {
        finSalRec.setSalid(id);
        List<FinSalRec> list = finSalRecService.selectFinSalRecList(finSalRec);
        ExcelUtil<FinSalRec> util = new ExcelUtil<FinSalRec>(FinSalRec.class);
        return util.exportExcel(list, "工资记录数据");
    }

    /**
     * 新增工资记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存工资记录
     */
    @RequiresPermissions("fin:rec:add")
    @Log(title = "工资记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinSalRec finSalRec)
    {
        return toAjax(finSalRecService.insertFinSalRec(finSalRec));
    }

    /**
     * 修改工资记录
     */
    @RequiresPermissions("fin:rec:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinSalRec finSalRec = finSalRecService.selectFinSalRecById(id);
        mmap.put("finSalRec", finSalRec);
        return prefix + "/edit";
    }

    /**
     * 修改保存工资记录
     */
    @RequiresPermissions("fin:rec:edit")
    @Log(title = "工资记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinSalRec finSalRec)
    {
        return toAjax(finSalRecService.updateFinSalRec(finSalRec));
    }

    /**
     * 删除工资记录
     */
    @RequiresPermissions("fin:rec:remove")
    @Log(title = "工资记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(finSalRecService.deleteFinSalRecByIds(ids));
    }


    /**
     * 查询工资记录列表
     */
    @RequiresPermissions("fin:rec:list")
    @PostMapping("/salList")
    @ResponseBody
    public TableDataInfo salList(FinSal finSal)
    {
        startPage();
        finSal.setCreateBy(getLoginName());
        List<FinSal> list = finSalRecService.selectFinSalList(finSal);
        return getDataTable(list);
    }

    /**
     * 新增保存工资记录
     */
    @RequiresPermissions("fin:rec:add")
    @Log(title = "工资记录", businessType = BusinessType.INSERT)
    @PostMapping("/addSal")
    @ResponseBody
    public AjaxResult addSalSave(FinSal finSal) throws Exception {
        AjaxResult result = new AjaxResult();
        String timetag = finSal.getTimetag();
        finSal.setCreateBy(getLoginName());
        finSal.setCreateTime(new Date());
        finSal.setSalname(timetag + "工资");
        Long deptId = getSysUser().getDept().getParentId();
        Dept userDept = deptService.selectDeptById(deptId);
        finSal.setDeptId(deptId);
        if (userDept != null){
            finSal.setDeptName(userDept.getDeptName());
        }
        Integer count = finSalRecService.insertFinSal(finSal);

        User userParam = new User();
        userParam.setDeptId(deptId);
        userParam.setNumber("order");
        List<User> userList = userService.selectUserList(userParam);

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date endTmp = df.parse(timetag);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTmp);
        //前一个月的考勤
        calendar.add(Calendar.MONTH, -2);
        Date startTmp = calendar.getTime();
        String startDate = df.format(startTmp);
        calendar.add(Calendar.MONTH, 1);
        endTmp = calendar.getTime();
        String endDate = df.format(endTmp);
        AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
        atdStatisticForm.setStarttime(startDate+"-21");
        atdStatisticForm.setEndtime(endDate+"-20");

        List<AtdStatisticForm> dataList = atdStatisticFormService.selectAtdStatisticFormList(atdStatisticForm);
        //1 形成时间列表
        List<String> timeList = DateUtils.getBetweenDate(startDate+"-21", endDate+"-20", "yyyy-MM-dd");

        //获取加班审核通过申请
        FinOvertime overtime = new FinOvertime();
        Map<String, Object> overtimeParams = new HashMap<String, Object>();
        overtimeParams.put("beginTime", startDate+"-01");
        overtimeParams.put("endTime", endDate+"-20");
        overtime.setParams(overtimeParams);
        overtime.setDeptid(deptId);
        overtime.setRecname("合并");
        overtime.setOpstatus("1");
        List<FinOvertime> overtimes = finOvertimeService.selectFinOvertimeList(overtime);

        //获取基本公共配置
        FinParam finParam = new FinParam();
        finParam.setDeptid(deptId);
        List<FinParam> finParams = finParamService.selectFinParamList(finParam);
        String gzxs = "";
        String yxqxed = "";
        String gdjj = "";
        String wybt = "";
        if (finParams.size() > 0) {
            finParam = finParams.get(0);
            //获取配置参数
            FinParamDetail finParamDetail = new FinParamDetail();
            finParamDetail.setPid(finParam.getId());
            //工资系数
            finParamDetail.setDetailCode("gzxs");
            gzxs = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
            //一线倾斜额度
            finParamDetail.setDetailCode("yxqxed");
            yxqxed = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
            //固定奖金
            finParamDetail.setDetailCode("gdjj");
            gdjj = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
            //物业补贴
            finParamDetail.setDetailCode("wybt");
            wybt = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
        }

        List<FinSalRec> salRecList = new ArrayList<FinSalRec>();
        //for (String name : names)
        for (User user : userList) {
            String name = user.getUserName();

            int hschidaoTotal = 0;
            int hszaotuiTotal = 0;
            int hsatdTotal = 0;
            for (String time : timeList) {
               if (atdStatisticFormService.judgeUserHschidaoByAtdList(dataList, name, time)){
                   hschidaoTotal += 1;
               }
               if (atdStatisticFormService.judgeUserHszaotuiByAtdList(dataList, name, time)){
                   hszaotuiTotal += 1;
               }
               if (atdStatisticFormService.judgeUserHsatdByAtdList(dataList, name, time)){
                   hsatdTotal += 1;
               }
            }

            FinSalRec finSalRec = new FinSalRec();
            finSalRec.setSalid(finSal.getId());
            finSalRec.setUserid(user.getUserId());
            finSalRec.setDeptid(user.getDeptId());
            finSalRec.setDeptName(user.getDept().getDeptName());
            finSalRec.setUserName(user.getUserName());
            finSalRec.setIdcard(user.getIdcard());
            finSalRec.setTimetag(timetag);
            finSalRec.setCreateBy(getLoginName());
            finSalRec.setCreateTime(new Date());
            finSalRec.setGzxs(gzxs);

            //考勤绩效
            finSalRec.setKqcd(String.valueOf(hschidaoTotal));
            finSalRec.setKqzt(String.valueOf(hszaotuiTotal));
            //构建考勤计划查询条件并查询指定用户在指定日期范围内的考勤计划数量
            AtdPlan findAtdPlan = new AtdPlan();
            findAtdPlan.setStartDate(startDate + "-21");
            findAtdPlan.setEndDate(endDate + "-20");
            findAtdPlan.setUserId(user.getUserId());
            int yingchuqin = atdPlanService.selectAtdPlanCount(findAtdPlan);

            int kqCount = hschidaoTotal + hszaotuiTotal;
            kqCount += (yingchuqin - hsatdTotal);
            String kqdj = "A";
            if (kqCount >= 6 && kqCount <= 10) kqdj = "B";
            if (kqCount > 10) kqdj = "C";
            finSalRec.setKqdj(kqdj);

            //公共基本参数
            finSalRec.setYxqxed(yxqxed);
            finSalRec.setGdjj(gdjj);
            finSalRec.setWybt(wybt);

            //绩效
            FinKpi kpi = new FinKpi();
            kpi.setDeptid(deptId);
            kpi.setRectime(endDate);
            kpi.setRecname("合并");
            kpi.setOpstatus("1");
            List<FinKpi> kpis = finKpiService.selectFinKpiList(kpi);

            //扣除
            FinDeduct deduct = new FinDeduct();
            deduct.setDeptid(deptId);
            //deduct.setRectime(timetag);
            List<FinDeduct> deducts = finDeductService.selectFinDeductList(deduct);

            //配置参数，加班天数, 计算工资
            finSalRec = finSalRecService.calcUserFinSalRec(user, finSalRec, finParams, overtimes, kpis, deducts);
            salRecList.add(finSalRec);
        }
        //计算总计
        salRecList = finSalRecService.calcFinSalOverview(finSal, salRecList);
        //统计内容
        int recSize = salRecList.size();
        String salContent = "应发合计 " + salRecList.get(recSize-3).getYfhj() + " 元，实发合计 " + salRecList.get(recSize-3).getSfgz() + " 元\n"
                + "建设公司：应发合计 " + salRecList.get(recSize-2).getYfhj() + " 元，实发合计 " + salRecList.get(recSize-2).getSfgz() + " 元\n"
                + "信息公司：应发合计 " + salRecList.get(recSize-1).getYfhj() + " 元，实发合计 " + salRecList.get(recSize-1).getSfgz() + " 元\n";
        FinSal updateFinSal = new FinSal();
        updateFinSal.setId(finSal.getId());
        updateFinSal.setContent(salContent);
        finSalRecService.updateFinSal(updateFinSal);

        count = finSalRecService.batchInsertFinSalRec(salRecList);
        return AjaxResult.success(count);
    }

    @PostMapping( "/updateOverview/{salid}")
    @ResponseBody
    public AjaxResult updateOverview(@PathVariable("salid") Long salid)
    {
        FinSal finSal = finSalRecService.selectFinSalById(salid);
        //删除旧统计
        FinSalRec deleteRec = new FinSalRec();
        deleteRec.setSalid(finSal.getId());
        finSalRecService.deleteFinSalRecOverview(deleteRec);
        //查询个人
        FinSalRec param = new FinSalRec();
        param.setSalid(salid);
        List<FinSalRec> finSalRecs = finSalRecService.selectFinSalRecList(param);
        //计算总计
        finSalRecs = finSalRecService.calcFinSalOverview(finSal, finSalRecs);
        //统计内容
        int recSize = finSalRecs.size();
        String salContent = "应发合计 " + finSalRecs.get(recSize-3).getYfhj() + " 元，实发合计 " + finSalRecs.get(recSize-3).getSfgz() + " 元\n"
                + "建设公司：应发合计 " + finSalRecs.get(recSize-2).getYfhj() + " 元，实发合计 " + finSalRecs.get(recSize-2).getSfgz() + " 元\n"
                + "信息公司：应发合计 " + finSalRecs.get(recSize-1).getYfhj() + " 元，实发合计 " + finSalRecs.get(recSize-1).getSfgz() + " 元\n";
        FinSal updateFinSal = new FinSal();
        updateFinSal.setId(finSal.getId());
        updateFinSal.setContent(salContent);
        finSalRecService.updateFinSal(updateFinSal);

        finSalRecs = finSalRecs.subList(finSalRecs.size()-3, finSalRecs.size());
        int count = finSalRecService.batchInsertFinSalRec(finSalRecs);
        return toAjax(count);
    }

    @RequiresPermissions("fin:rec:remove")
    @Log(title = "工资记录", businessType = BusinessType.DELETE)
    @PostMapping( "/removeSal")
    @ResponseBody
    public AjaxResult removeSal(String ids)
    {
        String[] idArr = ids.split(",");
        for (int i=0; i<idArr.length; i++){
            Long id = Long.valueOf(idArr[i]);
            flowLineService.deleteRecLineByRecId(id);
        }
        finSalRecService.deleteFinSalRecBySalids(ids);
        return toAjax(finSalRecService.deleteFinSalByIds(ids));
    }

    @RequiresPermissions("fin:rec:edit")
    @GetMapping("/rec2/{id}")
    public String rec2(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("salid", id);
        mmap.put("hasFile", false);
        FinSal sal = finSalRecService.selectFinSalById(id);
        String createBy = sal.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        sal.setCreateBy(createUser.getUserName());
        if (sal.getFilePath() != null && !sal.getFilePath().trim().equals("")){
            mmap.put("hasFile", true);
        }
        if (sal.getContent() != null && !sal.getContent().trim().equals("")){
            mmap.put("content", sal.getContent());
        }
        mmap.put("sal", sal);

        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(sal.getId());
        recLine.setCreateBy(createBy);
        //List<FlowLine> flowLines = flowLineService.selectFlowLineListByPostAndBusiness(busLine, createUser);
        AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
        mmap.put("recLines", operateInfo.get("recLines"));
        mmap.put("canOperate", operateInfo.get("canOperate"));
        mmap.put("canBack", operateInfo.get("canBack"));
        mmap.put("hasFinal", operateInfo.get("hasFinal"));
        mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        return prefix + "/record2";
    }

    // 上传方法
    @PostMapping("/uploadFile/{salid}")
    @ResponseBody
    public AjaxResult uploadFile(@PathVariable("salid") Long salid, MultipartFile file, boolean updateSupport) throws Exception
    {
        FinSal sal = finSalRecService.selectFinSalById(salid);
        //文件解析
        InputStream is = file.getInputStream();//获取文件的流;
        HSSFWorkbook sheets = new HSSFWorkbook(is);
        is.close();
        sal = finSalRecService.getFinSalOverview(sal, sheets);

        // 上传文件路径
        String filePath = AttendanceConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        sal.setFilePath(fileName);

        int count = 0;
        count = finSalRecService.updateFinSal(sal);

        return AjaxResult.success(count);
    }

    @GetMapping("/downloadFile/{salid}")
    public void downloadFile(@PathVariable("salid") Long salid, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        FinSal sal = finSalRecService.selectFinSalById(salid);
        // 上传文件路径
        String filePath = sal.getFilePath();
        String localPath = AttendanceConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

    @RequiresPermissions("fin:rec:edit")
    @GetMapping("/forward/{id}")
    public String forward(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinSalRec finSalRec = new FinSalRec();
        finSalRec.setSalid(id);
        List<FinSalRec> finSalRecs = finSalRecService.selectForwardFinItems(finSalRec);
        mmap.put("finSalRecs", finSalRecs);
        mmap.put("salid", id);
        return prefix + "/edit";
    }

    @PostMapping("/exportForward/{id}")
    @ResponseBody
    public AjaxResult exportForward(@PathVariable("id") Long id, FinSalRec finSalRec)
    {
        OutputStream out = null;
        ModelMap mmap = new ModelMap();
        try {
            //创建Excel文件
            wb = new XSSFWorkbook();
            finSalRec.setSalid(id);
            List<FinSalRec> finSalRecs = finSalRecService.selectForwardFinItems(finSalRec);
            mmap.put("finSalRecs", finSalRecs);
            finSalRecService.writeForwardExcel(this.wb, mmap);
            String filename = encodingFilename("结转", 1);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            // log.error("导出Excel异常{}", e.getMessage());
            throw new UtilException("导出Excel失败，请联系网站管理员！");
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(out);
        }
    }

    public String encodingFilename(String filename, int type) {
        if (type == 2) {
            filename = UUID.randomUUID().toString() + "_" + filename + ".docx";
        } else {
            filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        }
        return filename;
    }

    public String getAbsoluteFile(String filename) {
        String downloadPath = AttendanceConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    @PostMapping("/exportWord/{id}")
    @ResponseBody
    public AjaxResult exportWord(@PathVariable("id") Long id, FinSal finSal)
    {
        OutputStream out = null;
        ModelMap mmap = new ModelMap();
        try {
            finSal = finSalRecService.selectFinSalById(id);
            String createBy = finSal.getCreateBy();
            User createUser = userService.selectUserByLoginName(createBy);
            finSal.setCreateBy(createUser.getUserName());
            mmap.put("finSal", finSal);
            //获取审核相关操作信息
            RecLine recLine = new RecLine();
            recLine.setRecId(finSal.getId());
            recLine.setCreateBy(createBy);
            User curUser = getSysUser();
            if (curUser != null){
                AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
                mmap.put("recLines", operateInfo.get("recLines"));
                mmap.put("conRecLines", operateInfo.get("conRecLines"));
                mmap.put("flowNodeArr", operateInfo.get("flowNodeArr"));
            }
            String filename = encodingFilename("清单", 2);
            out = new FileOutputStream(getAbsoluteFile(filename));
            finSalRecService.writeFinSalWord(out, mmap);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            // log.error("导出Word异常{}", e.getMessage());
            e.printStackTrace();
            throw new UtilException("导出Word失败，请联系网站管理员！");
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(out);
        }
    }
}
