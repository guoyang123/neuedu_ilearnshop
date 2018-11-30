package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

/**
 *@Author teacher.wei
 * */
public interface IShippingService {
    public ServerResponse add(Integer userId,Shipping shipping);
    public ServerResponse del(Integer shippingId);
    public ServerResponse update(Shipping shipping);
    public ServerResponse select(Integer shippingId);
    public ServerResponse list(Integer pageNum, Integer pageSize);

}
