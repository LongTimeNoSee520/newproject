package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/24
 */
@Data
@TableName(value = "t_w_finance")
@Accessors(chain = true)
@ApiModel(value = "加价费开票记录表（原财务管理表）", description = "加价费开票记录表（原财务管理表）")
@EqualsAndHashCode(callSuper = true)
public class Finance extends Model<Finance> {


  @ApiModelProperty(value = "主键",allowableValues = "32",dataType = "String",required = false)
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "单位名称")
  @TableField(value = "unit_name", exist = true)
  private String unitName;

  @ApiModelProperty(value = "缴费金额")
  @TableField(value = "money", exist = true)
  private float money;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "入账时间")
  @TableField(value = "payment_date", exist = true)
  private Date paymentDate;

  @ApiModelProperty(value = "开票状态")
  @TableField(value = "invoice_state", exist = true)
  private String invoiceState;

  @ApiModelProperty(value = "开票人Id")
  @TableField(value = "drawer_id", exist = true)
  private String drawerId;

  @ApiModelProperty(value = "开票人")
  @TableField(value = "drawer", exist = true)
  private String drawer;

  @ApiModelProperty(value = "类型")
  @TableField(value = "type", exist = true)
  private char type;

  @ApiModelProperty(value = "单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty(value = "打印人")
  @TableField(value = "print_person", exist = true)
  private String printPerson;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "打印时间")
  @TableField(value = "print_time", exist = true)
  private Date printTime;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty(value = "发票号")
  @TableField(value = "invoice_number", exist = true)
  private String invoiceNumber;

  @ApiModelProperty(value = "用户类型")
  @TableField(value = "user_type", exist = true)
  private String userType;


  @Override
  protected Serializable pkVal() {
    return null;
  }
}
