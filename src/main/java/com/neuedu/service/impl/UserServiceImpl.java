package com.neuedu.service.impl;

import com.neuedu.dao.UserInfoMapper;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
}
