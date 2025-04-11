package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.common.Result;

import com.example.lostandfoundbackend.service.INewsService;
import com.example.lostandfoundbackend.entity.News;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 新闻信息 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-10
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private INewsService newsService;
    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody News news) {
        User currentUser = TokenUtils.getCurrentUser();
        news.setAuthorid(Objects.requireNonNull(currentUser, "当前用户未登录").getId());
        //2025-4-10 14:10:01
        news.setTime(DateUtil.now());
        newsService.saveOrUpdate(news);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        newsService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        newsService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(newsService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        News news = newsService.getById(id);
        User user = userService.getById(news.getAuthorid());
        news.setAuthor(user == null ? "用户已注销" : user.getName());
        return Result.success(news);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<News> page = newsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().forEach(news -> {
            news.setAuthor(userService.getById(news.getAuthorid()) == null ?
                    "用户已注销" : userService.getById(news.getAuthorid()).getName());
        });
        return Result.success(page);
    }

}

