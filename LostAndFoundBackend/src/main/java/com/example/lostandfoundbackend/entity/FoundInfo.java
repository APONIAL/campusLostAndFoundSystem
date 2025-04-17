package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 招领信息表
 * </p>
 *
 * @author admin
 * @since 2025-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("found_info")
@ApiModel(value = "FoundInfo对象", description = "招领信息表")
public class FoundInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("物品名称")
    private String name;

    @ApiModelProperty("物品图片")
    private String img;

    @ApiModelProperty("发布者id")
    private Integer userId;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("物品描述")
    private String content;

    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("发现地点")
    private String foundLocation;

    @ApiModelProperty("发现时间")
    private String foundTime;

    @ApiModelProperty("发布时间")
    private String createTime;

    @TableField(exist = false)
    private String founderName;


}
