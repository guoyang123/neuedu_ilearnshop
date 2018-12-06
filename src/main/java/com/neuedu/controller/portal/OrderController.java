package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.log.NeueduAnalyticsEngineSDK;
import com.neuedu.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author teacher.zhang
 */
@RestController
@RequestMapping(value = "/portal/order/")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /*创建订单*/
    @RequestMapping("create.do")
    public ServerResponse createNew(HttpSession session, Integer shippingId) {
        //判断登录状态
        ServerResponse sr = orderService.createNew(session, shippingId);

        return sr;
    }

    /*获取订单的商品信息*/
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session) {
        //判断登录状态
        ServerResponse sr = orderService.getOrderCartProduct(session);

        return sr;
    }

    /*订单List*/
    @RequestMapping("list.do")
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(required = false,defaultValue ="10")Integer pageSize,
                                  @RequestParam(required = false,defaultValue ="1")Integer pageNum) {
        //判断登录状态
        ServerResponse sr = orderService.getlist(session, pageSize,pageNum);

        return sr;
    }

    /*订单详情detail*/
    @RequestMapping("detail.do")
    public ServerResponse getDetail(HttpSession session, Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.getDetail(session, orderNo);

        return sr;
    }

    Logger logger=LoggerFactory.getLogger(OrderController.class);
    /*取消订单*/
    @RequestMapping("cancel.do")
    public ServerResponse cancelOrder(HttpSession session, Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.cancelOrder(session, orderNo);
        if(sr.isSuccess()){//订单取消成功

        }
        return sr;
    }

    /*支付宝支付模块*/
    /*支付*/
    @RequestMapping("pay.do")
    public ServerResponse aliPay(HttpSession session, Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.aliPay(session,orderNo);

        return sr;
    }

    /*查询订单支付状态*/
    @RequestMapping("query_order_pay_status.do")
    public ServerResponse queryOrderPayStatus(HttpSession session, Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.aliPay(session,orderNo);

        return sr;
    }
    /*支付宝回调*/
    @RequestMapping("alipay_callback.do")
    public String alipayCallback(HttpSession session, Long orderNo) {
        //判断登录状态
        //ServerResponse sr = orderService.aliPay(session,orderNo);

        return "SUCCESS";
    }
}
