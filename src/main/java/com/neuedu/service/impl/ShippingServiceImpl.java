package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId,Shipping shipping){
        if(shipping == null){
           return ServerResponse.createServerResponseByError("参数错误");
        }
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        if(shipping.getId() == null){
            return ServerResponse.createServerResponseBySuccess(1,"新建地址失败");
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess(map,"新建地址成功");
    }

    @Override
    public ServerResponse del(Integer shippingId){
        if(shippingId == null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        int flag = shippingMapper.deleteByPrimaryKey(shippingId);
        if (flag > 0){
            return ServerResponse.createServerResponseBySuccess("删除地址成功");
        }
        return ServerResponse.createServerResponseByError(1,"删除地址失败");
    }

    @Override
    public ServerResponse update(Shipping shipping){
        if(shipping == null || shipping.getId() == null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        int flag = shippingMapper.updateByPrimaryKeySelective(shipping);
        if(flag > 0){
            return ServerResponse.createServerResponseBySuccess("更新地址成功");
        }
        return  ServerResponse.createServerResponseBySuccess(1,"更新地址失败");
    }

    @Override
    public ServerResponse select(Integer shippingId){
        if(shippingId == null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        return ServerResponse.createServerResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

}
