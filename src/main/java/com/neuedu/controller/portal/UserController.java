package com.neuedu.controller.portal;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.log.NeueduAnalyticsEngineSDK;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IUserService;
import com.neuedu.util.IpUtils;
import com.neuedu.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author teacher.zhang
 */
@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {
    @Autowired
    private IUserService userService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    /*前台用户登录：登录成功添加session*/
    @RequestMapping("login.do")
    public ServerResponse<UserInfo> userLogin(HttpServletRequest request, HttpSession session, String username, String password) {
//        String info=NeueduAnalyticsEngineSDK.recordPaidOrderLog("127.0.0.1","1111111","12",String.valueOf(System.currentTimeMillis()),System.currentTimeMillis()+"");
//        logger.info(info);
        ServerResponse<UserInfo> sr = userService.selectByUserName(session, username, password);
        //判断状态码为0，创建session
        if (sr.isSuccess() && sr != null) {
            //更新用户ip
            String userIp = IpUtils.getRemoteAddress(request);
            sr.getData().setUserIp(userIp);
            userService.updateUserIp(sr.getData());
            session.setAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc(), sr.getData());
        }
        //返回数据
        return sr;
    }

    /*前台用户注册*/
    @RequestMapping("register.do")
    public ServerResponse userRegister(UserInfo ui) {
        ServerResponse sr = userService.insertNew(ui);

        //返回数据
        return sr;
    }

    /*用户注册用户名或者邮箱是否可用*/
    @RequestMapping("check_valid.do")
    public ServerResponse checkUser(String str, String type) {
        ServerResponse sr = userService.checkUser(str, type);

        //返回数据
        return sr;
    }

    /*获取登录用户信息*/
    @RequestMapping("get_user_info.do")
    public ServerResponse getLoginUser(HttpSession session) {
        ServerResponse sr = userService.getLoginUserMsg(session);

        //返回数据
        return sr;
    }

    /*忘记密码*/
    @RequestMapping("forget_get_question.do")
    public ServerResponse forgetUserName(String username) {
        ServerResponse sr = userService.forgetUserName(username);

        //返回数据
        return sr;
    }

    /*提交问题答案*/
    @RequestMapping("forget_check_answer.do")
    public ServerResponse forgetGetAnswer(String username, String question, String answer) {
        ServerResponse sr = userService.forgetGetAnswer(username, question, answer);

        //返回数据
        return sr;
    }

    /*忘记密码的重设密码*/
    @RequestMapping("forget_reset_password.do")
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        ServerResponse sr = userService.forgetResetPassword(username, passwordNew, forgetToken);

        //返回数据
        return sr;
    }

    /*登录中状态重置密码*/
    @RequestMapping("reset_password.do")
    public ServerResponse resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        ServerResponse sr = userService.resetPassword(session, passwordOld, passwordNew);

        //返回数据
        return sr;
    }

    /*登录状态更新个人信息*/
    @RequestMapping("update_information.do")
    public ServerResponse updateInformation(HttpSession session, UserInfo usr) {
        ServerResponse sr = userService.updateInformation(session, usr);
        //更新session
        if (sr.isSuccess()) {
            UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
            UserInfo newUi = userService.selectByPrimaryKey(ui.getId());
            session.setAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc(), newUi);
        }
        //返回数据
        return sr;
    }

    /*获取当前登录用户的详细信息*/
    @RequestMapping("get_inforamtion.do")
    public ServerResponse getInforamtion(HttpSession session) {
        ServerResponse sr = userService.getInforamtion(session);

        //返回数据
        return sr;
    }

    /*退出登录*/
    @RequestMapping("logout.do")
    public ServerResponse logout(HttpSession session) {
        ServerResponse sr = userService.logout(session);

        //返回数据
        return sr;
    }
}
