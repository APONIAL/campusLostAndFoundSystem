package com.example.lostandfoundbackend.service.impl;

import com.example.lostandfoundbackend.entity.LostInfo;
import com.example.lostandfoundbackend.mapper.LostInfoMapper;
import com.example.lostandfoundbackend.service.ILostInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 失物信息表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-04-14
 */
@Service
public class LostInfoServiceImpl extends ServiceImpl<LostInfoMapper, LostInfo> implements ILostInfoService {

}
