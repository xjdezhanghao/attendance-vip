package com.attendance.project.financial.controller;

import com.attendance.common.exception.UtilException;
import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.file.FileUploadUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.common.utils.uuid.Seq;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.config.AttendanceConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.*;
import com.attendance.project.financial.service.IFinKpiService;
import com.attendance.project.financial.service.IFinParamService;
import com.attendance.project.financial.service.IFinSalRecService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
import com.attendance.project.system.post.service.IPostService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * 绩效Controller
 *
 * @author ruoyi
 * @date 2024-12-02
 */
@Controller
@RequestMapping("/financial/kpi")
public class FinKpiController extends BaseController
{
    private String prefix = "financial/kpi";
    /**
     * 工作薄对象
     */
    private Workbook wb;
    private String sheetName;
    @Autowired
    private IFinKpiService finKpiService;

    @Autowired
    private IFlowLineService flowLineService;

    @Autowired
    private IFinSalRecService finSalRecService;

    @Autowired
    private IFinParamService finParamService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IPostService postService;

    @GetMapping()
    public String kpi(ModelMap modelMap)
    {
        String dateMonth = DateUtils.dateMonthString();
        modelMap.put("dateMonth", dateMonth);
        return prefix + "/kpi";
    }

    /**
     * 查询绩效列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinKpi finKpi)
    {
        startPage();
        List<FinKpi> list = finKpiService.selectFinKpiList(finKpi);
        return getDataTable(list);
    }

    /**
     * 修改绩效
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinKpi finKpi = finKpiService.selectFinKpiById(id);
        String createBy = finKpi.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finKpi.setCreateBy(createUser.getUserName());
        FinKpiDetail param = new FinKpiDetail();
        param.setPid(finKpi.getId());
        List<FinKpiDetail> kpiDetails = finKpiService.selectFinKpiDetailList(param);
        mmap.put("finKpi", finKpi);
        mmap.put("details", kpiDetails);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finKpi.getId());
        recLine.setCreateBy(createBy);
        AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
        mmap.put("recLines", operateInfo.get("recLines"));
        mmap.put("canOperate", operateInfo.get("canOperate"));
        mmap.put("canBack", operateInfo.get("canBack"));
        mmap.put("hasFinal", operateInfo.get("hasFinal"));
        mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        return prefix + "/edit";
    }

    /**
     * 修改保存绩效
     */
    @Log(title = "绩效", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinKpiDetail finKpiDetail)
    {
        return toAjax(finKpiService.updateFinKpiDetail(finKpiDetail));
    }

