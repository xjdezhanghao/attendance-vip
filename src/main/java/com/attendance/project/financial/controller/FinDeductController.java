package com.attendance.project.financial.controller;

import com.attendance.common.utils.file.FileUploadUtils;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.common.utils.text.Convert;
import com.attendance.common.utils.uuid.Seq;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.config.AttendanceConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.FinDeduct;
import com.attendance.project.financial.domain.FinDeductDetail;
import com.attendance.project.financial.service.IFinDeductService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 扣除Controller
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
@Controller
@RequestMapping("/financial/deduct")
public class FinDeductController extends BaseController
{
    private String prefix = "financial/deduct";

    @Autowired
    private IFinDeductService finDeductService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @GetMapping()
    public String deduct()
    {
        return prefix + "/deduct";
    }

    /**
     * 查询扣除列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinDeduct finDeduct)
    {
        startPage();
        List<FinDeduct> list = finDeductService.selectFinDeductList(finDeduct);
        return getDataTable(list);
    }

    /**
     * 导出扣除列表
     */
    @Log(title = "扣除", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinDeduct finDeduct)
    {
        List<FinDeduct> list = finDeductService.selectFinDeductList(finDeduct);
        ExcelUtil<FinDeduct> util = new ExcelUtil<FinDeduct>(FinDeduct.class);
        return util.exportExcel(list, "扣除数据");
    }

    /**
     * 新增扣除
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存扣除
     */
    @Log(title = "扣除", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinDeduct finDeduct)
    {
        return toAjax(finDeductService.insertFinDeduct(finDeduct));
    }

    /**
     * 修改扣除
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinDeduct deduct = finDeductService.selectFinDeductById(id);
        String createBy = deduct.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        deduct.setCreateBy(createUser.getUserName());
        FinDeductDetail param = new FinDeductDetail();
        param.setPid(deduct.getId());
        List<FinDeductDetail> details = finDeductService.selectFinDeductDetailList(param);
        mmap.put("deduct", deduct);
        mmap.put("details", details);
        return prefix + "/edit";
    }

    /**
     * 修改保存扣除
     */
    @Log(title = "扣除", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinDeductDetail deductDetail)
    {
        return toAjax(finDeductService.updateFinDeductDetail(deductDetail));
    }

    /**
     * 删除扣除
     */
    @Log(title = "扣除", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(finDeductService.deleteFinDeductByIds(ids));
    }

    // 导入方法
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        FinDeduct deduct = new FinDeduct();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfmonth = new SimpleDateFormat("yyyy-MM");
        //基本信息填写
        User curUser = getSysUser();
        Dept curDept = curUser.getDept();
        deduct.setDeptid(curDept.getDeptId());
        deduct.setDeptName(curDept.getDeptName());
        deduct.setCreateBy(curUser.getLoginName());
        deduct.setCreateTime(new Date());
        Date rectime = new Date();
        deduct.setRectime(dfmonth.format(rectime));

        //文件解析
        InputStream is = file.getInputStream();//获取文件的流;
        XSSFWorkbook sheets = new XSSFWorkbook(is);
        is.close();
        // 上传文件路径
        String filePath = AttendanceConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        deduct.setFilepath(fileName);

        XSSFSheet sheet = sheets.getSheetAt(0);

        XSSFRow row = sheet.getRow(1);
        XSSFCell cellA = row.getCell(0);
        String rectimeStr = cellA.toString();
        if (rectimeStr != null && !"".equals(rectimeStr)){
            rectimeStr = rectimeStr.substring(rectimeStr.indexOf("：")+1);
            rectimeStr = rectimeStr.replaceAll(" ", "");
        }
        deduct.setRectime(rectimeStr);

        List<FinDeductDetail> details = new ArrayList<FinDeductDetail>();

        deduct.setRecname(deduct.getRectime()+"扣除情况汇总");
        String seqId = Seq.getBusId();
        deduct.setId(Long.valueOf(seqId));
        deduct.setRectype("1");
        finDeductService.insertFinDeduct(deduct);

