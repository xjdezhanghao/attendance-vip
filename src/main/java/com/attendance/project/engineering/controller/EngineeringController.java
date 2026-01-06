package com.attendance.project.engineering.controller;

import com.attendance.framework.config.WxConfig;
import com.attendance.framework.web.controller.BaseController;
import com.attendance.project.business.service.IAtdRecordService;
import com.attendance.project.system.post.service.IPostService;
import com.attendance.project.system.role.service.IRoleService;
import com.attendance.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/engineering")
public class EngineeringController extends BaseController {

    private String prefix = "engineering";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private IAtdRecordService atdRecordService;

    @GetMapping("/board")
    public String board()
    {
        return prefix + "/board";
    }

}
