package com.example.lostandfoundbackend.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.common.Result;

import com.example.lostandfoundbackend.service.ILogsService;
import com.example.lostandfoundbackend.entity.Logs;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-12
 */
@RestController
@RequestMapping("/logs")
public class LogsController {

    @Resource
    private ILogsService logsService;


    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        logsService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        logsService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(logsService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String operation,
                           @RequestParam String type,
                           @RequestParam String user) {
        QueryWrapper<Logs> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(operation),"operation",operation);
        queryWrapper.like(StrUtil.isNotBlank(type),"type",type);
        queryWrapper.like(StrUtil.isNotBlank(user),"user",user);
        return Result.success(logsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

