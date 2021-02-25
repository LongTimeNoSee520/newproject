package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_wx_use_water_plan_add实体类
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@ApiModel("用水计划调整申请表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_wx_use_water_plan_add")
public class UseWaterPlanAddWX extends Model<UseWaterPlanAddWX> {

  /**
   *
   */
  @ApiModelProperty("主键")
  @TableField(value = "id", exist = true)
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
  @ApiModelProperty("编制年度")
  @TableField(value = "plan_year", exist = true)
  private Integer planYear;
  /**
   *
   */
  @ApiModelProperty("本年计划（当前年计划）")
  @TableField(value = "cur_year_plan", exist = true)
  private Double curYearPlan;
  /**
   *
   */
  @ApiModelProperty("第一季度计划")
  @TableField(value = "first_quarter", exist = true)
  private Double firstQuarter;
  /**
   *
   */
  @ApiModelProperty("第二季度计划")
  @TableField(value = "second_quarter", exist = true)
  private Double secondQuarter;
  /**
   *
   */
  @ApiModelProperty("第三季度计划")
  @TableField(value = "third_quarter", exist = true)
  private Double thirdQuarter;
  /**
   *
   */
  @ApiModelProperty("第四季度计划")
  @TableField(value = "fourth_quarter", exist = true)
  private Double fourthQuarter;
  /**
   *
   */
  @ApiModelProperty("第一水量")
  @TableField(value = "first_water", exist = true)
  private Double firstWater;
  /**
   *
   */
  @ApiModelProperty("第二水量")
  @TableField(value = "second_water", exist = true)
  private Double secondWater;
  /**
   *
   */
  @ApiModelProperty("调整类型")
  @TableField(value = "change_type", exist = true)
  private String changeType;
  /**
   *
   */
  @ApiModelProperty("调整季度")
  @TableField(value = "change_quarter", exist = true)
  private String changeQuarter;
  /**
   *
   */
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true, fill = FieldFill.INSERT)
  private Date createTime;
  /**
   *
   */
  @ApiModelProperty("是否确认")
  @TableField(value = "confirmed", exist = true)
  private String confirmed;
  /**
   *
   */
  @ApiModelProperty("确认时间")
  @TableField(value = "confirm_time", exist = true)
  private Date confirmTime;
  /**
   *
   */
  @ApiModelProperty("审核状态(0:提交,1:驳回,2:微信服务端审核通过,3默认,4办结单审核通过)")
  @TableField(value = "audit_status", exist = true)
  private String auditStatus;
  /**
   *
   */
  @ApiModelProperty("审核时间")
  @TableField(value = "audit_time", exist = true)
  private Date auditTime;
  /**
   *
   */
  @ApiModelProperty("审核人")
  @TableField(value = "audit_person", exist = true)
  private String auditPerson;
  /**
   *
   */
  @ApiModelProperty("审核人id")
  @TableField(value = "audit_person_id", exist = true)
  private String auditPersonId;
  /**
   *
   */
  @ApiModelProperty("审核结果")
  @TableField(value = "audit_result", exist = true)
  private String auditResult;
  /**
   *
   */
  @ApiModelProperty("是否执行")
  @TableField(value = "executed", exist = true)
  private String executed;
  /**
   *
   */
  @ApiModelProperty("是否打印")
  @TableField(value = "printed", exist = true)
  private String printed;
  /**
   *
   */
  @ApiModelProperty("审批申请附件id")
  @TableField(value = "audit_file_id", exist = true)
  private String auditFileId;
  /**
   *
   */
  @ApiModelProperty("近2月水量凭证附件id")
  @TableField(value = "water_proof_file_id", exist = true)
  private String waterProofFileId;
  /**
   *
   */
  @ApiModelProperty("其他证明材料")
  @TableField(value = "other_file_id", exist = true)
  private String otherFileId;
  /**
   *
   */
  @ApiModelProperty("核定调整计划(定额)")
  @TableField(value = "check_adjust_water", exist = true)
  private Double checkAdjustWater;
  /**
   *
   */
  @ApiModelProperty("第一季度计划（定额）")
  @TableField(value = "first_quarter_quota", exist = true)
  private Double firstQuarterQuota;
  /**
   *
   */
  @ApiModelProperty("第二季度计划（定额）")
  @TableField(value = "second_quarter_quota", exist = true)
  private Double secondQuarterQuota;
  /**
   *
   */
  @ApiModelProperty("第三季度计划（定额）")
  @TableField(value = "third_quarter_quota", exist = true)
  private Double thirdQuarterQuota;
  /**
   *
   */
  @ApiModelProperty("第四季度计划（定额）")
  @TableField(value = "fourth_quarter_quota", exist = true)
  private Double fourthQuarterQuota;


  @ApiModelProperty("当前年度第一季度")
  @TableField(value = "FrontFirstQuarter", exist = false)
  private Double FrontFirstQuarter;

  @ApiModelProperty("当前年度第二季度")
  @TableField(value = "FrontSecondQuarter", exist = false)
  private Double FrontSecondQuarter;

  @ApiModelProperty("当前年度第三季度")
  @TableField(value = "FrontThirdQuarter", exist = false)
  private Double FrontThirdQuarter;

  @ApiModelProperty("当前年度第四季度")
  @TableField(value = "FrontFourthQuarter", exist = false)
  private Double FrontFourthQuarter;

  @ApiModelProperty("当前年度")
  @TableField(value = "FrontFirstQuarter", exist = false)
  private Integer FrontPlanYear;

  @ApiModelProperty("年计划")
  @TableField(value = "FrontCurYearPlan", exist = false)
  private Double FrontCurYearPlan;

  /**
   * 实例化
   */
  public UseWaterPlanAddWX() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
