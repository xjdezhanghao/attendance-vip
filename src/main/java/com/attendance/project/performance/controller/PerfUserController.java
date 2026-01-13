package com.attendance.project.performance.controller;

import com.attendance.common.utils.text.Convert;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.framework.web.page.TableDataInfo;
import com.attendance.project.performance.domain.PerfUserParam;
import com.attendance.project.performance.domain.PerfUserPost;
import com.attendance.project.performance.service.IPerfPostService;
import com.attendance.project.performance.service.IPerfUserParamService;
import com.attendance.project.system.dept.domain.Dept;
import com.attendance.project.system.user.domain.User;
import com.attendance.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 员工信息Controller
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
@Controller
@RequestMapping("/performance/user")
public class PerfUserController extends BaseController
{
    private String prefix = "performance/user";

    @Autowired
    private IUserService userService;

    @Autowired
    private IPerfUserParamService userParamService;

    @Autowired
    private IPerfPostService perfPostService;

    @RequiresPermissions("perf:user:view")
    @GetMapping()
    public String rec()
    {
        return prefix + "/user";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfUserParam userParam)
    {
        User curUser = getSysUser();
        startPage();
        //userParam.setDeptId(curDept.getParentId());
        List<PerfUserParam> userParams = userParamService.selectPerfUserParamList(userParam);
        return getDataTable(userParams);
    }


    @RequiresPermissions("perf:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        PerfUserParam userParam = new PerfUserParam();
        userParam.setUserId(id);
        List<PerfUserParam> userParams = userParamService.selectPerfUserParamList(userParam);
        if (userParams.size() > 0){
            userParam = userParams.get(0);
        }
        mmap.put("userParam", userParam);
        mmap.put("posts", perfPostService.selectPostsByUserId(id));
        return prefix + "/edit";
    }

    /**
     * 修改保存员工信息
     */
    @RequiresPermissions("perf:user:edit")
    @Log(title = "员工信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PerfUserParam userParam)
    {
        // 查询是否已存在记录
        PerfUserParam queryParam = new PerfUserParam();
        queryParam.setUserId(userParam.getUserId());
        List<PerfUserParam> existingParams = userParamService.selectPerfUserParamListOnly(queryParam);

        int result;
        if (existingParams != null && !existingParams.isEmpty()) {
            // 存在记录则更新
            userParam.setId(existingParams.get(0).getId());
            result = userParamService.updatePerfUserParam(userParam);
        } else {
            // 不存在记录则新增
            result = userParamService.insertPerfUserParam(userParam);
        }
        return AjaxResult.success(result);
    }

}
