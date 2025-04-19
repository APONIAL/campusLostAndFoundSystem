package com.example.lostandfoundbackend.controller;

import cn.hutool.core.lang.Dict;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.FoundInfo;
import com.example.lostandfoundbackend.service.IFoundInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tao
 * @Date 2025/4/19 21:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private IFoundInfoService foundInfoService;

    @GetMapping("/getPieByFoundCategory")
    public Result getPieByFoundCategory() {
        // 假设从数据库中获取所有数据
        List<FoundInfo> foundInfoList = foundInfoService.list();
        // 定义类别列表
        List<String> categoryList = List.of(
                "学习用品",
                "电子设备",
                "个人用品",
                "生活用品",
                "贵重物品",
                "其他"
        );
        // 用于存储统计结果的列表
        ArrayList<Dict> barList = new ArrayList<>();
        // 遍历每个类别，统计数量
        for (String cate : categoryList) {
            // 统计当前类别的数量
            long count = foundInfoList.stream()
                    // 筛选匹配的类别
                    .filter(item -> cate.equals(item.getCategory()))
                    // 统计数量
                    .count();
            // 创建一个 Dict 对象并添加到结果列表中
            Dict dict = Dict.create();
            dict.set("name", cate).set("value", count);
            barList.add(dict);
        }
        // 返回结果
        return Result.success(barList);
    }
}
