package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lostandfoundbackend.common.Result;

import com.example.lostandfoundbackend.service.INoticeService;
import com.example.lostandfoundbackend.entity.Notice;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-11
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;
    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody Notice notice) {
        User currentUser = TokenUtils.getCurrentUser();
        assert currentUser != null;
        notice.setUserid(currentUser.getId());
        notice.setTime(DateUtil.now());
        noticeService.saveOrUpdate(notice);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        noticeService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(noticeService.list());
    }

    @GetMapping("/selectOpenNotice")
    public Result selectOpenNotice(){
        QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();
        //用户只能看到公开的公告数据
        noticeQueryWrapper.eq("open",1);
        return Result.success(noticeService.list(noticeQueryWrapper));
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id);
        User user = userService.getById(notice.getUser());
        notice.setUser(user == null ? "用户已注销" : user.getName());
        return Result.success(notice);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<Notice> page = noticeService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().forEach(notice -> {
            User user = userService.getById(notice.getUserid());
            notice.setUser(user == null ? "用户已注销" : user.getName());
        });
        return Result.success(page);
    }

}