package com.attendance.project.performance.controller;

import com.attendance.common.constant.UserConstants;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.performance.domain.PerfPost;
import com.attendance.project.performance.service.IPerfPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 * 
 * @author june
 */
@Controller
@RequestMapping("/performance/post")
public class PerfPostController extends BaseController
{
    private String prefix = "performance/post";

    @Autowired
    private IPerfPostService postService;

    @RequiresPermissions("perf:post:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/post";
    }

    //@RequiresPermissions("perf:post:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfPost post)
    {
        startPage();
        List<PerfPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @RequiresPermissions("perf:post:view")
    @GetMapping("/select")
    public String role2()
    {
        return prefix + "/post2";
    }

    //@RequiresPermissions("perf:post:list")
    @GetMapping("/list2")
    @ResponseBody
    public TableDataInfo list2()
    {
        List<PerfPost> list = postService.selectPostList(new PerfPost());
        return getDataTable(list);
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("perf:post:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfPost post)
    {
        List<PerfPost> list = postService.selectPostList(post);
        ExcelUtil<PerfPost> util = new ExcelUtil<PerfPost>(PerfPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    @RequiresPermissions("perf:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(postService.deletePostByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 新增岗位
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存岗位
     */
    @RequiresPermissions("perf:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PerfPost post)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @RequiresPermissions("perf:post:edit")
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, ModelMap mmap)
    {
        mmap.put("post", postService.selectPostById(postId));
        return prefix + "/edit";
    }

    /**
     * 修改保存岗位
     */
    @RequiresPermissions("perf:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PerfPost post)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.updatePost(post));
    }

    /**
     * 校验岗位名称
     */
    @PostMapping("/checkPostNameUnique")
    @ResponseBody
    public String checkPostNameUnique(PerfPost post)
    {
        return postService.checkPostNameUnique(post);
    }

    /**
     * 校验岗位编码
     */
    @PostMapping("/checkPostCodeUnique")
    @ResponseBody
    public String checkPostCodeUnique(PerfPost post)
    {
        return postService.checkPostCodeUnique(post);
    }
}
