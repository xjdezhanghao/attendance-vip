package com.attendance.project.atdform.atdstatisticform.controller;

import com.attendance.common.exception.UtilException;
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
import com.attendance.project.financial.domain.FinAtd;
import com.attendance.project.financial.service.IFinAtdService;
import com.attendance.project.system.config.service.IConfigService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.dept.service.IDeptService;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.domain.UserRole;
import com.attendance.project.system.user.service.IUserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤表统计Controller
 *
 * @author wangchengyan
 * @date 2022-08-02
 */
@Controller
@RequestMapping("/atdform/atdstatisticform")
public class AtdStatisticFormController extends BaseController {
    /**
     * 工作薄对象
     */
    private Workbook wb;
    private String sheetName;
    private String sName;
    private String prefix = "atdform/atdstatisticform";

    @Autowired
    private IAtdStatisticFormService atdStatisticFormService;
    @Autowired
    private IAtdPlanService atdPlanService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IFinAtdService atdService;
    @Autowired
    private IConfigService configService;

    @GetMapping()
    public String atdstatisticform(ModelMap mmap) {
        mmap.put("deptName", "");
        User curUser = getSysUser();
        if (curUser != null){
            Dept curDept = curUser.getDept();
            if (curDept != null){
                mmap.put("deptName", curDept.getDeptName());
            }
        }
        return prefix + "/atdstatisticform";
    }

