package com.example.lostandfoundbackend.exception;

/**
 * @Author Tao
 * @Date 2025/4/4 14:13
 * @Version 1.0
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String msg){
        super(msg);
    }
}
