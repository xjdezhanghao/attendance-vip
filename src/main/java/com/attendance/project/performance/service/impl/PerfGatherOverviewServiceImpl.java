package com.attendance.project.performance.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.attendance.common.utils.DateUtils;
import com.attendance.project.performance.domain.PerfGatherDetail;
import com.attendance.project.performance.domain.PerfIndProject;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.mapper.*;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.attendance.project.performance.domain.PerfGatherOverview;
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
}
