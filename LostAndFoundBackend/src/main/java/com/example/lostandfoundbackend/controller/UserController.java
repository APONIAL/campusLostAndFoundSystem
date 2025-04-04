package com.example.lostandfoundbackend.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.Constants;
import com.example.lostandfoundbackend.exception.ServiceException;
import io.swagger.annotations.Api;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.common.Result;

import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.entity.User;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/user")
@Api("系统用户管理")
public class UserController {

    @Resource
    private IUserService userService;


    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        if (user.getUsername().length()>10 ||user.getPassword().length()>20){
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        User dbUser = userService.login(user);
        if (dbUser == null) {
            //抛出一个自定义异常
            throw new ServiceException("账号或者密码错误");
        }
        return Result.success(dbUser);
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        userService.register(user);
        return Result.success();
    }


    /**
     * 新增或者更新
     *
     * @param user
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody User user) {
        try {
            userService.saveOrUpdate(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error(Constants.CODE_416, "插入数据库错误");
            } else {
                return Result.error(Constants.CODE_500, "系统错误");
            }
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 根据id 和 username 查询
     *
     * @param id
     * @param username
     * @return
     */
    @GetMapping("/selectByMore")
    public Result findByIdAndName(@RequestParam Integer id, @RequestParam String username) {
        return Result.success(userService.findByIdAndName(id, username));
    }

    /**
     * 根据name模糊查询
     *
     * @param name
     * @return
     */
    @GetMapping("/selectObsByName")
    public Result findObsByName(@RequestParam String name) {

        return Result.success(userService.findObsByName(name));
    }


}

