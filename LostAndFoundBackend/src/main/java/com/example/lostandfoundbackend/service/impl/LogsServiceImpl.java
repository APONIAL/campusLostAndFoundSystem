package com.example.lostandfoundbackend.service.impl;

import com.example.lostandfoundbackend.entity.Logs;
import com.example.lostandfoundbackend.mapper.LogsMapper;
import com.example.lostandfoundbackend.service.ILogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-04-12
 */
@Service
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {

}
