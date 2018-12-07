package com.neuedu.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.log.NeueduAnalyticsEngineSDK;
import com.neuedu.pojo.*;
import com.neuedu.pojo.pay.Configs;
import com.neuedu.pojo.pay.ZxingUtils;
import com.neuedu.pojo.vo.CartOrderItemVO;
import com.neuedu.pojo.vo.OrderItemVO;
import com.neuedu.pojo.vo.OrderVO;
import com.neuedu.service.IOrderService;

import com.neuedu.util.*;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.OpinionUtils;
import com.neuedu.util.POJOtoVOUtils;

import com.neuedu.util.PropertiesUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;

    /*参数非空校验*/
    private ServerResponse emptyParam(Integer shippingId) {
        ServerResponse sr = null;
        if (shippingId == null) {
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.ORDEREMPTY_PARAM.getCode(),Const.OrderStatusEnum.ORDEREMPTY_PARAM.getDesc());
        }
        return sr;
    }
    private ServerResponse emptyParam(Long orderNo) {
        ServerResponse sr = null;
        //参数非空校验
        if (orderNo == null) {
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.ORDEREMPTY_PARAM.getCode(),Const.OrderStatusEnum.ORDEREMPTY_PARAM.getDesc());
        }
        return sr;
    }
    
    /*创建订单ID*/
    private Long getOrderNo() {
        Long aLong = System.currentTimeMillis();
        return aLong;
    }
    
    /*创建一个订单类*/
    private Order getNewOrder(Integer uid, Integer shippingId, BigDecimal orderTotalPrice, Long orderNo) {
        Order order = new Order();
        order.setUserId(uid);
        order.setOrderNo(orderNo);
        order.setShippingId(shippingId);
        //订单总价格
        order.setPayment(orderTotalPrice);
        order.setPaymentType(Const.PaymentEnum.ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
    
        //保存订单
        int insert = orderMapper.insert(order);
        if (insert > 0) {
            return order;
        }
        return null;
    }
    
    /*创建一个订单详情类*/
    private OrderItem getNewOrderItem(Integer uid, Cart cart, Long orderNo, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(cart.getProductId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(cart.getQuantity());
        orderItem.setTotalPrice(BigDecimalUtils.mul(orderItem.getQuantity().doubleValue(), orderItem.getCurrentUnitPrice().doubleValue()));
    
        return orderItem;
    }
    
    /*生成订单详情*/
    
    /*创建订单:就是创建一个订单类*/
    @Override
    public ServerResponse createNew(HttpSession session, Integer shippingId) {
        ServerResponse sr = null;
    
        //参数非空校验
        sr = emptyParam(shippingId);
        if(sr != null){
            return sr;
        }
    
        //获取登陆用户id
        Integer uid = OpinionUtils.getUID(session);
    
        //创建订单ID
        Long orderNo = getOrderNo();
    
        //创建订单详情list
        List<OrderItemVO> orderItemVoList = new ArrayList<>();
    
        //查询该用户购物车中已选中商品
        List<Cart> liCarts = cartMapper.selectByUidAndCheckIn(uid);
    
        //创建订单总价
        BigDecimal orderTotalPrice = new BigDecimal(0);


        //判断购物信息是否存在
        if (liCarts == null || liCarts.size() < 1) {
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.EMPTY_CARTS.getCode(), Const.OrderStatusEnum.EMPTY_CARTS.getDesc());
            return sr;
        }
        //获取订单总价
        for (Cart liCart : liCarts) {
            //查看购物信息对应的商品是否存在
            Product product = productMapper.selectByPrimaryKey(liCart.getProductId());
    
            //购物信息转换成订单详情
            OrderItem newOrderItem = getNewOrderItem(uid, liCart, orderNo, product);
    
            //计算订单总价
            orderTotalPrice = BigDecimalUtils.add(orderTotalPrice.doubleValue(), newOrderItem.getTotalPrice().doubleValue());
        }
    
        //创建订单并保存至数据库中
        Order newOrder = getNewOrder(uid, shippingId, orderTotalPrice, orderNo);
    
        //创建订单成功后，才保存订单详情，扣除库存，清空购物车
        if(newOrder != null){
            for (Cart liCart : liCarts) {
                //查看购物信息对应的商品是否存在
                Product product = productMapper.selectByPrimaryKey(liCart.getProductId());
                if (product == null || product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()) {
                    //商品已下架或者被删除
                    sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.NO_PRODUCT.getCode(), Const.ProductStatusEnum.NO_PRODUCT.getDesc());
                    return sr;
                }
                if (product.getStock() < liCart.getQuantity()) {
                    //商品库存不足
                    sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.LACK_PRODUCT.getCode(), Const.OrderStatusEnum.LACK_PRODUCT.getDesc());
                    return sr;
                }
    
                //购物信息转换成订单详情
                OrderItem newOrderItem = getNewOrderItem(uid, liCart, orderNo, product);
    
                //计算订单总价
                orderTotalPrice = BigDecimalUtils.add(orderTotalPrice.doubleValue(), newOrderItem.getTotalPrice().doubleValue());
    
                //转换成商品详情VO类
                OrderItemVO aNew = POJOtoVOUtils.getNew(newOrderItem);
    
                //放到集合中
                orderItemVoList.add(aNew);
    
                //将订单详情保存到数据库中
                int insert = orderItemMapper.insert(newOrderItem);
    
                //扣除对应商品库存,更新商品库存
                product.setStock(product.getStock() - liCart.getQuantity());
                int i = productMapper.updateByPrimaryKey(product);
    
                //清空购物车中已经下单的商品
                int is = cartMapper.deleteByPrimaryKey(liCart.getId());
            }
    
            //获取地址
            Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
    
            //创建orderVO类
            OrderVO aNewOrderVO = POJOtoVOUtils.getNew(newOrder, orderItemVoList, shipping);
            //设置订单VO类中的地址VO类
            aNewOrderVO.setShippingVO(POJOtoVOUtils.getNew(shippingMapper.selectByPrimaryKey(shippingId)));
    
            //返回数据
            sr = ServerResponse.createServerResponseBySuccess(aNewOrderVO);
    
        }else{
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.FALSE_CREAT.getCode(),Const.OrderStatusEnum.FALSE_CREAT.getDesc());
        }
        return sr;
    }
    
    /*获取购物车中的商品信息转换订单*/
    @Override
    public ServerResponse getOrderCartProduct(HttpSession session) {
        ServerResponse sr = null;
    
        //获取登陆用户id
        Integer uid = OpinionUtils.getUID(session);
    
        //查询该用户购物车中已选中商品
        List<Cart> liCarts = cartMapper.selectByUidAndCheckIn(uid);
    
        //创建订单总价
        BigDecimal orderTotalPrice = new BigDecimal(0);
    
        //创建订单详情list
        List<OrderItemVO> orderItemVoList = new ArrayList<>();
    
        //判断购物信息是否存在
        if (liCarts == null || liCarts.size() < 1) {
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_CART.getCode(),Const.CartCheckedEnum.EMPTY_CART.getDesc());
            return sr;
        }
        for (Cart liCart : liCarts) {
            //查看购物信息对应的商品是否存在
            Product product = productMapper.selectByPrimaryKey(liCart.getProductId());
    
            //购物信息转换成订单详情
            OrderItem newOrderItem = getNewOrderItem(uid, liCart, null, product);
    
            //计算订单总价
            orderTotalPrice = BigDecimalUtils.add(orderTotalPrice.doubleValue(), newOrderItem.getTotalPrice().doubleValue());
    
            //转换成商品详情VO类
            OrderItemVO aNew = POJOtoVOUtils.getNew(newOrderItem);
    
            //放到集合中
            orderItemVoList.add(aNew);
        }
    
            //创建转换类
        CartOrderItemVO coi = new CartOrderItemVO();
    
        //赋值
        coi.setOrderItemVoList(orderItemVoList);
        coi.setProductTotalPrice(orderTotalPrice);
        coi.setImageHost(PropertiesUtils.readByKey("imageHost"));
    
        sr = ServerResponse.createServerResponseBySuccess(coi);
        return sr;
    }
    
    /*订单List*/
    @Override
    public ServerResponse getlist(HttpSession session, Integer pageSize, Integer pageNum) {
        ServerResponse sr = null;
    
        //设置分页插件
        PageHelper.startPage(pageNum,pageSize);
    
        //设置orderVoList
        List<OrderVO> voList = new ArrayList<OrderVO>();
    
        //获取用户对应订单
        List<Order> orders = orderMapper.selectByUID(OpinionUtils.getUID(session));
    
        //判断是否有订单
        if(orders == null ||orders.size()<1){
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.NO_ORDERMSG.getCode(),Const.OrderStatusEnum.NO_ORDERMSG.getDesc());
            return sr;
        }
        for (Order order : orders) {
            //根据订单号查对应商品详情
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
            //设置OrderItemVOList
            List<OrderItemVO> orderItemVOList = new ArrayList<OrderItemVO>();
            //封装
            for (OrderItem orderItem : orderItems) {
                OrderItemVO aNew = POJOtoVOUtils.getNew(orderItem);
                orderItemVOList.add(aNew);
            }
            //获取收货地址信息
            Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
            //封装orderVo
            OrderVO orderVO = POJOtoVOUtils.getNew(order, orderItemVOList, shipping);
            //放入集合中
            voList.add(orderVO);
        }
    
        //分页处理
        PageInfo pageInfo = new PageInfo(voList);
        sr = ServerResponse.createServerResponseBySuccess(pageInfo);
        return sr;
    }
    
    /*订单详情detail*/
    @Override
    public ServerResponse getDetail(HttpSession session, Long orderNo) {
        ServerResponse sr = null;
    
        //非空校验
        sr = emptyParam(orderNo);
        if(sr != null){
            return sr;
        }
        //根据订单号查找订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        //判断是否有订单
        if(order == null){
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.NO_ORDERMSG.getCode(),Const.OrderStatusEnum.NO_ORDERMSG.getDesc());
            return sr;
        }
        //根据订单号查对应商品详情
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
        //设置OrderItemVOList
        List<OrderItemVO> orderItemVOList = new ArrayList<OrderItemVO>();
        //封装
        for (OrderItem orderItem : orderItems) {
            OrderItemVO aNew = POJOtoVOUtils.getNew(orderItem);
            orderItemVOList.add(aNew);
        }
        //获取收货地址信息
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        //封装orderVo
        OrderVO orderVO = POJOtoVOUtils.getNew(order, orderItemVOList, shipping);
    
        //
        sr = ServerResponse.createServerResponseBySuccess(orderVO);
        return sr;
    }
    
    /*取消订单*/
    @Override
    public ServerResponse cancelOrder(HttpSession session, Long orderNo) {
        ServerResponse sr = null;
        //非空校验
        sr = emptyParam(orderNo);
        if(sr != null){
            return sr;
        }
    
        //改变订单状态
        Order order = orderMapper.selectByOrderNo(orderNo);
        //非空判断
        if(order == null){
            //订单不存在
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.NO_ORDERMSG.getCode(),Const.OrderStatusEnum.NO_ORDERMSG.getDesc());
            return sr;
        }
        //状态判断
        if(order.getStatus() != 10){
            //除了未付款之外的订单不允许取消
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.ACCOUNT_PAID.getCode(),Const.OrderStatusEnum.ACCOUNT_PAID.getDesc());
            return sr;
        }
    
        //修改订单状态
        order.setStatus(0);
        int i = orderMapper.updateByPrimaryKey(order);
    
        //根据订单号查对应商品详情
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
    
        //恢复商品库存
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectByPrimaryKey(item.getProductId());
    
            product.setStock(product.getStock()+item.getQuantity());
            int ins = productMapper.updateByPrimaryKey(product);
        }
    
        sr = ServerResponse.createServerResponseBySuccess("订单取消成功");
        return sr;
    }




    /*==========================支付宝支付模块================================*/
    public ServerResponse aliPay(HttpSession session, Long orderNo){
        ServerResponse sr = null;
        //参数非空判断
        if(orderNo == null){
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.ORDEREMPTY_PARAM.getCode(),Const.OrderStatusEnum.ORDEREMPTY_PARAM.getDesc());
            return sr;
        }
    
        //判断订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.NO_PAYORDER.getCode(),Const.OrderStatusEnum.NO_PAYORDER.getDesc());
            return sr;
        }else if(order.getUserId() != OpinionUtils.getUID(session)){
            //判断是否是该用户订单
            sr = ServerResponse.createServerResponseByError(Const.OrderStatusEnum.BAD_PAYORDER.getCode(),Const.OrderStatusEnum.BAD_PAYORDER.getDesc());
            return sr;
        }
    
        //根据订单号查对应商品详情
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
    
        //调用支付宝接口获取支付二维码
        try {
            //使用封装方法获得预下单成功后返回的二维码信息串
            AlipayTradePrecreateResponse response = test_trade_precreate(order, orderItems);

//            String basePath = request.getSession().getServletContext().getRealPath("/");
//            String fileName = String.format("images%sqr-%s.png", File.separator, response.getOutTradeNo());
//            String filePath = new StringBuilder(basePath).append(fileName).toString();

            //成功执行下一步
            if(response.isSuccess()){
                // 将二维码信息串生成图片，并保存，（需要修改为运行机器上的路径）
                String filePath = String.format("D:/payimages/qr-%s.png",
                        response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                //预下单成功返回信息
                Map map =new HashMap();
                map.put("orderNo",order.getOrderNo());
                map.put("qrCode",filePath);
                sr = ServerResponse.createServerResponseBySuccess(map);
                return sr;
            }else{
                //预下单失败
                sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.ALIPAY_FALSE.getCode(),Const.PaymentPlatformEnum.ALIPAY_FALSE.getDesc());
                return sr;
            }
        } catch (AlipayApiException e) {
            //出现异常，预下单失败
            e.printStackTrace();
            sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.ALIPAY_FALSE.getCode(),Const.PaymentPlatformEnum.ALIPAY_FALSE.getDesc());
            return sr;
        }
    }

    // 测试当面付2.0生成支付二维码
    private AlipayTradePrecreateResponse test_trade_precreate(Order order,List<OrderItem> orderItems) throws AlipayApiException {
        Configs.init("zfbinfo.properties");


        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Configs.getOpenApiDomain(),
                Configs.getAppid(),Configs.getPrivateKey(),"json","utf-8",
                Configs.getAlipayPublicKey(),Configs.getSignType());
    
        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

    
        //获取一个BizContent对象,并转换成json格式
        String str = JsonUtils.obj2String(POJOtoVOUtils.getBizContent(order,orderItems));
        request.setBizContent(str);
        request.setNotifyUrl(Configs.getNotifyUrl_test()+"portal/order/alipay_callback.do");
        //获取响应,这里要处理一下异常
        AlipayTradePrecreateResponse response = alipayClient.execute(request);


        //返回响应的结果
        return response;
    }

    /*支付宝回调函数*/
    @Override
    public ServerResponse alipayCallback(Map<String, String> map) {
        ServerResponse sr = null;

        //step1:获取ordrNo
        Long orderNo=Long.parseLong(map.get("out_trade_no"));
        //step2:获取流水号
        String tarde_no=map.get("trade_no");
        //step3:获取支付状态
        String trade_status=map.get("trade_status");
        //step4:获取支付时间
        String payment_time=map.get("gmt_payment");


        Order order=orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            //不是要付款的订单
            sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.VERIFY_ORDER_FALSE.getCode(),orderNo+Const.PaymentPlatformEnum.VERIFY_ORDER_FALSE.getDesc());
            return sr;
        }

        if(order.getStatus()>=Const.OrderStatusEnum.ORDER_PAYED.getCode()){
            //防止支付宝重复回调
            sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.REPEAT_USEALIPAY.getCode(),Const.PaymentPlatformEnum.REPEAT_USEALIPAY.getDesc());
            return sr;
        }

        if(trade_status.equals(Const.TRADE_SUCCESS)){
            //校验状态码，支付成功
            //更改数据库中订单的状态+更改支付时间+更新时间
            order.setStatus(Const.OrderStatusEnum.ORDER_PAYED.getCode());
            order.setPaymentTime(DateUtils.strToDate(payment_time));
            orderMapper.updateByPrimaryKey(order);
        }

        //保存支付宝支付信息
        PayInfo payInfo=new PayInfo();
        payInfo.setOrderNo(orderNo);
        payInfo.setPayPlatform(Const.PaymentPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformStatus(trade_status);
        payInfo.setPlatformNumber(tarde_no);
        payInfo.setUserId(order.getUserId());

        int result= payInfoMapper.insert(payInfo);
        if(result>0){
            //支付信息保存成功返回结果
            sr = ServerResponse.createServerResponseBySuccess("支付信息保存成功");
            return sr;
        }
        //支付信息保存失败返回结果
        sr = ServerResponse.createServerResponseByError(Const.PaymentPlatformEnum.SAVEPAYMSG_FALSE.getCode(),Const.PaymentPlatformEnum.SAVEPAYMSG_FALSE.getDesc());
        return sr;
    }

    //支付成功，修改订单状态、支付时间、最后一次更新时间，并返回成功信息
    //支付失败，不做修改，返回失败信息

    /*查询订单支付状态*/
    @Override
    public ServerResponse queryOrderPayStatus(Long orderNo) {
        if(orderNo==null){
            return  ServerResponse.createServerResponseByError("订单号不能为空");
        }
        Order order= orderMapper.selectByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        if(order.getStatus()==Const.OrderStatusEnum.ORDER_PAYED.getCode()){
            return ServerResponse.createServerResponseBySuccess(true);
        }
        return ServerResponse.createServerResponseBySuccess(false);
    }

    /**
     * 配置取消订单日志记录
     * */
    Logger logger=LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;
    public void recordOrderCancelLog(Long orderno){
        Order order=orderMapper.selectByOrderNo(orderno);
        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(order.getUserId());
        String userIp=userInfo.getUserIp()!=null?userInfo.getUserIp():"0.0.0.0";
    
        String info=NeueduAnalyticsEngineSDK.recordCancelOrderLog(userIp,String.valueOf(orderno),
                String.valueOf(userInfo.getId()),
                String.valueOf(order.getCreateTime().getTime()),
                System.currentTimeMillis()+"");
        logger.info(info);
    }

}
