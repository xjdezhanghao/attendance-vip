package com.attendance.project.financial.controller;

import com.attendance.common.utils.PushWxWorkMes;
import com.attendance.common.utils.text.Convert;
import com.attendance.framework.shiro.token.CustomToken;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.project.financial.domain.*;
import com.attendance.project.financial.service.IFinAtdService;
import com.attendance.project.financial.service.IFinKpiService;
import com.attendance.project.financial.service.IFinOvertimeService;
import com.attendance.project.financial.service.IFinSalRecService;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.BusLine;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端Controller
 *
 * @author ruoyi
 * @date 2024-12-02
 */
@Controller
@RequestMapping("/financial/mobile")
public class FinMobileController extends BaseController
{
    private String prefix = "financial/mobile";

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    /**
     * 工作薄对象
     */
    private Workbook wb;
    private String sheetName;
    @Autowired
    private IFinOvertimeService finOvertimeService;
    @Autowired
    private IFinKpiService finKpiService;
    @Autowired
    private IFinAtdService finAtdService;
    @Autowired
    private IFlowLineService flowLineService;
    @Autowired
    private PushWxWorkMes pushWxWorkMes;
    @Autowired
    private IFinSalRecService finSalRecService;

    @GetMapping()
    public String mobile()
    {
        return prefix + "/mobile";
    }

    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    @ResponseBody
    @CrossOrigin
    public AjaxResult getUserInfo(@Param("code") String code) throws Exception
    {

        AjaxResult result = new AjaxResult();
        String wxUserName = pushWxWorkMes.getUserInfoByCode(code);
        User sysUser = getSysUser();
        if (sysUser == null && wxUserName != null){
            String loginName = pushWxWorkMes.getLoginNameByTouser(wxUserName);
            //单点登录
            sysUser = userService.selectUserByLoginName(loginName);
            CustomToken token = new CustomToken(sysUser.getLoginName());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }
        result.put("userInfo", sysUser);
        return result;
    }

