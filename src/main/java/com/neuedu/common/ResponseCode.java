package com.neuedu.common;

/**
 * 维护状态码
 * */
public class ResponseCode {

    /**
     * 成功的状态码
     * */
    public static final  int SUCESS=0;

    /**
     * 失败时通用状态码
     * */
    public static  final int ERROR=100;

    /*
    * 用户登录失败状态码:密码错误
    * */
    public static final int ERROR_PASSWORD=1;

    /*用户不存在状态码*/
    public static final int INEXISTENCE_USER = 101;

    /*用户注册失败状态码：用户已存在*/
    public static final int EXIST_USER=1;

    /*用户注册失败状态码：邮箱已存在*/
    public static final int EXIST_EMAIL=2;

    /*获取用户信息失败状态码：用户未登录*/
    public static final int NO_LOGIN=10;

    /*忘记密码失败状态码：该用户未设置找回密码问题*/
    public static final int NO_QUESTION = 1;

    /*密码问题答案错误*/
    public static final int ERROR_ANSWER = 1;

    /*token已经失效*/
    public static final int LOSE_EFFICACY = 103;

    /*非法的token*/
    public static final int UNLAWFULNESS_TOKEN = 104;

    /*修改密码操作失效*/
    public static final int DEFEACTED_PASSWORDNEW = 1;

    /*修改密码操作失效*/
    public static final int ERROR_PASSWORDOLD = 1;

    /*用户未登录，无法获取当前用户信息,status=10强制退出*/
    public static final int FORCE_EXIT = 10;

    /*=====================商品相关==========================*/

    /*搜索商品参数错误*/
    public static final int ERROR_PAMAR = 1;

    /*该商品已下架*/
    public static final int NO_PRODUCT = 4;

    /*====================购物车相关=========================*/
    /*参数不能为空*/
    public static final int EMPTY_PARAM = 9;

    /*用户未登录*/
    public  static final int NO_SESSION = 10;

    /*购物车是空的*/
    public  static final int EMPTY_CART = 1;

    /*要移除的商品不存在*/
    public static final int UNEXIST_P = 3;

    /*更新数据失败*/
    public  static final int FALSE_UPDATE = 2;

    /*====================订单相关=========================*/
    /*创建订单失败*/
    public  static final int FALSE_CREAT = 1;

    /*参数不能为空*/
    public  static final int ORDEREMPTY_PARAM = 9;

    /*还没有选中任何商品*/
    public  static final int EMPTY_CARTS = 2;

    /*商品库存不足*/
    public  static final int LACK_PRODUCT = 3;

    /*未查询到订单信息*/
    public  static final int NO_ORDERMSG = 5;

    /*该订单已付款*/
    public  static final int ACCOUNT_PAID = 6;

    /*要支付的订单不存在*/
    public  static final int NO_PAYORDER= 7;

    /*要支付的订单不合法*/
    public  static final int BAD_PAYORDER= 7;
}
