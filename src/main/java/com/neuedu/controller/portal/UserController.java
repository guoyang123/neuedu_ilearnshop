package com.neuedu.controller.portal;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserInfoMapper userInfoMapper;
    @RequestMapping(value = "/login.do")
    public PageInfo login(){
      // UserInfo userInfo=userInfoMapper.selectByPrimaryKey(21);
        PageHelper.startPage(1,10);
        List<UserInfo> userInfoList=userInfoMapper.selectAll();
        PageInfo pageInfo=new PageInfo(userInfoList);

        return pageInfo;
    }
}
