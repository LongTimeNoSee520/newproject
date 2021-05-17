package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuyantian
 * @date 2021/5/13
 * @description
 */

@ApiModel(value = "催缴通知打印记录", description = "消息表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_pay_info_print")
public class PayInfoPrint extends Model<PayInfoPrint> {
  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("节点编码")
  @TableField(value = "node_code",exist = true)
  private String nodeCode;

  @ApiModelProperty("缴费记录id")
  @TableField(value = "pay_id",exist = true)
  private String payId;
  //生成规则：单位编号+年+季度+编号（3位）
  @ApiModelProperty("打印通知单编号")
  @TableField(value = "print_num",exist = true)
  private String printNum;

  @ApiModelProperty("打印状态，0否1是")
  @TableField(value = "status",exist = true)
  private String status;

  @ApiModelProperty("打印人")
  @TableField(value = "print_person",exist = true)
  private String printPerson;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("打印时间")
  @TableField(value = "create_time",exist = true)
  private Date createTime;

}
