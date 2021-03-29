package com.zjtc.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/11
 */
@Data
@ApiModel(value = "用水计划调整申请表", description = "用水计划调整申请表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterPlanAddWXVO extends Model<UseWaterPlanAddWXVO> {

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

  @ApiModelProperty("调整类型(0:调整计划,1:增加计划)")
  @TableField(value = "change_type_name", exist = false)
  private String changeTypeName;

  @ApiModelProperty("调整类型编码")
  @TableField(value = "changeCode",exist = false)
  private String changeCode;
  /**
   *
   */
  @ApiModelProperty("调整季度")
  @TableField(value = "change_quarter", exist = true)
  private String changeQuarter;
  /**
   *
   */
  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
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
  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("确认时间")
  @TableField(value = "confirm_time", exist = true)
  private Date confirmTime;
  /**
   *
   */
  @ApiModelProperty("审核状态")
  @TableField(value = "audit_status", exist = true)
  private String auditStatus;
  /**
   *
   */
  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
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
  @ApiModelProperty("核定调整计划")
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

  @ApiModelProperty("审批申请附件id")
  @TableField(value = "audit_file_id", exist = true)
  private String auditFileId;

  @ApiModelProperty("审批申请附件id")
  private List<FileVO> auditFileIds;
  /**
   *
   */
  @ApiModelProperty("近2月水量凭证附件id")
  @TableField(value = "water_proof_file_id", exist = true)
  private String waterProofFileId;

  @ApiModelProperty("近2月水量凭证附件id")
  @TableField(value = "water_proof_file_id", exist = true)
  private List<FileVO> waterProofFileIds;
  /**
   *
   */
  @ApiModelProperty("其他证明材料")
  @TableField(value = "other_file_id", exist = true)
  private String otherFileId;

  @ApiModelProperty("其他证明材料")
  @TableField(value = "other_file_id", exist = true)
  private List<FileVO> otherFileIds;

  @ApiModelProperty("预览路径")
  private String preViewRealPath;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
