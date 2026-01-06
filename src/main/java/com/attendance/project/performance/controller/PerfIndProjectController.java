package com.attendance.project.performance.controller;

import java.util.List;

import com.attendance.project.performance.domain.PerfIndItem;
import com.attendance.project.performance.service.IPerfIndCategoryService;
import com.attendance.project.performance.service.IPerfIndItemService;
import com.attendance.project.performance.service.IPerfPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.attendance.framework.aspectj.lang.annotation.Log;
import com.attendance.framework.aspectj.lang.enums.BusinessType;
import com.attendance.project.performance.domain.PerfIndProject;
import com.attendance.project.performance.service.IPerfIndProjectService;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.framework.web.domain.AjaxResult;
import com.attendance.common.utils.poi.ExcelUtil;
import com.attendance.framework.web.page.TableDataInfo;

/**
 * 考核项目主Controller
 * 
 * @author ruoyi
 * @date 2025-12-18
 */
@Controller
@RequestMapping("/performance/indicator")
public class PerfIndProjectController extends BaseController
{
    private String prefix = "performance/indicator";

    @Autowired
    private IPerfIndProjectService perfIndProjectService;

    @Autowired
    private IPerfIndCategoryService perfIndCategoryService;

    @Autowired
    private IPerfIndItemService perfIndItemService;

    @Autowired
    private IPerfPostService perfPostService;

    @RequiresPermissions("perf:ind:view")
    @GetMapping()
    public String project()
    {
        return prefix + "/project";
    }

    /**
     * 查询考核项目主列表
     */
    @RequiresPermissions("perf:ind:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PerfIndProject perfIndProject)
    {
        startPage();
        List<PerfIndProject> list = perfIndProjectService.selectPerfIndProjectList(perfIndProject);
        return getDataTable(list);
    }

    /**
     * 导出考核项目主列表
     */
    @RequiresPermissions("perf:ind:export")
    @Log(title = "考核项目主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PerfIndProject perfIndProject)
    {
        List<PerfIndProject> list = perfIndProjectService.selectPerfIndProjectList(perfIndProject);
        ExcelUtil<PerfIndProject> util = new ExcelUtil<PerfIndProject>(PerfIndProject.class);
        return util.exportExcel(list, "考核项目主数据");
    }

    /**
     * 新增考核项目主
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考核项目主
     */
    @RequiresPermissions("perf:ind:add")
    @Log(title = "考核项目主", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody PerfIndProject perfIndProject)
    {
        try {
            // 保存考核项目主
            perfIndProjectService.insertPerfIndProject(perfIndProject);
            perfIndProjectService.saveCategoriesAndItems(perfIndProject);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("保存失败，请重试");
        }
    }

    /**
     * 修改考核项目主
     */
    @RequiresPermissions("perf:ind:edit")
    @GetMapping("/edit/{projectId}")
    public String edit(@PathVariable("projectId") Long projectId, ModelMap mmap)
    {
        PerfIndProject perfIndProject = perfIndProjectService.selectPerfIndProjectWithCategoriesAndItems(projectId);
        mmap.put("perfIndProject", perfIndProject);
        return prefix + "/add";
    }

    /**
     * 修改保存考核项目主
     */
    @RequiresPermissions("perf:ind:edit")
    @Log(title = "考核项目主", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody PerfIndProject perfIndProject)
    {
        try {
            // 更新考核项目主
            perfIndProjectService.updatePerfIndProject(perfIndProject);
            // 删除原有大类和小项
            perfIndProjectService.deleteExistingCategoriesAndItems(perfIndProject.getProjectId());
            // 保存新的大类和小项
            perfIndProjectService.saveCategoriesAndItems(perfIndProject);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("修改失败，请重试");
        }
    }

    /**
     * 删除考核项目主
     */
    @RequiresPermissions("perf:ind:remove")
    @Log(title = "考核项目主", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(perfIndProjectService.deletePerfIndProjectByProjectIds(ids));
    }


    /**
     * 小项配置页面
     */
    @GetMapping("/addItem")
    public String addItem(@RequestParam(required = false) Long categoryIndex,
                          @RequestParam(required = false) Integer layerIndex,
                          ModelMap mmap)
    {
        /*if (categoryIndex != null) {
            // 获取小项信息
            PerfIndItem itemQuery = new PerfIndItem();
            itemQuery.setCategoryId(categoryIndex);
            List<PerfIndItem> items = perfIndItemService.selectPerfIndItemList(itemQuery);
            mmap.put("items", items);
        }*/
        mmap.put("categoryIndex", categoryIndex);
        mmap.put("layerIndex", layerIndex);
        return prefix + "/addItem";
    }

}