    @GetMapping("/overtime/edit/{id}")
    public String overtimeEdit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinOvertime finOvertime = finOvertimeService.selectFinOvertimeById(id);
        String createBy = finOvertime.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finOvertime.setCreateBy(createUser.getUserName());
        FinOvertimeDetail param = new FinOvertimeDetail();
        param.setOid(finOvertime.getId());
        Map<String, String> textMap = new HashMap<String, String>();
        String userNameText = "";
        String contentText = "";
        if ("1".equals(finOvertime.getRectype())){
            List<FinOvertimeDetail> multipleDetails = new ArrayList<FinOvertimeDetail>();
            List<FinOvertimeDetail> singleDetails = new ArrayList<FinOvertimeDetail>();
            if (getSysUser() != null && !getLoginName().equals(createBy) || finOvertime.getRecname().indexOf("合并") >= 0){
                param.setOvertype("three");
                multipleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                param.setOvertype("two");
                singleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", true);
            }else{
                param.setOvertype("three");
                multipleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                param.setOvertype("two");
                singleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", false);
            }
            //生成汇总
            textMap = getOvertimeOverview(multipleDetails, "three");
            userNameText += "法定节假日\n" + textMap.get("userNameText");
            contentText += "法定节假日\n" + textMap.get("contentText");
            textMap = getOvertimeOverview(singleDetails, "two");
            if (userNameText.length() > 0)  userNameText += "\n";
            userNameText += "非法定节假日\n" + textMap.get("userNameText");
            if (contentText.length() > 0)  contentText += "\n";
            contentText += "非法定节假日\n" + textMap.get("contentText");
        } else if ("2".equals(finOvertime.getRectype())){
            param.setOvertype("night");
            List<FinOvertimeDetail> nightDetails = finOvertimeService.selectFinOvertimeDetailList(param);
            mmap.put("night", nightDetails);
            textMap = getOvertimeOverview(nightDetails, "night");
            userNameText += textMap.get("userNameText");
            contentText += textMap.get("contentText");
        }
        finOvertime.setUpdateBy(userNameText);
        finOvertime.setRemark(contentText);
        mmap.put("finOvertime", finOvertime);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finOvertime.getId());
        recLine.setCreateBy(createBy);
        BusLine busLine = new BusLine();
        busLine.setBusName("overtime");
        //生成备注内容
        //List<FlowLine> flowLines = flowLineService.selectFlowLineListByPostAndBusiness(busLine, createUser);
        User curUser = getSysUser();
        if (curUser != null){
            AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
            mmap.put("recLines", operateInfo.get("recLines"));
            mmap.put("conRecLines", operateInfo.get("conRecLines"));
            mmap.put("canOperate", operateInfo.get("canOperate"));
            mmap.put("canBack", operateInfo.get("canBack"));
            mmap.put("hasFinal", operateInfo.get("hasFinal"));
            mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        }
        if (finOvertime.getRecname().indexOf("合并") >= 0) mmap.put("canEdit", false);
        String pagePath = prefix + "/overtime";
        if ("2".equals(finOvertime.getRectype())) pagePath = prefix + "/shifts";
        return pagePath;
    }

    @GetMapping("/kpi/edit/{id}")
    public String kpiEdit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinKpi finKpi = finKpiService.selectFinKpiById(id);
        String createBy = finKpi.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finKpi.setCreateBy(createUser.getUserName());
        FinKpiDetail param = new FinKpiDetail();
        param.setPid(finKpi.getId());
        List<FinKpiDetail> kpiDetails = finKpiService.selectFinKpiDetailList(param);
        Map<String, String> textMap = getKpiOverview(kpiDetails);
        finKpi.setUpdateBy(textMap.get("jiangli"));
        finKpi.setRemark(textMap.get("koufa"));
        mmap.put("finKpi", finKpi);
        mmap.put("details", kpiDetails);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finKpi.getId());
        recLine.setCreateBy(createBy);
        User curUser = getSysUser();
        if (curUser != null){
            AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
            mmap.put("recLines", operateInfo.get("recLines"));
            mmap.put("conRecLines", operateInfo.get("conRecLines"));
            mmap.put("canOperate", operateInfo.get("canOperate"));
            mmap.put("canBack", operateInfo.get("canBack"));
            mmap.put("hasFinal", operateInfo.get("hasFinal"));
            mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        }
        return prefix + "/kpi";
    }

    @GetMapping("/atd/edit/{id}")
    public String attendanceEdit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinAtd finAtd = finAtdService.selectFinAtdById(id);
        String createBy = finAtd.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finAtd.setCreateBy(createUser.getUserName());
        Map<String, String> textMap = getAtdOverview(finAtd);
        finAtd.setRectype(textMap.get("remark"));
        mmap.put("finAtd", finAtd);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finAtd.getId());
        recLine.setCreateBy(createBy);
        User curUser = getSysUser();
        if (curUser != null){
            AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
            mmap.put("recLines", operateInfo.get("recLines"));
            mmap.put("conRecLines", operateInfo.get("conRecLines"));
            mmap.put("canOperate", operateInfo.get("canOperate"));
            mmap.put("canBack", operateInfo.get("canBack"));
            mmap.put("hasFinal", operateInfo.get("hasFinal"));
            mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        }
        return prefix + "/atd";
    }

    @GetMapping("/record/rec2/{id}")
    public String finSalEdit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinSal finSal = finSalRecService.selectFinSalById(id);
        String createBy = finSal.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finSal.setCreateBy(createUser.getUserName());
        mmap.put("sal", finSal);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finSal.getId());
        recLine.setCreateBy(createBy);
        User curUser = getSysUser();
        if (curUser != null){
            AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
            mmap.put("recLines", operateInfo.get("recLines"));
            mmap.put("conRecLines", operateInfo.get("conRecLines"));
            mmap.put("canOperate", operateInfo.get("canOperate"));
            mmap.put("canBack", operateInfo.get("canBack"));
            mmap.put("hasFinal", operateInfo.get("hasFinal"));
            mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        }
        return prefix + "/sal";
    }

    Map<String, String> getOvertimeOverview(List<FinOvertimeDetail> details, String type){
        Map<String, String> resultMap = new HashMap<String, String>();
        String userNameText = "";
        String contentText = "";
        for (int i=0; i<details.size(); i++){
            FinOvertimeDetail detail = details.get(i);
            if (userNameText.indexOf(detail.getDeptName() + "：")<0){
                if (i==0){
                    userNameText += detail.getDeptName() + "：";
                } else {
                    userNameText += "\n" + detail.getDeptName() + "：";
                }
            }

            if (userNameText.indexOf(detail.getUserName() + " - ")<0){
                userNameText += detail.getUserName() + " - ";
            }
            if ("three".equals(type) || "two".equals(type)){
                userNameText += detail.getOvertime() + "；";
            }
            if ("night".equals(type)){
                userNameText += detail.getOverdays() + "天；";
            }

            if (contentText.indexOf(detail.getDeptName() + "：")<0){
                if (i==0){
                    contentText += detail.getDeptName() + "：";
                } else {
                    contentText += "\n" + detail.getDeptName() + "：";
                }
            }
            if (contentText.indexOf(detail.getContent())<0){
                contentText += detail.getContent() + "；";
            }

        }
        resultMap.put("userNameText", userNameText);
        resultMap.put("contentText", contentText);
        return resultMap;
    }

    Map<String, String> getKpiOverview(List<FinKpiDetail> details){
        Map<String, String> resultMap = new HashMap<String, String>();
        Double jiangli = 0.0;
        Double koufa = 0.0;
        for (int i=0; i<details.size(); i++){
            FinKpiDetail detail = details.get(i);
            String jl = detail.getJljx();
            String kf = detail.getKfjx();
            jiangli += Convert.toDouble(jl, 0.0, 2);
            koufa += Convert.toDouble(kf, 0.0, 2);
        }
        resultMap.put("jiangli", String.valueOf(Convert.simpDouble(jiangli, 2)));
        resultMap.put("koufa", String.valueOf(Convert.simpDouble(koufa, 2)));
        return resultMap;
    }

    Map<String, String> getAtdOverview(FinAtd finAtd){
        Map<String, String> resultMap = new HashMap<String, String>();
        String rectype = finAtd.getRectype();
        String newRectype = "";
        if (rectype != null && !"".equals(rectype)){
            String[] recArr = rectype.split("#");
            for (int i=0; i<recArr.length; i++){
                String rec = recArr[i];
                String[] itemArr = rec.split(",");
                if (itemArr.length == 4){
                    newRectype += itemArr[0] + " 迟到" + itemArr[2] + "次，" + "早退" + itemArr[3] + "次 考勤等级" + itemArr[1] + "\n";
                }
            }
        }
        resultMap.put("remark", newRectype);
        return resultMap;
    }
}
