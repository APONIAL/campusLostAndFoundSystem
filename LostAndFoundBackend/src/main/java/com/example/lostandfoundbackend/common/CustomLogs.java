package com.example.lostandfoundbackend.common;

import java.lang.annotation.*;

/**
 * @Author Tao
 * @Date 2025/4/12 13:39
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomLogs {
    /**
     * 操作的模块，如 用户管理
     */
    String operation();

    /**
     * 操作类型，如 新增，删除，修改
     */
    String type();
}
