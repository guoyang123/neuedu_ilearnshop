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
import org.apache.ibatis.annotations.Param;
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

    /*创建获取用户id私有方法*/
    private Integer getUid(HttpSession session) {
        //获取用户
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        return ui.getId();
    }

    /*创建私有方法对cart的增删改查*/
    /*增加*/
    private int addCart(Integer uid, Integer productId, Integer count) {
        //创建一条新的信息
        Cart c = new Cart();
        c.setUserId(uid);
        c.setProductId(productId);
        c.setQuantity(count);
        c.setChecked(1);

        //插入到数据库中
        int insert = cartMapper.insert(c);
        return insert;
    }

    /*删除*/
    /*修改*/
    private int updateCart(Cart c, Integer count, Integer uid, int qb) {
        //商品信息已存在，在原有基础上更新数量数据
        //标识符判断,更新数量
        if (qb == 101) {
            c.setQuantity(c.getQuantity() + count);
        } else {
            c.setQuantity(count);
        }
        //确定用户
        c.setUserId(uid);
        //更新数据库中数据
        int insert = cartMapper.updateByPrimaryKey(c);
        return insert;
    }
    /*查询*/


    /*判断用户购物信息是否存在*/
    private int existCart(HttpSession session, Integer productId, Integer count, int qb) {
        //获取用户ID
        Integer uid = getUid(session);

        //判断该用户商品信息是否存在,存在就更新，不存在就新建一条
        Cart c = cartMapper.selectByUidAndProductId(uid, productId);
        int insert = 0;
        if (c == null) {
            //商品信息不存在，创建一条新的信息
            //插入到数据库中
            insert = addCart(uid, productId, count);
        } else {
            //商品信息已存在，在原有基础上更新数量数据
            //计算新的购物数量
            //更新数量
            //更新数据库中数据
            insert = updateCart(c, count, uid, qb);
        }
        return insert;
    }

    /*封裝购物车实体类*/
    private CartVO getCartVO(HttpSession session) {
        List<ProductCartVo> li = new ArrayList<ProductCartVo>();
        BigDecimal cartTotalPrice = new BigDecimal(0.00);
        boolean allChecked = true;
        CartVO cv = null;
        Integer uid = getUid(session);

        //查询用户对应购物信息
        List<Cart> liCart = cartMapper.selectByUID(getUid(session));
        //商品信息不存在返回购物车实体类空
        if (liCart == null || liCart.size() == 0) {
            return cv;
        } else {
            //根据购物信息获取对应的商品信息
            for (Cart cart : liCart) {
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());

                //封装ProductCartVo复合类
                ProductCartVo pcv = POJOtoVOUtils.getNew(uid, cart, product);
                //封装成集合
                li.add(pcv);
                //计算选中的购物信息总价格
                if (cart.getChecked() == Const.CartCheckedEnum.PRODUCT_CHECKED.getCode()) {
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(), pcv.getProductTotalPrice().doubleValue());
                }
                //判断是否全选
                if (cart.getChecked() == Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode()) {
                    allChecked = false;
                }
            }
            //封装购物车类
            cv = new CartVO(li, allChecked, cartTotalPrice);
            //返回成功数据
            return cv;
        }
    }

    /*返回数据统一*/
    private ServerResponse sendSR(HttpSession session) {
        ServerResponse sr = null;
        //封装购物车类
        CartVO cv = getCartVO(session);

        if (cv == null) {
            //返回失败数据
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_CART.getCode(), Const.CartCheckedEnum.EMPTY_CART.getDesc());
        } else {
            //返回成功数据
            sr = ServerResponse.createServerResponseBySuccess(cv);
        }
        return sr;
    }

    private ServerResponse sendSR(HttpSession session, int insert) {
        ServerResponse sr = null;
        //失败返回错误信息
        if (insert < 1) {
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.FALSE_UPDATE.getCode(), Const.CartCheckedEnum.FALSE_UPDATE.getDesc());
        } else {
            //成功返回数据
            sr = ServerResponse.createServerResponseBySuccess(getCartVO(session));
        }
        return sr;
    }

    /*参数非空判断，参数任意数量处理*/
    private ServerResponse getSR(Integer productId,Integer count) {
        ServerResponse sr = null;
        //参数非空判断
        if (productId == null || count == null) {
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_PARAM.getDesc());
        }
        return sr;
    }
    private ServerResponse getSR(Integer productId) {
        ServerResponse sr = null;
        //参数非空判断
        if (productId == null) {
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_PARAM.getDesc());
        }
        return sr;
    }

    private ServerResponse getSR(String productIds) {
        ServerResponse sr = null;
        //参数非空判断
        if (productIds.equals("") || productIds == null) {
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.EMPTY_PARAM.getDesc());
        }
        return sr;
    }

    /*购物车List列表*/
    @Override
    public ServerResponse getDetail(HttpSession session) {
        return sendSR(session);
    }

    /*购物车添加商品*/
    @Override
    public ServerResponse addNew(HttpSession session, Integer productId, Integer count) {
        ServerResponse sr = getSR(productId, count);
        if(sr != null){
            return sr;
        }

        //区别标识符101
        int qb = Const.CartCheckedEnum.BZ_XQ.getCode();
        int insert = existCart(session, productId, count, qb);

        //返回数据
        return sendSR(session, insert);
    }

    /*更新购物车某个产品数量*/
    @Override
    public ServerResponse updateCart(HttpSession session, Integer productId, Integer count) {
        ServerResponse sr = getSR(productId, count);
        if(sr != null){
            return sr;
        }

        //设置标识符102
        int qb = Const.CartCheckedEnum.BZ_GWC.getCode();
        int insert = existCart(session, productId, count, qb);

        //返回数据
        return sendSR(session, insert);
    }

    /*移除购物车某个产品数量*/
    @Override
    public ServerResponse deleteProduct(HttpSession session, String productIds) {
        ServerResponse sr = getSR(productIds);
        if(sr != null){
            return sr;
        }

        //字符串参数转成集合
        String[] productsLi = productIds.split(",");

        //查询商品是否存在
        for (String s : productsLi) {
            Cart c = cartMapper.selectByUidAndProductId(getUid(session), Integer.parseInt(s));
            if (c == null) {
                //要移除的商品不存在
                sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.UNEXIST_P.getCode(), Const.CartCheckedEnum.UNEXIST_P.getDesc());
            } else {
                //移除选中的商品
                int insert = cartMapper.deleteByPrimaryKey(c.getId());
                //返回数据
                sr = sendSR(session, insert);
            }
        }
        return sr;
    }

    /*购物车选中某个商品*/
    @Override
    public ServerResponse selectProduct(HttpSession session, Integer productId) {
        ServerResponse sr = getSR(productId);
        if(sr != null){
            return sr;
        }
        //找到该用户对应的这条商品信息
        Cart c = cartMapper.selectByUidAndProductId(getUid(session), productId);
        if (c == null) {
            //信息不存在
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.UNEXIST_P.getCode(), Const.CartCheckedEnum.UNEXIST_P.getDesc());
        } else {
            //信息存在，设置选中
            c.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            //更新信息
            int insert = cartMapper.updateByPrimaryKey(c);
            //返回数据
            sr = sendSR(session, insert);
        }
        return sr;
    }

    /*购物车取消选中某个商品*/
    @Override
    public ServerResponse unSelectProduct(HttpSession session, Integer productId) {
        ServerResponse sr = getSR(productId);
        if(sr != null){
            return sr;
        }
        //找到该用户对应的这条商品信息
        Cart c = cartMapper.selectByUidAndProductId(getUid(session), productId);
        if (c == null) {
            //信息不存在
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.UNEXIST_P.getCode(), Const.CartCheckedEnum.UNEXIST_P.getDesc());
        } else {
            //信息存在，设置取消选中
            c.setChecked(Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
            //更新信息
            int insert = cartMapper.updateByPrimaryKey(c);
            //返回数据
            sr = sendSR(session, insert);
        }
        return sr;
    }

    /*查询在购物车里的产品数量*/
    @Override
    public ServerResponse getCartProductCount(HttpSession session) {
        ServerResponse sr = null;

        //查询用户对应购物信息
        List<Cart> liCart = cartMapper.selectByUID(getUid(session));
        sr = ServerResponse.createServerResponseBySuccess(liCart.size());
        return sr;
    }

    /*购物车全选*/
    @Override
    public ServerResponse selectAllProduct(HttpSession session) {
        ServerResponse sr = null;


        //查询用户对应购物信息
        List<Cart> liCart = cartMapper.selectByUID(getUid(session));

        if (liCart == null || liCart.size()<1) {
            //信息不存在
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.UNEXIST_P.getCode(), Const.CartCheckedEnum.UNEXIST_P.getDesc());
        } else {

            //信息存在，设置全部选中
            int insert = 0;
            for (Cart cart : liCart) {
                cart.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
                //更新信息
                insert = cartMapper.updateByPrimaryKey(cart);
            }
            //返回数据
            sr = sendSR(session, insert);
        }
        return sr;
    }

    /*购物车全不选*/
    @Override
    public ServerResponse unSelectAll(HttpSession session) {
        ServerResponse sr = null;


        //查询用户对应购物信息
        List<Cart> liCart = cartMapper.selectByUID(getUid(session));

        if (liCart == null || liCart.size()<1) {
            //信息不存在
            sr = ServerResponse.createServerResponseByError(Const.CartCheckedEnum.UNEXIST_P.getCode(), Const.CartCheckedEnum.UNEXIST_P.getDesc());
        } else {

            //信息存在，设置全部选中
            int insert = 0;
            for (Cart cart : liCart) {
                cart.setChecked(Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
                //更新信息
                insert = cartMapper.updateByPrimaryKey(cart);
            }
            //返回数据
            sr = sendSR(session, insert);
        }
        return sr;
    }
}
