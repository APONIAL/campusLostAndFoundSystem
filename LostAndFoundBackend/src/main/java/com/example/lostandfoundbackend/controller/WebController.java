package com.example.lostandfoundbackend.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.example.lostandfoundbackend.common.AuthAccess;
import com.example.lostandfoundbackend.common.Result;
import com.example.lostandfoundbackend.entity.Orders;
import com.example.lostandfoundbackend.service.IOrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Tao
 * @Date 2025/4/13 20:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/web")
public class WebController {

    @Resource
    private IOrdersService ordersService;

    /**
     * 获取统计图数据
      * @return
     */
    @AuthAccess
    @GetMapping("/charts")
    public Result charts(){
        //包装折线图的数据
        List<Orders> ordersList = ordersService.list();
        //横轴日期  将对象集合转换为对象熟悉集合
        //set无序无重复集合
        Set<String> dates = ordersList.stream().map(Orders::getDate).collect(Collectors.toSet());
        List<String> dateList = CollUtil.newArrayList(dates);
        //日期是以字符串形式存储的（如 "2025-04-13"），即格式看起来是标准的 "yyyy-MM-dd"，Comparator.naturalOrder()
        //仍然会按照字典顺序进行排序（升序）。
        dateList.sort(Comparator.naturalOrder());
        List<Dict> lineList = new ArrayList<>();
        for (String date : dateList) {
            //获取当前日期的总金额和
            BigDecimal sum = ordersList.stream().filter(item -> item.getDate().equals(date)).
                    map(Orders::getMoney).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create();
            Dict line = dict.set("date", date).set("value", sum);
            lineList.add(line);
        }

        //柱状图与饼图数据
        ArrayList<Dict> barList = new ArrayList<>();
        Set<String> categories = ordersList.stream().map(Orders::getCategory).collect(Collectors.toSet());
        for (String cate : categories) {
            BigDecimal sum = ordersList.stream().filter(item -> item.getCategory().equals(cate)).
                    map(Orders::getMoney).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create();
            Dict bar = dict.set("name", cate).set("value", sum);
            barList.add(bar);
        }

        //饼图数据（比例`
        //包装所有数据
        Dict res = Dict.create().set("line", lineList).set("bar", barList);
        return Result.success(res);
    }

}
