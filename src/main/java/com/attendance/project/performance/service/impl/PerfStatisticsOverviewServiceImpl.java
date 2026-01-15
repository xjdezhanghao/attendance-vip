package com.attendance.project.performance.service.impl;

import com.attendance.common.utils.DateUtils;
import com.attendance.common.utils.text.Convert;
import com.attendance.project.performance.domain.*;
import com.attendance.project.performance.mapper.*;
import com.attendance.project.performance.service.IPerfGatherOverviewService;
import com.attendance.project.performance.service.IPerfStatisticsOverviewService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * 绩效采集主Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class PerfStatisticsOverviewServiceImpl implements IPerfStatisticsOverviewService
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
    private PerfStatisticsOverviewMapper perfStatisticsOverviewMapper;

    @Autowired
    private PerfUserPostMapper perfUserPostMapper;

    @Autowired
    private PerfPostMapper perfPostMapper;

    @Override
    public PerfStatisticsOverview selectPerfStatisticsOverviewByOverviewId(Long overviewId) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewByOverviewId(overviewId);
    }

    @Override
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewList(PerfStatisticsOverview perfStatisticsOverview) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewList(perfStatisticsOverview);
    }

    @Override
    public List<PerfStatisticsOverview> selectPerfStatisticsOverviewListAll(PerfStatisticsOverview perfStatisticsOverview) {
        return perfStatisticsOverviewMapper.selectPerfStatisticsOverviewListAll(perfStatisticsOverview);
    }
}