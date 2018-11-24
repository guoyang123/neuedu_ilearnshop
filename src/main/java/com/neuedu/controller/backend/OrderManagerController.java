package com.neuedu.controller.backend;

import com.neuedu.service.ICategoryService;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.zhang
 * */
@RestController
@RequestMapping(value = "/manage/order/")
public class OrderManagerController {

    @Autowired
    private IOrderService orderService;
}
