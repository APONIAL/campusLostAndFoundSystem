package com.example.lostandfoundbackend.exception;

import com.example.lostandfoundbackend.common.Constants;
import lombok.Data;
import lombok.Getter;

/**
 * @Author Tao
 * @Date 2025/4/4 14:13
 * @Version 1.0
 */
@Getter
public class ServiceException extends RuntimeException{

    private final String code;
    public ServiceException(String msg){
        super(msg);
        this.code = Constants.CODE_600;

    }
    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }
}
