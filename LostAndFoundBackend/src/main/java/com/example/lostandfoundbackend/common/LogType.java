package com.example.lostandfoundbackend.common;

/**
 * 系统日志操作类型枚举
 * @Author Tao
 * @Date 2025/4/12 13:46
 * @Version 1.0
 */
public enum LogType {
    //枚举查询类型
    ADD("新增"),UPDATE("修改"),DELETE("删除"),BATCH_DELETE("批量删除"),LOGIN("登录"),REGISTER("注册"),
    ADD_OR_UPDATE("新增或者修改"),FORGET_PASSWORD("忘记密码"),OTHER("其他"),;

    /**
     * 枚举值
     */
    private String value;

    /**
     * 获取枚举值
     * @return value
     */
    public String getValue() {
        return value;
    }

    LogType(String value) {
        this.value = value;
    }
}
