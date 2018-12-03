package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *@Author teacher.zhang
 * */
public interface ICartService {

    /*购物车List列表*/
    ServerResponse getDetail(HttpSession session);

    /*购物车添加商品*/
    ServerResponse addNew(HttpSession session,Integer productId,Integer count);

    /*更新购物车某个产品数量*/
    ServerResponse updateCart(HttpSession session,Integer productId,Integer count);

    /*移除购物车某个产品数量*/
    ServerResponse deleteProduct(HttpSession session, String productIds);

    /*购物车选中某个商品*/
    ServerResponse selectProduct(HttpSession session, Integer productId);

    /*购物车取消选中某个商品*/
    ServerResponse unSelectProduct(HttpSession session, Integer productId);

    /*查询在购物车里的产品数量*/
    ServerResponse getCartProductCount(HttpSession session);

    /*购物车全选*/
    ServerResponse selectAllProduct(HttpSession session);

    /*购物车取消全选*/
    ServerResponse unSelectAll(HttpSession session);
}
