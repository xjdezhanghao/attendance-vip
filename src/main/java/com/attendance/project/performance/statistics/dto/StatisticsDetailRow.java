package com.attendance.project.performance.statistics.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 绩效统计明细原始行
 */
public class StatisticsDetailRow {
    private Long detailId;
    private Long overviewId;
    private Long projectId;
    private String projectName;
    private Long categoryId;
    private String categoryName;
    private Integer categorySort;
    private Long itemId;
    private String itemName;
    private Integer itemSort;
    private String ruleDesc;
    private BigDecimal scoreMin;
    private BigDecimal scoreMax;
    private String scoreType;
    private BigDecimal itemScore;
    private String itemRemark;
    private String imagePath;
    private Date createTime;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOverviewId() {
        return overviewId;
    }

    public void setOverviewId(Long overviewId) {
        this.overviewId = overviewId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(Integer categorySort) {
        this.categorySort = categorySort;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemSort() {
        return itemSort;
    }

    public void setItemSort(Integer itemSort) {
        this.itemSort = itemSort;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
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

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public BigDecimal getItemScore() {
        return itemScore;
    }

    public void setItemScore(BigDecimal itemScore) {
        this.itemScore = itemScore;
    }

    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