        details = foreachRowToDeductDetail(sheet, deduct, 3, sheet.getLastRowNum(), df);
        int count = 0;
        if (details.size() > 0){
            count = finDeductService.batchInsertFinDeductDetail(details);
        }
        return AjaxResult.success(count);
    }

    List<FinDeductDetail> foreachRowToDeductDetail(XSSFSheet sheet, FinDeduct deduct, int startIndex, int endIndex, DateFormat df) throws Exception {
        List<FinDeductDetail> details = new ArrayList<FinDeductDetail>();
        if (startIndex > endIndex) return details;
        for (int i = startIndex; i <= endIndex; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) break;
            XSSFCell cellB = row.getCell(2);
            if (cellB == null) continue;
            String username = cellB.toString();
            if (username == null || "".equals(username)) break;
            username = username.replaceAll(" ", "");
            XSSFCell cellC = row.getCell(3);
            if (cellC == null) continue;
            String idcard = cellC.toString();
            if (idcard != null) idcard = idcard.replaceAll(" ", "");;
            User paramUser = new User();
            paramUser.setUserName(username);
            paramUser.setIdcard(idcard);
            List<User> findList = userService.selectUserList(paramUser);
            if (findList.size() > 0) {
                User find = findList.get(0);
                FinDeductDetail detail = new FinDeductDetail();
                XSSFCell cellD = row.getCell(3+1);
                detail.setDkylbx(Convert.toDouble(cellD.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellE = row.getCell(4+1);
                detail.setDkyilbx(Convert.toDouble(cellE.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellF = row.getCell(5+1);
                detail.setDksybx(Convert.toDouble(cellF.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellG = row.getCell(6+1);
                detail.setDkgjj(Convert.toDouble(cellG.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellH =  row.getCell(7+1);
                detail.setDkqynj(Convert.toDouble(cellH.toString().replaceAll(",", ""), 0.0, 2).toString());

                XSSFCell cellI =  row.getCell(8+1);
                detail.setBkyl(Convert.toDouble(cellI.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellJ =  row.getCell(9+1);
                detail.setBkyil(Convert.toDouble(cellJ.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellK =  row.getCell(10+1);
                detail.setBksy(Convert.toDouble(cellK.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellL =  row.getCell(11+1);
                detail.setBkgjj(Convert.toDouble(cellL.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellM =  row.getCell(12+1);
                detail.setBkqynj(Convert.toDouble(cellM.toString().replaceAll(",", ""), 0.0, 2).toString());

                XSSFCell cellN =  row.getCell(13+1);
                detail.setDksf(Convert.toDouble(cellN.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellO =  row.getCell(14+1);
                detail.setDkglwsf(Convert.toDouble(cellO.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellP =  row.getCell(15+1);
                detail.setKgsdjsb(Convert.toDouble(cellP.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellQ =  row.getCell(16+1);
                detail.setBydkgrsds(Convert.toDouble(cellQ.toString().replaceAll(",", ""), 0.0, 2).toString());
                XSSFCell cellR =  row.getCell(17+1);
                detail.setKqkkhj(Convert.toDouble(cellR.toString().replaceAll(",", ""), 0.0, 2).toString());
                //XSSFCell cellS =  row.getCell(18);
                //detail.setKkhj(String.valueOf(cellS.getNumericCellValue()).replaceAll(",", ""));

                Double kkhj2 = Convert.toDouble(detail.getDkylbx(),0.0,2) +
                        Convert.toDouble(detail.getDkyilbx(),0.0,2) +
                        Convert.toDouble(detail.getDkgjj(),0.0,2)+
                        Convert.toDouble(detail.getDksybx(),0.0,2)+
                        Convert.toDouble(detail.getDkqynj(),0.0,2)+
                        Convert.toDouble(detail.getBkyl(),0.0,2)+
                        Convert.toDouble(detail.getBkyil(),0.0,2)+
                        Convert.toDouble(detail.getBkgjj(),0.0,2)+
                        Convert.toDouble(detail.getBkqynj(),0.0,2)+
                        Convert.toDouble(detail.getBksy(),0.0,2)+
                        Convert.toDouble(detail.getDksf(),0.0,2)+
                        Convert.toDouble(detail.getDkglwsf(),0.0,2)+
                        Convert.toDouble(detail.getKgsdjsb(),0.0,2)+
                        Convert.toDouble(detail.getBydkgrsds(),0.0,2)+
                        Convert.toDouble(detail.getKqkkhj(),0.0,2);
                detail.setKkhj(Convert.simpDouble(kkhj2, 2).toString());

                detail.setUserName(find.getUserName());
                detail.setUserid(find.getUserId());
                detail.setDeptid(find.getDept().getDeptId());
                detail.setDeptName(find.getDept().getDeptName());
                detail.setPid(deduct.getId());
                detail.setCreateBy(getSysUser().getLoginName());
                detail.setCreateTime(new Date());
                details.add(detail);
            }
        }

        return details;
    }

}
