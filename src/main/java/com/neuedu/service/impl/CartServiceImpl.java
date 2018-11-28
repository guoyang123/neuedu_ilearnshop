package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.vo.CartVO;
import com.neuedu.pojo.vo.ProductCartVo;
import com.neuedu.service.ICartService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.POJOtoVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /*购物车List列表*/

    @Override
    public ServerResponse getDetail(HttpSession session) {
        ServerResponse sr = null;
        List<ProductCartVo> li = new ArrayList<ProductCartVo>();
        BigDecimal cartTotalPrice = new BigDecimal(0.00);
        boolean allChecked = true;

        //获取用户
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());

        //查询用户对应购物信息
        List<Cart> liCart = cartMapper.selectByUID(ui.getId());

        //是否有购物信息判断
        if(liCart == null || liCart.size() == 0){
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_CART.getCode(),Const.CartCheckedEnum.EMPTY_CART.getDesc());
            return sr;
        }else{

            //根据购物信息获取对应的商品信息
            for (Cart cart : liCart) {
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());

                //封装ProductCartVo复合类
                ProductCartVo pcv = POJOtoVOUtils.getNew(ui,cart,product);
                //封装成集合
                li.add(pcv);
                //计算购物信息总价格
                cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),pcv.getProductTotalPrice().doubleValue());
                //判断是否全选
                if(cart.getChecked() == Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode()){
                    allChecked = false;
                }
            }
        }



        //封装购物车类
        CartVO cv = new CartVO(li,allChecked,cartTotalPrice);

        //放入数据
        sr = ServerResponse.createServerResponseBySuccess(cv);
        return sr;
    }
}
