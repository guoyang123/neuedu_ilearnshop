package com.neuedu.pojo.vo;

import java.math.BigDecimal;
import java.util.List;

/*高复用购物车实体类*/
public class CartVO {
    private List<ProductCartVo> cartProductVoList;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    public CartVO(List<ProductCartVo> cartProductVoList, boolean allChecked, BigDecimal cartTotalPrice) {
        this.cartProductVoList = cartProductVoList;
        this.allChecked = allChecked;
        this.cartTotalPrice = cartTotalPrice;
    }
    public CartVO() {

    }

    public List<ProductCartVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<ProductCartVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
