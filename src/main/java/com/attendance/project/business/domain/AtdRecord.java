package com.attendance.project.business.domain;

import com.attendance.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AtdRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Long userid;

    private Date atdtime;

    private String atddesc;

    private String atdfile;

    private String atdloc;

    private String atdtag;

    private String atdstatus;

    private String selyear;

    private String selmonth;

    private String seldate;

    //查询参数
    private Date startDate;
    private Date endDate;

    //返回参数
    private String username;

    private String deptname;

    //分页参数
    private Integer pageNum;

    private Integer pageSize;

    private String img;

    private String chidao;
    private String zaotui;
    private String buqian;
    private String chuqin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Date getAtdtime() {
        return atdtime;
    }

    public void setAtdtime(Date atdtime) {
        this.atdtime = atdtime;
    }

    public String getAtddesc() {
        return atddesc;
    }

    public void setAtddesc(String atddesc) {
        this.atddesc = atddesc;
    }

    public String getAtdfile() {
        return atdfile;
    }

    public void setAtdfile(String atdfile) {
        this.atdfile = atdfile;
    }

    public String getAtdloc() {
        return atdloc;
    }

    public void setAtdloc(String atdloc) {
        this.atdloc = atdloc;
    }

    public String getAtdtag() {
        return atdtag;
    }

    public void setAtdtag(String atdtag) {
        this.atdtag = atdtag;
    }

    public String getSelyear() {
        return selyear;
    }

    public void setSelyear(String selyear) {
        this.selyear = selyear;
    }

    public String getSelmonth() {
        return selmonth;
    }

    public void setSelmonth(String selmonth) {
        this.selmonth = selmonth;
    }

    public String getSeldate() {
        return seldate;
    }

    public void setSeldate(String seldate) {
        this.seldate = seldate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAtdstatus() {
        return atdstatus;
    }

    public void setAtdstatus(String atdstatus) {
        this.atdstatus = atdstatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChidao() {
        return chidao;
    }

    public void setChidao(String chidao) {
        this.chidao = chidao;
    }

    public String getZaotui() {
        return zaotui;
    }

    public void setZaotui(String zaotui) {
        this.zaotui = zaotui;
    }

    public String getBuqian() {
        return buqian;
    }

    public void setBuqian(String buqian) {
        this.buqian = buqian;
    }

    public String getChuqin() {
        return chuqin;
    }

    public void setChuqin(String chuqin) {
        this.chuqin = chuqin;
    }

    @Override
    public String toString() {
        return "AtdRecord{" +
                "id=" + id +
                ", userid=" + userid +
                ", atdtime=" + atdtime +
                ", atddesc='" + atddesc + '\'' +
                ", atdfile='" + atdfile + '\'' +
                ", atdloc='" + atdloc + '\'' +
                ", atdtag='" + atdtag + '\'' +
                ", createtime='" + getCreateTime() + '\'' +
                '}';
    }
}
