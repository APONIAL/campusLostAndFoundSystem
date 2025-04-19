package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.common.Result;

import com.example.lostandfoundbackend.service.IFeedbackService;
import com.example.lostandfoundbackend.entity.Feedback;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 反馈建议 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-19
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private IFeedbackService feedbackService;
    @Resource
    private IUserService userService;

    @PostMapping("/save")
    public Result save(@RequestBody Feedback feedback) {
        feedback.setCreateTime(DateUtil.now());
        feedback.setUserId(Objects.requireNonNull(TokenUtils.getCurrentUser()).getId());
        feedbackService.save(feedback);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        feedbackService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        feedbackService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(feedbackService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(feedbackService.getById(id));
    }

    /**
     * 未后端管理提供的分页查询接口
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "" ) String userName,
                       @RequestParam(defaultValue = "-1" ) Integer rate) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //模糊查询用户名
        userQueryWrapper.like(StrUtil.isNotBlank(userName), "name", userName);
        List<Integer> fuzzyUserIds = userService.list(userQueryWrapper).stream().map(User::getId).distinct().toList();
        if (!fuzzyUserIds.isEmpty()){
            queryWrapper.in("user_id", fuzzyUserIds);
        }
        //匹配评分
        queryWrapper.eq(rate != -1, "rate", rate);
        queryWrapper.orderByDesc("id");
        Page<Feedback> page = feedbackService.page(new Page<>(pageNum, pageSize), queryWrapper);
        if (page.getRecords() == null){
            return Result.success(new Page<>(pageNum,pageSize));
        }
        //提取所有userId
        List<Integer> userIds = page.getRecords().stream()
                .map(Feedback::getUserId).distinct().toList();
        //批量查询所有用户信息，为了防止id为空导致错误，需要先判断是否为空
        Map<Integer, User> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                userService.listByIds(userIds)
                        .stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));
        //填充userName;
        page.getRecords().forEach(feedback -> {
            User user = userMap.get(feedback.getUserId());
            feedback.setUserName(user == null ? "用户已注销" : user.getName());
        });
        return Result.success(page);
    }

    /**
     * front 获取所有反馈建议
     *
     * @return feedbackList
     */
    @GetMapping("getAllFeedback")
    public Result getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.list();
        List<Integer> userIds = feedbackList.stream().map(Feedback::getUserId).distinct().toList();
        Map<Integer, User> userMap = userIds.isEmpty()
                ? Collections.emptyMap() : userService.listByIds(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        feedbackList.forEach(feedback -> {
            User user = userMap.get(feedback.getUserId());
            feedback.setUserName(user == null ? "用户已注销" : user.getName());
            feedback.setAvatarUrl(user == null ? "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" : user.getAvatar());
        });
        return Result.success(feedbackList);
    }

}

