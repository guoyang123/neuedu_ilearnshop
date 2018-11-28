package com.neuedu.util;

import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.vo.ProductCartVo;
import com.neuedu.pojo.vo.ProductVO;

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
        p.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        p.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        return p;
    }

    /*Cart+Product+UserInfo to ProductCartVo*/
    public static ProductCartVo getNew(UserInfo ui, Cart cart, Product product){
        ProductCartVo p = new ProductCartVo();
        p.setId(cart.getId());
        p.setUserId(ui.getId());
        p.setProductId(cart.getProductId());
        p.setQuantity(cart.getQuantity());
        p.setProductName(product.getName());
        p.setProductSubtitle(product.getSubtitle());
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
}
