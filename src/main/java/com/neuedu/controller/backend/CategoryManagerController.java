package com.neuedu.controller.backend;

import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.wei
 * */
@RestController
@RequestMapping(value = "/manage/category/")
public class CategoryManagerController {
    @Autowired
    private ICategoryService categoryService;
}
