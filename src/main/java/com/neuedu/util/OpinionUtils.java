package com.neuedu.util;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/*
* 一系列判断工具类
* by zhangxin
* */
public class OpinionUtils {
    /*字符串非空判断*/
    public static boolean emptyString(String obj){
        boolean flag = true;
        if(obj == null || obj.equals("") || obj.length()==0){
            flag =  false;
        }
        return flag;
    }

    /*判断登录状态*/
    /*统一判断登录状态*/
    public static ServerResponse isSession(HttpSession session){
        ServerResponse sr = null;
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        if (ui == null){
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.NO_SESSION.getCode(),Const.CartCheckedEnum.NO_SESSION.getDesc());
        }else{
            sr = ServerResponse.createServerResponseBySuccess();
        }
        return sr;
    }
}
