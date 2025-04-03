package com.example.lostandfoundbackend.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 * @Author Tao
 * @Date 2023/5/3 17:34
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private String code;
    private String msg;
    private Object data;
    public static Result success(){
       return Result.builder().code(Constants.CODE_200).msg("请求成功").build();
    }
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"请求成功",data);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
