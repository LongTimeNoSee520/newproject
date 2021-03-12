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
 * @author lianghao
 * @date 2021/01/19
 */
@ApiModel(value = "审核流程表", description = "审核流程表")
@TableName("t_sms_flow_process")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FlowProcess extends Model<FlowProcess> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("审核状态(0:短信创建1:待审核,2:审核通过,3:审核不通过)")
  @TableField(value = "audit_status", exist = true)
  private String auditStatus;

  @ApiModelProperty("审核状态名称")
  @TableField(exist = false)
  private String auditStatusName;

  @ApiModelProperty("审核内容")
  @TableField(value = "audit_content", exist = true)
  private String auditContent;

  @ApiModelProperty("操作人员id")
  @TableField(value = "operator_id", exist = true)
  private String operatorId;

  @ApiModelProperty("操作人员")
  @TableField(value = "operator", exist = true)
  private String operator;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("操作时间")
  @TableField(value = "operation_time", exist = true)
  private Date operationTime;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty("业务id")
  @TableField(value = "business_id", exist = true)
  private String businessId;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
