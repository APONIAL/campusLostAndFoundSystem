package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 *
 * </p>
 *
 * @author admin
 * @since 2025-04-12
 */
@Getter
@Setter
@TableName("sys_logs")
@Builder
@ApiModel(value = "Logs对象", description = "")
public class Logs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作名称")
    private String operation;

    @ApiModelProperty("操作类型")
    private String type;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("操作人")
    private String user;

    @ApiModelProperty("操作时间")
    private String time;


}
