package com.neuedu.util;

import com.github.pagehelper.PageHelper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Product;

import java.util.ArrayList;
import java.util.List;

public class PaiXuUtils {


    /*分页处理小方法*/
    public static List<Product> px1(String orderBy,
                                    Integer pageNum, Integer pageSize,
                                    Integer categoryId, String keyword,
                                    ProductMapper productMapper){
        //分页处理
        //判断排序方式
        List<Product> li = new ArrayList<>();

        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
            li = productMapper.selectByCategoryIdAndKeyword(categoryId,keyword);
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
                li = productMapper.selectByCategoryIdAndKeyword(categoryId,keyword);
            }else{
                PageHelper.startPage(pageNum,pageSize);
                li = productMapper.selectByCategoryIdAndKeyword(categoryId,keyword);
            }
        }

        return li;

    }

    public static List<Product> px2(String orderBy,
                                    Integer pageNum, Integer pageSize,
                                    Integer categoryId, String keyword,
                                    ProductMapper productMapper,
                                    List data){
        //分页处理
        //判断排序方式
        List<Product> li = new ArrayList<>();

        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
            li = productMapper.selectByCategoryIdAndKeywordAndData(categoryId,keyword,data);
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
                li = productMapper.selectByCategoryIdAndKeywordAndData(categoryId,keyword,data);
            }else{
                PageHelper.startPage(pageNum,pageSize);
                li = productMapper.selectByCategoryIdAndKeywordAndData(categoryId,keyword,data);
            }
        }

        return li;

    }

    public static List<Product> px3(String orderBy,
                                    Integer pageNum, Integer pageSize,
                                    Integer categoryId, String keyword,
                                    ProductMapper productMapper){
        //分页处理
        //判断排序方式
        List<Product> li = new ArrayList<>();

        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
            li = productMapper.selectByCategoryId(categoryId);
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
                li = productMapper.selectByCategoryId(categoryId);
            }else{
                PageHelper.startPage(pageNum,pageSize);
                li = productMapper.selectByCategoryId(categoryId);
            }
        }

        return li;

    }

    public static List<Product> px4(String orderBy,
                                    Integer pageNum, Integer pageSize,
                                    Integer categoryId, String keyword,
                                    ProductMapper productMapper,
                                    List data){
        //分页处理
        //判断排序方式
        List<Product> li = new ArrayList<>();

        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
            li = productMapper.selectByCategoryIdAndData(categoryId,data);
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
                li = productMapper.selectByCategoryIdAndData(categoryId,data);
            }else{
                PageHelper.startPage(pageNum,pageSize);
                li = productMapper.selectByCategoryIdAndData(categoryId,data);
            }
        }

        return li;

    }

    public static List<Product> px5(String orderBy,
                                    Integer pageNum, Integer pageSize,
                                    Integer categoryId, String keyword,
                                    ProductMapper productMapper){
        //分页处理
        //判断排序方式
        List<Product> li = new ArrayList<>();

        if(orderBy.equals("")){
            //不需要排序
            PageHelper.startPage(pageNum,pageSize);
            li = productMapper.selectByKeyword(keyword);;
        }else{
            //按参数排序
            String[] split = orderBy.split("_");
            if(split.length>1){
                PageHelper.startPage(pageNum,pageSize,split[0]+""+split[1]);
                li = productMapper.selectByKeyword(keyword);;
            }else{
                PageHelper.startPage(pageNum,pageSize);
                li = productMapper.selectByKeyword(keyword);;
            }
        }

        return li;

    }
}
