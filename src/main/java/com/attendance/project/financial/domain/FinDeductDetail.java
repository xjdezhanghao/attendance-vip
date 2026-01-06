package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 扣除详情对象 fin_deduct_detail
 * 
 * @author ruoyi
 * @date 2024-12-04
 */
public class FinDeductDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userid;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 代扣养老保险 */
    @Excel(name = "代扣养老保险")
    private String dkylbx;

    /** 代扣医疗保险 */
    @Excel(name = "代扣医疗保险")
    private String dkyilbx;

    /** 代扣失业保险 */
    @Excel(name = "代扣失业保险")
    private String dksybx;

    /** 代扣公积金 */
    @Excel(name = "代扣公积金")
    private String dkgjj;

    /** 代扣企业年金 */
    @Excel(name = "代扣企业年金")
    private String dkqynj;

    /** 补扣养老 */
    @Excel(name = "补扣养老")
    private String bkyl;

    /** 补扣医疗 */
    @Excel(name = "补扣医疗")
    private String bkyil;

    /** 补扣失业 */
    @Excel(name = "补扣失业")
    private String bksy;

    /** 补扣公积金 */
    @Excel(name = "补扣公积金")
    private String bkgjj;

    /** 补扣企业年金 */
    @Excel(name = "补扣企业年金")
    private String bkqynj;

    /** 代扣水费 */
    @Excel(name = "代扣水费")
    private String dksf;

    /** 代扣管理卫生费 */
    @Excel(name = "代扣管理卫生费")
    private String dkglwsf;

    /** 扣公司代缴社保 */
    @Excel(name = "扣公司代缴社保")
    private String kgsdjsb;

    /** 本月代扣个人所得税 */
    @Excel(name = "本月代扣个人所得税")
    private String bydkgrsds;

    /** 考勤扣款合计 */
    @Excel(name = "考勤扣款合计")
    private String kqkkhj;

    /** 扣款合计 */
    @Excel(name = "扣款合计")
    private String kkhj;

    /** 外键 */
    @Excel(name = "外键")
    private Long pid;

    private String idcard;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public Long getUserid()
    {
        return userid;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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
    public void setDkylbx(String dkylbx)
    {
        this.dkylbx = dkylbx;
    }

    public String getDkylbx()
    {
        return dkylbx;
    }
    public void setDkyilbx(String dkyilbx)
    {
        this.dkyilbx = dkyilbx;
    }

    public String getDkyilbx()
    {
        return dkyilbx;
    }
    public void setDksybx(String dksybx)
    {
        this.dksybx = dksybx;
    }

    public String getDksybx()
    {
        return dksybx;
    }
    public void setDkgjj(String dkgjj)
    {
        this.dkgjj = dkgjj;
    }

    public String getDkgjj()
    {
        return dkgjj;
    }
    public void setDkqynj(String dkqynj)
    {
        this.dkqynj = dkqynj;
    }

    public String getDkqynj()
    {
        return dkqynj;
    }
    public void setBkyl(String bkyl)
    {
        this.bkyl = bkyl;
    }

    public String getBkyl()
    {
        return bkyl;
    }
    public void setBkyil(String bkyil)
    {
        this.bkyil = bkyil;
    }

    public String getBkyil()
    {
        return bkyil;
    }
    public void setBksy(String bksy)
    {
        this.bksy = bksy;
    }

    public String getBksy()
    {
        return bksy;
    }
    public void setBkgjj(String bkgjj)
    {
        this.bkgjj = bkgjj;
    }

    public String getBkgjj()
    {
        return bkgjj;
    }
    public void setBkqynj(String bkqynj)
    {
        this.bkqynj = bkqynj;
    }

    public String getBkqynj()
    {
        return bkqynj;
    }
    public void setDksf(String dksf)
    {
        this.dksf = dksf;
    }

    public String getDksf()
    {
        return dksf;
    }
    public void setDkglwsf(String dkglwsf)
    {
        this.dkglwsf = dkglwsf;
    }

    public String getDkglwsf()
    {
        return dkglwsf;
    }
    public void setKgsdjsb(String kgsdjsb)
    {
        this.kgsdjsb = kgsdjsb;
    }

    public String getKgsdjsb()
    {
        return kgsdjsb;
    }
    public void setBydkgrsds(String bydkgrsds)
    {
        this.bydkgrsds = bydkgrsds;
    }

    public String getBydkgrsds()
    {
        return bydkgrsds;
    }
    public void setKqkkhj(String kqkkhj)
    {
        this.kqkkhj = kqkkhj;
    }

    public String getKqkkhj()
    {
        return kqkkhj;
    }
    public void setKkhj(String kkhj)
    {
        this.kkhj = kkhj;
    }

    public String getKkhj()
    {
        return kkhj;
    }
    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public Long getPid()
    {
        return pid;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userid", getUserid())
            .append("userName", getUserName())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("dkylbx", getDkylbx())
            .append("dkyilbx", getDkyilbx())
            .append("dksybx", getDksybx())
            .append("dkgjj", getDkgjj())
            .append("dkqynj", getDkqynj())
            .append("bkyl", getBkyl())
            .append("bkyil", getBkyil())
            .append("bksy", getBksy())
            .append("bkgjj", getBkgjj())
            .append("bkqynj", getBkqynj())
            .append("dksf", getDksf())
            .append("dkglwsf", getDkglwsf())
            .append("kgsdjsb", getKgsdjsb())
            .append("bydkgrsds", getBydkgrsds())
            .append("kqkkhj", getKqkkhj())
            .append("kkhj", getKkhj())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("pid", getPid())
            .toString();
    }
}
