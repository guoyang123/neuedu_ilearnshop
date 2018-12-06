
CREATE USER 'neuedu'@'localhost' IDENTIFIED BY 'Qinfo20180507@';
flush privileges;

create database `shopping` default character set utf8 collate utf8_general_ci;
grant all privileges  on shopping.* to  'neuedu'@'localhost' identified by 'Qinfo20180507@';

create database if not exists shopping;
use shopping;
# 用户表
create table neuedu_user(
 `id`       int(11)      not null  auto_increment comment '用户id',
 `username`       varchar(50)      not null   comment '用户名',
 `password`       varchar(50)      not null   comment '用户密码,MD5加密',
 `email`       varchar(50)      not null   comment '用户email',
 `phone`       varchar(20)    not null   comment '用户phone',
 `question`      varchar(100)      not null   comment '找回密码问题',
 `answer`      varchar(100)      not null   comment '找回密码答案',
 `role`       int(4)      not null   comment '角色0-管理员,1-普通用户',
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`),
 UNIQUE KEY `user_name_unique` (`username`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

alter table neuedu_user add column ip varchar(20) default  '';

# 类别表
create table neuedu_category(
 `id`       int(11)      not null  auto_increment comment '类别id',
 `parent_id`       int(11)      default null   comment '父类Id,当pareng_id=0,说明是根节点，一级类别',
 `name`       varchar(50)      DEFAULT null   comment '类别名称',
 `status`       tinyint(1)      DEFAULT '1'  comment '类别状态1-正常，2-已废弃',
 `sort_order`       int(4)    DEFAULT null   comment '排序编号，同类展示顺序，数值相等则自然排序',
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=100032 DEFAULT CHARSET=utf8;

 

# 产品表
create table neuedu_product(
 `id`       int(11)      not null  auto_increment comment '商品id',
 `category_id`       int(11)      not  null   comment '类别id，对应neuedu_category表的主键',
 `name`       varchar(100)      not null   comment '商品名称',
 `subtitle`       varchar(200)      DEFAULT null   comment '商品副标题',
 `main_image`       varchar(500)      DEFAULT null   comment '产品主图，url相对地址',
 `sub_images`       text       comment '图片地址，json格式',
 `detail`       text        comment '商品详情',
 `price`       DECIMAL (20,2)      not NULL   comment '价格，单位-元保留两位小数',
 `stock`       int(11)   not NULL   comment '库存数量',
 `status`       int(6)    DEFAULT '1'   comment '商品状态，1-在售 2-下架 3-删除',
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


# 购物车表
create table neuedu_cart(
 `id`       int(11)      not null  auto_increment,
 `user_id`       int(11)      not  null  ,
 `product_id`       int(11)      not  null  ,
 `quantity`       int(11)      not null   comment '商品数量',
 `checked`       int(11)      DEFAULT null   comment '是否选择，1=已勾选，0=未勾选',
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`),
 key `user_id_index`(`user_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;

# 收货地址表
create table neuedu_shipping(
 `id`       int(11)      not null  auto_increment,
 `user_id`       int(11)      not  null  ,
 `receiver_name`       varchar(20)      default   null  COMMENT '收货姓名' ,
`receiver_phone`       varchar(20)      default   null  COMMENT '收货固定电话' ,
 `receiver_mobile`       varchar(20)      default   null  COMMENT '收货移动电话' ,
 `receiver_province`       varchar(20)      default   null  COMMENT '省份' ,
 `receiver_city`       varchar(20)      default   null  COMMENT '城市' ,
 `receiver_district`       varchar(20)      default   null  COMMENT '区/县' ,
 `receiver_address`       varchar(200)      default   null  COMMENT '详细地址' ,
  `receiver_zip`       varchar(6)      default   null  COMMENT '邮编' ,
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

#支付表
create table neuedu_payinfo(
 `id`       int(11)      not null  auto_increment,
 `user_id`       int(11)      DEFAULT  null  ,
 `order_no`       bigint(20)      DEFAULT  null comment '订单号'  ,
 `pay_platform`       int(10)      DEFAULT null   comment '支付平台 1-支付宝 2-微信',
  `platform_number`       VARCHAR (200)      DEFAULT null   comment '支付宝支付流水号',
  `platform_status`       VARCHAR (20)      DEFAULT null   comment '支付宝支付状态',
 `create_time`       datetime      not null   comment '创建时间',
`update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;



#订单表
create table neuedu_order(
  `id`       int(11)      not null  auto_increment comment '订单id',
  `user_id`       int(11)      DEFAULT  null  ,
  `order_no`       bigint(20)      DEFAULT  null comment '订单号'  ,
  `shipping_id`       int(11)      DEFAULT  null  ,
  `payment`       decimal(20,2)      DEFAULT  null  comment '实际付款金额，单位元，保留两位小数' ,
  `payment_type`       int(4)      DEFAULT  null comment '支付类型，1-在线支付' ,
  `postage`       int(10)      DEFAULT null   comment '运费，单位是元',
  `status`       int (10)      DEFAULT null   comment '订单状态：0-已取消 10-未付款 20-已付款 40-已发货 50-交易成功 60-交易关闭',
  `payment_time`      datetime     DEFAULT null   comment '支付时间',
  `send_time`       datetime      DEFAULT null   comment '发货时间',
  `end_time`       datetime      DEFAULT null   comment '交易完成时间',
  `close_time`       datetime      DEFAULT null   comment '交易关闭时间',
  `create_time`       datetime      not null   comment '创建时间',
  `update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`),
 UNIQUE  KEY `order_no_index` (`order_no`) USING  BTREE
)ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

#订单明细表
create table neuedu_order_item(
  `id`       int(11)      not null  auto_increment comment '订单id',
  `user_id`       int(11)      DEFAULT  null  ,
  `order_no`       bigint(20)      DEFAULT  null comment '订单号'  ,
  `product_id`       int(11)      DEFAULT  null  comment '商品id',
  `product_name`       varchar(100)      DEFAULT  null  comment '商品名称' ,
  `product_image`       varchar(500)      DEFAULT  null comment '商品图片地址' ,
  `current_unit_price`       decimal(20,2)      DEFAULT null   comment '生成订单时的商品单价，单位元，保留两位小数',
  `quantity`       int (10)      DEFAULT null   comment '商品数量',
  `total_price`      decimal(20,2)     DEFAULT null   comment '商品总价，单位元，保留两位小数',
  `create_time`       datetime      not null   comment '创建时间',
  `update_time`       datetime      not null   comment '最后一次更新时间',
 PRIMARY KEY(`id`),
   KEY `order_no_index` (`order_no`) USING  BTREE,
      KEY `order_no_user_id_index` (`user_id`,`order_no`) USING  BTREE
)ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8;

