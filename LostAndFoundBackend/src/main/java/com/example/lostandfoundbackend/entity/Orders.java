package com.example.lostandfoundbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2025-04-13
 */
@Data
@TableName("t_orders")
@ApiModel(value = "Orders对象", description = "")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单编号")
    private String no;

    @ApiModelProperty("订单名称")
    private String name;

    @ApiModelProperty("订单金额")
    private BigDecimal money;

    @ApiModelProperty("用户ID")
    private Integer usrid;

    @ApiModelProperty("订单分类")
    private String category;

    @ApiModelProperty("订单日期")
    private String date;


    @TableField(exist = false)
    private String user;

}
