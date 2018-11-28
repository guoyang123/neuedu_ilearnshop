package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.ProductVO;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.util.POJOtoVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    //注入后台品类业务层
    @Autowired
    private ICategoryService ics;

    /*产品搜索及动态排序List*/
    @Override
    public ServerResponse getList(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        ServerResponse sr = null;
        List<Product> li = null;

        //参数都为空时，报错
        if(categoryId == null &&(keyword == null || keyword.equals(""))){
            sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.ERROR_PAMAR.getCode(),Const.ProductStatusEnum.ERROR_PAMAR.getDesc());
            return sr;
        }


        if(categoryId != null ){
            //递归获取所有子类
            List data = null;
            ServerResponse<Category> srCategory = ics.getAllChilds(categoryId);
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
                    li = productMapper.selectByCategoryIdAndKeyword(categoryId,keyword);
                }else{
                    //有子类的情况
                    li = productMapper.selectByCategoryIdAndKeywordAndData(categoryId,keyword,data);
                }

            }
            if(keyword == null || keyword.equals("")){
                //判断当前分类是否为有子类
                if(data == null || data.size()==0){
                    //没有子类的情况
                    //根据该商品类型查询所有商品数据
                    li = productMapper.selectByCategoryId(categoryId);
                }else{
                    //有子类的情况，应该返回所有子类商品
                    li = productMapper.selectByCategoryIdAndData(categoryId,data);
                }
            }
        }else{
            //关键词不为空时查询所有数据
            if(keyword != null && !keyword.equals("")){
                //关键词模糊查询
                keyword = "%"+keyword+"%";

                li = productMapper.selectByKeyword(keyword);
            }
        }

        //集合空值处理
        if(li == null){
            li.add(new Product());
        }
        //分页处理
        //判断排序方式
        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        PageInfo pageInfo = new PageInfo(li);

        //返回结果
        sr = ServerResponse.createServerResponseBySuccess(pageInfo);
        return sr;
    }

    /*获取产品detail*/
    @Override
    public ServerResponse getDetail(Integer productId) {
        ServerResponse sr = null;

        //判断参数
        if(productId == null ){
            sr = ServerResponse.createServerResponseByError(Const.ProductStatusEnum.ERROR_PAMAR.getCode(),Const.ProductStatusEnum.ERROR_PAMAR.getDesc());
            return sr;
        }else{
            //参数不为空但商品不存在
            Product product = productMapper.selectByPrimaryKey(productId);
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
}
