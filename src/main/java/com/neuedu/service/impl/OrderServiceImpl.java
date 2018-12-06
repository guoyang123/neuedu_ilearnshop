package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.log.NeueduAnalyticsEngineSDK;
import com.neuedu.pojo.*;
import com.neuedu.pojo.vo.CartOrderItemVO;
import com.neuedu.pojo.vo.OrderItemVO;
import com.neuedu.pojo.vo.OrderVO;
import com.neuedu.service.IOrderService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.OpinionUtils;
import com.neuedu.util.POJOtoVOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        coi.setImageHost("暂时不设置");

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
        //
        Order order = orderMapper.selectByPrimaryKey(orderNo);
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
        Order order = orderMapper.selectByPrimaryKey(orderNo);
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

    /**
     * 配置取消订单日志记录
     * */
    Logger logger=LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;
    public void recordOrderCancelLog(Long orderno){
        Order order=orderMapper.selectByPrimaryKey(orderno);
        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(order.getUserId());
        String userIp=userInfo.getUserIp()!=null?userInfo.getUserIp():"0.0.0.0";

        String info=NeueduAnalyticsEngineSDK.recordCancelOrderLog(userIp,String.valueOf(orderno),
                String.valueOf(userInfo.getId()),
                String.valueOf(order.getCreateTime().getTime()),
                System.currentTimeMillis()+"");
        logger.info(info);
    }

}
