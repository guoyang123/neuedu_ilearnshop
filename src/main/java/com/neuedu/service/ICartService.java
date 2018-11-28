package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;

/**
 *@Author teacher.zhang
 * */
public interface ICartService {

    /*购物车List列表*/
    ServerResponse getDetail(HttpSession session);
}
