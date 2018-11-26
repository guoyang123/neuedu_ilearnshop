package com.neuedu.controller.portal;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
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
@Slf4j
@RequestMapping(value = "/portal/user/")
public class UserController {
    @Autowired
    private IUserService userService;

}
