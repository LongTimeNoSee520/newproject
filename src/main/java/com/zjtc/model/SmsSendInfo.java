package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 短信信息
 * @author lianghao
 * @date 2020/11/30
 */
@Data
@TableName(value = "t_sms_sendInfo")
@Accessors(chain = true)
@ApiModel(value = "短信信息", description = "短信信息实体类")
@EqualsAndHashCode(callSuper = true)
public class SmsSendInfo extends Model<SmsSendInfo> {
  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("区域code")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;


  @ApiModelProperty("单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty("单位名称")
  @TableField(value = "unit_name", exist = true)
  private String unitName;

  @ApiModelProperty("接收人id")
  @TableField(value = "receiver_id", exist = true)
  private String receiverId;

  @ApiModelProperty("接收人名称")
  @TableField(value = "receiver_name", exist = true)
  private String receiverName;

  @ApiModelProperty("接收人电话号")
  @TableField(value = "phone_number", exist = true)
  private String phoneNumber;

  @ApiModelProperty("发送状态：0-成功，1-失败，2-等待回执")
  @TableField(value = "status", exist = true)
  private String status;


  @ApiModelProperty("发送时间")
    @TableField(value = "send_time", exist = true)
  private Date sendTime;

  @ApiModelProperty("回执时间")
  @TableField(value = "receipt_time", exist = true)
  private Date receiptTime;

  @ApiModelProperty("短信类型：1-发送(下行)，2-接收(上行)")
  @TableField(value = "sms_type", exist = true)
  private String smsType;

  @ApiModelProperty("短信信息表关联id")
  @TableField(value = "sms_id", exist = true)
  private String smsId;

  @ApiModelProperty("预留字段1")
  @TableField(value = "exp1", exist = true)
  private String exp1;

  @ApiModelProperty("预留字段2")
  @TableField(value = "exp1", exist = true)
  private String exp2;

  @ApiModelProperty("预留字段3")
  @TableField(value = "exp3", exist = true)
  private String exp3;

  @ApiModelProperty("短信描述")
  @TableField(value = "description", exist = true)
  private String description;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
