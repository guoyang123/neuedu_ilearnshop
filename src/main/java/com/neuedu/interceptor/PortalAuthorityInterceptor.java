package com.neuedu.interceptor;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.util.JsonUtils;
import com.neuedu.util.OpinionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 前台用户权限拦截器
 * */
@Component
@Slf4j
public class PortalAuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       log.error("=====前台=====preHandler=====");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        return judgeJurisdiction(request,response);

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.error("=====前台=====postHandle=====");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.error("=====前台=====afterCompletion=====");
    }

    //
    private boolean judgeJurisdiction(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取session
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        //重构HttpServletResponse
        if(userInfo == null){
            //获取流
            response.reset();
            PrintWriter writer = response.getWriter();

            //
            ServerResponse sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_LOGIN.getCode(),Const.ReponseCodeEnum.NO_LOGIN.getDesc());

            //转换成json
            String s = JsonUtils.obj2String(sr);
//            writer.write(s);
            writer.println(sr.getMsg());
            writer.flush();
            writer.close();

            //返回数据
            return false;

        }


        //用户没有权限

        return true;
    }
}
