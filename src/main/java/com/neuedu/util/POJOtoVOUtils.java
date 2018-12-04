package com.neuedu.util;

import com.neuedu.common.Const;
import com.neuedu.pojo.*;
import com.neuedu.pojo.vo.*;

import java.util.List;

/*pojo类转换成VO类*/
public class POJOtoVOUtils {
    /*product to productVO*/
    public static ProductVO getNew(Product product){
        ProductVO p = new ProductVO();
        p.setId(product.getId());
        p.setCategoryId(product.getCategoryId());
        p.setName(product.getName());
        p.setSubtitle(product.getSubtitle());
        p.setMainImage(product.getMainImage());
        p.setSubImages(product.getSubImages());
        p.setDetail(product.getDetail());
        p.setPrice(product.getPrice());
        p.setStock(product.getStock());
        p.setStatus(product.getStatus());
        p.setIs_new(product.getIs_new());
        p.setIs_hot(product.getIs_hot());
        p.setIs_banner(product.getIs_banner());
        p.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        p.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        //设置图片服务器
        p.setImageHost(PropertiesUtils.readByKey("imageHost"));
        return p;
    }

    /*Cart+Product+UserInfo to ProductCartVo*/
    public static ProductCartVo getNew(Integer uid, Cart cart, Product product){
        ProductCartVo p = new ProductCartVo();
        p.setId(cart.getId());
        p.setUserId(uid);
        p.setProductId(cart.getProductId());
        p.setQuantity(cart.getQuantity());
        p.setProductName(product.getName());
        p.setProductSubtitle(product.getSubtitle());
        p.setProductMainImage(product.getMainImage());
        p.setProductPrice(product.getPrice());
        p.setProductStatus(product.getStatus());
        //本条购物信息中的商品总价格=商品数量*商品单价
        p.setProductTotalPrice(BigDecimalUtils.mul(cart.getQuantity(),product.getPrice().doubleValue()));
        //商品库存数量
        p.setProductStock(product.getStock());
        //商品是否勾选
        p.setProductChecked(cart.getChecked());
        //商品数量与库存数量比较
        if(p.getQuantity()>p.getProductStock()){
            p.setLimitQuantity("LIMIT_NUM_FAIL");
        }else{
            p.setLimitQuantity("LIMIT_NUM_SUCCESS");
        }
        return p;
    }

    /*OrderItem to OrderItemVO*/
    public  static OrderItemVO getNew(OrderItem oi){
        OrderItemVO itemVO = new OrderItemVO();
        itemVO.setOrderNo(oi.getOrderNo());
        itemVO.setProductId(oi.getProductId());
        itemVO.setProductName(oi.getProductName());
        itemVO.setProductImage(oi.getProductImage());
        itemVO.setCurrentUnitPrice(oi.getCurrentUnitPrice());
        itemVO.setQuantity(oi.getQuantity());
        //该条详情中的总价格
        itemVO.setTotalPrice(oi.getTotalPrice());
        //获取创建时间，并转换成字符串
        itemVO.setCreateTime(DateUtils.dateToStr(oi.getCreateTime()));

        return itemVO;
    }

    /*Order+orderItemList+shipping to */
    public static OrderVO getNew(Order order, List<OrderItemVO> itemVOList,Shipping shipping){
        OrderVO vo = new OrderVO();
        vo.setOrderNo(order.getOrderNo());
        vo.setPayment(order.getPayment());
        vo.setPayment_type(order.getPaymentType());
        //线上支付
        if(order.getStatus() == 1){
            vo.setPaymentTypeDesc(Const.PaymentEnum.ONLINE.getDesc());
        }

        if(order.getStatus() == 2){
            vo.setPaymentTypeDesc(Const.PaymentEnum.OUTLINE.getDesc());
        }
        vo.setPostage(order.getPostage());
        vo.setStatus(order.getStatus());
        //支付状态
        if(order.getStatus() == 0){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_CANCELED.getDesc());
        }
        if(order.getStatus() == 10){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_UN_PAY.getDesc());
        }
        if(order.getStatus() == 20){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_PAYED.getDesc());
        }
        if(order.getStatus() == 40){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_SEND.getDesc());
        }
        if(order.getStatus() == 50){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_SUCCESS.getDesc());
        }
        if(order.getStatus() == 60){
            vo.setStatusDesc(Const.OrderStatusEnum.ORDER_CLOSED.getDesc());
        }

        vo.setPaymentTime(DateUtils.dateToStr(order.getPaymentTime()));
        vo.setSendTime(DateUtils.dateToStr(order.getSendTime()));
        vo.setEndTime(DateUtils.dateToStr(order.getEndTime()));
        vo.setCloseTime(DateUtils.dateToStr(order.getCloseTime()));
        //设置购物信息
        vo.setOrderItemVoList(itemVOList);
        //设置图片服务器
        vo.setImageHost(PropertiesUtils.readByKey("imageHost"));
        //设置地址id
        vo.setShippingId(order.getShippingId());
        //设置收货人姓名
        vo.setReceiverName(shipping.getReceiverName());

        return vo;
    }

    /*shipping to shippingVO*/
    public static ShippingVO getNew(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        shippingVO.setReceiverName(shipping.getReceiverName());
        shippingVO.setReceiverPhone(shipping.getReceiverPhone());
        shippingVO.setReceiverMobile(shipping.getReceiverMobile());
        shippingVO.setReceiverProvince(shipping.getReceiverProvince());
        shippingVO.setReceiverCity(shipping.getReceiverCity());
        shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVO.setReceiverAddress(shipping.getReceiverAddress());
        shippingVO.setReceiverZip(shipping.getReceiverZip());
        return shippingVO;
    }
}
