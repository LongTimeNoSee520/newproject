package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true, fill = FieldFill.INSERT)
  private Date createTime;
  /**
   *
   */
  @ApiModelProperty("审核状态(0:未审核1:审核不通过,2:审核通过,3:在审核提交之前都默认为此值)")
  @TableField(value = "audit_status", exist = true)
  private String auditStatus;
  /**
   *
   */
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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


  @ApiModelProperty("是否打印")
  @TableField(value = "printed", exist = true)
  private String printed;

  @ApiModelProperty("自平数据确认")
  @TableField(value = "data_sure", exist = true)
  private String dataSure;

//  @ApiModelProperty("状态(1:初始值,2:待确认,3:已确认,4:执行")
//  @TableField(value = "status", exist = true)
//  private String status;

  @ApiModelProperty("自平材料附件id")
  @TableField(value = "SelfDefineFiles", exist = false)
  private List<File> selfDefineFiles;


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
