package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.CustomLogs;
import com.example.lostandfoundbackend.common.LogType;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.Orders;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IOrdersService;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-13
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;
    @Resource
    private IUserService userService;


    // 新增或者更新
    @CustomLogs(operation = "订单", type = LogType.ADD_OR_UPDATE)
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody Orders orders) {
        User currentUser = TokenUtils.getCurrentUser();
        assert currentUser != null;
        orders.setUsrid(currentUser.getId());
        //年-月-日
        orders.setDate(DateUtil.today());
        orders.setNo(IdUtil.fastSimpleUUID());
        ordersService.saveOrUpdate(orders);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        ordersService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        ordersService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(ordersService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(ordersService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        Page<Orders> page = ordersService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().forEach(orders -> {
            orders.setUser(userService.getById(orders.getUsrid()) == null ?
                    "用户已注销" : userService.getById(orders.getUsrid()).getName());
        });
        return Result.success(page);
    }


}

