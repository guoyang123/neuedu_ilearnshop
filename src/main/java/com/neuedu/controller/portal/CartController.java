package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import com.neuedu.service.ICategoryService;
import com.neuedu.util.OpinionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 *@Author teacher.zhang
 * */
@RestController
@RequestMapping(value = "/portal/cart/")
public class CartController {
    @Autowired
    private ICartService cartService;

    /*购物车List列表*/
    @RequestMapping("list.do")
    public ServerResponse getDetail(HttpSession session){
        ServerResponse sr = OpinionUtils.isSession(session);
        //判断登录状态
        if(sr.isSuccess()){
            sr = cartService.getDetail(session);
            return sr;
        }else{
            return sr;
        }
    }
}
