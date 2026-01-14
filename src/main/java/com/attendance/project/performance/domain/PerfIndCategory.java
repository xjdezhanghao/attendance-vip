package com.attendance.project.performance.domain;

import java.math.BigDecimal;
import java.util.List;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考核大类对象 perf_ind_category
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class PerfIndCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 大类ID */
    private Long categoryId;

    /** 关联考核项目ID */
    @Excel(name = "关联考核项目ID")
    private Long projectId;

    /** 大类名称 */
    @Excel(name = "大类名称")
    private String categoryName;

    /** 加减分类型（0加分1减分2不加减） */
    @Excel(name = "加减分类型", readConverterExp = "0=不加减1加分2减分")
    private String scoreType;

    /** 权重 */
    @Excel(name = "权重")
    private BigDecimal weight;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 小项列表 */
    private List<PerfIndItem> items;

    public void setItems(List<PerfIndItem> items) {
        this.items = items;
    }

    public List<PerfIndItem> getItems() {
        return items;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    public void setScoreType(String scoreType)
    {
        this.scoreType = scoreType;
    }

    public String getScoreType()
    {
        return scoreType;
    }
    public void setWeight(BigDecimal weight)
    {
        this.weight = weight;
    }

    public BigDecimal getWeight()
    {
        return weight;
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
            .append("categoryId", getCategoryId())
            .append("projectId", getProjectId())
            .append("categoryName", getCategoryName())
            .append("scoreType", getScoreType())
            .append("weight", getWeight())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
