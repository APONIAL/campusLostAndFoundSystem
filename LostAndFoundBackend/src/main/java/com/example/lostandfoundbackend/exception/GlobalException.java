package com.example.lostandfoundbackend.exception;

import com.example.lostandfoundbackend.common.Constants;
import com.example.lostandfoundbackend.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Tao
 * @Date 2025/4/4 13:420
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result serviceException(ServiceException e){
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return Result.error(Constants.CODE_500, "系统错误");
    }
}
