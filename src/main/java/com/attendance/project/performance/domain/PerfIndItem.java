package com.attendance.project.performance.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 考核小项对象 perf_ind_item
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class PerfIndItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小项ID */
    private Long itemId;

    /** 关联主项ID */
    @Excel(name = "关联主项ID")
    private Long projectId;

    /** 关联大类ID */
    @Excel(name = "关联大类ID")
    private Long categoryId;

    /** 小项名称 */
    @Excel(name = "小项名称")
    private String itemName;

    /** 描述规则 */
    @Excel(name = "描述规则")
    private String ruleDesc;

    /** 是否关联数据（0否1是） */
    @Excel(name = "是否关联数据", readConverterExp = "0=否1是")
    private String isRelated;

    /** 数据来源（如sys_attendance:late_count） */
    @Excel(name = "数据来源", readConverterExp = "如=sys_attendance:late_count")
    private String dataSource;

    /** 分值最小值 */
    @Excel(name = "分值最小值")
    private BigDecimal scoreMin;

    /** 分值最大值 */
    @Excel(name = "分值最大值")
    private BigDecimal scoreMax;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getItemId()
    {
        return itemId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemName()
    {
        return itemName;
    }
    public void setRuleDesc(String ruleDesc)
    {
        this.ruleDesc = ruleDesc;
    }

    public String getRuleDesc()
    {
        return ruleDesc;
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
    public void setScoreMin(BigDecimal scoreMin)
    {
        this.scoreMin = scoreMin;
    }

    public BigDecimal getScoreMin()
    {
        return scoreMin;
    }
    public void setScoreMax(BigDecimal scoreMax)
    {
        this.scoreMax = scoreMax;
    }

    public BigDecimal getScoreMax()
    {
        return scoreMax;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("categoryId", getCategoryId())
            .append("itemName", getItemName())
            .append("ruleDesc", getRuleDesc())
            .append("isRelated", getIsRelated())
            .append("dataSource", getDataSource())
            .append("scoreMin", getScoreMin())
            .append("scoreMax", getScoreMax())
            .append("sort", getSort())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
