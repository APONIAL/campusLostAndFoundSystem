package com.example.lostandfoundbackend.service;

import com.example.lostandfoundbackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2025-03-28
 */
public interface IUserService extends IService<User> {

    Object findByIdAndName(Integer id, String username);

    List<User> findObsByName(String name);

    User login(User user);

    Boolean register(User user);
}
