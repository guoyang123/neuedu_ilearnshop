package com.neuedu.pojo.pay;


import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;

import java.util.List;


/*获取一个BizContent对象*/
public class BizContent {

    private String outTradeNo;

    private String sellerId;

    private String totalAmount;

    private String discountableAmount;

    private String undiscountableAmount;
    private String subject;
    private String body;

    private List<GoodsDetail> goodsDetailList;

    private String operatorId;

    private String storeId;

    private String alipayStoreId;

    private String terminalId;

    private ExtendParams extendParams;

    private String timeoutExpress;
    private String notifyUrl;

    public BizContent() {
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscountableAmount() {
        return discountableAmount;
    }

    public void setDiscountableAmount(String discountableAmount) {
        this.discountableAmount = discountableAmount;
    }

    public String getUndiscountableAmount() {
        return undiscountableAmount;
    }

    public void setUndiscountableAmount(String undiscountableAmount) {
        this.undiscountableAmount = undiscountableAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<GoodsDetail> getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlipayStoreId() {
        return alipayStoreId;
    }

    public void setAlipayStoreId(String alipayStoreId) {
        this.alipayStoreId = alipayStoreId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public ExtendParams getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(ExtendParams extendParams) {
        this.extendParams = extendParams;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }
}
