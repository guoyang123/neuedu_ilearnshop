package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;

/**
 *@Author teacher.zhang & teacher.guo
 * */
public interface IOrderService {

    /*创建订单*/
    ServerResponse createNew (HttpSession session,Integer shippingId);

    /*获取订单的商品信息*/
    ServerResponse getOrderCartProduct(HttpSession session);

    /*订单List*/
    ServerResponse getlist(HttpSession session, Integer pageSize,Integer pageNum);

    /*订单详情detail*/
    ServerResponse getDetail (HttpSession session, Long orderNo);

    /*取消订单*/
    ServerResponse cancelOrder(HttpSession session, Long orderNo);
}
