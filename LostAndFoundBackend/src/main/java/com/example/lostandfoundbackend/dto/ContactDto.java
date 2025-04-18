package com.example.lostandfoundbackend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LostContact表 DTO
 * @Author Tao
 * @Date 2025/4/18 13:02
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    /**
     * 被留言者id
     */
    private Integer contactedId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contactMethod;

    /**
     * 暂存表单中id
     */
    private Integer itemId;

    /**
     * 留言类型 0 失物招领 1 寻物启事
     */
    private Integer itemType;
}
