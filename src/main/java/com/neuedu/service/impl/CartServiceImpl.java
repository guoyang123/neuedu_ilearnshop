package com.neuedu.service.impl;

import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
}
