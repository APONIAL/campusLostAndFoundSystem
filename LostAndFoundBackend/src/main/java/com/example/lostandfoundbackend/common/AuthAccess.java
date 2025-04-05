package com.example.lostandfoundbackend.common;

import java.lang.annotation.*;

/**
 * 权限注解，用于标识哪些不需要被拦截的接口
 * @Author Tao
 * @Date 2025/4/5 15:54
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {

}
