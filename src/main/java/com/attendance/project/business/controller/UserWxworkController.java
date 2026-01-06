package com.attendance.project.business.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.project.business.domain.UserWxwork;
import com.attendance.project.business.service.IUserWxworkService;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.web.page.TableDataInfo;

/**
 * 用户和企业微信账号关联Controller
 * 
 * @author ruoyi
 * @date 2025-01-13
 */
@Controller
@RequestMapping("/system/wxwork")
public class UserWxworkController extends BaseController
{
    private String prefix = "system/wxwork";

    @Autowired
    private IUserWxworkService userWxworkService;

    @RequiresPermissions("system:wxwork:view")
    @GetMapping()
    public String wxwork()
    {
        return prefix + "/wxwork";
    }

    /**
     * 查询用户和企业微信账号关联列表
     */
    @RequiresPermissions("system:wxwork:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserWxwork userWxwork)
    {
        startPage();
        List<UserWxwork> list = userWxworkService.selectWxWorkUserList(userWxwork);
        return getDataTable(list);
    }

    /**
     * 导出用户和企业微信账号关联列表
     */
    @RequiresPermissions("system:wxwork:export")
    @Log(title = "用户和企业微信账号关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserWxwork userWxwork)
    {
        List<UserWxwork> list = userWxworkService.selectWxWorkUserList(userWxwork);
        ExcelUtil<UserWxwork> util = new ExcelUtil<UserWxwork>(UserWxwork.class);
        return util.exportExcel(list, "用户和企业微信账号关联数据");
    }

    /**
     * 新增用户和企业微信账号关联
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户和企业微信账号关联
     */
    @RequiresPermissions("system:wxwork:add")
    @Log(title = "用户和企业微信账号关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserWxwork userWxwork)
    {
        return toAjax(userWxworkService.insertWxWorkUser(userWxwork));
    }

    /**
     * 修改用户和企业微信账号关联
     */
    @RequiresPermissions("system:wxwork:edit")
    @GetMapping("/edit/{loginName}")
    public String edit(@PathVariable("loginName") String loginName, ModelMap mmap)
    {
        UserWxwork userWxwork = userWxworkService.selectWxWorkUserByLoginName(loginName);
        mmap.put("userWxwork", userWxwork);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户和企业微信账号关联
     */
    @RequiresPermissions("system:wxwork:edit")
    @Log(title = "用户和企业微信账号关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UserWxwork userWxwork)
    {
        return toAjax(userWxworkService.updateWxWorkUser(userWxwork));
    }

    /**
     * 删除用户和企业微信账号关联
     */
    @RequiresPermissions("system:wxwork:remove")
    @Log(title = "用户和企业微信账号关联", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(userWxworkService.deleteUserWxworkByLoginName(ids));
    }
}
