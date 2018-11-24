package com.neuedu.controller.portal;

import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author teacher.wei
 * */
@RestController
@RequestMapping(value = "/portal/shipping/")
public class ShippingController {
    @Autowired
   private IShippingService shippingService;

}
