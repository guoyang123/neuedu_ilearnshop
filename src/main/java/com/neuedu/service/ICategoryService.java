package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;

/**
 *@Author teacher.wei
 * */
public interface ICategoryService {
    public ServerResponse<Category> getChilds(Integer parentId);
    public ServerResponse<Category> add(String name,Integer parentId);
    public ServerResponse<Category> updateName(String name,Integer categoryId);
    public ServerResponse<Category> getAllChilds(Integer parentId);
}
