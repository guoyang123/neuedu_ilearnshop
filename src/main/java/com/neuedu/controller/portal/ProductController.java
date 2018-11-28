package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.guo
 * */
@RestController
@RequestMapping(value = "/portal/product/")
public class ProductController {
    @Autowired
    private IProductService productService;

    /*产品搜索及动态排序List*/
    @RequestMapping("list.do")
    public ServerResponse getList(@RequestParam(required = false) Integer categoryId,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false,defaultValue ="1") Integer pageNum,
                                  @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false,defaultValue = "") String orderBy
                                  ){
        ServerResponse sr = productService.getList(categoryId,keyword,pageNum,pageSize,orderBy);

        return sr;
    }

    /*获取产品detail*/
    @RequestMapping("detail.do")
    public ServerResponse getDetail(Integer productId){
        ServerResponse sr = productService.getDetail(productId);

        return sr;
    }
}
