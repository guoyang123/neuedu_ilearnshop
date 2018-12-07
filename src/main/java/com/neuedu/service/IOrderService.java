package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;
import java.util.Map;

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

    /*==================支付宝支付模块=======================*/
    /*支付*/
    ServerResponse aliPay(HttpSession session, Long orderNo);
    /*支付宝回调函数*/
    ServerResponse  alipayCallback(Map<String,String> map);
    /*查询订单状态*/
    ServerResponse queryOrderPayStatus(Long orderNo);

     /**记录日志*/
     void recordOrderCancelLog(Long orderno);
}
