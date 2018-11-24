package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.zhang
 * */
@RestController
@RequestMapping(value = "/manage/user/")
@Slf4j
public class UserManagerController {
    @Autowired
    private IUserService userService;
    @RequestMapping(value = "login.do")
    public ServerResponse login(){
        log.error("=========login===controller==");
        return null;
    }
    @RequestMapping(value = "add.do")
    public ServerResponse add(){
        log.error("=========add===controller==");
        return null;
    }
}
