package com.attendance.project.performance.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 绩效小项统计对象 perf_gather_detail
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class PerfStatisticsDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小项统计ID */
    private Long detailId;

    /** 统计记录ID */
    @Excel(name = "统计记录ID")
    private Long overviewId;

    /** 考核主项ID */
    @Excel(name = "考核主项ID")
    private Long projectId;

    /** 考核大类ID */
    @Excel(name = "考核大类ID")
    private Long categoryId;

    /** 考核小项ID */
    @Excel(name = "考核小项ID")
    private Long itemId;

    private Long userId;

    /** 小项得分 */
    @Excel(name = "小项得分")
    private BigDecimal itemScore;

    /** 加减分类型（0加分1减分2不加减） */
    @Excel(name = "加减分类型", readConverterExp = "0=加分1减分2不加减")
    private String scoreType;

    /** 小项备注 */
    @Excel(name = "小项备注")
    private String itemRemark;

    /** 是否关联数据（0否1是） */
    @Excel(name = "是否关联数据", readConverterExp = "0=否1是")
    private String isRelated;

    /** 数据来源（手动填报/考勤/工单） */
    @Excel(name = "数据来源", readConverterExp = "手=动填报/考勤/工单")
    private String dataSource;

    private String imagePath;

    /** 统计日期（格式：YYYY-MM-DD） */
    private String gatherDate;

    //关联项
    private String categoryName;

    private String projectName;

    private String itemName;

    private String ruleDesc;

    private Double weight;

    private BigDecimal scoreMin;

    private BigDecimal scoreMax;

    private String startDate;
    private String endDate;

    public void setDetailId(Long detailId)
    {
        this.detailId = detailId;
    }

    public Long getDetailId()
    {
        return detailId;
    }
    public void setOverviewId(Long overviewId)
    {
        this.overviewId = overviewId;
    }

    public Long getOverviewId()
    {
        return overviewId;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getItemId()
    {
        return itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItemScore(BigDecimal itemScore)
    {
        this.itemScore = itemScore;
    }

    public BigDecimal getItemScore()
    {
        return itemScore;
    }

    public void setItemRemark(String itemRemark)
    {
        this.itemRemark = itemRemark;
    }

    public String getItemRemark()
    {
        return itemRemark;
    }
    public void setIsRelated(String isRelated)
    {
        this.isRelated = isRelated;
    }

    public String getIsRelated()
    {
        return isRelated;
    }
    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }

    public String getDataSource()
    {
        return dataSource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGatherDate() {
        return gatherDate;
    }

    public void setGatherDate(String gatherDate) {
        this.gatherDate = gatherDate;
    }

    //关联项
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public BigDecimal getScoreMin() {
        return scoreMin;
    }

    public void setScoreMin(BigDecimal scoreMin) {
        this.scoreMin = scoreMin;
    }

    public BigDecimal getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(BigDecimal scoreMax) {
        this.scoreMax = scoreMax;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("detailId", getDetailId())
            .append("overviewId", getOverviewId())
            .append("projectId", getProjectId())
            .append("categoryId", getCategoryId())
            .append("itemId", getItemId())
                .append("userId", getUserId())
            .append("itemScore", getItemScore())
            .append("scoreType", getScoreType())
            .append("itemRemark", getItemRemark())
            .append("isRelated", getIsRelated())
            .append("dataSource", getDataSource())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
