package com.example.lostandfoundbackend.service.impl;

import com.example.lostandfoundbackend.entity.News;
import com.example.lostandfoundbackend.mapper.NewsMapper;
import com.example.lostandfoundbackend.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新闻信息 服务实现类
 * </p>
 *
 * @author admin
 * @since 2025-04-10
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
