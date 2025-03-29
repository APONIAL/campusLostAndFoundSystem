package com.example.lostandfoundbackend.service.impl;

import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.mapper.UserMapper;
import com.example.lostandfoundbackend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-03-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
