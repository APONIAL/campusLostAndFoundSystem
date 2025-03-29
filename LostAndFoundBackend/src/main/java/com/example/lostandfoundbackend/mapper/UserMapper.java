package com.example.lostandfoundbackend.mapper;

import com.example.lostandfoundbackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2025-03-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
