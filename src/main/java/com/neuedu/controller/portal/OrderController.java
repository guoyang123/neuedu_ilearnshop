package com.neuedu.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.log.NeueduAnalyticsEngineSDK;
import com.neuedu.pojo.pay.Configs;
import com.neuedu.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

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

    /*取消订单*/
    @RequestMapping("cancel.do")
    public ServerResponse cancelOrder(HttpSession session, Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.cancelOrder(session, orderNo);
        if(sr.isSuccess()){//订单取消成功
           orderService.recordOrderCancelLog(orderNo);
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
    public ServerResponse queryOrderPayStatus(Long orderNo) {
        //判断登录状态
        ServerResponse sr = orderService.queryOrderPayStatus(orderNo);

        return sr;
    }
    /*支付宝回调*/
    @RequestMapping("alipay_callback.do")
    public ServerResponse alipayCallback(HttpServletRequest request) {
        ServerResponse sr = null;
        System.out.println("====支付宝回调接口成功===");

        //使用map结合来获取支付宝回传的所有数据
        Map<String,String[]> params=request.getParameterMap();
        //
        Map<String,String> requestparams= Maps.newHashMap();
        //通过迭代器进行遍历，获取到回传的所有数据，并进行处理
        Iterator<String> it=params.keySet().iterator();
        while(it.hasNext()){
            //获取key
            String key=it.next();
            //根据key获取value，对应的值是String数组
            String[] strArr=params.get(key);
            //遍历数组，将内容转换成
            String value="";
            for(int i=0;i<strArr.length;i++){
                value= (i==strArr.length-1)?value+strArr[i]: value+strArr[i]+",";
            }
            //放到一个新的map集合中
            requestparams.put(key,value);
        }

        //setp1:支付宝验签

        try {
            requestparams.remove("sign_type");
            boolean result=AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!result){
                //验签失败，返回错误信息
                sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getCode(),Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getDesc());
                return sr;
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            //验签失败，返回错误信息
            sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getCode(),Const.PaymentPlatformEnum.VERIFY_SIGNATURE_FALSE.getDesc());
            return sr;
        }

        //处理业务逻辑
        sr = orderService.alipayCallback(requestparams);
        return sr;

    }
}
