package com.attendance.project.financial.controller;

import com.attendance.common.exception.UtilException;
import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.file.FileUploadUtils;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.common.utils.uuid.Seq;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.config.AttendanceConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.financial.domain.FinOvertime;
import com.attendance.project.financial.domain.FinOvertimeDetail;
import com.attendance.project.financial.service.IFinOvertimeService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.line.domain.BusLine;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.line.service.IFlowLineService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 加班申请Controller
 *
 * @author ruoyi
 * @date 2024-11-08
 */
@Controller
@RequestMapping("/financial/overtime")
public class FinOvertimeController extends BaseController
{
    private String prefix = "financial/overtime";
    /**
     * 工作薄对象
     */
    private Workbook wb;
    private String sheetName;
    @Autowired
    private IFinOvertimeService finOvertimeService;

    @Autowired
    private IFlowLineService flowLineService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @GetMapping()
    public String overtime(ModelMap modelMap)
    {
        String dateMonth = DateUtils.dateMonthString();
        modelMap.put("dateMonth", dateMonth);
        return prefix + "/overtime";
    }

    /**
     * 查询加班申请列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinOvertime finOvertime)
    {
        startPage();
        List<FinOvertime> list = finOvertimeService.selectFinOvertimeList(finOvertime);
        return getDataTable(list);
    }

    /**
     * 导出加班申请列表
     */
    @Log(title = "加班申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinOvertime finOvertime)
    {
        List<FinOvertime> list = finOvertimeService.selectFinOvertimeList(finOvertime);
        ExcelUtil<FinOvertime> util = new ExcelUtil<FinOvertime>(FinOvertime.class);
        return util.exportExcel(list, "加班申请数据");
    }

    /**
     * 新增加班申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    /**
     * 新增保存加班申请
     */
    @Log(title = "加班申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinOvertime finOvertime)
    {
        return toAjax(finOvertimeService.insertFinOvertime(finOvertime));
    }

    /**
     * 修改加班申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinOvertime finOvertime = finOvertimeService.selectFinOvertimeById(id);
        String createBy = finOvertime.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finOvertime.setCreateBy(createUser.getUserName());
        FinOvertimeDetail param = new FinOvertimeDetail();
        param.setOid(finOvertime.getId());
        if ("1".equals(finOvertime.getRectype())){
            if (!getLoginName().equals(createBy) || finOvertime.getRecname().indexOf("合并") >= 0){
                param.setOvertype("three");
                List<FinOvertimeDetail> multipleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                param.setOvertype("two");
                List<FinOvertimeDetail> singleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", true);
            }else{
                param.setOvertype("three");
                List<FinOvertimeDetail> multipleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                param.setOvertype("two");
                List<FinOvertimeDetail> singleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", false);
            }
        } else if ("2".equals(finOvertime.getRectype())){
            param.setOvertype("night");
            List<FinOvertimeDetail> nightDetails = finOvertimeService.selectFinOvertimeDetailList(param);
            mmap.put("night", nightDetails);
        }
        mmap.put("finOvertime", finOvertime);
        //获取审核相关操作信息
        RecLine recLine = new RecLine();
        recLine.setRecId(finOvertime.getId());
        recLine.setCreateBy(createBy);
        BusLine busLine = new BusLine();
        busLine.setBusName("overtime");
        //List<FlowLine> flowLines = flowLineService.selectFlowLineListByPostAndBusiness(busLine, createUser);
        AjaxResult operateInfo = flowLineService.operateInfo(getSysUser(), recLine);
        mmap.put("recLines", operateInfo.get("recLines"));
        mmap.put("canOperate", operateInfo.get("canOperate"));
        mmap.put("canBack", operateInfo.get("canBack"));
        mmap.put("hasFinal", operateInfo.get("hasFinal"));
        mmap.put("canEdit", createBy.equals(getLoginName())&&(boolean)operateInfo.get("canOperate"));
        if (finOvertime.getRecname().indexOf("合并") >= 0) mmap.put("canEdit", false);
        return prefix + "/edit";
    }

    /**
     * 修改保存加班申请
     */
    @Log(title = "加班申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinOvertimeDetail finOvertimeDetail)
    {
        return toAjax(finOvertimeService.updateFinOvertimeDetail(finOvertimeDetail));
    }

    /**
     * 删除加班申请
     */
    @Log(title = "加班申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] idArr = ids.split(",");
        for (int i=0; i<idArr.length; i++){
            Long id = Long.valueOf(idArr[i]);
            flowLineService.deleteRecLineByRecId(id);
        }
        return toAjax(finOvertimeService.deleteFinOvertimeByIds(ids));
    }

    //合并记录
    @PostMapping("/combineRecs")
    @ResponseBody
    public AjaxResult combineRecs(FinOvertime finOvertime) throws Exception{
        AjaxResult result = new AjaxResult();
        FinOvertime overtime = new FinOvertime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfmonth = new SimpleDateFormat("yyyy-MM");
        //基本信息填写
        User curUser = getSysUser();
        Dept curDept = curUser.getDept();
        if (curDept.getParentId() != null){
            curDept = deptService.selectDeptById(curDept.getParentId());
        }
        overtime.setDeptid(curDept.getDeptId());
        overtime.setDeptName(curDept.getDeptName());
        overtime.setCreateBy(curUser.getLoginName());
        overtime.setCreateTime(new Date());
        Date rectime = new Date();
        overtime.setRectime(dfmonth.format(rectime));
        overtime.setRecname(dfmonth.format(rectime)+"合并申请表");
        String seqId = Seq.getBusId();
        overtime.setId(Long.valueOf(seqId));

        Long[] ids = finOvertime.getIds();
        List<FinOvertimeDetail> details = new ArrayList<FinOvertimeDetail>();
        for(int i=0; i<ids.length; i++){
            Long id = ids[i];
            if (i == 0){
                FinOvertime firstOt = finOvertimeService.selectFinOvertimeById(id);
                overtime.setRectype(firstOt.getRectype());
                overtime.setRectime(firstOt.getRectime());
                overtime.setRecname(firstOt.getRectime()+"合并申请表");
                if (firstOt.getRecname().indexOf("补") >= 0){
                    overtime.setRecname(firstOt.getRectime()+"合并申请表（补）");
                }
            }
            FinOvertimeDetail param = new FinOvertimeDetail();
            param.setOid(id);
            details.addAll(finOvertimeService.selectFinOvertimeDetailList(param));
        }
        for(int i=0; i<details.size(); i++){
            details.get(i).setId(null);
            details.get(i).setOid(overtime.getId());
        }
        finOvertimeService.insertFinOvertime(overtime);
        int count = finOvertimeService.batchInsertFinOvertimeDetail(details);
        return AjaxResult.success(count);
    }

    // 导入方法
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        FinOvertime overtime = new FinOvertime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfmonth = new SimpleDateFormat("yyyy-MM");
        //基本信息填写
        User curUser = getSysUser();
        Dept curDept = curUser.getDept();
        overtime.setDeptid(curDept.getDeptId());
        overtime.setDeptName(curDept.getDeptName());
        overtime.setCreateBy(curUser.getLoginName());
        overtime.setCreateTime(new Date());
        Date rectime = new Date();
        overtime.setRectime(dfmonth.format(rectime));

        //文件解析
        InputStream is = file.getInputStream();//获取文件的流;
        XSSFWorkbook sheets = new XSSFWorkbook(is);
        is.close();

        // 上传文件路径
        String filePath = AttendanceConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        overtime.setFilepath(fileName);

        XSSFSheet sheet = sheets.getSheetAt(0);

        XSSFRow titleRow = sheet.getRow(0);
        XSSFCell titleCell = titleRow.getCell(0);
        String titleStr = titleCell.toString().replaceAll(" ", "");
        if (titleStr.indexOf("加班情况汇总表")>=0){
            overtime.setRectype("1");
        } else if (titleStr.indexOf("倒班、值班情况汇总表")>=0){
            overtime.setRectype("2");
        }

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
            overtime.setDeptName(curDept.getDeptName());
            overtime.setDeptid(curDept.getDeptId());
        }
        //处理值班时间
        XSSFRow timeRow = sheet.getRow(2);
        XSSFCell timeCell = timeRow.getCell(0);
        String timeCellStr = timeCell.toString();
        if (timeCellStr.contains("：") && timeCellStr.indexOf("：") != timeCellStr.length()-1){
            timeCellStr = timeCellStr.substring(timeCellStr.indexOf("：")+1);
            timeCellStr = timeCellStr.trim();
            rectime = DateUtils.timeFormat(df, timeCellStr, null);
            overtime.setRectime(dfmonth.format(rectime));
        }

        int count = 0;
        if ("1".equals(overtime.getRectype())){
            //获取两种类型加班分界点
            int firIndex = 3;
            int secIndex = 7;
            for (int i = firIndex+1; i <= sheet.getLastRowNum(); i++){//遍历表中的每一行
                XSSFRow row = sheet.getRow(i);//这里是获取表中的第i行
                XSSFCell cell1 = row.getCell(0);//这里获取的就是第i行第0列数据
                if ("2.非法定节假日加班汇总表".equals(cell1.toString())) {
                    secIndex = i;
                    break;
                }
            }

            List<FinOvertimeDetail> details = new ArrayList<FinOvertimeDetail>();

            String recname = overtime.getDeptName();
            overtime.setRecname(dfmonth.format(rectime)+recname+"加班汇总表");
            if (titleStr.indexOf("补") >= 0) {
                overtime.setRecname(dfmonth.format(rectime)+recname+"加班汇总表（补）");
            }

            String seqId = Seq.getBusId();
            overtime.setId(Long.valueOf(seqId));
            finOvertimeService.insertFinOvertime(overtime);
            //节假日加班
            List<FinOvertimeDetail> innerDetails = foreachRowToOvertimeDetail(sheet, overtime,firIndex+2,secIndex-1,"three", df);
            details.addAll(innerDetails);
            //非节假日加班
            List<FinOvertimeDetail> innerDetails2 = foreachRowToOvertimeDetail(sheet, overtime,secIndex+2,sheet.getLastRowNum(),"two", df);
            details.addAll(innerDetails2);

            if (details.size() > 0) count = finOvertimeService.batchInsertFinOvertimeDetail(details);
        } else if ("2".equals(overtime.getRectype())){
            //获取两种类型加班分界点
            int firIndex = 3;

            List<FinOvertimeDetail> details = new ArrayList<FinOvertimeDetail>();

            String recname = overtime.getDeptName();
            overtime.setRecname(dfmonth.format(rectime)+recname+"倒班、值班汇总表");

            String seqId = Seq.getBusId();
            overtime.setId(Long.valueOf(seqId));
            finOvertimeService.insertFinOvertime(overtime);
            //节假日加班
            List<FinOvertimeDetail> innerDetails = foreachRowToOvertimeDetail(sheet, overtime,firIndex+1,sheet.getLastRowNum(),"night", df);
            details.addAll(innerDetails);

            if (details.size() > 0) count = finOvertimeService.batchInsertFinOvertimeDetail(details);
        }

        return AjaxResult.success(count);
    }

    //循环处理表格行，转换为加班详情
    List<FinOvertimeDetail> foreachRowToOvertimeDetail(XSSFSheet sheet, FinOvertime overtime, int startIndex, int endIndex, String type, DateFormat df) throws  Exception{
        List<FinOvertimeDetail> details = new ArrayList<FinOvertimeDetail>();
        if (startIndex > endIndex) return details;
        for (int i = startIndex; i <= endIndex; i++){
            XSSFRow row = sheet.getRow(i);
            if (row == null) break;
            XSSFCell cell2 = row.getCell(1);
            String username = cell2.toString();
            if (username == null || "".equals(username)) break;
            username = username.replaceAll(" ", "");
            User paramUser = new User();
            paramUser.setUserName(username);
            List<User> findList = userService.selectUserList(paramUser);
            if (findList.size() > 0){
                User find = findList.get(0);

                if ("three".equals(type) || "two".equals(type) || "one".equals(type)){
                    XSSFCell cell4 = row.getCell(3);
                    String timeStr = cell4.toString().trim();
                    String[] innerTimeArr = timeStr.split("、");

                    for (int j=0; j<innerTimeArr.length; j++){
                        String innerTimeStr = innerTimeArr[j];
                        Date innerTime = new Date();
                        if (j == 0){
                            innerTime = DateUtils.timeFormat(df, innerTimeStr, null);
                        }else{
                            innerTime = DateUtils.timeFormat(df, innerTimeStr, innerTimeArr[j-1]);
                        }
                        innerTimeArr[j] = df.format(innerTime);
                        FinOvertimeDetail detail = new FinOvertimeDetail();
                        XSSFCell cell3 = row.getCell(2);
                        detail.setIdcard(cell3.toString().trim());
                        detail.setOvertype(type);
                        XSSFCell cell6 =  row.getCell(5);
                        detail.setContent(cell6.toString().trim());
                        detail.setOvertime(df.format(innerTime));
                        detail.setUserName(find.getUserName());
                        detail.setUserid(find.getUserId());
                        detail.setDeptid(overtime.getDeptid());
                        detail.setDeptName(overtime.getDeptName());
                        detail.setOid(overtime.getId());
                        detail.setCreateBy(getSysUser().getLoginName());
                        detail.setCreateTime(new Date());
                        details.add(detail);
                    }
                } else if ("night".equals(type)){
                    FinOvertimeDetail detail = new FinOvertimeDetail();
                    XSSFCell cell3 = row.getCell(2);
                    detail.setIdcard(cell3.toString().trim());
                    XSSFCell cell4 =  row.getCell(3);
                    String overdays = cell4.toString().trim();
                    if (overdays.indexOf(".")>=0){
                        overdays = overdays.substring(0, overdays.indexOf("."));
                    }
                    detail.setOverdays(overdays);
                    XSSFCell cell5 =  row.getCell(4);
                    detail.setContent(cell5.toString().trim());
                    XSSFCell cell6 =  row.getCell(5);
                    detail.setRemark(cell6.toString().trim());
                    detail.setUserName(find.getUserName());
                    detail.setUserid(find.getUserId());
                    detail.setDeptid(overtime.getDeptid());
                    detail.setDeptName(overtime.getDeptName());
                    detail.setOid(overtime.getId());
                    detail.setCreateBy(getSysUser().getLoginName());
                    detail.setCreateTime(new Date());
                    detail.setOvertype("night");
                    details.add(detail);
                }
            }
        }
        return details;
    }


    /**
     * 导出加班情况统计 ,倒班列表
     */
    @Log(title = "考勤表统计", businessType = BusinessType.EXPORT)
    @PostMapping("/exportData/{id}")
    @ResponseBody
    public AjaxResult export(@PathVariable("id") Long id) throws ParseException {
        ModelMap mmap=new ModelMap();
        FinOvertime finOvertime = finOvertimeService.selectFinOvertimeById(id);
        String createBy = finOvertime.getCreateBy();
        User createUser = userService.selectUserByLoginName(createBy);
        finOvertime.setCreateBy(createUser.getUserName());
        FinOvertimeDetail param = new FinOvertimeDetail();
        param.setOid(finOvertime.getId());
        mmap.put("finOvertime", finOvertime);
        mmap.put("rectype", finOvertime.getRectype());
        if ("1".equals(finOvertime.getRectype())){
            if (!getLoginName().equals(createBy) || finOvertime.getRecname().indexOf("合并") >= 0){
                param.setOvertype("three");
                List<FinOvertimeDetail> multipleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                param.setOvertype("two");
                List<FinOvertimeDetail> singleDetails = finOvertimeService.selectFinOvertimeDetailListSuper(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", true);
            }else{
                param.setOvertype("three");
                List<FinOvertimeDetail> multipleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                param.setOvertype("two");
                List<FinOvertimeDetail> singleDetails = finOvertimeService.selectFinOvertimeDetailList(param);
                mmap.put("multiple", multipleDetails);
                mmap.put("single", singleDetails);
                mmap.put("seeDays", false);
            }
            mmap.put("sheetName", "加班情况汇总表");
        } else if ("2".equals(finOvertime.getRectype())){
            param.setOvertype("night");
            List<FinOvertimeDetail> nightDetails = finOvertimeService.selectFinOvertimeDetailList(param);
            mmap.put("night", nightDetails);
            mmap.put("sheetName", "倒班值班情况汇总表");
        }
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
    //根据类型看是加班还是倒班信息
    public  List<String> getLabelList(String rectype){
        // 声明保存日期集合
        List<String> labelList = new ArrayList<String>();
        if ("1".equals(rectype)){
           labelList = Arrays.asList("序号", "所属科室", "姓名", "加班日期", "合计天数", "加班事由");
        }else if ("2".equals(rectype)){
           labelList = Arrays.asList("序号", "所属科室", "姓名", "夜班天数", "倒班、值班情况", "备注");
        }
        return labelList;
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
        Sheet sheet = wb.createSheet("考勤表sheet");
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
        //前景填充色
//        cs_header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        //设置前景填充样式
//        cs_header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String rectype = (String)mmap.get("rectype");//获取类别

        List<String> headList = getLabelList(rectype);//表头列表
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
        //启用文本换行
        cs_text.setWrapText(true);
        //设置边框
        cs_text.setBorderBottom(BorderStyle.THIN);
        cs_text.setBorderLeft(BorderStyle.THIN);
        cs_text.setBorderRight(BorderStyle.THIN);
        cs_text.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_text.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_text.setVerticalAlignment(VerticalAlignment.CENTER);

        //1、设置标题行1
        row = sheet.createRow(0);
        //设置单元格行高
        row.setHeightInPoints(24);


        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        String xlsName  = (String) mmap.get("sheetName");
        cell.setCellValue(xlsName);
        //设置单元格样式
        cell.setCellStyle(cs_header);



        FinOvertime finOvertime  = (FinOvertime) mmap.get("finOvertime");
        //2、设置标题行2
        row = sheet.createRow(1);
        //设置单元格行高
        row.setHeightInPoints(20);
        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        String DeptName  = "部门 ：" +finOvertime.getDeptName();
        cell.setCellValue(DeptName);


        //3、设置标题行3
        row = sheet.createRow(2);
        //设置单元格行高
        row.setHeightInPoints(20);
        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        //设置单元格内容
        String Date = DateUtils.formattedDate(finOvertime.getCreateTime());
        String createTime  = "填表时间 ：" +Date;
        cell.setCellValue(createTime);

        //判断是否要生成第二个list
        //1、设置标题行1
        row = sheet.createRow(3);
        //设置单元格行高
        row.setHeightInPoints(30);

        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        cell.setCellValue("1.法定节假日加班汇总表");
        //设置单元格样式
        cell.setCellStyle(cs_header);
        sheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, headList.size() - 1));

        //4、设置标题行4
        row = sheet.createRow(4);
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
        List<FinOvertimeDetail> multipleDetails = (List<FinOvertimeDetail>) mmap.get("night");
        if (multipleDetails == null || multipleDetails.isEmpty()) {
            multipleDetails = (List<FinOvertimeDetail>) mmap.get("multiple");
        }

        int rowNum = 5;
        if (multipleDetails != null && !multipleDetails.isEmpty()) {
            // 预先定义好表头的数量，以便后续使用
            for (FinOvertimeDetail detail : multipleDetails) {
                row = sheet.createRow(rowNum++); // 数据行从第5行开始（索引为4）
                // 假设 Person 类有 getName(), getAge(), getEmail() 方法
                //设置单元格行高
                row.setHeightInPoints(18);
                //序号
                cell=row.createCell(0);
                cell.setCellStyle(cs_text);
                cell.setCellValue(rowNum-4);
                //部门姓名
                cell=row.createCell(1);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getDeptName());
                //姓名
                cell=row.createCell(2);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getUserName());
                //身份证号
               /* cell=row.createCell(2);
                cell.setCellStyle(cs_text);
                cell.setCellValue(detail.getIdcard());*/

                if ("2".equals(rectype)){
                    //合计天数
                    cell=row.createCell(3);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getOverdays());
                    //加班事由
                    cell=row.createCell(4);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getContent());
                    //备注
                    cell=row.createCell(5);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getRemark());
                }else if("1".equals(rectype)){
                    //加班日期
                    cell=row.createCell(3);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getOvertime());
                    //合计天数
                    cell=row.createCell(4);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getOverdays());
                    //加班事由
                    cell=row.createCell(5);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(detail.getContent());
                }

            }

        }
        cellSum=rowNum;
        row = sheet.createRow(cellSum);
        for (int i = 0; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
        }
        Integer celltwoSum = cellSum +1;
        if ("1".equals(rectype)){

            //判断是否要生成第二个list
            //1、设置标题行1
            row = sheet.createRow(celltwoSum);
            //设置单元格行高
            row.setHeightInPoints(30);

            //创建单元格
            cell = row.createCell(0);
            //设置单元格内容
            String xlstName  = "2.非法定节假日加班汇总表";
            cell.setCellValue(xlstName);
            //设置单元格样式
            cell.setCellStyle(cs_header);
            sheet.addMergedRegionUnsafe(new CellRangeAddress(celltwoSum, celltwoSum, 0, headList.size() - 1));

            //4、设置标题行2
            row = sheet.createRow(celltwoSum +1);
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

            // 假设 FinOvertimeDetail 类有合适的方法来获取其字段值
            List<FinOvertimeDetail> singleDetails = (List<FinOvertimeDetail>) mmap.get("single");
            Integer twoSum = celltwoSum +2;
            int singleSum = cellSum + 3;//从哪计算第二个list的索引位置 加上一个list 4行
            if (singleDetails != null && !singleDetails.isEmpty()) {
                // 预先定义好表头的数量，以便后续使用
                for (FinOvertimeDetail single : singleDetails) {
                    row = sheet.createRow(twoSum++); // 数据行索引加一
                    // 假设 Person 类有 getName(), getAge(), getEmail() 方法
                    //设置单元格行高
                    row.setHeightInPoints(18);
                    //序号
                    cell=row.createCell(0);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(twoSum-singleSum);
                    //部门姓名
                    cell=row.createCell(1);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getDeptName());
                    //姓名
                    cell=row.createCell(2);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getUserName());
                    //身份证号
                    /*cell=row.createCell(2);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getIdcard());*/

                    //加班日期
                    cell=row.createCell(3);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getOvertime());
                    //合计天数
                    cell=row.createCell(4);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getOverdays());
                    //加班事由
                    cell=row.createCell(5);
                    cell.setCellStyle(cs_text);
                    cell.setCellValue(single.getContent());
                    }

                }else {
                    //初始化
                     row = sheet.createRow(twoSum++); // 数据行从第5行开始（索引为4）
                    //设置单元格行高
                    row.setHeightInPoints(18);
                    //序号
                    for (int i = 0; i < 6; i++) {
                        cell=row.createCell(i);
                        cell.setCellStyle(cs_text);
                        cell.setCellValue("");
                    }
                }
            cellSum=twoSum;
        }


        //合并单元格，横向
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, headList.size() - 1));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 1, 0, headList.size() - 1));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(2, 2, 0, headList.size() - 1));

   /*     //合并单元格，横向
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 11, 12));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1,1,3,4));
        //竖着合并
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1,list.size(),4,4));*/
        //设置单元格宽度自适应
//        for (int i = 0; i <= cellSum; i++) {
//            sheet.autoSizeColumn((short) i, true); //自动调整列宽
//        }
        for (int i = 0; i <= cellSum; i++) {
            sheet.autoSizeColumn(i);
            // 设置最小宽度（单位为字符宽度）
            sheet.setColumnWidth(i, Math.max(sheet.getColumnWidth(i), 256 * 15)); // 最小宽度为15个字符
            // 如果需要设置最大宽度，可以类似地使用Math.min
        }
    }
}
