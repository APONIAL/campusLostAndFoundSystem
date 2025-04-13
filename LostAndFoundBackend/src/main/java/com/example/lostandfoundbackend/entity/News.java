package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 新闻信息
 * </p>
 *
 * @author admin
 * @since 2025-04-10
 */
@Getter
@Setter
@ApiModel(value = "News对象", description = "新闻信息")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("发布人id")
    private Integer authorid;

    @ApiModelProperty("发布时间")
    private String time;

    @TableField(exist = false)
    private String author;

}
