package com.example.lostandfoundbackend.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.lostandfoundbackend.common.CustomLogs;
import com.example.lostandfoundbackend.entity.Logs;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.ILogsService;
import com.example.lostandfoundbackend.utils.IpUtils;
import com.example.lostandfoundbackend.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Tao
 * @Date 2025/4/12 19:23
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class LogsAspect {

    @Resource
    ILogsService logsService;
    @AfterReturning(pointcut = "execution(* *(..)) && @annotation(customLogs)", returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, CustomLogs customLogs,Object jsonResult){
        //获取当前登录用户信息
        User loginUser = TokenUtils.getCurrentUser();
        //用户未登录，请求头没有token，就要从参数里面获取操作人信息
        if (loginUser == null){
            //登录，注册
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                loginUser = (User) args[0];
            }
        }
        if (loginUser == null){
            log.error("记录日志信息报错，未获取当前操作用户信息");
            return;
        }
        //获取HttpServletRequest对象
        ServletRequestAttributes servletRequestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取IP信息
        //组装日志的实体对象
        Logs logs = Logs.builder()
                .user(loginUser.getUsername())
                .type(customLogs.type().getValue())
                .operation(customLogs.operation())
                .ip(IpUtils.getIpAddr(request))
                .time(DateUtil.now()).build();
        //插入数据到数据库，hutool的异步方式，防止阻塞主线程，即日志插入错误，导致程序阻塞，其他数据
        //插入不了数据库中
        ThreadUtil.execAsync(() -> {
            logsService.save(logs);
        });

    }
}
