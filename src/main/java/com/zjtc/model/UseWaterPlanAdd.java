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
 * @author lianghao
 * @date 2021/01/04
 */
@Data
@TableName(value = "t_w_use_water_plan_add")
@Accessors(chain = true)
@ApiModel(value = "用水计划日常调整", description = "用水计划日常调整")
@EqualsAndHashCode(callSuper = true)
public class UseWaterPlanAdd extends Model<UseWaterPlanAdd> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "单位名称")
  @TableField(value = "unit_name", exist = true)
  private String unitName;//单位名称

  @ApiModelProperty(value = "单位编码")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty(value = "水表档案号")
  @TableField(value = "water_meter_code", exist = true)
  private String waterMeterCode;

  @ApiModelProperty(value = "编制年度")
  @TableField(value = "plan_year", exist = true)
  private int planYear;

  @ApiModelProperty(value = "本年计划（当前年计划）")
  @TableField(value = "cur_year_plan", exist = true)
  private Double curYearPlan;

  @ApiModelProperty(value = "第一季度计划")
  @TableField(value = "first_quarter", exist = true)
  private Double firstQuarter;

  @ApiModelProperty(value = "第二季度计划")
  @TableField(value = "second_quarter", exist = true)
  private Double secondQuarter;

  @ApiModelProperty(value = "第三季度计划")
  @TableField(value = "third_quarter", exist = true)
  private Double thirdQuarter;

  @ApiModelProperty(value = "第四季度计划")
  @TableField(value = "fourth_quarter", exist = true)
  private Double fourthQuarter;

  @ApiModelProperty(value = "第一水量")
  @TableField(value = "first_water", exist = true)
  private Double firstWater;

  @ApiModelProperty(value = "第二水量")
  @TableField(value = "second_water", exist = true)
  private Double secondWater;

  @ApiModelProperty(value = "调整类型")
  @TableField(value = "plan_type", exist = true)
  private String planType;

  @ApiModelProperty(value = "调整季度")
  @TableField(value = "change_quarter", exist = true)
  private String changeQuarter;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "创建者")
  @TableField(value = "creater", exist = true)
  private String creater;

  @ApiModelProperty(value = "创建者id")
  @TableField(value = "creater_id", exist = true)
  private String createrId;

  @ApiModelProperty(value = "审批申请附件id")
  @TableField(value = "audit_file_id", exist = true)
  private String auditFileId;

  @ApiModelProperty(value = "近2月水量凭证附件id")
  @TableField(value = "water_proof_file_id", exist = true)
  private String waterProofFileId;

  @ApiModelProperty(value = "其他证明材料附件id")
  @TableField(value = "other_file_id", exist = true)
  private String otherFileId;

  @ApiModelProperty(value = "是否打印(0否1是)")
  @TableField(value = "printed", exist = true)
  private String printed;

  @ApiModelProperty(value = "状态")
  @TableField(value = "status", exist = true)
  private String status;

  @ApiModelProperty(value = "备注")
  @TableField(value = "remarks", exist = true)
  private String remarks;

  @ApiModelProperty(value = "是否执行")
  @TableField(value = "auditStatus", exist = false)
  private String auditStatus;

  @ApiModelProperty(value = "是否审核")
  @TableField(value = "executed", exist = false)
  private String executed;

  @ApiModelProperty(value = "审核时间")
  @TableField(value = "confirmTime", exist = false)
  private Date confirmTime;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
