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

    @GetMapping("getChilds")
    public ServerResponse<Category> getChilds(Integer parentId){
        //用户登录校验
        //用户权限校验
        //查询子类别
         return categoryService.getChilds(parentId);
    }

    @PostMapping("add")
    public ServerResponse<Category> add(String name, @RequestParam(value = "parentId",required = false,defaultValue = "0") Integer parentId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.add(name,parentId);
    }

    @PostMapping("updateName")
    public ServerResponse<Category> updateName(String name,  Integer categoryId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.updateName(name,categoryId);
    }

    @GetMapping("getAllChilds")
    public ServerResponse<Category> getAllChilds(Integer parentId){
        //用户登录校验
        //用户权限校验
        //查询子类别
        return categoryService.getAllChilds(parentId);
    }


}
