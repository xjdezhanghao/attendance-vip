package com.attendance.project.performance.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 绩效采集文件关联对象 perf_gather_file
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class PerfGatherFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件关联ID */
    private Long fileId;

    /** 关联小项采集ID */
    @Excel(name = "关联小项采集ID")
    private Long gatherItemId;

    /** 文件排序 */
    @Excel(name = "文件排序")
    private Long sort;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String fileUrl;

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setGatherItemId(Long gatherItemId)
    {
        this.gatherItemId = gatherItemId;
    }

    public Long getGatherItemId()
    {
        return gatherItemId;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("gatherItemId", getGatherItemId())
            .append("sort", getSort())
            .append("fileUrl", getFileUrl())
            .append("createTime", getCreateTime())
            .toString();
    }
}
