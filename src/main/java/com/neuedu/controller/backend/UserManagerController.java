package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.redis.RedisService;
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

    @Autowired
    RedisService redisService;
    @RequestMapping(value = "set.do")
    public ServerResponse set(){
        log.error("=========add===controller==");
        UserInfo userInfo=new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("张三");
        redisService.set("user:1:info",userInfo);

        return ServerResponse.createServerResponseBySuccess(userInfo);
    }
    @RequestMapping(value = "get.do")
    public ServerResponse get(){
        log.error("=========add===controller==");

        UserInfo userInfo=redisService.get("user:1:info",UserInfo.class);
        System.out.println(userInfo.getUsername());
        return ServerResponse.createServerResponseBySuccess(userInfo);

    }

    @RequestMapping(value = "setex.do")
    public ServerResponse setex(){
        log.error("=========add===controller==");
        UserInfo userInfo=new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("张三");
        redisService.setex("user:1:info",userInfo,100);

        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    @RequestMapping(value = "del.do")
    public ServerResponse del(){
        log.error("=========add===controller==");
        long re=redisService.del("user:1:info");

        return ServerResponse.createServerResponseBySuccess(re);
    }
    @RequestMapping(value = "expire.do")
    public ServerResponse expire(){
        log.error("=========add===controller==");
        long re=redisService.expire("user:1:info",110);

        return ServerResponse.createServerResponseBySuccess(re);
    }

}
