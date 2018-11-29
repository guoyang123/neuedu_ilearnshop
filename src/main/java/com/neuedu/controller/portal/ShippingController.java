package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 *@Author teacher.wei
 * */
@RestController
@RequestMapping(value = "/portal/shipping/")
public class ShippingController {
    @Autowired
   private IShippingService shippingService;

    /**
     * 添加地址
     * @param session
     * @param shipping
     * @return
     */
    @PostMapping("add.do")
    public ServerResponse add(HttpSession session,Shipping shipping){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        return shippingService.add(userInfo.getId(),shipping);
        /*
        return shippingService.add(1,shipping);
        */
    }

    @GetMapping("del.do")
    public ServerResponse del(Integer shippingId){
        return shippingService.del(shippingId);
    }

    @PostMapping("update.do")
    public ServerResponse update(Shipping shipping){
        return shippingService.update(shipping);
    }

    @GetMapping("select.do")
    public ServerResponse select(Integer shippingId){
        return shippingService.select(shippingId);
    }

    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return shippingService.list(pageNum,pageSize);
    }
}
