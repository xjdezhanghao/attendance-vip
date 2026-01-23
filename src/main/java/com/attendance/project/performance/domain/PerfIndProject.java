package com.attendance.project.performance.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 考核项目主对象 perf_ind_project
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
public class PerfIndProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考核项目ID */
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 适用层级（1：贵宾公司/2：科室/3：班组） */
    @Excel(name = "适用层级", readConverterExp = "1=：贵宾公司/2：科室/3：班组")
    private Long deptLevel;

    /** 适用科室/班组ID（关联sys_dept） */
    @Excel(name = "适用科室/班组ID", readConverterExp = "关=联sys_dept")
    private Long deptId;

    /** 适用岗位ID（关联vip_post） */
    @Excel(name = "适用岗位ID", readConverterExp = "关=联vip_post")
    private Long postId;

    /** 状态（0启用1停用） */
    @Excel(name = "状态", readConverterExp = "0=启用1停用")
    private String status;

    private String deptName;

    private String postName;

    private String delFlag;

    /** 大类列表 */
    private List<PerfIndCategory> categories;

    public void setCategories(List<PerfIndCategory> categories) {
        this.categories = categories;
    }

    public List<PerfIndCategory> getCategories() {
        return categories;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectName()
    {
        return projectName;
    }
    public void setDeptLevel(Long deptLevel)
    {
        this.deptLevel = deptLevel;
    }

    public Long getDeptLevel()
    {
        return deptLevel;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public Long getPostId()
    {
        return postId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("deptLevel", getDeptLevel())
            .append("deptId", getDeptId())
            .append("postId", getPostId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
