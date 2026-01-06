package com.attendance.project.financial.domain;

import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 工资记录对象 fin_sal_rec
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
public class FinSal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    private String salname;

    private String timetag;

    private Long deptId;

    private String deptName;

    private String filePath;

    private String content;

    //流程相关：审核状态
    private String opstatus;

    //流程相关：最后操作时间
    private Date maxtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalname() {
        return salname;
    }

    public void setSalname(String salname) {
        this.salname = salname;
    }

    public String getTimetag() {
        return timetag;
    }

    public void setTimetag(String timetag) {
        this.timetag = timetag;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpstatus() {
        return opstatus;
    }

    public void setOpstatus(String opstatus) {
        this.opstatus = opstatus;
    }

    public Date getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(Date maxtime) {
        this.maxtime = maxtime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("salname", getSalname())
            .append("timetag", getTimetag())
            .toString();
    }
}
