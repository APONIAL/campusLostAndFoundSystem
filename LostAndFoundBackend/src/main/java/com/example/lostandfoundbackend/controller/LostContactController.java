package com.example.lostandfoundbackend.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.lostandfoundbackend.common.Constants;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.LostContact;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.exception.ServiceException;
import com.example.lostandfoundbackend.service.ILostContactService;
import com.example.lostandfoundbackend.service.IUserService;
import com.example.lostandfoundbackend.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 留言信息表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2025-04-16
 */
@RestController
@RequestMapping("/lost-contact")
public class LostContactController {

    @Resource
    private ILostContactService lostContactService;
    @Resource
    private IUserService userService;


    @PostMapping("/save")
    public Result save(@RequestBody LostContact lostContact) {
        if (TokenUtils.getCurrentUser() == null) {
            throw new ServiceException(Constants.CODE_401, "请先登录");
        }
        Integer publisherId = TokenUtils.getCurrentUser().getId();
        lostContact.setPublisherId(publisherId);
        lostContact.setCreateTime(DateUtil.now());
        lostContactService.save(lostContact);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        lostContactService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batchByIds")
    public Result deleteBatchByIds(@RequestBody List<Integer> ids) {
        lostContactService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping("/{id}")
    public Result findOneById(@PathVariable Integer id) {
        return Result.success(lostContactService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String publisherName,
            @RequestParam(required = false) String contactedName) {

        // 创建失物联系信息的查询条件
        QueryWrapper<LostContact> queryWrapper = new QueryWrapper<>();

        // 如果提供了发布者名称，则添加模糊查询条件（通过关联 user 表）
        if (StrUtil.isNotBlank(publisherName)) {
            List<Integer> publisherIds = userService.list(new QueryWrapper<User>()
                            .like("name", publisherName))
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            // 如果没有匹配到任何发布者 ID，则直接返回空结果
            if (publisherIds.isEmpty()) {
                return Result.success(new Page<>(pageNum, pageSize));
            }
            queryWrapper.in("publisher_id", publisherIds);
        }

        // 如果提供了被联系人名称，则添加模糊查询条件（通过关联 user 表）
        if (StrUtil.isNotBlank(contactedName)) {
            List<Integer> contactedIds = userService.list(new QueryWrapper<User>()
                            .like("name", contactedName))
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            // 如果没有匹配到任何被联系人 ID，则直接返回空结果
            if (contactedIds.isEmpty()) {
                return Result.success(new Page<>(pageNum, pageSize));
            }
            queryWrapper.in("contacted_id", contactedIds);
        }

        // 按照 ID 倒序排序
        queryWrapper.orderByDesc("id");

        // 分页查询失物联系信息
        Page<LostContact> page = lostContactService.page(new Page<>(pageNum, pageSize), queryWrapper);

        //有一种情况是，模糊查询匹配到user表中name，但是lostContact表中没有该用户的留言，需要返回空表
        //不然后续要报错
        if (page.getRecords().isEmpty()){
            return Result.success(new Page<>(pageNum, pageSize));
        }
        // 获取所有涉及的用户 ID（包括发布者和被联系人）
        Set<Integer> userIds = new HashSet<>();
        page.getRecords().forEach(lostContact -> {
            if (lostContact.getPublisherId() != null) {
                userIds.add(lostContact.getPublisherId());
            }
            if (lostContact.getContactedId() != null) {
                userIds.add(lostContact.getContactedId());
            }
        });

        // 批量查询用户信息
        Map<Integer, User> userMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 遍历结果集，填充发布者和被联系人的名称
        page.getRecords().forEach(lostContact -> {
            // 设置发布者名称
            User publisher = userMap.get(lostContact.getPublisherId());
            lostContact.setPublisherName(publisher != null ? publisher.getName() : "用户已注销");

            // 设置被联系人名称
            User contacted = userMap.get(lostContact.getContactedId());
            lostContact.setContactedName(contacted != null ? contacted.getName() : "用户已注销");
        });
        // 返回成功的结果
        return Result.success(page);
    }

}

