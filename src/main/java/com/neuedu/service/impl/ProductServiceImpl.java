package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.ProductVO;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.util.POJOtoVOUtils;
import com.neuedu.util.PaiXuUtils;
import com.neuedu.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    //注入后台品类业务层
    @Autowired
    private ICategoryService ics;

    /*产品搜索及动态排序List*/
    @Override
    public ServerResponse getList(Integer categoryId, String keyword,Integer pageNum, Integer pageSize, String orderBy) {
        ServerResponse sr = null;

        List<Product> li = new ArrayList<>();
        List<ProductVO> voList = new ArrayList<>();

        //参数都为空时，报错
        if(categoryId == null &&(keyword == null || keyword.equals(""))){
            sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.ERROR_PAMAR.getCode(),Const.ProductStatusEnum.ERROR_PAMAR.getDesc());
            return sr;
        }


        if(categoryId != null ){
            //递归获取所有子类
            List data = new ArrayList();
            ServerResponse<Category> srCategory = ics.getDeepCategory(categoryId);
            if(srCategory.isSuccess()){
                data = (List) srCategory.getData();
            }

            //判断keyword是否为空
            if(keyword != null && !keyword.equals("")){
                //关键词模糊查询
                keyword = "%"+keyword+"%";

                //判断当前分类是否为有子类
                if(data == null || data.size()==0){
                    //没有子类的情况
                    li = PaiXuUtils.px1(orderBy,pageNum,pageSize,categoryId,keyword,productMapper);
                }else{
                    //有子类的情况
                    li = PaiXuUtils.px2(orderBy,pageNum,pageSize,categoryId,keyword,productMapper,data);

                    //li = productMapper.selectByCategoryIdAndKeywordAndData(categoryId,keyword,data);
                }

            }
            if(keyword == null || keyword.equals("")){
                //判断当前分类是否为有子类
                if(data == null || data.size()==0){
                    //没有子类的情况
                    //根据该商品类型查询所有商品数据
                    li = PaiXuUtils.px3(orderBy,pageNum,pageSize,categoryId,keyword,productMapper);

                    //li = productMapper.selectByCategoryId(categoryId);
                }else{
                    //有子类的情况，应该返回所有子类商品
                    li = PaiXuUtils.px4(orderBy,pageNum,pageSize,categoryId,keyword,productMapper,data);

                    //li = productMapper.selectByCategoryIdAndData(categoryId,data);
                }
            }
        }else{
            //关键词不为空时查询所有数据
            if(keyword != null && !keyword.equals("")){
                //关键词模糊查询
                keyword = "%"+keyword+"%";

                li = PaiXuUtils.px5(orderBy,pageNum,pageSize,categoryId,keyword,productMapper);

                //li = productMapper.selectByKeyword(keyword);
            }
        }

        //集合空值处理
        if(li == null){
            li.add(new Product());
        }

        //转换成productVO类
        for (Product product : li) {
            ProductVO aNew = POJOtoVOUtils.getNew(product);
            voList.add(aNew);
        }

        //分页处理,传入原始查询数据
        PageInfo pageInfo = new PageInfo(li,4);
        //再把封装数据设置进来
        pageInfo.setList(voList);


        //返回结果
        sr = ServerResponse.createServerResponseBySuccess(pageInfo);
        return sr;
    }


    /*获取产品detail*/
    @Override
    public ServerResponse getDetail(Integer productId, Integer is_new,Integer is_hot,Integer is_banner) {
        ServerResponse sr = null;

        //判断参数
        if(productId == null && is_new == null && is_hot == null && is_banner == null){
            sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.ERROR_PAMAR.getCode(),Const.ProductStatusEnum.ERROR_PAMAR.getDesc());
            return sr;
        }


        if(productId == null){
            List<Product> productList = new ArrayList<>();
            List<ProductVO> voList = new ArrayList<>();
            //搜索最新商品
            if(is_new ==1 || is_hot ==1 || is_banner == 1){
                productList = productMapper.selectBys_NewAndIs_HotAndIs_Banner(is_new,is_hot,is_banner);
            }

            //参数都为0查询所有数据
            if(is_new ==0 && is_hot ==0 && is_banner == 0){
                productList = productMapper.selectAll();
            }


            if(productList == null){
                sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.NO_PRODUCT.getCode(),Const.ProductStatusEnum.NO_PRODUCT.getDesc());
                return sr;
            }else{
                for (Product product : productList) {
                    //参数不为空_商品存在_校验商品状态
                    if(product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                        //商品不在售
                        sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.NO_PRODUCT.getCode(),Const.ProductStatusEnum.NO_PRODUCT.getDesc());
                        return sr;
                    }else{
                        //商品在售，返回商品数据
                        //日期转换成字符串
                        ProductVO productVO = POJOtoVOUtils.getNew(product);
                        voList.add(productVO);
                    }
                }
                sr = ServerResponse.createServerResponseBySuccess(voList);
                return sr;
            }

        }else{

            Product product = null;
            //搜索最新商品
            if(is_new ==1 ){
                product = productMapper.selectByIdAndIs_New(productId,is_new);
            }
            //搜索最热商品
            else if(is_hot ==1 ){
                product = productMapper.selectByIdAndIs_Hot(productId,is_hot);
            }
            //搜索banner商品
            else if(is_banner == 1){
                product = productMapper.selectByIdAndIs_Banner(productId,is_banner);
            }else{
                //搜索普通商品
                product = productMapper.selectByPrimaryKey(productId);
            }


            //参数不为空但商品不存在
            if(product == null){
                sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.NO_PRODUCT.getCode(),Const.ProductStatusEnum.NO_PRODUCT.getDesc());
                return sr;
            }else{
                //参数不为空_商品存在_校验商品状态
                if(product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                    //商品不在售
                    sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.NO_PRODUCT.getCode(),Const.ProductStatusEnum.NO_PRODUCT.getDesc());
                    return sr;
                }else{
                    //商品在售，返回商品数据
                    //日期转换成字符串
                    ProductVO productVO = POJOtoVOUtils.getNew(product);

                    sr = ServerResponse.createServerResponseBySuccess(productVO);
                    return sr;
                }
            }
        }
    }

    /*获取产品顶级分类*/
    @Override
    public ServerResponse topcategory(Integer sid) {
        ServerResponse sr = null;
        List<Category> categories = categoryMapper.selectTopCategory(sid);
        sr = ServerResponse.createServerResponseBySuccess(categories);
        return sr;
    }
}
