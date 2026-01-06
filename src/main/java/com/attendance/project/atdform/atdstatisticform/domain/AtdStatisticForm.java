package com.attendance.project.atdform.atdstatisticform.domain;

import java.util.Date;
import java.util.List;

import com.attendance.framework.web.domain.BaseEntity;
import com.attendance.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考勤表统计对象 atd_record_pc
 * 
 * @author wangchengyan
 * @date 2022-08-02
 */
public class AtdStatisticForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /**  */
    private Long userid;

    /**  */
    private Date atdtime;
    private String atdtimestring;
    /**  */
    private String createtime ;
    private Date kjcreateTime ;

    /**  */
    private String atddesc;
    /**  */
    private String atddesckj;
    /**  */
    private String atdfile;

    /**  */
    private String atdloc;

    /**  */
    private String atdtag;
    /**  */
    private String atdstatus;
    /**  */
    private String atdstatuskj;

    /**  */
    private String operatetime;
    /**  */
    private String operatename;
    /**  */
    private String starttime;

    /**  */
    private String endtime;

    /**  */
    private Date startDate;

    /**  */
    private Date endDate;

    /**  */
    @Excel(name = "姓名")
    private String username;

    /**  */
    private String deptname;

    /**  */
    @Excel(name = "迟到")
    private String chidao;

    /**  */
    @Excel(name = "早退")
    private String zaotui;

    /**  */
    private String buqian;

    /**  */
    @Excel(name = "出勤")
    private String chuqin;

    private  String  time;

    private Long deptId;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

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
    public void setAtdtime(Date atdtime)
    {
        this.atdtime = atdtime;
    }

    public Date getAtdtime()
    {
        return atdtime;
    }
    public void setAtddesc(String atddesc)
    {
        this.atddesc = atddesc;
    }

    public String getAtddesc()
    {
        return atddesc;
    }
    public void setAtdfile(String atdfile)
    {
        this.atdfile = atdfile;
    }

    public String getAtdfile()
    {
        return atdfile;
    }
    public void setAtdloc(String atdloc)
    {
        this.atdloc = atdloc;
    }

    public String getAtdloc()
    {
        return atdloc;
    }
    public void setAtdtag(String atdtag)
    {
        this.atdtag = atdtag;
    }

    public String getAtdtag()
    {
        return atdtag;
    }
    public void setAtdstatus(String atdstatus)
    {
        this.atdstatus = atdstatus;
    }

    public String getAtdstatus()
    {
        return atdstatus;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setDeptname(String deptname)
    {
        this.deptname = deptname;
    }

    public String getDeptname()
    {
        return deptname;
    }
    public void setChidao(String chidao)
    {
        this.chidao = chidao;
    }

    public String getChidao()
    {
        return chidao;
    }
    public void setZaotui(String zaotui)
    {
        this.zaotui = zaotui;
    }

    public String getZaotui()
    {
        return zaotui;
    }
    public void setBuqian(String buqian)
    {
        this.buqian = buqian;
    }

    public String getBuqian()
    {
        return buqian;
    }
    public void setChuqin(String chuqin)
    {
        this.chuqin = chuqin;
    }

    public String getChuqin()
    {
        return chuqin;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAtdstatuskj() {
        return atdstatuskj;
    }

    public void setAtdstatuskj(String atdstatuskj) {
        this.atdstatuskj = atdstatuskj;
    }

    public String getAtddesckj() {
        return atddesckj;
    }

    public void setAtddesckj(String atddesckj) {
        this.atddesckj = atddesckj;
    }

    public String getAtdtimestring() {
        return atdtimestring;
    }

    public void setAtdtimestring(String atdtimestring) {
        this.atdtimestring = atdtimestring;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getOperatename() {
        return operatename;
    }

    public void setOperatename(String operatename) {
        this.operatename = operatename;
    }

    public Date getKjcreateTime() {
        return kjcreateTime;
    }

    public void setKjcreateTime(Date kjcreateTime) {
        this.kjcreateTime = kjcreateTime;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userid", getUserid())
            .append("atdtime", getAtdtime())
            .append("atddesc", getAtddesc())
            .append("atdfile", getAtdfile())
            .append("atdloc", getAtdloc())
            .append("atdtag", getAtdtag())
            .append("kjcreateTime", getKjcreateTime())
            .append("atdstatus", getAtdstatus())

            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("username", getUsername())
            .append("deptname", getDeptname())
            .append("chidao", getChidao())
            .append("zaotui", getZaotui())
            .append("buqian", getBuqian())
            .append("chuqin", getChuqin())
                .append("createtime", getCreatetime())
                .append("starttime", getStarttime())
                .append("endtime", getEndtime())
                .append("atdstatuskj", getAtdstatuskj())
                .append("atddesckj", getAtddesckj())
                .append("atdtimestring", getAtdtimestring())
                .append("operatetime", getOperatetime())
                .append("operatename", getOperatename())
            .toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
