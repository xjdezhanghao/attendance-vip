package com.attendance.project.performance.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.attendance.common.utils.StringUtils;
import com.attendance.project.performance.domain.PerfGatherDetail;
import com.attendance.project.performance.statistics.dto.StatisticsDetailQuery;
import com.attendance.project.performance.statistics.dto.StatisticsDetailRow;
import com.attendance.project.performance.statistics.dto.StatisticsOverviewQuery;
import com.attendance.project.performance.statistics.dto.StatisticsProjectSummaryRow;
import com.attendance.project.performance.statistics.mapper.PerfStatisticsMapper;
import com.attendance.project.performance.statistics.service.IPerfStatisticsService;
import com.attendance.project.performance.statistics.vo.StatisticsDetailItemVO;
import com.attendance.project.performance.statistics.vo.StatisticsDetailView;
import com.attendance.project.performance.statistics.vo.StatisticsOverviewVO;
import com.attendance.project.performance.statistics.vo.StatisticsProjectOverviewVO;
import com.attendance.project.performance.service.IPerfGatherDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfStatisticsServiceImpl implements IPerfStatisticsService {
    @Autowired
    private PerfStatisticsMapper statisticsMapper;

    @Autowired
    private IPerfGatherDetailService gatherDetailService;

    @Override
    public List<StatisticsOverviewVO> selectStatisticsOverviewList(StatisticsOverviewQuery query) {
        return statisticsMapper.selectStatisticsOverviewList(query);
    }

    @Override
    public List<StatisticsOverviewVO> selectStatisticsDailyList(StatisticsOverviewQuery query) {
        return statisticsMapper.selectStatisticsDailyList(query);
    }

    @Override
    public StatisticsDetailView buildDailyDetail(StatisticsDetailQuery query) {
        List<StatisticsProjectOverviewVO> overviewList = statisticsMapper.selectDailyProjectOverviewList(query);
        List<List<StatisticsDetailItemVO>> detailList = new ArrayList<>();
        for (StatisticsProjectOverviewVO overview : overviewList) {
            PerfGatherDetail detailParam = new PerfGatherDetail();
            detailParam.setProjectId(overview.getProjectId());
            detailParam.setOverviewId(overview.getOverviewId());
            List<PerfGatherDetail> details = gatherDetailService.selectPerfGatherDetailList(detailParam);
            detailList.add(convertDailyDetails(details));
        }
        StatisticsDetailView view = new StatisticsDetailView();
        view.setOverviewList(overviewList);
        view.setDetailList(detailList);
        return view;
    }

    @Override
    public StatisticsDetailView buildRangeDetail(StatisticsDetailQuery query) {
        List<StatisticsProjectSummaryRow> summaryRows = statisticsMapper.selectRangeProjectSummaryList(query);
        Map<Long, StatisticsProjectOverviewVO> summaryMap = new LinkedHashMap<>();
        for (StatisticsProjectSummaryRow row : summaryRows) {
            StatisticsProjectOverviewVO overview = summaryMap.computeIfAbsent(row.getProjectId(), key -> {
                StatisticsProjectOverviewVO item = new StatisticsProjectOverviewVO();
                item.setProjectId(row.getProjectId());
                item.setProjectName(row.getProjectName());
                item.setTotalScore(BigDecimal.ZERO);
                item.setRemark("");
                return item;
            });
            if (row.getTotalScore() != null) {
                overview.setTotalScore(overview.getTotalScore().add(row.getTotalScore()));
            }
            if (StringUtils.isNotBlank(row.getRemark())) {
                String remark = overview.getRemark();
                overview.setRemark(StringUtils.isBlank(remark) ? row.getRemark() : remark + "；" + row.getRemark());
            }
        }

        List<StatisticsDetailRow> detailRows = statisticsMapper.selectRangeDetailRows(query);
        Map<Long, LinkedHashMap<Long, AggregationHolder>> projectItemMap = new LinkedHashMap<>();
        for (StatisticsDetailRow row : detailRows) {
            LinkedHashMap<Long, AggregationHolder> itemMap =
                projectItemMap.computeIfAbsent(row.getProjectId(), key -> new LinkedHashMap<>());
            AggregationHolder holder = itemMap.computeIfAbsent(row.getItemId(), key -> {
                AggregationHolder item = new AggregationHolder();
                item.item.setProjectId(row.getProjectId());
                item.item.setProjectName(row.getProjectName());
                item.item.setCategoryId(row.getCategoryId());
                item.item.setCategoryName(row.getCategoryName());
                item.item.setItemId(row.getItemId());
                item.item.setItemName(row.getItemName());
                item.item.setRuleDesc(row.getRuleDesc());
                item.item.setScoreMin(row.getScoreMin());
                item.item.setScoreMax(row.getScoreMax());
                item.item.setScoreType(row.getScoreType());
                item.item.setItemScore(BigDecimal.ZERO);
                item.item.setItemRemark("");
                return item;
            });

            BigDecimal score = row.getItemScore() == null ? BigDecimal.ZERO : row.getItemScore();
            BigDecimal signedScore = applyScoreType(score, row.getScoreType());
            holder.item.setItemScore(holder.item.getItemScore().add(signedScore));

            if (StringUtils.isNotBlank(row.getItemRemark())) {
                holder.appendRemark(row.getItemRemark());
            }
            holder.updateImage(row);
        }

        List<StatisticsProjectOverviewVO> overviewList = new ArrayList<>(summaryMap.values());
        List<List<StatisticsDetailItemVO>> detailList = new ArrayList<>();
        for (StatisticsProjectOverviewVO overview : overviewList) {
            LinkedHashMap<Long, AggregationHolder> items = projectItemMap.get(overview.getProjectId());
            List<StatisticsDetailItemVO> list = new ArrayList<>();
            if (items != null) {
                for (AggregationHolder holder : items.values()) {
                    if (StringUtils.isNotBlank(holder.item.getItemRemark())) {
                        holder.item.setItemRemark(holder.item.getItemRemark());
                    } else {
                        holder.item.setItemRemark("");
                    }
                    holder.item.setImagePath(holder.imagePath);
                    list.add(holder.item);
                }
            }
            detailList.add(list);
        }

        StatisticsDetailView view = new StatisticsDetailView();
        view.setOverviewList(overviewList);
        view.setDetailList(detailList);
        return view;
    }

    private List<StatisticsDetailItemVO> convertDailyDetails(List<PerfGatherDetail> details) {
        List<StatisticsDetailItemVO> result = new ArrayList<>();
        if (details == null) {
            return result;
        }
        for (PerfGatherDetail detail : details) {
            StatisticsDetailItemVO item = new StatisticsDetailItemVO();
            item.setProjectId(detail.getProjectId());
            item.setCategoryId(detail.getCategoryId());
            item.setCategoryName(detail.getCategoryName());
            item.setItemId(detail.getItemId());
            item.setItemName(detail.getItemName());
            item.setRuleDesc(detail.getRuleDesc());
            item.setScoreMin(detail.getScoreMin());
            item.setScoreMax(detail.getScoreMax());
            item.setScoreType(detail.getScoreType());
            item.setItemScore(detail.getItemScore());
            item.setItemRemark(detail.getItemRemark());
            item.setImagePath(detail.getImagePath());
            result.add(item);
        }
        return result;
    }

    private BigDecimal applyScoreType(BigDecimal score, String scoreType) {
        if ("1".equals(scoreType)) {
            return score;
        }
        if ("2".equals(scoreType)) {
            return score.negate();
        }
        return BigDecimal.ZERO;
    }

    private static class AggregationHolder {
        private final StatisticsDetailItemVO item = new StatisticsDetailItemVO();
        private String imagePath;
        private java.util.Date lastImageTime;
        private Long lastImageId;
        private StringBuilder remarkBuilder;

        void appendRemark(String remark) {
            if (remarkBuilder == null) {
                remarkBuilder = new StringBuilder();
            }
            if (remarkBuilder.length() > 0) {
                remarkBuilder.append("；");
            }
            remarkBuilder.append(remark);
            item.setItemRemark(remarkBuilder.toString());
        }

        void updateImage(StatisticsDetailRow row) {
            if (StringUtils.isBlank(row.getImagePath())) {
                return;
            }
            if (lastImageTime == null) {
                lastImageTime = row.getCreateTime();
                lastImageId = row.getDetailId();
                imagePath = row.getImagePath();
                return;
            }
            if (row.getCreateTime() != null) {
                if (row.getCreateTime().after(lastImageTime)) {
                    lastImageTime = row.getCreateTime();
                    lastImageId = row.getDetailId();
                    imagePath = row.getImagePath();
                    return;
                }
                if (row.getCreateTime().equals(lastImageTime) && row.getDetailId() != null
                    && (lastImageId == null || row.getDetailId() > lastImageId)) {
                    lastImageId = row.getDetailId();
                    imagePath = row.getImagePath();
                }
                return;
            }
            if (row.getDetailId() != null && (lastImageId == null || row.getDetailId() > lastImageId)) {
                lastImageId = row.getDetailId();
                imagePath = row.getImagePath();
            }
        }
    }
}
