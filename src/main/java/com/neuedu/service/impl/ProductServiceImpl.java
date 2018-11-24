package com.neuedu.service.impl;

import com.neuedu.dao.ProductMapper;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;
}
