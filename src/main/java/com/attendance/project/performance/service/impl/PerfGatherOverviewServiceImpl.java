package com.attendance.project.performance.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.attendance.common.utils.DateUtils;
import com.attendance.project.performance.domain.*;
import com.attendance.project.performance.mapper.*;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.mapper.UserMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 绩效采集主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class PerfGatherOverviewServiceImpl implements IPerfGatherOverviewService 
{
    @Autowired
    private PerfGatherOverviewMapper perfGatherOverviewMapper;

    @Autowired
    private PerfIndProjectMapper perfIndProjectMapper;

    @Autowired
    private PerfIndItemMapper  perfIndItemMapper;

    @Autowired
    private PerfUserParamMapper perfUserParamMapper;

    @Autowired
    private PerfGatherDetailMapper perfGatherDetailMapper;

    @Autowired
    private PerfUserPostMapper perfUserPostMapper;

    @Autowired
    private PerfPostMapper perfPostMapper;

    /**
     * 查询绩效采集主
     * 
     * @param overviewId 绩效采集主主键
     * @return 绩效采集主
     */
    @Override
    public PerfGatherOverview selectPerfGatherOverviewByOverviewId(Long overviewId)
    {
        return perfGatherOverviewMapper.selectPerfGatherOverviewByOverviewId(overviewId);
    }

    /**
     * 查询绩效采集主列表
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 绩效采集主
     */
    @Override
    public List<PerfGatherOverview> selectPerfGatherOverviewList(PerfGatherOverview perfGatherOverview)
    {
        return perfGatherOverviewMapper.selectPerfGatherOverviewList(perfGatherOverview);
    }

    @Override
    public List<PerfGatherOverview> selectPerfGatherOverviewListAll(PerfGatherOverview perfGatherOverview) {
        return perfGatherOverviewMapper.selectPerfGatherOverviewListAll(perfGatherOverview);
    }

    /**
     * 新增绩效采集主
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 结果
     */
    @Override
    public int insertPerfGatherOverview(PerfGatherOverview perfGatherOverview)
    {
        perfGatherOverview.setCreateTime(DateUtils.getNowDate());
        return perfGatherOverviewMapper.insertPerfGatherOverview(perfGatherOverview);
    }

    /**
     * 修改绩效采集主
     * 
     * @param perfGatherOverview 绩效采集主
     * @return 结果
     */
    @Override
    public int updatePerfGatherOverview(PerfGatherOverview perfGatherOverview)
    {
        perfGatherOverview.setUpdateTime(DateUtils.getNowDate());
        return perfGatherOverviewMapper.updatePerfGatherOverview(perfGatherOverview);
    }

    /**
     * 批量删除绩效采集主
     * 
     * @param overviewIds 需要删除的绩效采集主主键
     * @return 结果
     */
    @Override
    public int deletePerfGatherOverviewByOverviewIds(String overviewIds)
    {
        return perfGatherOverviewMapper.deletePerfGatherOverviewByOverviewIds(Convert.toStrArray(overviewIds));
    }

    /**
     * 删除绩效采集主信息
     * 
     * @param overviewId 绩效采集主主键
     * @return 结果
     */
    @Override
    public int deletePerfGatherOverviewByOverviewId(Long overviewId)
    {
        return perfGatherOverviewMapper.deletePerfGatherOverviewByOverviewId(overviewId);
    }


    @Override
    @Transactional
    public void generateDateGatherRecords() {
        // 获取当前月份
        String currentDate = DateUtils.dateTimeNow("yyyy-MM-DD");

        // 查询生效中的考核项目
        PerfIndProject projectQuery = new PerfIndProject();
        List<PerfIndProject> activeProjects = perfIndProjectMapper.selectPerfIndProjectList(projectQuery);

        // 为每个考核项目生成对应的采集记录
        for (PerfIndProject project : activeProjects) {
            generateGatherRecordsForProject(project, currentDate);
        }
    }

    /**
     * 为特定考核项目生成采集记录
     * 每个人根据其部门岗位设置可能存在多条采集记录
     */
    private void generateGatherRecordsForProject(PerfIndProject project, String gatherDate) {
        // 根据考核项目的层级和关联条件查询对应人员
        List<PerfUserParam> users = findUsersByProjectConditions(project);

        // 为每个用户生成采集记录（如果不存在）
        for (PerfUserParam user : users) {
            // 检查是否已存在当月的采集记录
            PerfGatherOverview checkRecord = new PerfGatherOverview();
            checkRecord.setUserId(user.getUserId());
            checkRecord.setProjectId(project.getProjectId());
            checkRecord.setGatherDate(gatherDate);

            List<PerfGatherOverview> existingRecords = perfGatherOverviewMapper.selectPerfGatherOverviewList(checkRecord);

            if (existingRecords.isEmpty()) {
                // 创建新的采集记录
                PerfGatherOverview newRecord = new PerfGatherOverview();
                newRecord.setUserId(user.getUserId());
                newRecord.setDeptId(user.getDeptId());
                newRecord.setProjectId(project.getProjectId());
                newRecord.setGatherDate(gatherDate);
                newRecord.setTotalScore(new BigDecimal(0)); // 默认分数
                newRecord.setGatherStatus(0); // 0未采集
                newRecord.setCreateTime(DateUtils.getNowDate());

                perfGatherOverviewMapper.insertPerfGatherOverview(newRecord);
            }
        }
    }

    /**
     * 根据考核项目条件查询对应用户
     */
    private List<PerfUserParam> findUsersByProjectConditions(PerfIndProject project) {
        // 创建用户查询条件
        PerfUserParam paramQuery = new PerfUserParam();

        if (project.getDeptId() != null) {
            // 根据具体部门ID查询
            paramQuery.setDeptId(project.getDeptId());
        }

        if (project.getPostId() != null) {
            // 需要结合岗位信息查询
            paramQuery.setPostId(project.getPostId());
        }

        // 直接根据用户查询条件查询
        return perfUserParamMapper.selectPerfUserParamList(paramQuery);
    }



    @Override
    public void updateScoresAndRemarks(Long overviewId, Map<Long, BigDecimal> scores, Map<Long, String> remarks, Map<Long, String> imagePaths) {
        // 删除现有的考核项详情
        PerfGatherDetail deleteParam = new PerfGatherDetail();
        deleteParam.setOverviewId(overviewId);
        perfGatherDetailMapper.deletePerfGatherDetailByOverviewId(deleteParam);

        // 获取该项目的考核项基础信息（从项目配置表中获取）
        PerfGatherOverview overview = selectPerfGatherOverviewByOverviewId(overviewId);
        Long projectId = overview.getProjectId();

        // 查询该项目的所有考核项基础信息
        PerfGatherDetail baseParam = new PerfGatherDetail();
        baseParam.setProjectId(projectId);
        List<PerfGatherDetail> baseDetails = perfGatherDetailMapper.selectPerfGatherDetailList(baseParam);

        // 重新插入考核项详情
        for (PerfGatherDetail baseDetail : baseDetails) {
            Long itemId = baseDetail.getItemId();

            PerfGatherDetail detail = new PerfGatherDetail();
            detail.setOverviewId(overviewId);
            detail.setProjectId(projectId);
            detail.setCategoryId(baseDetail.getCategoryId());
            detail.setItemId(itemId);
            detail.setItemName(baseDetail.getItemName());
            detail.setRuleDesc(baseDetail.getRuleDesc());
            detail.setScoreMin(baseDetail.getScoreMin());
            detail.setScoreMax(baseDetail.getScoreMax());

            // 设置评分
            if (scores != null && scores.containsKey(itemId)) {
                detail.setItemScore(scores.get(itemId));
            }

            // 设置备注
            if (remarks != null && remarks.containsKey(itemId)) {
                detail.setItemRemark(remarks.get(itemId));
            }

            // 设置图片路径
            if (imagePaths != null && imagePaths.containsKey(itemId)) {
                detail.setImagePath(imagePaths.get(itemId));
            }

            detail.setCreateTime(DateUtils.getNowDate());
            detail.setUpdateTime(DateUtils.getNowDate());

            perfGatherDetailMapper.insertPerfGatherDetail(detail);
        }
    }

    @Override
    public Workbook generateGatherTemplate(List<PerfGatherOverview> overviewList) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();

        // 按userId分组，构建Sheet
        Map<Long, PerfGatherOverview> overviewMap = new LinkedHashMap<>();
        for (PerfGatherOverview overview : overviewList) {
            // 每个userId只保留一个overview，并获取第一个
            if (!overviewMap.containsKey(overview.getUserId())) {
                overviewMap.put(overview.getUserId(), overview);
            }
        }

        // 为每个人员创建一个Sheet
        for (PerfGatherOverview overview : overviewMap.values()) {
            createGatherTemplateSheet(wb, overview);
        }

        return wb;
    }

    /**
     * 为单个人员创建采集模板Sheet
     */
    private void createGatherTemplateSheet(XSSFWorkbook wb, PerfGatherOverview overview) throws Exception {
        // Sheet名为userId（作为唯一标识）
        String sheetName = overview.getUserName() + "-" + overview.getUserId();
        XSSFSheet sheet = wb.createSheet(sheetName);

        // 创建样式
        CellStyle headerStyle = createHeaderStyle(wb);
        CellStyle titleStyle = createTitleStyle(wb);
        CellStyle dataStyle = createDataStyle(wb);
        CellStyle hiddenStyle = createHiddenStyle(wb);

        // 第0行：项目名称和姓名
        XSSFRow row0 = sheet.createRow(0);
        row0.setHeightInPoints(24);

        XSSFCell titleCell = row0.createCell(0);
        titleCell.setCellStyle(titleStyle);

        // 查询项目名称
        PerfIndProject project = perfIndProjectMapper.selectPerfIndProjectByProjectId(overview.getProjectId());
        String projectName = (project != null) ? project.getProjectName() : "未知项目";
        String userInfo = projectName + " - " + overview. getUserName();
        titleCell.setCellValue(userInfo);

        // 合并单元格
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 8));

        // 第1行：采集时间
        XSSFRow row1 = sheet.createRow(1);
        row1.setHeightInPoints(24);
        XSSFCell dateCell = row1.createCell(0);
        dateCell.setCellValue(overview.getGatherDate());
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 1, 0, 8));


        // 第2行：列标题
        XSSFRow headerRow = sheet.createRow(2);
        headerRow.setHeightInPoints(20);

        String[] headers = {"类别", "项目", "得分", "备注", "itemId", "overviewId", "categoryId", "projectId", "scoreType"};
        for (int i = 0; i < headers.length; i++) {
            XSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue(headers[i]);
            // 隐藏itemId及之后的列
            if (i >= 4) {
                sheet.setColumnHidden(i, true);
            }
        }

        // 查询考核项目信息，按category和item的sort排序
        PerfIndItem indItem = new PerfIndItem();
        indItem.setProjectId(overview.getProjectId());
        List<PerfIndItem> gatherDetails = perfIndItemMapper.selectPerfIndItemCatProList(indItem);

        // 数据行从第2行开始
        int rowIndex = 3;
        if (gatherDetails != null && ! gatherDetails.isEmpty()) {
            for (PerfIndItem detail : gatherDetails) {
                XSSFRow dataRow = sheet.createRow(rowIndex);

                dataRow.setHeightInPoints(45);

                // B列：小项描述
                XSSFCell cell1 = dataRow.createCell(1);
                cell1.setCellStyle(dataStyle);
                cell1.setCellValue(detail.getRuleDesc() != null ? detail.getRuleDesc() : "");

                // C列：得分（可填写）
                XSSFCell cell2 = dataRow.createCell(2);
                cell2.setCellStyle(dataStyle);
                // 如果有existing score则填充，否则为空

                // D列：备注（可填写）
                XSSFCell cell3 = dataRow.createCell(3);
                cell3.setCellStyle(dataStyle);
                cell3.setCellValue("");

                // E列：itemId（隐藏列，用于导入时定位）
                XSSFCell cell4 = dataRow.createCell(4);
                cell4.setCellStyle(hiddenStyle);
                cell4.setCellValue(detail.getItemId() != null ? detail.getItemId() : 0);

                // E列：overviewId（隐藏列，用于导入时定位）
                XSSFCell cell5 = dataRow.createCell(5);
                cell5.setCellStyle(hiddenStyle);
                cell5.setCellValue(overview.getOverviewId() != null ? overview.getOverviewId() : 0);
                // E列：categoryId（隐藏列，用于导入时定位）
                XSSFCell cell6 = dataRow.createCell(6);
                cell6.setCellStyle(hiddenStyle);
                cell6.setCellValue(detail.getCategoryId() != null ? detail.getCategoryId() : 0);
                // E列：projectId（隐藏列，用于导入时定位）
                XSSFCell cell7 = dataRow.createCell(7);
                cell7.setCellStyle(hiddenStyle);
                cell7.setCellValue(detail.getProjectId() != null ? detail.getProjectId() : 0);
                // E列：scoreType（隐藏列，用于导入时定位）
                XSSFCell cell8 = dataRow.createCell(8);
                cell8.setCellStyle(hiddenStyle);
                cell8.setCellValue(detail.getScoreType() != null ? detail.getScoreType() : "");

                rowIndex++;
            }
        }

        // 设置列宽
        sheet.setColumnWidth(0, 256 * 15); // 大类
        sheet.setColumnWidth(1, 256 * 50); // 小项描述
        sheet.setColumnWidth(2, 256 * 10); // 得分
        sheet.setColumnWidth(3, 256 * 30); // 备注
        sheet.setColumnWidth(4, 256 * 10); // itemId（隐藏）
        sheet.setColumnWidth(5, 256 * 10); // overviewId（隐藏）
        sheet.setColumnWidth(6, 256 * 10); // categoryId（隐藏）
        sheet.setColumnWidth(7, 256 * 10); // projectId（隐藏）
        sheet.setColumnWidth(8, 256 * 10); // scoreType（隐藏）
    }

    /**
     * 创建标题行样式
     */
    private CellStyle createTitleStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建表头样式
     */
    private CellStyle createHeaderStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle. THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    /**
     * 创建数据行样式
     */
    private CellStyle createDataStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle. THIN);
        style.setWrapText(true);
        return style;
    }

    /**
     * 创建隐藏列样式
     */
    private CellStyle createHiddenStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle. THIN);
        return style;
    }
}
