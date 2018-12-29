package com.neuedu;

import com.google.common.collect.Lists;
import com.neuedu.interceptor.ManageAuthorityInterceptor;
import com.neuedu.interceptor.PortalAuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer {

    /**
     * 后台权限检查拦截器
     * */
    @Autowired
    ManageAuthorityInterceptor manageAuthorityInterceptor;
    /**
     * 前台权限检查拦截器
     * */
    @Autowired
    PortalAuthorityInterceptor portalAuthorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不在拦截后台接口
//        registry.addInterceptor(manageAuthorityInterceptor).addPathPatterns("/manage/**").
//                excludePathPatterns("/manage/user/login.do");

        List<String> excludeList= Lists.newArrayList();
        excludeList.add("/portal/user/login.do");//前台登录
        excludeList.add("/portal/user/register.do");//前台注册
        excludeList.add("/portal/product/**");//商品
        excludeList.add("/portal/order/alipay_callback.do");//支付宝回调

        registry.addInterceptor(portalAuthorityInterceptor).addPathPatterns("/portal/**").
                excludePathPatterns(excludeList);
    }



}
