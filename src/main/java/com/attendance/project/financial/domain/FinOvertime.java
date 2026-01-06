package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 加班申请对象 fin_overtime
 * 
 * @author ruoyi
 * @date 2024-11-08
 */
public class FinOvertime extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    //挂载流程的业务记录主键使用唯一生成值
    private Long id;

    /** 记录名称 */
    @Excel(name = "记录名称")
    private String recname;

    /** 记录时间 */
    @Excel(name = "记录时间")
    private String rectime;

    @Excel(name = "记录类型")
    private String rectype;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    private String filepath;

    //流程相关：审核状态
    private String opstatus;

    //流程相关：最后操作时间
    private Date maxtime;

    private Long[] ids;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRecname(String recname)
    {
        this.recname = recname;
    }

    public String getRecname()
    {
        return recname;
    }
    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public String getRectime()
    {
        return rectime;
    }
    public String getRectype() {
        return rectype;
    }

    public void setRectype(String rectype) {
        this.rectype = rectype;
    }
    public void setDeptid(Long deptid)
    {
        this.deptid = deptid;
    }

    public Long getDeptid()
    {
        return deptid;
    }
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getOpstatus() {
        return opstatus;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
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
            .append("recname", getRecname())
            .append("rectime", getRectime())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
