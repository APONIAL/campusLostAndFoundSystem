package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.FoundInfo;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IFoundInfoService;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 招领信息表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-17
 */
@RestController
@RequestMapping("/found-info")
public class FoundInfoController {

    @Resource
    private IFoundInfoService foundInfoService;
    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody FoundInfo foundInfo) {
        foundInfo.setUserId(Objects.requireNonNull(TokenUtils.getCurrentUser()).getId());
        foundInfo.setCreateTime(DateUtil.now());
        foundInfoService.saveOrUpdate(foundInfo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        foundInfoService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        foundInfoService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(foundInfoService.list());
    }

    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(foundInfoService.getById(id));
    }

    @GetMapping("/myFoundListPage")
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "6") Integer pageSize) {
        QueryWrapper<FoundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("user_id", Objects.requireNonNull(TokenUtils.getCurrentUser()).getId());
        return Result.success(foundInfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 提供给前端招领广场，后端招领管理的分页查询接口
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param name  名称
     * @param user  发布人
     * @param category 种类
     * @param foundLocation 发现地点
     * @return page
     */
    @GetMapping("/FoundSquarePage")
    public Result foundSquarePage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String founderName,
                                  @RequestParam(defaultValue = "") String category,
                                  @RequestParam(defaultValue = "") String foundLocation) {
        QueryWrapper<FoundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(category), "category", category);
        queryWrapper.like(StrUtil.isNotBlank(foundLocation), "found_location", foundLocation);
        if (StrUtil.isNotBlank(founderName)){
            List<Integer> userIds = userService.list(new QueryWrapper<User>().like("name", founderName)).stream()
                    .map(User::getId).collect(Collectors.toList());
            if (userIds.isEmpty()) {
                return Result.success(new Page<>(pageNum, pageSize));
            }
            queryWrapper.in("user_id", userIds);
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        Page<FoundInfo> page = foundInfoService.page(new Page<>(pageNum, pageSize), queryWrapper);
        if (page.getRecords().isEmpty()){
            return Result.success(new Page<>(pageNum, pageSize));
        }
        page.getRecords().forEach(lostInfo -> {
            lostInfo.setFounderName(userService.getById(lostInfo.getUserId()) == null ?
                    "用户已注销" : userService.getById(lostInfo.getUserId()).getName());
        });
        return Result.success(page);

}

    /**
     * front home页面获取最新4条数据
     *
     * @return 最新四条数据
     */
    @GetMapping("/selectNew4")
    public Result selectNew4() {
        QueryWrapper<FoundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("status", 0);
        queryWrapper.last("limit 4");
        List<FoundInfo> list = foundInfoService.list(queryWrapper);
        list.forEach(lostInfo -> {
            lostInfo.setFounderName(userService.getById(lostInfo.getUserId()) == null ?
                    "用户已注销" : userService.getById(lostInfo.getUserId()).getName());
        });
        return Result.success(list);
    }
}

