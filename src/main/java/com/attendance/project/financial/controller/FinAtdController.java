package com.attendance.project.financial.controller;

import com.attendance.common.utils.uuid.Seq;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.FinAtd;
import com.attendance.project.financial.service.IFinAtdService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 考勤Controller
 * 
 * @author ruoyi
 * @date 2024-12-02
 */
@Controller
@RequestMapping("/financial/atd")
public class FinAtdController extends BaseController
{
    private String prefix = "financial/atd";

    @Autowired
    private IFinAtdService finAtdService;

    @Autowired
    private IFlowLineService flowLineService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @GetMapping()
    public String atd()
    {
        return prefix + "/atd";
    }

    /**
     * 查询考勤列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinAtd finAtd)
    {
        startPage();
        List<FinAtd> list = finAtdService.selectFinAtdList(finAtd);
        return getDataTable(list);
    }

    /**
     * 新增保存考勤记录
     */
    @Log(title = "考勤记录", businessType = BusinessType.INSERT)
    @PostMapping("/addAtd")
    @ResponseBody
    public AjaxResult addAtdSave(FinAtd finAtd) throws Exception {
        AjaxResult result = new AjaxResult();
        String rectime = finAtd.getRectime();
        finAtd.setCreateBy(getLoginName());
        finAtd.setCreateTime(new Date());
        finAtd.setRecname(rectime + "考勤统计");
        Long deptId = getSysUser().getDept().getParentId();
        Dept userDept = deptService.selectDeptById(deptId);

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date endTmp = df.parse(rectime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTmp);
        //前一个月的考勤
        calendar.add(Calendar.MONTH, -1);
        Date startTmp = calendar.getTime();
        String startDate = df.format(startTmp);
        finAtd.setStartdate(startDate+"-21");
        finAtd.setEnddate(rectime+"-20");
        String seqId = Seq.getBusId();
        finAtd.setId(Long.valueOf(seqId));

        finAtd.setDeptid(deptId);
        if (userDept != null){
            finAtd.setDeptName(userDept.getDeptName());
        }
        Integer count = finAtdService.insertFinAtd(finAtd);
        return AjaxResult.success(count);
    }

    /**
     * 修改考勤
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinAtd finAtd = finAtdService.selectFinAtdById(id);
        String createBy = finAtd.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finAtd.setCreateBy(createUser.getUserName());
        mmap.put("finAtd", finAtd);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finAtd.getId());
        recLine.setCreateBy(createBy);
        AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
        mmap.put("recLines", operateInfo.get("recLines"));
        mmap.put("canOperate", operateInfo.get("canOperate"));
        mmap.put("canBack", operateInfo.get("canBack"));
        mmap.put("hasFinal", operateInfo.get("hasFinal"));
        return prefix + "/edit";
    }

    /**
     * 删除考勤
     */
    @Log(title = "考勤", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] idArr = ids.split(",");
        for (int i=0; i<idArr.length; i++){
            Long id = Long.valueOf(idArr[i]);
            flowLineService.deleteRecLineByRecId(id);
        }
        return toAjax(finAtdService.deleteFinAtdByIds(ids));
    }


}
