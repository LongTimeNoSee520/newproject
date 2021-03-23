package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_refund_or_refund实体类
 *
 * @author
 */
@ApiModel("退减免单")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_refund_or_refund")
public class RefundOrRefund extends Model<RefundOrRefund> {

  /**
   *
   */
  @ApiModelProperty("id")
  @TableId(value = "id", type = IdType.UUID)
  private String id;
  /**
   *
   */
  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;
  /**
   *
   */
  @ApiModelProperty("单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;
  /**
   *
   */
  @ApiModelProperty("单位名称")
  @TableField(value = "unit_name", exist = true)
  private String unitName;
  /**
   *
   */
  @ApiModelProperty("单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;
  /**
   *
   */
  @ApiModelProperty("水表档案号")
  @TableField(value = "water_meter_code", exist = true)
  private String waterMeterCode;
  /**
   *
   */
  @ApiModelProperty("缴费id")
  @TableField(value = "pay_id", exist = true)
  private String payId;
  /**
   *
   */
  @ApiModelProperty("单据类型,单据类型：1退款单，2：减免单")
  @TableField(value = "type", exist = true)
  private String type;
  /**
   *
   */
  @ApiModelProperty("单据类型名称")
  @TableField(exist = false)
  private String typeName;
  /**
   *
   */
  @ApiModelProperty("退款/减免季度")
  @TableField(value = "quarter", exist = true)
  private String quarter;
  /**
   *
   */
  @ApiModelProperty("退款/减免年度")
  @TableField(value = "year", exist = true)
  private Integer year;
  /**
   *
   */
  @ApiModelProperty("实收金额")
  @TableField(value = "actual_amount", exist = true)
  private Double actualAmount;
  /**
   *
   */
  @ApiModelProperty("退款/减免金额")
  @TableField(value = "money", exist = true)
  private Double money;
  /**
   *
   */
  @ApiModelProperty("退款/减免原因")
  @TableField(value = "reason", exist = true)
  private String reason;
  /**
   *
   */
  @ApiModelProperty("经办人")
  @TableField(value = "drawer", exist = true)
  private String drawer;
  /**
   *
   */
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true, fill = FieldFill.INSERT)
  private Date createTime;
  /**
   *
   */
  @ApiModelProperty("审核人")
  @TableField(value = "audit_person", exist = true)
  private String auditPerson;
  /**
   *
   */
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty("审核时间")
  @TableField(value = "audit_time", exist = true)
  private Date auditTime;
  /**
   * 只要科长角色提交审核后，状态为：待审核，之前都是：提交
   */
  @ApiModelProperty("状态,0，提交，1:待审核，2：同意，3：不同意")
  @TableField(value = "status", exist = true)
  private String status;
  /**
   *
   */
  @ApiModelProperty("下一环节id")
  @TableField(value = "next_node_id", exist = true)
  private String nextNodeId;
  /**
   *
   */
  @ApiModelProperty("是否撤销，0否，1是")
  @TableField(value = "is_revoke", exist = true)
  private String isRevoke;
  /**
   *
   */
  @ApiModelProperty("服务人员处理意见")
  @TableField(value = "treatment_advice", exist = true)
  private String treatmentAdvice;

  /**
   *
   */
  @ApiModelProperty("附件集合")
  @TableField(exist = false)
  private List<File> sysFiles;

  /**
   *
   */
  @ApiModelProperty("审核流程")
  @TableField(exist = false)
  private List<Map<String, Object>> auditFlow;

  /**
   *
   */
  @ApiModelProperty("修改按钮标识")
  @TableField(exist = false)
  private String editBtn;

  /**
   * 实例化
   */
  public RefundOrRefund() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
