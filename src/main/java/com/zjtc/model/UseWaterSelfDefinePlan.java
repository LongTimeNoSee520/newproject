package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_use_water_self_define_plan实体类
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/4
 */
@ApiModel("用水自平计划表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_self_define_plan")
public class UseWaterSelfDefinePlan extends Model<UseWaterSelfDefinePlan> {

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
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true, fill = FieldFill.INSERT)
  private Date createTime;
  /**
   *
   */
  @ApiModelProperty("审核状态(0:未审核(提交申请审核信息时设置此值),1:审核不通过,2:审核通过,3:默认为空,在审核提交之前都为此值)")
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
  @ApiModelProperty("执行时间")
  @TableField(value = "execute_time", exist = true)
  private Date executeTime;
  /**
   *
   */
  @ApiModelProperty("执行人")
  @TableField(value = "executor", exist = true)
  private String executor;
  /**
   *
   */
  @ApiModelProperty("执行人id")
  @TableField(value = "executor_id", exist = true)
  private String executorId;
  /**
   *
   */
  @ApiModelProperty("执行结果")
  @TableField(value = "execute_result", exist = true)
  private String executeResult;

  @ApiModelProperty("调整类型(取数据字典值)")
  @TableField(value = "change_type", exist = true)
  private String changeType;

//  @ApiModelProperty("状态(1:初始值,2:待确认,3:已确认,4:执行")
//  @TableField(value = "status", exist = true)
//  private String status;

  @ApiModelProperty("自平计划材料")
  @TableField(value = "execute_result", exist = false)
  List<File> files;



  /**
   * 实例化
   */
  public UseWaterSelfDefinePlan() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
