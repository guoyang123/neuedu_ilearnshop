package com.neuedu.controller.portal;

import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.guo
 * */
@RestController
@RequestMapping(value = "/portal/product/")
public class ProductController {
    @Autowired
    private IProductService productService;
}