    /**
     * 查询考勤表统计列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestParam(value = "atdId", required = false) Long atdId, @RequestParam("startDate") String startDate0, @RequestParam("endDate") String endDate0, @RequestParam(value = "deptname", required = false) String deptname0, @RequestParam(value = "username", required = false) String username0) throws ParseException {
        String startDate = startDate0;
        String endDate = endDate0;
        String deptname = deptname0;
        String username = username0;
        //查询时间为空获取本月第一天和今天
        if (startDate == null || startDate.length() == 0) {
            //获取当前日期年月日
            LocalDate today = LocalDate.now();
            //本月的第一天年月日
            LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
            //LocalDate转String
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            endDate = today.format(df);
            startDate = firstday.format(df);
        }
        //1 形成时间列表
        List<String> timeList = getBetweenDate(startDate, endDate);

        AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
        atdStatisticForm.setStarttime(startDate);
        atdStatisticForm.setEndtime(endDate);
        atdStatisticForm.setDeptname(deptname);
        atdStatisticForm.setUsername(username);

        //获取当前登录用户部门
        atdStatisticForm.setDeptId(getSysUser().getDept().getDeptId());

        //2 查出所有姓名
        // List<String>  names = atdStatisticFormService.selectNames(atdStatisticForm);
        List<Integer> ids = atdStatisticFormService.selectIds(atdStatisticForm);


        //3 查询数据库数据
        startPage();

        List<AtdStatisticForm> dataList = atdStatisticFormService.selectAtdStatisticFormList(atdStatisticForm);

        //获取排班计划
        AtdPlan findAtdPlan = new AtdPlan();
        findAtdPlan.setStartDate(startDate);
        findAtdPlan.setEndDate(endDate);
        Dept findDeptParam = new Dept();
        findDeptParam.setDeptName(deptname);
        List<Dept> findDepts = deptService.selectDeptList(findDeptParam);
        if (deptname != null && !"".equals(deptname.trim())){
            if (findDepts != null && findDepts.size() > 0 && findDepts.getFirst() != null) {
                findAtdPlan.setDeptId(findDepts.getFirst().getDeptId());
            }
        }
        findAtdPlan.setUserName(username);
        List<AtdPlan> planList = atdPlanService.selectAtdPlanList(findAtdPlan);

        //4 把所有人考勤赋值  对每个人的考勤形成字符串
        List<String> resultList = new ArrayList<>();
        String dult0 = " " + "," + " " + "," + "星期";
        for (String time : timeList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date_time = sdf.parse(time);
            String week = dateToWeek(date_time);
            dult0 = dult0 + "," + week;//星期
        }

        dult0 = dult0 + "," + " " + "," + " " + "," + " " + "," + " " + "," + " " + "," + " ";
        /*dult0 += ", , ";*/
        resultList.add(dult0);
        //for (String name : names)
        String descr = "";
        String ignIds = configService.selectConfigByKey("atd.ignore.ids");
        //获取
        for (Integer id : ids) {
            if (ignIds.indexOf(("#"+id+"#")) >= 0) continue;
            String name = atdStatisticFormService.selectNameById(id);
            //初始化字符串
            String dult1 = id + "," + name + "," + "上班";
            String dult2 = id + "," + " " + "," + "下班";
            //初始化出勤、迟到、早退天数
            int atdTotal = 0;///当天有打卡数据的，有2条或1条的，出勤加1/当天有打卡数据的，按姓名去重求和，纯备注不算 *倒班后未排班即0+倒班不记录
            int chidaoTotal = 0;//atdstatus为1的和+上下班数据对中只有下班打卡的（未备注）
            int zaotuiTotal = 0;//atdstatus为2的和+上下班数据对中只有上班打卡的（未备注）
            //核实出勤、迟到、早退
            int hsatdTotal = 0;///当天有考勤数据并且备注为null或者备注包括'忘"‘差’
            int hschidaoTotal = 0;//迟到无备注+只有下班有一条数据的（包含只有下班一个备注的和只有一个下班打卡数据）
            int hszaotuiTotal = 0;//早退无备注+只有上班有一条数据的（包含只有上班一个备注的和只有一个上班打卡数据）

            /*findAtdPlan.setUserId(id.longValue());
            int yingchuqin = atdPlanService.selectAtdPlanCount(findAtdPlan);*/

            for (String time : timeList) {
                //初始化日期数据
                String eTime = "," + "/"; //上午
                String aTime = "," + "/";//下午

                //1、计算出勤
                //1、当天有打卡数据的，计算出勤加1（去掉没打卡有备注的，即atdStatus=0的数据）
                List<AtdStatisticForm> dataList11 = dataList.stream()
                        // 首先过滤出满足条件的元素
                        .filter(item -> item.getTime().equals(time) && name.equals(item.getUsername())&& !"0".equals(item.getAtdstatus()))
                        // 使用Collectors.toMap来基于username去重，保留第一次出现的元素
                        .collect(Collectors.toMap(
                                AtdStatisticForm::getUsername, // 作为键的函数
                                item -> item, // 作为值的函数
                                (existing, replacement) -> existing // 如果键冲突，保留现有的元素
                        ))
                        // 将Map的值转换回List
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                List<AtdStatisticForm> dataList20 = dataList.stream() //前一天倒班第二天未安排的状态0数据不算出勤 desc
                        // 首先过滤出满足条件的元素
                        .filter(item -> item.getTime().equals(time) && name.equals(item.getUsername())&& "0".equals(item.getAtdstatus()) && "倒班".equals(item.getAtddesc()))
                        // 使用Collectors.toMap来基于username去重，保留第一次出现的元素
                        .collect(Collectors.toMap(
                                AtdStatisticForm::getUsername, // 作为键的函数
                                item -> item, // 作为值的函数
                                (existing, replacement) -> existing // 如果键冲突，保留现有的元素
                        ))
                        // 将Map的值转换回List
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                List<AtdPlan> planList11 = planList.stream()
                        // 首先过滤出满足条件的元素
                        .filter(item -> item.getPlanDate().equals(time) && name.equals(item.getUserName())&& !"0".equals(item.getEnable()))
                        // 使用Collectors.toMap来基于username去重，保留第一次出现的元素
                        .collect(Collectors.toMap(
                                AtdPlan::getUserName, // 作为键的函数
                                item -> item, // 作为值的函数
                                (existing, replacement) -> existing // 如果键冲突，保留现有的元素
                        ))
                        // 将Map的值转换回List
                        .values()
                        .stream()
                        .collect(Collectors.toList());


                if( dataList11.size()!=0 && dataList20.size() <= 0){
                    atdTotal = atdTotal+1;
                }
                //2、核实出勤

                List<AtdStatisticForm> dataList12 = dataList.stream()
                        // 首先过滤掉null元素
                        .filter(Objects::nonNull)
                        // 然后根据条件过滤
                        .filter(item -> item.getTime().equals(time) && name.equals(item.getUsername()) &&
                                ((item.getAtddesckj() == null || (item.getAtddesckj().contains(":") || item.getAtddesckj().contains("：") || item.getAtddesckj().contains("忘") || item.getAtddesckj().contains("差")))||(
                                        !"0".equals(item.getAtdstatus())
                                )))
                        // 使用Collectors.toMap来基于username去重，保留第一次出现的元素
                        .collect(Collectors.toMap(
                                AtdStatisticForm::getUsername, // 作为键的函数
                                item -> item, // 作为值的函数
                                (existing, replacement) -> existing // 如果键冲突，保留现有的元素
                        ))
                        // 将Map的值转换回List
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                // if( dataList12.size()!=0 && dataList20.size() <= 0 && planList11.size()!=0){
                if( dataList12.size()!=0 && dataList20.size() <= 0){
                    hsatdTotal= hsatdTotal+1;
                }

                //3、计算一天只有一个打卡数据的，匹配每个人上下班数据(去掉没打卡有备注的数据库状态为atdstatus=0)
                List<AtdStatisticForm> dataList0 =  dataList.stream()
                        .filter(item->item.getTime().equals(time) && name.equals(item.getUsername())&&(!("0".equals(item.getAtdstatus())) || "倒班".equals(item.getAtddesc())))
                        .distinct()
                        .collect(Collectors.toList());

                //上下班数据中只有一个下班打卡的，迟到加1
                if(dataList0.size()==1 &&"2".equals(dataList0.get(0).getAtdtag())){
                    chidaoTotal +=1;
                }
                //上下班数据中只有一个上班打卡的,核实早退加1
                if(dataList0.size()==1 &&"1".equals(dataList0.get(0).getAtdtag())){
                    zaotuiTotal +=1;
                }
                //4、核实迟到：迟到无备注+只有下班有数据（含只有下班备注的）
                //4.1迟到无备注
                List<AtdStatisticForm> dataList4 =  dataList.stream()
                        .filter(item->item.getTime().equals(time) && name.equals(item.getUsername())&&("1".equals(item.getAtdstatus()))&&(item.getAtddesckj() == null ))
                        .distinct()
                        .collect(Collectors.toList());

                hschidaoTotal =hschidaoTotal+dataList4.size();


                //4.2只有下班有数据（含只有下班备注的）
                List<AtdStatisticForm> dataList41 =  dataList.stream()
                        .filter(item->item.getTime().equals(time) && name.equals(item.getUsername()))
                        .distinct()
                        .collect(Collectors.toList());
                if(dataList41.size()==1 &&"2".equals(dataList41.get(0).getAtdtag())){
                    hschidaoTotal +=1;
                }


                //5、核实早退：早退无备注+只有上班有数据（含只有上班有备注的）
                //5.1早退无备注
                List<AtdStatisticForm> dataList5 =  dataList.stream()
                        .filter(item->item.getTime().equals(time) && name.equals(item.getUsername())&&("2".equals(item.getAtdstatus()))&&(item.getAtddesckj() == null ))
                        .distinct()
                        .collect(Collectors.toList());

                hszaotuiTotal =hszaotuiTotal+dataList5.size();


                //4.2只有下班有数据（含只有下班备注的）
                List<AtdStatisticForm> dataList51 =  dataList.stream()
                        .filter(item->item.getTime().equals(time) && name.equals(item.getUsername()))
                        .distinct()
                        .collect(Collectors.toList());
                if(dataList51.size()==1 &&"1".equals(dataList51.get(0).getAtdtag())){
                    hszaotuiTotal +=1;
                }

                //遍历数据库数据SS
                for (AtdStatisticForm data : dataList) {
                    String[] s = data.getCreatetime().split(" "); //获取时间
                    String atdStatus = data.getAtdstatus();
                    String atddesc = data.getAtddesc();
                    String atddesckj = data.getAtddesckj();//获取科级修改内容
                    String createTime = data.getCreatetime();

                    //判断姓名  时间 相等
                    //上班
                    if (name.equals(data.getUsername()) && time.equals(data.getTime()) && "1".equals(data.getAtdtag())) {

                        if (atddesckj != null) {
                            eTime = "," + s[1].substring(0, 5) + ";" + atdStatus + ";" + ((atddesc == null) ? "" : atddesc) + " |" + atddesckj; //时间获取到分,如果科级有修改获取修改内容
                        } else {
                            eTime = "," + s[1].substring(0, 5) + ";" + atdStatus + ";" + atddesc;
                        }
                        //迟到chidaoTotal：atdstatus为1的和+上下班数据对中只有下班打卡的（未备注）
                        if ("1".equals(atdStatus)) {
                            chidaoTotal = chidaoTotal + 1;//迟到

                        }
                    }
                    //下班
                    if (name.equals(data.getUsername()) && time.equals(data.getTime()) && "2".equals(data.getAtdtag())) {
                        if (atddesckj != null) {
                            aTime = "," + s[1].substring(0, 5) + ";" + atdStatus + ";" + ((atddesc == null) ? "" : atddesc) + " |" + atddesckj;//时间获取到分
                        } else {
                            aTime = "," + s[1].substring(0, 5) + ";" + atdStatus + ";" + atddesc;
                        }

                        //早退zaotuiTotal：atdstatus为2的和+上下班数据对中只有上班打卡的（未备注）

                        if ("2".equals(atdStatus)) {
                            zaotuiTotal = zaotuiTotal + 1;//早退

                        }

                    }

                }
                dult1 = dult1 + eTime;
                dult2 = dult2 + aTime;

            }
            dult1 = dult1 + "," + atdTotal + "," + chidaoTotal + "," + zaotuiTotal + "," + hsatdTotal + "," + hschidaoTotal + "," + hszaotuiTotal;
            //dult1 = dult1 + "," + atdTotal + "," + chidaoTotal + "," + zaotuiTotal+ "," +" " + "," + " " + "," +" ";
            dult2 = dult2 + "," + " " + "," + " " + "," + " " + "," + " " + "," + " " + "," + " ";
            //考勤绩效
           /* int kqCount = hschidaoTotal + hszaotuiTotal;
            kqCount += (yingchuqin - hsatdTotal);
            String kqdj = "A";
            if (kqCount >= 6 && kqCount <= 10) kqdj = "B";
            if (kqCount > 10) kqdj = "C";
            dult1 += ","+kqdj+","+yingchuqin;
            dult2 += ", , ";*/
            resultList.add(dult1);
            resultList.add(dult2);
            /*if (atdId != null && !kqdj.equals("A")){
                descr += name+","+kqdj+","+hschidaoTotal+","+hszaotuiTotal+"#";
            }*/
        }
        if (atdId != null){
            if (descr.endsWith("#")) descr = descr.substring(0, descr.length()-1);
            FinAtd finAtd = new FinAtd();
            finAtd.setRectype(descr);
            finAtd.setId(atdId);
            atdService.updateFinAtd(finAtd);
        }
        //如果查询时间为空   默认查询本月的
        return getDataTable(resultList);
    }


    /**
     * 导出考勤表统计列表
     */
    @Log(title = "考勤表统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AtdStatisticForm atdStatisticForm) throws ParseException {

        String startDate0 = atdStatisticForm.getStarttime();
        String endDate0 = atdStatisticForm.getEndtime();
        String deptname0 = atdStatisticForm.getDeptname();
        String username0 = atdStatisticForm.getUsername();

        //查询时间为空获取本月第一天和今天
        if (startDate0 == null || startDate0.length() == 0) {
            //获取当前日期年月日
            LocalDate today = LocalDate.now();
            //本月的第一天年月日
            LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
            //LocalDate转String
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            endDate0 = today.format(df);
            startDate0 = firstday.format(df);
        }
        List<String> headList = getBetweenDate(startDate0, endDate0);//表头列表
        TableDataInfo resList = list(null, startDate0, endDate0, deptname0, username0);
        List<?> tableList = resList.getRows();

        headList.add(0, "姓名");
        headList.add(1, "日期");
        headList.add("出勤");
        headList.add("迟到");
        headList.add("早退");
        headList.add("核实出勤");
        headList.add("核实迟到");
        headList.add("核实早退");
        /*headList.add("考勤等级");
        headList.add("应出勤");*/
        OutputStream out = null;
        try {
            writeExcel(tableList, headList);
            String mothName = "";
            mothName = endDate0.substring(5, 6);
            if ("0".equals(mothName)) {
                sName = endDate0.substring(6, 7);
            } else {
                sName = endDate0.substring(5, 7);
            }
            sheetName = "考勤信息导出数据" + sName + "月";
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void writeExcel(List<?> list, List<String> headList) {
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
        cs_header.setBorderBottom(BorderStyle.THIN);
        cs_header.setBorderLeft(BorderStyle.THIN);
        cs_header.setBorderRight(BorderStyle.THIN);
        cs_header.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_header.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_header.setVerticalAlignment(VerticalAlignment.CENTER);
        //前景填充色
        cs_header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        //设置前景填充样式
        cs_header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //1、设置标题行1
        row = sheet.createRow(0);
        //设置单元格行高
        row.setHeightInPoints(24);
   /*     //设置标题
        String[] headers=headList.toString().split(",");*/

        //逐个设置标题样式
        for (int i = 0; i < headList.size(); i++) {
            //创建单元格
            cell = row.createCell(i);
            //设置单元格内容
            String val = headList.get(headList.size() - 9).substring(5, 6);
            String smoth;
            String syear;
            if ("0".equals(val)) {
                smoth = headList.get(headList.size() - 9).substring(6, 7);
            } else {
                smoth = headList.get(headList.size() - 9).substring(5, 7);
            }
            syear = headList.get(headList.size() - 9).substring(0, 4);
            String xlsName = syear + "年" + smoth + "月考勤表";
            cell.setCellValue(xlsName);
            //设置单元格样式
            cell.setCellStyle(cs_header);

        }


        //2、设置标题行2
        row = sheet.createRow(1);
        //设置单元格行高
        row.setHeightInPoints(24);
   /*     //设置标题
        String[] headers=headList.toString().split(",");*/

        //逐个设置标题样式
        for (int i = 0; i < headList.size(); i++) {
            //创建单元格
            cell = row.createCell(i);
            //设置单元格内容
            String val = headList.get(i);
            if (val.length() > 4) {
                val = headList.get(i).substring(8);

            }
            //val = headList.get(i);
            cell.setCellValue(val);
            //设置单元格样式
            cell.setCellStyle(cs_header);

        }
        //创建文本单元格样式
        CellStyle cs_text = wb.createCellStyle();
        //创建文字设置
        Font textFont = wb.createFont();
        //设置文字类型
        textFont.setFontName("Arial");//Consolas
        //设置文字大小
        textFont.setFontHeightInPoints((short) 10);
        //应用设置
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
        //调取数据
        //记录总共多少列（由于接口查询出来的实体类集合，所以不好循环，使用）
        Integer cellSum = 0;
        //将数据写入表格
        for (int i = 0; i < list.size(); i++) {
            //将实体类集合转成string数组，通过‘，’，获取总共多少列
            String entityString = list.get(i).toString();
            String[] split = entityString.split(",");
            cellSum = split.length - 1;
            //创建行，由于0行是标题，所以+1
            row = sheet.createRow(i + 2);
            //实体类集合不太好循环，所以逐一设置，如果是其他则可使用for循环
            for (int j = 0; j < cellSum; j++) {
                String[] atdtime = split[j + 1].split(";", 3);
                if (atdtime.length == 1) {
                    row.createCell(j).setCellValue(atdtime[0]);
                    //设置单元格样式
                    row.getCell(j).setCellStyle(cs_text);
                } else {

                    //创建文本单元格样式
                    CellStyle cs_cell = wb.createCellStyle();
                    //创建文字设置
                    Font textFont1 = wb.createFont();
                    //设置文字类型
                    textFont1.setFontName("Arial");//Consolas
                    //设置文字大小
                    textFont1.setFontHeightInPoints((short) 10);
                    //应用设置
                    cs_cell.setFont(textFont1);
                    //设置边框
                    cs_cell.setBorderBottom(BorderStyle.THIN);
                    cs_cell.setBorderLeft(BorderStyle.THIN);
                    cs_cell.setBorderRight(BorderStyle.THIN);
                    cs_cell.setBorderTop(BorderStyle.THIN);
                    //水平居中
                    cs_cell.setAlignment(HorizontalAlignment.CENTER);
                    //垂直居中
                    cs_cell.setVerticalAlignment(VerticalAlignment.CENTER);
                    if ("null".equals(atdtime[2]) || (" |".equals(atdtime[2]))) {
                        atdtime[2] = atdtime[2].replace(" |", "");
                    }
                    if ("1".equals(atdtime[1])) {//迟到

                        String value = atdtime[0] + " " + atdtime[2];
                        String newValue = value.replace("null", "");
                        row.createCell(j).setCellValue(newValue);

                        //设置单元格样式
                        //cs_cell.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
                        //设置前景填充样式
                        //cs_cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        row.getCell(j).setCellStyle(cs_cell);

                    } else if ("2".equals(atdtime[1])) {

                        String value = atdtime[0] + " " + atdtime[2];
                        String newValue = value.replace("null", "");
                        row.createCell(j).setCellValue(newValue);
                        //设置单元格样式
                        //cs_cell.setFillForegroundColor(IndexedColors.RED.index);
                        //设置前景填充样式
                        //cs_cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        row.getCell(j).setCellStyle(cs_cell);


                    } else if ("0".equals(atdtime[1])) {
                        String value = atdtime[2];
                        String newValue = value.replace("null", "");
                        row.createCell(j).setCellValue(newValue);
                        //设置单元格样式
                        cs_cell.setFillForegroundColor(IndexedColors.GREEN.index);
                        //设置前景填充样式
                        //cs_cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        //设置单元格样式
                        row.getCell(j).setCellStyle(cs_text);

                    } else {
                        String value = atdtime[0] + " " + atdtime[2];
                        String newValue = value.replace("null", "");
                        row.createCell(j).setCellValue(newValue);
                        //设置单元格样式
                        row.getCell(j).setCellStyle(cs_text);
                    }
                }
            }

        }

        //合并单元格，横向
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, cellSum - 1));

   /*     //合并单元格，横向
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 11, 12));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1,1,3,4));
        //竖着合并
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1,list.size(),4,4));*/
        for (int i = 1; i < list.size() + 1; i = i + 2) {
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 0, 0));//第一列合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 1, cellSum - 1));//核实早退合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 2, cellSum - 2));//核实迟到合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 3, cellSum - 3));//核实出勤合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 4, cellSum - 4));//早退合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 5, cellSum - 5));//迟到合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 6, cellSum - 6));//出勤合并单元格
            /*sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 7, cellSum - 7));//考勤等级
            sheet.addMergedRegion(new CellRangeAddress(i, i + 1, cellSum - 8, cellSum - 8));//应出勤*/
        }

        //设置单元格宽度自适应
        for (int i = 0; i <= cellSum; i++) {
            sheet.autoSizeColumn((short) i, true); //自动调整列宽
        }
    }

    /**
     * 新增考勤表统计
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤表统计
     */
    @Log(title = "考勤表统计", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AtdStatisticForm atdStatisticForm) {
        return toAjax(atdStatisticFormService.insertAtdStatisticForm(atdStatisticForm));
    }

    /**
     * 修改考勤表统计
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        AtdStatisticForm atdStatisticForm = atdStatisticFormService.selectAtdStatisticFormById(id);
        mmap.put("atdStatisticForm", atdStatisticForm);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤表统计
     */
    @Log(title = "科长修改考勤表", businessType = BusinessType.UPDATE)
    /* @RequiresRoles("lowlead")*/
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxResult editSave(@RequestParam("userId") String index, @RequestParam("field") String field, @RequestParam("value") String opervalue, @RequestParam("atd") String atd) {

        //1、获取当前登录用户名
        User sysUser = getSysUser();
        String userName = sysUser.getUserName();
        //2、获取当前登录角色
        Long userId = sysUser.getUserId();
        List<UserRole> userRoles = userService.selectUserRoleByUserId(userId);
        List<Long> roleIdList = new ArrayList<Long>();
        for (int i=0; i<userRoles.size(); i++) {
            UserRole userRole = userRoles.get(i);
            roleIdList.add(userRole.getRoleId());
        }
        //根据userid获取登录人、打卡人部门
        Long kqdeptId = userService.selectUserById(Long.valueOf(index)).getDeptId();
        Long deptId = userService.selectUserById(Long.valueOf(userId)).getDeptId();
        Dept kqdept = deptService.selectDeptById(kqdeptId);
        Long pkqdeptId = kqdept.getParentId();
        //3、获取当前时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = new Date();
        String dateString = sf.format(newDate).replace("T", "");
        AjaxResult result = new AjaxResult();
        //姓名、出勤合计不许修改
        if (field.length() < 8) {
            result.put("mark", 3);
        } else {
            //科室科级修改权限对本科室roleId == 103    部级修改权限对所有人
            if (roleIdList.contains(1l) || roleIdList.contains(2l) || roleIdList.contains(103l) || roleIdList.contains(104l) || roleIdList.contains(106l)) {

                AtdStatisticForm atdStatisticForm = new AtdStatisticForm();
                atdStatisticForm.setUserid(Long.valueOf(index));
                atdStatisticForm.setAtdtimestring(field);
                atdStatisticForm.setAtddesckj(opervalue);

                String atd_value;
                if ("上班".equals(atd)) {
                    atd_value = "1";
                } else if ("下班".equals(atd)) {
                    atd_value = "2";
                } else {
                    atd_value = "";
                }
                atdStatisticForm.setAtdtime(StrToDate(field));
                atdStatisticForm.setAtdtag(atd_value);
                atdStatisticForm.setOperatetime(dateString);
                atdStatisticForm.setOperatename(userName);
                //没打卡情况下，判断是否出勤，出勤时科长添加备注自动增加一条出勤记录
                AtdStatisticForm tag = atdStatisticFormService.selectAtdStatistic(atdStatisticForm);
                int flag = 0;
                if (tag != null) {
                    if ("0".equals(tag.getAtdstatus()) && (opervalue == null || "".equals(opervalue))) {
                        flag = atdStatisticFormService.deleteAtdStatisticFormById(tag.getId());

                    } else {
                        flag = atdStatisticFormService.updateAtdStatisticForm(atdStatisticForm);
                    }


                } else {
                    atdStatisticForm.setKjcreateTime(StrToDate(field + " 08:00:00"));
                    atdStatisticForm.setAtdstatus("0");
                    flag = atdStatisticFormService.insertAtdStatisticForm(atdStatisticForm);
                }
                result.put("mark", flag);
            } else {
                result.put("mark", 2);
            }
        }
        return result;
    }

    /**
     * 删除考勤表统计
     */
    @Log(title = "考勤表统计", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(atdStatisticFormService.deleteAtdStatisticFormByIds(ids));
    }


    /**
     * 获取两个日期之间的所有日期 (年月日)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getBetweenDate(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        // 转化成日期类型
        Date startDate = new Date();
        Date endDate = new Date();
        if (!"".equals(startTime) && "".equals(endTime)) {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(startTime);
        } else if (!"".equals(endTime) && "".equals(startTime)) {
            startDate = sdf.parse(endTime);
            endDate = sdf.parse(endTime);
        } else if (!"".equals(endTime) && !"".equals(startTime)) {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        }

        //用Calendar 进行日期比较判断
        Calendar calendar = Calendar.getInstance();
        while (startDate.getTime() <= endDate.getTime()) {
            // 把日期添加到集合
            list.add(sdf.format(startDate));
            // 设置日期
            calendar.setTime(startDate);
            //把日期增加一天
            calendar.add(Calendar.DATE, 1);
            // 获取增加后的日期
            startDate = calendar.getTime();
        }
        return list;
    }

    /**
     * 根据日期获取 星期
     *
     * @param date
     * @return
     */
    public String dateToWeek(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
