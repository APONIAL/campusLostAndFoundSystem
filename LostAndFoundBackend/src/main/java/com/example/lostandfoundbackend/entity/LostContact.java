package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 留言信息表
 * </p>
 *
 * @author admin
 * @since 2025-04-16
 */
@Getter
@Setter
@TableName("lost_contact")
@ApiModel(value = "LostContact对象", description = "留言信息表")
public class LostContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("联系人id")
    private Integer publisherId;

    @ApiModelProperty("被联系人id")
    private Integer contactedId;

    @ApiModelProperty("留言")
    private String content;

    @ApiModelProperty("联系方式")
    private String contactMethod;

    @ApiModelProperty("留言时间")
    private String createTime;

    @ApiModelProperty("失物信息表中id")
    private Integer lostId;

    @ApiModelProperty("招领信息表中id")
    private Integer foundId;

    @TableField(exist = false)
    private String publisherName;

    @TableField(exist = false)
    private String contactedName;






}
