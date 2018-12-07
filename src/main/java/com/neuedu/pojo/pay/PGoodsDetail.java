package com.neuedu.pojo.pay;

import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.internal.mapping.ApiField;

public class PGoodsDetail extends GoodsDetail {
    private static final long serialVersionUID = 7677251125598872494L;

    private String alipay_goods_id;

    private String body;

    private String categories_tree;

    private String goods_category;

    private String goods_id;

    private String goods_name;

    private String price;

    private Long quantity;

    private String show_url;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAlipay_goods_id() {
        return alipay_goods_id;
    }

    public void setAlipay_goods_id(String alipay_goods_id) {
        this.alipay_goods_id = alipay_goods_id;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    public String getCategories_tree() {
        return categories_tree;
    }

    public void setCategories_tree(String categories_tree) {
        this.categories_tree = categories_tree;
    }

    public String getGoods_category() {
        return goods_category;
    }

    public void setGoods_category(String goods_category) {
        this.goods_category = goods_category;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public Long getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }
}
