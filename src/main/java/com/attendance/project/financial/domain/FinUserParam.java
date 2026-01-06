package com.attendance.project.financial.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户参数对象 fin_user_param
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public class FinUserParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    private Long deptId;

    private String userName;

    private String deptName;

    /** 职级参数 */
    @Excel(name = "职级参数")
    private String paramRank;

    /** 岗位参数 */
    @Excel(name = "岗位参数")
    private String paramPost;

    /** 年功工资 */
    @Excel(name = "年功工资")
    private String nggz;

    /** 岗位工资 */
    @Excel(name = "岗位工资")
    private String gwgz;

    /** 独子费 */
    @Excel(name = "独子费")
    private String dzf;

    /** 是否怀孕 */
    @Excel(name = "是否怀孕")
    private String sfhy;

    private String[] ids;

    @Excel(name = "银行卡号")
    private String yhkh;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setParamRank(String paramRank)
    {
        this.paramRank = paramRank;
    }

    public String getParamRank()
    {
        return paramRank;
    }
    public void setParamPost(String paramPost)
    {
        this.paramPost = paramPost;
    }

    public String getParamPost()
    {
        return paramPost;
    }
    public void setNggz(String nggz)
    {
        this.nggz = nggz;
    }

    public String getNggz()
    {
        return nggz;
    }
    public void setGwgz(String gwgz)
    {
        this.gwgz = gwgz;
    }

    public String getGwgz()
    {
        return gwgz;
    }
    public void setDzf(String dzf)
    {
        this.dzf = dzf;
    }

    public String getDzf()
    {
        return dzf;
    }
    public void setSfhy(String sfhy)
    {
        this.sfhy = sfhy;
    }

    public String getSfhy()
    {
        return sfhy;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getYhkh() {
        return yhkh;
    }

    public void setYhkh(String yhkh) {
        this.yhkh = yhkh;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
                .append("userName", getUserName())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
            .append("paramRank", getParamRank())
            .append("paramPost", getParamPost())
            .append("nggz", getNggz())
            .append("gwgz", getGwgz())
            .append("dzf", getDzf())
            .append("sfhy", getSfhy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj.getClass().equals(this.getClass())){
            FinUserParam objUserParam = (FinUserParam)obj;
            if (objUserParam.getUserId().equals(getUserId())){
                result = true;
            }
        }
        return result;
    }
}
