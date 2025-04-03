package com.example.lostandfoundbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.mapper.UserMapper;
import com.example.lostandfoundbackend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Override
    public Object findByIdAndName(Integer id, String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",id);
        userQueryWrapper.eq("username",username);
        return list(userQueryWrapper);
    }

    @Override
    public List<User> findObsByName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("name",name);
        return list(userQueryWrapper);
    }
}
