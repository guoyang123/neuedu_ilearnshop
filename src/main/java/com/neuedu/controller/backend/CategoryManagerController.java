package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *@Author teacher.wei
 * */
@RestController
@RequestMapping(value = "/manage/category/")
public class CategoryManagerController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("get_category.do")
    public ServerResponse<Category> getChilds(Integer categoryId){
        //用户登录校验
        //用户权限校验
        //查询子类别
         return categoryService.getChilds(categoryId);
    }

    @PostMapping("add_category.do")
    public ServerResponse<Category> add(String categoryName, @RequestParam(value = "parentId",required = false,defaultValue = "0") Integer parentId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.add(categoryName,parentId);
    }

    @PostMapping("set_category_name.do")
    public ServerResponse<Category> updateName(String categoryName,  Integer categoryId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.updateName(categoryName,categoryId);
    }

    @GetMapping("get_deep_category.do")
    public ServerResponse getDeepCategory(Integer categoryId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.getDeepCategory(categoryId);
    }

    @GetMapping("getAllChilds")
    public ServerResponse<Category> getAllChilds(Integer parentId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.getAllChilds(parentId);
    }


}