    /**
     * 删除绩效
     */
    @Log(title = "绩效", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] idArr = ids.split(",");
        for (int i=0; i<idArr.length; i++){
            Long id = Long.valueOf(idArr[i]);
            flowLineService.deleteRecLineByRecId(id);
        }
        return toAjax(finKpiService.deleteFinKpiByIds(ids));
    }

    //合并记录
    @PostMapping("/combineRecs")
    @ResponseBody
    public AjaxResult combineRecs(FinKpi finKpi) throws Exception{
        AjaxResult result = new AjaxResult();
        FinKpi kpi = new FinKpi();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfmonth = new SimpleDateFormat("yyyy-MM");
        //基本信息填写
        User curUser = getSysUser();
        Dept curDept = curUser.getDept();
        if (curDept.getParentId() != null){
            Dept pDept = deptService.selectDeptById(curDept.getParentId());
            if (pDept != null){
                curDept = pDept;
            }
        }
        kpi.setDeptid(curDept.getDeptId());
        kpi.setDeptName(curDept.getDeptName());
        kpi.setCreateBy(curUser.getLoginName());
        kpi.setCreateTime(new Date());
        Date rectime = new Date();
        kpi.setRectime(dfmonth.format(rectime));
        kpi.setRecname(dfmonth.format(rectime)+"合并申请表");
        String seqId = Seq.getBusId();
        kpi.setId(Long.valueOf(seqId));
        kpi.setRectype("2");

        Long[] ids = finKpi.getIds();
        List<FinKpiDetail> details = new ArrayList<FinKpiDetail>();
        for(int i=0; i<ids.length; i++){
            Long id = ids[i];
            if (i == 0){
                FinKpi firstK = finKpiService.selectFinKpiById(id);
                kpi.setRectime(firstK.getRectime());
                kpi.setRecname(firstK.getRectime()+"合并申请表");
            }
            FinKpiDetail param = new FinKpiDetail();
            param.setPid(id);
            details.addAll(finKpiService.selectFinKpiDetailList(param));
        }
        for(int i=0; i<details.size(); i++){
            details.get(i).setId(null);
            details.get(i).setPid(kpi.getId());
        }
        finKpiService.insertFinKpi(kpi);
        int count = finKpiService.batchInsertFinKpiDetail(details);

        return AjaxResult.success(count);
    }

    // 导入方法
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        FinKpi kpi = new FinKpi();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfmonth = new SimpleDateFormat("yyyy-MM");
        //基本信息填写
        User curUser = getSysUser();
        Dept curDept = curUser.getDept();
        kpi.setDeptid(curDept.getDeptId());
        kpi.setDeptName(curDept.getDeptName());
        kpi.setCreateBy(curUser.getLoginName());
        kpi.setCreateTime(new Date());
        Date rectime = new Date();
        kpi.setRectime(dfmonth.format(rectime));

        //文件解析
        InputStream is = file.getInputStream();//获取文件的流;
        XSSFWorkbook sheets = new XSSFWorkbook(is);
        is.close();
        // 上传文件路径
        String filePath = AttendanceConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        kpi.setFilepath(fileName);

        XSSFSheet sheet = sheets.getSheetAt(0);

        //处理填写部门，有冒号时取冒号之后部分，并代替填写部门
        XSSFRow deptRow = sheet.getRow(1);
        XSSFCell deptCell = deptRow.getCell(0);
        String excelDeptStr = deptCell.toString();
        if (excelDeptStr.contains("：") && excelDeptStr.indexOf("：") != excelDeptStr.length()-1){
            excelDeptStr = excelDeptStr.substring(excelDeptStr.indexOf("：")+1);
            excelDeptStr = excelDeptStr.trim();
        }
        Dept paramDept = new Dept();
        paramDept.setDeptName(excelDeptStr);
        List<Dept> excelDepts = deptService.selectDeptList(paramDept);
        if (excelDepts.size() > 0) {
            curDept = excelDepts.get(0);
            kpi.setDeptName(curDept.getDeptName());
            kpi.setDeptid(curDept.getDeptId());
        }
        //处理绩效时间
        XSSFRow timeRow = sheet.getRow(2);
        XSSFCell timeCell = timeRow.getCell(0);
        String timeCellStr = timeCell.toString();
        if (timeCellStr.contains("：") && timeCellStr.indexOf("：") != timeCellStr.length()-1){
            timeCellStr = timeCellStr.substring(timeCellStr.indexOf("：")+1);
            timeCellStr = timeCellStr.trim();
            rectime = DateUtils.timeFormat(df, timeCellStr, null);
            kpi.setRectime(dfmonth.format(rectime));
        }

        List<FinKpiDetail> details = new ArrayList<FinKpiDetail>();

        String recname = kpi.getDeptName();
        kpi.setRecname(dfmonth.format(rectime)+recname+"绩效统计表");
        String seqId = Seq.getBusId();
        kpi.setId(Long.valueOf(seqId));
        kpi.setRectype("1");
        finKpiService.insertFinKpi(kpi);

        details = foreachRowToKpiDetail(sheet, kpi, 4, sheet.getLastRowNum(), df);
        int count = 0;
        count = finKpiService.batchInsertFinKpiDetail(details);

        return AjaxResult.success(count);
    }

    //循环处理表格行，转换为加班详情
    List<FinKpiDetail> foreachRowToKpiDetail(XSSFSheet sheet, FinKpi kpi, int startIndex, int endIndex, DateFormat df) throws Exception {
        List<FinKpiDetail> details = new ArrayList<FinKpiDetail>();
        if (startIndex > endIndex) return details;
        for (int i = startIndex; i <= endIndex; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) break;
            XSSFCell cellB = row.getCell(2);
            String username = cellB.toString();
            if (username == null || "".equals(username)) break;
            username = username.replaceAll(" ", "");
            User paramUser = new User();
            paramUser.setUserName(username);
            List<User> findList = userService.selectUserList(paramUser);
            if (findList.size() > 0) {
                User find = findList.get(0);
                FinKpiDetail detail = new FinKpiDetail();
                XSSFCell cellD = row.getCell(3);
                if (cellD != null) {
                    detail.setJxdj(cellD.toString());
                } else {
                    detail.setJxdj("A");
                }
                XSSFCell cellE = row.getCell(4);
                if (cellE != null) {
                    detail.setJljx(cellE.toString());
                } else {
                    detail.setJljx("");
                }
                XSSFCell cellF = row.getCell(5);
                if (cellF != null){
                    detail.setKfjx(cellF.toString());
                } else {
                    detail.setKfjx("");
                }
                XSSFCell cellG = row.getCell(6);
                if (cellG != null){
                    detail.setKhzf(cellG.toString());
                } else {
                    detail.setKhzf("");
                }

                XSSFCell cellH =  row.getCell(7);
                if (cellH != null) {
                    detail.setJlf(cellH.toString());
                } else {
                    detail.setJlf("");
                }
                XSSFCell cellI =  row.getCell(8);
                if (cellI != null) {
                    detail.setKff(cellI.toString());
                } else {
                    detail.setKff("");
                }
                XSSFCell cellJ =  row.getCell(9);
                if (cellJ != null) {
                    detail.setContent(cellJ.toString());
                } else {
                    detail.setContent("");
                }
                detail.setFjzb("N");

                detail.setUserName(find.getUserName());
                detail.setUserid(find.getUserId());
                detail.setDeptid(kpi.getDeptid());
                detail.setDeptName(kpi.getDeptName());
                detail.setPid(kpi.getId());
                detail.setCreateBy(getSysUser().getLoginName());
                detail.setCreateTime(new Date());
                details.add(detail);
            }
        }

        return details;
    }
    /**
     * 导出绩效情况统计
     */
    @Log(title = "考勤表统计", businessType = BusinessType.EXPORT)
    @PostMapping("/exportData/{id}")
    @ResponseBody
    public AjaxResult export(@PathVariable(value = "id", required = false) Long id)
    {
        ModelMap mmap=new ModelMap();
        if (id != 0){
            FinKpi finKpi = finKpiService.selectFinKpiById(id);
            String createBy = finKpi.getCreateBy();
            User createUser = userService.selectUserByLoginName(createBy);
            finKpi.setCreateBy(createUser.getUserName());
            FinKpiDetail param = new FinKpiDetail();
            param.setPid(finKpi.getId());
            List<FinKpiDetail> kpiDetails = finKpiService.selectFinKpiDetailList(param);
            mmap.put("finKpi", finKpi);
            mmap.put("details", kpiDetails);
        } else {
            User curUser = getSysUser();
            Dept dept = curUser.getDept();
            User param = new User();
            param.setDeptId(dept.getDeptId());
            List<User> users = userService.selectUserList(param);
            FinKpi finKpi = new FinKpi();
            finKpi.setDeptName(dept.getDeptName());
            finKpi.setCreateTime(new Date());
            mmap.put("finKpi", finKpi);
            //计算效益工资
            FinParam finParam = new FinParam();
            finParam.setDeptid(dept.getDeptId());
            List<FinParam> finParams = finParamService.selectFinParamList(finParam);
            String gzxs = "";
            String gdjj = "";
            if (finParams.size() > 0) {
                finParam = finParams.get(0);
                //获取配置参数
                FinParamDetail finParamDetail = new FinParamDetail();
                finParamDetail.setPid(finParam.getId());
                //工资系数
                finParamDetail.setDetailCode("gzxs");
                gzxs = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
                //固定奖金
                finParamDetail.setDetailCode("gdjj");
                gdjj = finParamService.selectFinParamDetailByCodeKey(finParamDetail);
            }

            List<FinKpiDetail> finKpiDetails = new ArrayList<FinKpiDetail>();
            for (int i=0; i<users.size(); i++){
                User user = users.get(i);
                FinKpiDetail detail = new FinKpiDetail();
                detail.setUserName(user.getUserName());
                detail.setDeptName(dept.getDeptName());
                FinSalRec finSalRec = new FinSalRec();
                finSalRec.setGzxs(gzxs);
                finSalRec.setGdjj(gdjj);
                finSalRec = finSalRecService.calcUserXygz(user, finSalRec);
                detail.setRemark(finSalRec.getGrxyze());
                detail.setJxdj("A");
                detail.setKhzf("100");
                finKpiDetails.add(detail);
            }
            mmap.put("details", finKpiDetails);
        }

        mmap.put("sheetName", "绩效考核结果统计表");
        OutputStream out = null;
        try {
            writeExcel(mmap);
            sheetName = (String) mmap.get("sheetName");
            String filename = encodingFilename(this.sheetName);
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

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename) {
//        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename) {
        String downloadPath = AttendanceConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }
    public void writeExcel(ModelMap mmap) {
        //创建Excel文件
        this.wb = new XSSFWorkbook();
        //生成sheet
        Sheet sheet = wb.createSheet("sheet");
        //创建行
        Row row = null;

        //创建列
        Cell cell = null;

        //创建表头单元格样式
        CellStyle cs_header = wb.createCellStyle();
        //设置字体样式
        Font boldFont = wb.createFont();

        //设置文字类型
        boldFont.setFontName("宋体");
        //设置加粗
        boldFont.setBold(true);
        //设置文字大小
        boldFont.setFontHeightInPoints((short) 16);
        //应用设置的字体
        cs_header.setFont(boldFont);

        //设置边框下、左、右、上
//        cs_header.setBorderBottom(BorderStyle.THIN);
//        cs_header.setBorderLeft(BorderStyle.THIN);
//        cs_header.setBorderRight(BorderStyle.THIN);
//        cs_header.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_header.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_header.setVerticalAlignment(VerticalAlignment.CENTER);

        List<String> headList = Arrays.asList("序号", "所在科室", "姓名", "月度考核等级", "个人奖励绩效", "个人扣发绩效", "考核总分"
                , "奖励分", "扣罚分", "奖惩事由");//表头列表
        //创建文本单元格样式
        CellStyle cs_text = wb.createCellStyle();
        //创建文字设置
        Font textFont = wb.createFont();
        //设置文字类型
        textFont.setFontName("宋体");//Consolas
        //设置文字大小
        textFont.setFontHeightInPoints((short) 10);
        // 应用设置
        cs_text.setFont(textFont);
        //设置边框
        cs_text.setBorderBottom(BorderStyle.THIN);
        cs_text.setBorderLeft(BorderStyle.THIN);
        cs_text.setBorderRight(BorderStyle.THIN);
        cs_text.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_text.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_text.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置自动换行
        cs_text.setWrapText(true);

        //1、设置标题行1
        row = sheet.createRow(0);
        //设置单元格行高
        row.setHeightInPoints(24);

        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        String xlsName = (String) mmap.get("sheetName");
        cell.setCellValue(xlsName);
        //设置单元格样式
        cell.setCellStyle(cs_header);

        FinKpi finKpi = (FinKpi) mmap.get("finKpi");
        if (finKpi.getId() == null){
            headList = Arrays.asList("序号", "所在科室", "姓名", "月度考核等级", "个人奖励绩效", "个人扣发绩效", "考核总分"
                    , "奖励分", "扣罚分", "奖惩事由", "效益工资(无需改动)");
        }
        //2、设置标题行2
        row = sheet.createRow(1);
        //设置单元格行高
        row.setHeightInPoints(20);
        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        String DeptName = "部门 ：" + finKpi.getDeptName();
        cell.setCellValue(DeptName);


        //3、设置标题行3
        row = sheet.createRow(2);
        //设置单元格行高
        row.setHeightInPoints(20);
        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        //设置单元格内容
        String Date = DateUtils.formattedDate(finKpi.getCreateTime());
        String createTime = "填表时间 ：" + Date;
        cell.setCellValue(createTime);

        //4、设置标题行4
        row = sheet.createRow(3);
        //设置单元格行高
        row.setHeightInPoints(20);
        //逐个设置标题样式
        for (int i = 0; i < headList.size(); i++) {
            //创建单元格
            cell = row.createCell(i);
            //设置单元格内容
            String val = headList.get(i);
            //设置单元格样式
            cell.setCellStyle(cs_text);
            cell.setCellValue(val);
        }
        //调取数据
        //记录总共多少列（由于接口查询出来的实体类集合，所以不好循环，使用）
        Integer cellSum = 0;
        // 假设 FinOvertimeDetail 类有合适的方法来获取其字段值
        List<FinKpiDetail> kpiDetails = (List<FinKpiDetail>) mmap.get("details");
        int rowNum = 4;
        if (kpiDetails != null && !kpiDetails.isEmpty()) {
            Double jiangli = 0.0;
            Double koufa = 0.0;
            // 预先定义好表头的数量，以便后续使用
            for (FinKpiDetail detail : kpiDetails) {
                row = sheet.createRow(rowNum++); // 数据行从第5行开始（索引为4）
                // 假设 Person 类有 getName(), getAge(), getEmail() 方法
                //设置单元格行高
                row.setHeightInPoints(18);
                // 自动调整行高以适应内容
//                row.setHeightInPoints(-1); // -1 表示自动调整行高
                //序号
                cell = row.createCell(0);
                cell.setCellStyle(cs_text);
                cell.setCellValue(rowNum - 4);
                //部门姓名
                cell = row.createCell(1);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getDeptName());
                //姓名
                cell = row.createCell(2);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getUserName());
                //绩效等级
                cell = row.createCell(3);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getJxdj());
                //个人奖励绩效
                cell = row.createCell(4);
                cell.setCellStyle(cs_text);
                if (detail.getJljx() != null && !"".equals(detail.getJljx())){
                    Double jl = Convert.toDouble(detail.getJljx(), 0.0, 2);
                    cell.setCellValue(jl);
                    jiangli += jl;
                } else {
                    cell.setCellValue(detail.getJljx());
                }
                //个人扣发绩效
                cell = row.createCell(5);
                cell.setCellStyle(cs_text);
                if (detail.getKfjx() != null && !"".equals(detail.getKfjx())){
                    Double kf = Convert.toDouble(detail.getKfjx(), 0.0, 2);
                    cell.setCellValue(kf);
                    koufa += kf;
                } else {
                    cell.setCellValue(detail.getKfjx());
                }
                //考核总分
                cell = row.createCell(6);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getKhzf());
                //奖励分
                cell = row.createCell(7);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getJlf());
                //扣罚分
                cell = row.createCell(8);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getKff());
                //奖惩事由
                cell = row.createCell(9);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getContent());
                //效益工资
                if (finKpi.getId() == null){
                    cell = row.createCell(10);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getRemark());
                }
            }
            if(finKpi.getId()!= null){
                row = sheet.createRow(rowNum++);
                row.setHeightInPoints(18);
                cell = row.createCell(1);
                cell.setCellValue("合计");
                cell = row.createCell(4);
                cell.setCellValue(Convert.simpDouble(jiangli, 2));
                cell = row.createCell(5);
                cell.setCellValue(Convert.simpDouble(koufa, 2));
            }
        }
        cellSum = rowNum;
        //合并单元格，横向
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, headList.size() - 1));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 1, 0, headList.size() - 1));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(2, 2, 0, headList.size() - 1));


        // 假设 cellSum 是最大列索引，从0开始计数
        for (int i = 0; i <=cellSum ; i++) {
            // 自动调整列宽
            sheet.autoSizeColumn(i);
//            // 获取当前列宽，并设置最小宽度（单位为字符宽度）
//            int currentWidth = sheet.getColumnWidth(i);
//            int minWidth = 256 * 15; // 最小宽度为20个字符
//            if (currentWidth < minWidth) {
//                sheet.setColumnWidth(i, minWidth);
//            }
            // 确保最小宽度足够大以适应内容
            int columnIndex = cell.getColumnIndex();
            int currentWidth = sheet.getColumnWidth(columnIndex);
            int minWidth = 256 * 20; // 最小宽度为20个字符
            if (currentWidth < minWidth) {
                sheet.setColumnWidth(columnIndex, minWidth);
            }
        }

    }
}
