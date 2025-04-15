package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 失物信息表
 * </p>
 *
 * @author admin
 * @since 2025-04-14
 */
@Getter
@Setter
@TableName("lost_info")
@ApiModel(value = "LostInfo对象", description = "失物信息表")
public class LostInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("物品名称")
    private String name;

    @ApiModelProperty("物品图片")
    private String img;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("物品描述")
    private String content;

    @ApiModelProperty("发布时间")
    private String time;

    @TableField(exist = false)
    private String user;

}
