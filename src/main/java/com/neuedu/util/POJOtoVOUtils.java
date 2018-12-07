package com.neuedu.util;

import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.neuedu.common.Const;
import com.neuedu.pojo.*;
import com.neuedu.pojo.pay.BizContent;
import com.neuedu.pojo.pay.Configs;
import com.neuedu.pojo.pay.PGoodsDetail;
import com.neuedu.pojo.vo.*;

import java.util.ArrayList;
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
        if(order.getPaymentType() == 1){
            vo.setPaymentTypeDesc(Const.PaymentEnum.ONLINE.getDesc());
        }

        if(order.getPaymentType() == 2){
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

        vo.setPaymentTime("");
        vo.setSendTime("");
        vo.setEndTime("");
        vo.setCloseTime("");
        //设置创建时间
        vo.setCreateTime(DateUtils.dateToStr(order.getCreateTime()));
        //设置更新时间
        vo.setUpdateTime(DateUtils.dateToStr(order.getUpdateTime()));
        //设置购物信息
        vo.setOrderItemVoList(itemVOList);
        //设置图片服务器
        vo.setImageHost(PropertiesUtils.readByKey("imageHost"));
        //设置地址id
        vo.setShippingId(order.getShippingId());
        if(shipping!=null){
            //设置收货人姓名
            vo.setReceiverName(shipping.getReceiverName());
            //设置收货对象
            vo.setShippingVO(getNew(shipping));
        }


        return vo;
    }

    /*shipping to shippingVO*/
    public static ShippingVO getNew(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        if(shipping==null){
            return shippingVO;
        }
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

    /*商品详情和支付宝商品类转换*/
    public static PGoodsDetail getNewPay(OrderItem orderItem){
        PGoodsDetail info = new PGoodsDetail();
        info.setGoods_id(orderItem.getProductId().toString());
        info.setGoods_name(orderItem.getProductName());
        info.setPrice(orderItem.getCurrentUnitPrice().toString());
        info.setQuantity(orderItem.getQuantity().longValue());
        return info;
    }

    /*获取一个BizContent对象*/
    public static BizContent getBizContent(Order order,List<OrderItem> orderItems){
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "睿乐GO在线平台"+order.getPayment();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getPayment());

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+orderItems.size()+"件共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "001";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem orderItem : orderItems) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = getNewPay(orderItem);
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        BizContent biz = new BizContent();
        biz.setSubject(subject);
        biz.setTotal_amount(totalAmount);
        biz.setOut_trade_no(outTradeNo);
        biz.setUndiscountable_amount(undiscountableAmount);
        biz.setSeller_id(sellerId);
        biz.setBody(body);
        biz.setOperator_id(operatorId);
        biz.setStore_id(storeId);
        biz.setExtend_params(extendParams);
        biz.setTimeout_express(timeoutExpress);
        //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        //biz.setNotify_url(Configs.getNotifyUrl_test()+"portal/order/alipay_callback.do");
        biz.setGoods_detail(goodsDetailList);

        return biz;
    }
}
