package com.example.lostandfoundbackend.service.impl;

import com.example.lostandfoundbackend.entity.Orders;
import com.example.lostandfoundbackend.mapper.OrdersMapper;
import com.example.lostandfoundbackend.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-04-13
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
