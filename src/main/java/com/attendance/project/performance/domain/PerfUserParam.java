package com.attendance.project.performance.domain;

import com.attendance.framework.aspectj.lang.annotation.Excel;
import com.attendance.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户参数对象 perf_user_param
 * 
 * @author ruoyi
 * @date 2024-11-23
 */
public class PerfUserParam extends BaseEntity
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

    private String loginName;

    /** 职级参数 */
    @Excel(name = "职级参数")
    private String paramRank;

    /** 岗位参数 */
    @Excel(name = "岗位参数")
    private String paramPost;

    private String postName;

    /** 岗位组 */
    private Long[] postIds;

    private Long postId;

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

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
                .append("userName", getUserName())
                .append("loginName", getLoginName())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("postName", getPostName())
            .append("paramRank", getParamRank())
            .append("paramPost", getParamPost())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj.getClass().equals(this.getClass())){
            PerfUserParam objUserParam = (PerfUserParam)obj;
            if (objUserParam.getUserId().equals(getUserId())){
                result = true;
            }
        }
        return result;
    }
}
