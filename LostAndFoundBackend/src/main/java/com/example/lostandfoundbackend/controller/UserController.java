package com.example.lostandfoundbackend.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.*;
import com.example.lostandfoundbackend.dto.UserDto;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.exception.ServiceException;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @AuthAccess
    @CustomLogs(operation = "用户", type = LogType.LOGIN)
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
        //生成token
        String token = TokenUtils.createToken(dbUser.getId().toString(), user.getPassword());
        dbUser.setToken(token);
        return Result.success(dbUser);
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */

    @CustomLogs(operation = "用户", type = LogType.REGISTER)
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        userService.register(user);
        return Result.success();
    }

    /**
     * 重置密码接口
     * @param userDto
     * @return
     */
    @AuthAccess
    @PostMapping("/forgetPassword")
    public Result forgetPassword(@RequestBody UserDto userDto){
        if (StrUtil.isBlank(userDto.getUsername()) || StrUtil.isBlank(userDto.getPhone())) {
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        userService.resetPassword(userDto);
        return Result.success();
    }

    /**
     * 新增或者更新
     *
     * @param user
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @CustomLogs(operation = "用户", type = LogType.ADD_OR_UPDATE)
    public Result save(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername())){
            return Result.error(Constants.CODE_400, "数据输入不合法");
        }
        if (StrUtil.isBlank(user.getPassword())){
            user.setPassword("123456");
        }
        try {
            userService.saveOrUpdate(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error(Constants.CODE_416, "用户名重复");
            } else {
                return Result.error(Constants.CODE_500, "系统错误");
            }
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @CustomLogs(operation = "用户", type = LogType.DELETE)
    public Result deleteById(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/del/batchByIds")
    @CustomLogs(operation = "用户", type = LogType.BATCH_DELETE)
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @AuthAccess
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String username,
                           @RequestParam String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(username),"username",username);
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UserDto userDto){
        userService.updatePassword(userDto);
        return Result.success();
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

