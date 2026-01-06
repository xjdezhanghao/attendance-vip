package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 工资记录对象 fin_sal_rec
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
public class FinSalRec extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户id */
    private Long userid;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 部门id */
    private Long deptid;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    @Excel(name = "身份证号")
    private String idcard;

    @Excel(name = "银行卡号")
    private String yhkh;

    /** 职级文本 */
    private String rankText;

    /** 职级数值 */
    private String rankValue;

    /** 岗位编码 */
    private String postCode;

    /** 岗位文本 */
    private String postText;

    /** 年功工资 */
    @Excel(name = "年功工资")
    private String nggz;

    /** 生产一线倾斜额度 */
    @Excel(name = "生产一线倾斜额度")
    private String yxqxed;

    /** 岗位数值 */
    @Excel(name = "岗位工资")
    private String postValue;

    /** 住房补贴 */
    @Excel(name = "住房补贴")
    private String zfbt;

    /** 独子费 */
    @Excel(name = "独子费")
    private String dzf;

    /** 岗位效益 */
    @Excel(name = "岗位效益")
    private String gwxy;

    /** 住房补贴效益 */
    @Excel(name = "住房补贴效益")
    private String zfbtxy;

    /** 固定奖金 */
    @Excel(name = "固定奖金")
    private String gdjj;

    /** 职务补贴 */
    @Excel(name = "职务补贴")
    private String zwbt;

    /** 个人效益工资总额 */
    @Excel(name = "个人效益工资总额")
    private String grxyze;

    /** 部门奖励绩效 */
    @Excel(name = "部门奖励绩效")
    private String bmjljx;

    /** 个人奖励绩效 */
    @Excel(name = "个人奖励绩效")
    private String grjljx;

    /** 个人扣发绩效 */
    @Excel(name = "个人扣发绩效")
    private String grkfjx;

    /** 实发效益绩效 */
    @Excel(name = "实发效益绩效")
    private String sfxygz;

    /** 绩效工资2 */
    @Excel(name = "绩效工资2")
    private String jxgz2;

    /** 应发小计 */
    @Excel(name = "应发小计")
    private String yfxj;

    /** 物业补贴 */
    @Excel(name = "物业补贴")
    private String wybt;

    /** 补发 */
    @Excel(name = "补发")
    private String bf;

    /** 公里补助、加班、x光机、补贴等 */
    @Excel(name = "公里补助、加班、x光机、补贴等")
    private String bzbt;

    /** 外派通讯补助 */
    @Excel(name = "外派通讯补助")
    private String wptxbz;

    /** 公司奖励 */
    @Excel(name = "公司奖励")
    private String gsjl;

    /** 防暑降温费 */
    @Excel(name = "防暑降温费")
    private String fsjwf;

    /** 取暖费 */
    @Excel(name = "取暖费")
    private String qnf;

    /** 应发合计 */
    @Excel(name = "应发合计")
    private String yfhj;

    /** 代扣养老保险 */
    @Excel(name = "代扣养老保险")
    private String dkylbx;

    /** 代扣医疗保险 */
    @Excel(name = "代扣医疗保险")
    private String dkyibx;

    @Excel(name = "代扣失业保险")
    private String dksybx;

    /** 代扣公积金 */
    @Excel(name = "代扣公积金")
    private String dkgjj;

    @Excel(name = "代扣企业年金")
    private String dkqynj;

    @Excel(name = "补扣养老")
    private String bkyl;
    @Excel(name = "补扣医疗")
    private String bkyil;
    @Excel(name = "补扣失业")
    private String bksy;
    @Excel(name = "补扣公积金")
    private String bkgjj;
    @Excel(name = "补扣企业年金")
    private String bkqynj;
    @Excel(name = "代扣水费")
    private String dksf;

    @Excel(name = "补扣")
    private String dkglwsf;
    @Excel(name = "扣公司代缴社保")
    private String kgsdjsb;

    /** 本月代扣个人所得税 */
    @Excel(name = "本月代扣个人所得税")
    private String dksds;

    /** 考勤扣款合计 */
    @Excel(name = "考勤扣款合计")
    private String kqkk;

    /** 扣款合计 */
    @Excel(name = "扣款合计")
    private String kkhj;

    /** 实发工资总额 */
    @Excel(name = "实发工资总额")
    private String sfgzze;

    /** 本月实发工资 */
    @Excel(name = "本月实发工资")
    private String sfgz;

    /** 工作日延时 */
    @Excel(name = "工作日延时")
    private String gzrys;

    /** 双薪天数 */
    @Excel(name = "双薪天数")
    private String shxts;

    /** 三薪天数 */
    @Excel(name = "三薪天数")
    private String sxts;

    /** 夜班天数 */
    @Excel(name = "夜班天数")
    private String ybts;

    /** 是否怀孕 */
    //@Excel(name = "是否怀孕")
    private String sfhy;

    private String timetag;

    private Long salid;

    @Excel(name = "考勤等级", color = "true")
    private String kqdj;

    /** 职级编码 */
    @Excel(name = "部门绩效等级")
    private String rankCode;

    @Excel(name = "迟到次数")
    private String kqcd;

    @Excel(name = "早退次数")
    private String kqzt;

    private String excelColor;

    private String preDateStr;

    private String nextDateStr;

    private String gzxs;

    //用户id列表
    private List<Integer> userids;

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
    public void setRankCode(String rankCode)
    {
        this.rankCode = rankCode;
    }

    public String getRankCode()
    {
        return rankCode;
    }
    public void setRankText(String rankText)
    {
        this.rankText = rankText;
    }

    public String getRankText()
    {
        return rankText;
    }
    public void setRankValue(String rankValue)
    {
        this.rankValue = rankValue;
    }

    public String getRankValue()
    {
        return rankValue;
    }
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getPostCode()
    {
        return postCode;
    }
    public void setPostText(String postText)
    {
        this.postText = postText;
    }

    public String getPostText()
    {
        return postText;
    }
    public void setPostValue(String postValue)
    {
        this.postValue = postValue;
    }

    public String getPostValue()
    {
        return postValue;
    }
    public void setNggz(String nggz)
    {
        this.nggz = nggz;
    }

    public String getNggz()
    {
        return nggz;
    }
    public void setYxqxed(String yxqxed)
    {
        this.yxqxed = yxqxed;
    }

    public String getYxqxed()
    {
        return yxqxed;
    }
    public void setZfbt(String zfbt)
    {
        this.zfbt = zfbt;
    }

    public String getZfbt()
    {
        return zfbt;
    }
    public void setDzf(String dzf)
    {
        this.dzf = dzf;
    }

    public String getDzf()
    {
        return dzf;
    }
    public void setGwxy(String gwxy)
    {
        this.gwxy = gwxy;
    }

    public String getGwxy()
    {
        return gwxy;
    }
    public void setZfbtxy(String zfbtxy)
    {
        this.zfbtxy = zfbtxy;
    }

    public String getZfbtxy()
    {
        return zfbtxy;
    }
    public void setGdjj(String gdjj)
    {
        this.gdjj = gdjj;
    }

    public String getGdjj()
    {
        return gdjj;
    }
    public void setZwbt(String zwbt)
    {
        this.zwbt = zwbt;
    }

    public String getZwbt()
    {
        return zwbt;
    }
    public void setGrxyze(String grxyze)
    {
        this.grxyze = grxyze;
    }

    public String getGrxyze()
    {
        return grxyze;
    }
    public void setBmjljx(String bmjljx)
    {
        this.bmjljx = bmjljx;
    }

    public String getBmjljx()
    {
        return bmjljx;
    }
    public void setGrjljx(String grjljx)
    {
        this.grjljx = grjljx;
    }

    public String getGrjljx()
    {
        return grjljx;
    }
    public void setGrkfjx(String grkfjx)
    {
        this.grkfjx = grkfjx;
    }

    public String getGrkfjx()
    {
        return grkfjx;
    }
    public void setSfxygz(String sfxygz)
    {
        this.sfxygz = sfxygz;
    }

    public String getSfxygz()
    {
        return sfxygz;
    }
    public void setJxgz2(String jxgz2)
    {
        this.jxgz2 = jxgz2;
    }

    public String getJxgz2()
    {
        return jxgz2;
    }
    public void setYfxj(String yfxj)
    {
        this.yfxj = yfxj;
    }

    public String getYfxj()
    {
        return yfxj;
    }
    public void setWybt(String wybt)
    {
        this.wybt = wybt;
    }

    public String getWybt()
    {
        return wybt;
    }
    public void setBf(String bf)
    {
        this.bf = bf;
    }

    public String getBf()
    {
        return bf;
    }
    public void setBzbt(String bzbt)
    {
        this.bzbt = bzbt;
    }

    public String getBzbt()
    {
        return bzbt;
    }
    public void setWptxbz(String wptxbz)
    {
        this.wptxbz = wptxbz;
    }

    public String getWptxbz()
    {
        return wptxbz;
    }
    public void setGsjl(String gsjl)
    {
        this.gsjl = gsjl;
    }

    public String getGsjl()
    {
        return gsjl;
    }
    public void setQnf(String qnf)
    {
        this.qnf = qnf;
    }

    public String getQnf()
    {
        return qnf;
    }
    public void setYfhj(String yfhj)
    {
        this.yfhj = yfhj;
    }

    public String getYfhj()
    {
        return yfhj;
    }
    public void setDkylbx(String dkylbx)
    {
        this.dkylbx = dkylbx;
    }

    public String getDkylbx()
    {
        return dkylbx;
    }
    public void setDkyibx(String dkyibx)
    {
        this.dkyibx = dkyibx;
    }

    public String getDkyibx()
    {
        return dkyibx;
    }
    public void setDkgjj(String dkgjj)
    {
        this.dkgjj = dkgjj;
    }

    public String getDkgjj()
    {
        return dkgjj;
    }
    public void setDksds(String dksds)
    {
        this.dksds = dksds;
    }

    public String getDksds()
    {
        return dksds;
    }
    public void setKqkk(String kqkk)
    {
        this.kqkk = kqkk;
    }

    public String getKqkk()
    {
        return kqkk;
    }
    public void setKkhj(String kkhj)
    {
        this.kkhj = kkhj;
    }

    public String getKkhj()
    {
        return kkhj;
    }
    public void setSfgzze(String sfgzze)
    {
        this.sfgzze = sfgzze;
    }

    public String getSfgzze()
    {
        return sfgzze;
    }
    public void setSfgz(String sfgz)
    {
        this.sfgz = sfgz;
    }

    public String getSfgz()
    {
        return sfgz;
    }
    public void setGzrys(String gzrys)
    {
        this.gzrys = gzrys;
    }

    public String getGzrys()
    {
        return gzrys;
    }
    public void setShxts(String shxts)
    {
        this.shxts = shxts;
    }

    public String getShxts()
    {
        return shxts;
    }
    public void setSxts(String sxts)
    {
        this.sxts = sxts;
    }

    public String getSxts()
    {
        return sxts;
    }
    public void setYbts(String ybts)
    {
        this.ybts = ybts;
    }

    public String getYbts()
    {
        return ybts;
    }
    public void setFsjwf(String fsjwf)
    {
        this.fsjwf = fsjwf;
    }

    public String getFsjwf()
    {
        return fsjwf;
    }
    public void setSfhy(String sfhy)
    {
        this.sfhy = sfhy;
    }

    public String getSfhy()
    {
        return sfhy;
    }

    public String getTimetag() {
        return timetag;
    }

    public void setTimetag(String timetag) {
        this.timetag = timetag;
    }

    public Long getSalid() {
        return salid;
    }

    public void setSalid(Long salid) {
        this.salid = salid;
    }

    public String getKqcd() {
        return kqcd;
    }

    public void setKqcd(String kqcd) {
        this.kqcd = kqcd;
    }

    public String getKqzt() {
        return kqzt;
    }

    public void setKqzt(String kqzt) {
        this.kqzt = kqzt;
    }

    public String getKqdj() {
        return kqdj;
    }

    public void setKqdj(String kqdj) {
        this.kqdj = kqdj;
    }

    public String getExcelColor() {
        return excelColor;
    }

    public void setExcelColor(String excelColor) {
        this.excelColor = excelColor;
    }

    public String getPreDateStr() {
        return preDateStr;
    }

    public void setPreDateStr(String preDateStr) {
        this.preDateStr = preDateStr;
    }

    public String getNextDateStr() {
        return nextDateStr;
    }

    public void setNextDateStr(String nextDateStr) {
        this.nextDateStr = nextDateStr;
    }

    public String getGzxs() {
        return gzxs;
    }

    public void setGzxs(String gzxs) {
        this.gzxs = gzxs;
    }

    public String getDksybx() {
        return dksybx;
    }

    public void setDksybx(String dksybx) {
        this.dksybx = dksybx;
    }

    public String getDkqynj() {
        return dkqynj;
    }

    public void setDkqynj(String dkqynj) {
        this.dkqynj = dkqynj;
    }

    public String getBkyl() {
        return bkyl;
    }

    public void setBkyl(String bkyl) {
        this.bkyl = bkyl;
    }

    public String getBkyil() {
        return bkyil;
    }

    public void setBkyil(String bkyil) {
        this.bkyil = bkyil;
    }

    public String getBksy() {
        return bksy;
    }

    public void setBksy(String bksy) {
        this.bksy = bksy;
    }

    public String getBkgjj() {
        return bkgjj;
    }

    public void setBkgjj(String bkgjj) {
        this.bkgjj = bkgjj;
    }

    public String getBkqynj() {
        return bkqynj;
    }

    public void setBkqynj(String bkqynj) {
        this.bkqynj = bkqynj;
    }

    public String getDksf() {
        return dksf;
    }

    public void setDksf(String dksf) {
        this.dksf = dksf;
    }

    public String getDkglwsf() {
        return dkglwsf;
    }

    public void setDkglwsf(String dkglwsf) {
        this.dkglwsf = dkglwsf;
    }

    public String getKgsdjsb() {
        return kgsdjsb;
    }

    public void setKgsdjsb(String kgsdjsb) {
        this.kgsdjsb = kgsdjsb;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getYhkh() {
        return yhkh;
    }

    public void setYhkh(String yhkh) {
        this.yhkh = yhkh;
    }

    public List<Integer> getUserids() {
        return userids;
    }

    public void setUserids(List<Integer> userids) {
        this.userids = userids;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
                .append("gzxs", getGzxs())
            .append("userid", getUserid())
            .append("userName", getUserName())
            .append("deptid", getDeptid())
            .append("deptName", getDeptName())
            .append("rankCode", getRankCode())
            .append("rankText", getRankText())
            .append("rankValue", getRankValue())
            .append("postCode", getPostCode())
            .append("postText", getPostText())
            .append("postValue", getPostValue())
            .append("nggz", getNggz())
            .append("yxqxed", getYxqxed())
            .append("zfbt", getZfbt())
            .append("dzf", getDzf())
            .append("gwxy", getGwxy())
            .append("zfbtxy", getZfbtxy())
            .append("gdjj", getGdjj())
            .append("zwbt", getZwbt())
            .append("grxyze", getGrxyze())
            .append("bmjljx", getBmjljx())
            .append("grjljx", getGrjljx())
            .append("grkfjx", getGrkfjx())
            .append("sfxygz", getSfxygz())
            .append("jxgz2", getJxgz2())
            .append("yfxj", getYfxj())
            .append("wybt", getWybt())
            .append("bf", getBf())
            .append("bzbt", getBzbt())
            .append("wptxbz", getWptxbz())
            .append("gsjl", getGsjl())
            .append("qnf", getQnf())
            .append("yfhj", getYfhj())
            .append("dkylbx", getDkylbx())
            .append("dkyibx", getDkyibx())
            .append("dkgjj", getDkgjj())
            .append("dksds", getDksds())
            .append("kqkk", getKqkk())
            .append("kkhj", getKkhj())
            .append("sfgzze", getSfgzze())
            .append("sfgz", getSfgz())
            .append("gzrys", getGzrys())
            .append("shxts", getShxts())
            .append("sxts", getSxts())
            .append("ybts", getYbts())
            .append("fsjwf", getFsjwf())
            .append("sfhy", getSfhy())
                .append("kqdj", getKqdj())
                .append("kqcd", getKqcd())
                .append("kqzt", getKqzt())
                .append("remark", getRemark())
            .toString();
    }
}
