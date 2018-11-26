package com.neuedu.controller.portal;

import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.guo
 * */
@RestController
@RequestMapping(value = "/portal/order/")
public class OrderController {
@Autowired
private IOrderService orderService;
}
