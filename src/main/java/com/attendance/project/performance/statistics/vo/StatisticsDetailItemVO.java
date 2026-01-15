package com.attendance.project.performance.statistics.vo;

import java.math.BigDecimal;

/**
 * 绩效统计明细展示项
 */
public class StatisticsDetailItemVO {
    private Long projectId;
    private String projectName;
    private Long categoryId;
    private String categoryName;
    private Long itemId;
    private String itemName;
    private String ruleDesc;
    private BigDecimal scoreMin;
    private BigDecimal scoreMax;
    private String scoreType;
    private BigDecimal itemScore;
    private String itemRemark;
    private String imagePath;

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
}
