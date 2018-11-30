package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<Category> getChilds(Integer parentId) {
        //非空校验
        if(parentId == null){
            ServerResponse.createServerResponseByError("参数不能为空！");
        }
        List<Category> list = categoryMapper.getChilds(parentId);
        return ServerResponse.createServerResponseBySuccess(list);

    }

    @Override
    public ServerResponse<Category> add(String name,Integer parentId) {
        //非空校验
        if(name == null || "".equals(name)){
            ServerResponse.createServerResponseByError("类型名不能为空！");
        }
        Category c = new Category();
        c.setName(name);
        c.setParentId(parentId);
        c.setCreateTime(new Date());
        c.setUpdateTime(new Date());
        c.setStatus(true);
        c.setSortOrder(1);
        int flag = categoryMapper.insert(c);
        if(flag > 0)
            return ServerResponse.createServerResponseBySuccess("添加品类成功");
        return  ServerResponse.createServerResponseByError("添加品类失败");

    }

    @Override
    public ServerResponse<Category> updateName(String name,Integer categoryId) {
        //非空校验
        if(name == null || "".equals(name)){
            ServerResponse.createServerResponseByError("类型名不能为空！");
        }

        int flag = categoryMapper.updateName(name,categoryId);
        if(flag > 0)
            return ServerResponse.createServerResponseBySuccess("更新品类名字成功");
        return  ServerResponse.createServerResponseByError("更新品类名字失败");
    }

    @Override
    public ServerResponse<Category> getAllChilds(Integer parentId) {
        //非空校验
        if(parentId == null){
            ServerResponse.createServerResponseByError("参数不能为空！");
        }
        List<Category> list = getFirstLevalChilds(parentId);
        return ServerResponse.createServerResponseBySuccess(list);
    }

    @Override
    public ServerResponse getDeepCategory(Integer parentId) {
        //非空校验
        if(parentId == null){
            ServerResponse.createServerResponseByError("参数不能为空！");
        }
        List<Integer> list = new ArrayList<>();
        getDeepChilds(parentId,list);
        return ServerResponse.createServerResponseBySuccess(list);
    }

    private void getDeepChilds(Integer parentId,List<Integer> ids){
        List<Integer> list = categoryMapper.getChildIds(parentId);
        if(list != null && list.size() != 0){
            for (Integer c : list){
                ids.add(c);
                getDeepChilds(c,ids);
            }
        }
    }

    private List<Category> getFirstLevalChilds(Integer parentId){
        List<Category> list = categoryMapper.getChilds(parentId);
        if(list != null && list.size() != 0){
            for (Category c : list){
                c.setChilds(getFirstLevalChilds(c.getId()));
            }
        }
        return list;
    }
}
