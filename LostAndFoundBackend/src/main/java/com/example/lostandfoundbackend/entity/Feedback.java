package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2025-04-19
 */
@Getter
@Setter
@TableName("sys_feedback")
@ApiModel(value = "Feedback对象", description = "")
public class Feedback implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("评分")
    private Integer rate;

    @ApiModelProperty("反馈建议")
    private String content;

    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatarUrl;

}
