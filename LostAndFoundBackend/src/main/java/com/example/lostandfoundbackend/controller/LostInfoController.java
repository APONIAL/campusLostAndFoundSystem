package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.LostInfo;
import com.example.lostandfoundbackend.service.ILostInfoService;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 失物信息表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-14
 */
@RestController
@RequestMapping("/lostInfo")
public class LostInfoController {

    @Resource
    private ILostInfoService lostInfoService;
    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody LostInfo lostInfo) {
        lostInfo.setUserId(TokenUtils.getCurrentUser().getId());
        lostInfo.setTime(DateUtil.today());
        lostInfoService.saveOrUpdate(lostInfo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        lostInfoService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        lostInfoService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/selectNew4")
    public Result selectNew4() {
        QueryWrapper<LostInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("status",0);
        queryWrapper.last("limit 4");

        List<LostInfo> list = lostInfoService.list(queryWrapper);
        list.forEach(lostInfo -> {
            lostInfo.setUser(userService.getById(lostInfo.getUserId()) == null ?
                    "用户已注销" : userService.getById(lostInfo.getUserId()).getName());
        });
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(lostInfoService.getById(id));
    }

    /**
     * front 查看我的失物信息的分页接口
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return result
     */
    @GetMapping("/myLostListPage")
    public Result myLostListPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<LostInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("user_id", Objects.requireNonNull(TokenUtils.getCurrentUser()).getId());
        Page<LostInfo> page = lostInfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     * front 失物广场分页接口
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return result
     */
    @GetMapping("/lostSquarePage")
    public Result lostSquarePage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize) {
        QueryWrapper<LostInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<LostInfo> page = lostInfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().forEach(lostInfo -> {
            lostInfo.setUser(userService.getById(lostInfo.getUserId()) == null ?
                    "用户已注销" : userService.getById(lostInfo.getUserId()).getName());
        });
        return Result.success(page);
    }

}

