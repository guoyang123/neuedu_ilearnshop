package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;

import javax.servlet.http.HttpSession;

/**
 *@Author teacher.zhang
 * */
public interface IUserService {
    /*前台用户登录：登录成功添加session*/
    ServerResponse selectByUserName(String username,String password);

    /*前台用户注册*/
    ServerResponse insertNew(UserInfo ui);

    /*用户注册用户名或者邮箱是否可用*/
    ServerResponse checkUser(String str,String type);

    /*获取登录用户信息*/
    ServerResponse getLoginUserMsg(HttpSession session);

    /*忘记密码*/
    ServerResponse forgetUserName(String username);

    /*提交问题答案*/
    ServerResponse forgetGetAnswer(String username,String question,String answer);

    /*忘记密码的重设密码*/
    ServerResponse forgetResetPassword(String username,String passwordNew,String forgetToken);

    /*登录中状态重置密码*/
    ServerResponse resetPassword(HttpSession session,String passwordOld,String passwordNew);

    /*登录状态更新个人信息*/
    ServerResponse updateInformation(HttpSession session,UserInfo usr);

    /*获取更新个人信息*/
    UserInfo selectByPrimaryKey(Integer id);

    /*获取当前登录用户的详细信息*/
    ServerResponse getInforamtion(HttpSession session);

    /*退出登录*/
    ServerResponse logout(HttpSession session);
}
