package com.example.lostandfoundbackend.mapper;

import com.example.lostandfoundbackend.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 新闻信息 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2025-04-10
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

}
