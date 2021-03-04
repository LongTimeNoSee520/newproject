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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_use_water_plan实体类
 * 
 * @author 
 *
 */
@ApiModel("用水计划表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_plan")
public class UseWaterPlan extends Model<UseWaterPlan>{
    /**
    * 
    */
    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
    * 
    */
    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;
    /**
    * 
    */
    @ApiModelProperty("单位id")
    @TableField(value = "use_water_unit_id",exist = true)
    private String useWaterUnitId;
    /**
    * 
    */
    @ApiModelProperty("单位名称")
    @TableField(value = "unit_name",exist = true)
    private String unitName;
    /**
    * 
    */
    @ApiModelProperty("单位编号")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;
    /**
    * 
    */
    @ApiModelProperty("算法类型")
    @TableField(value = "algorithm_type",exist = true)
    private String algorithmType;
    /**
    * 
    */
    @ApiModelProperty("水表档案号")
    @TableField(value = "water_meter_code",exist = true)
    private String waterMeterCode;
    /**
    * 
    */
    @ApiModelProperty("编制年度")
    @TableField(value = "plan_year",exist = true)
    private Integer planYear;
    /**
    * 
    */
    @ApiModelProperty("第三年（编制基础）")
    @TableField(value = "base_water_amount",exist = true)
    private Double baseWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("第二年（前年用水量）")
    @TableField(value = "before_last_year_water_amount",exist = true)
    private Double beforeLastYearWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("第一年（去年用水量）")
    @TableField(value = "last_year_water_amount",exist = true)
    private Double lastYearWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("三年平均水量")
    @TableField(value = "three_year_avg",exist = true)
    private Double threeYearAvg;
    /**
    * 
    */
    @ApiModelProperty("当前水价")
    @TableField(value = "now_price",exist = true)
    private Double nowPrice;
    /**
    * 
    */
    @ApiModelProperty("n8")
    @TableField(value = "n8",exist = true)
    private Integer n8;
    /**
    * 
    */
    @ApiModelProperty("扣加价")
    @TableField(value = "minus_pay_status",exist = true)
    private String minusPayStatus;
    /**
    * 
    */
    @ApiModelProperty("水平衡测试")
    @TableField(value = "balance_test",exist = true)
    private String balanceTest;
    /**
    * 
    */
    @ApiModelProperty("创建")
    @TableField(value = "create_type",exist = true)
    private String createType;
    /**
    * 
    */
    @ApiModelProperty("当前年基础计划")
    @TableField(value = "cur_year_base_plan",exist = true)
    private Double curYearBasePlan;
    /**
    * 
    */
    @ApiModelProperty("本年计划（当前年计划）")
    @TableField(value = "cur_year_plan",exist = true)
    private Double curYearPlan;
    /**
    * 
    */
    @ApiModelProperty("下年初始计划")
    @TableField(value = "next_year_start_plan",exist = true)
    private Double nextYearStartPlan;
    /**
    * 
    */
    @ApiModelProperty("下年终计划")
    @TableField(value = "next_year_end_plan",exist = true)
    private Double nextYearEndPlan;
    /**
    * 
    */
    @ApiModelProperty("第一季度计划")
    @TableField(value = "first_quarter",exist = true)
    private Double firstQuarter;
    /**
    * 
    */
    @ApiModelProperty("第二季度计划")
    @TableField(value = "second_quarter",exist = true)
    private Double secondQuarter;
    /**
    * 
    */
    @ApiModelProperty("第三季度计划")
    @TableField(value = "third_quarter",exist = true)
    private Double thirdQuarter;
    /**
    * 
    */
    @ApiModelProperty("第四季度计划")
    @TableField(value = "fourth_quarter",exist = true)
    private Double fourthQuarter;
    /**
    * 
    */
    @ApiModelProperty("计划类型")
    @TableField(value = "plan_type",exist = true)
    private String planType;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("调整用户id")
    @TableField(value = "update_user_id",exist = true)
    private String updateUserId;

    @ApiModelProperty("调整时间")
    @TableField(value = "update_time",exist = true,fill = FieldFill.INSERT)
    private Date updateTime;

    /**
    * 
    */
    @ApiModelProperty("是否新增")
    @TableField(value = "added",exist = true)
    private String added;
    /**
    * 
    */
    @ApiModelProperty("减扣计划")
    @TableField(value = "reduce_plan",exist = true)
    private Double reducePlan;
    /**
    * 
    */
    @ApiModelProperty("奖励计划")
    @TableField(value = "reward_plan",exist = true)
    private Double rewardPlan;
    /**
    * 
    */
    @ApiModelProperty("开始考核季度")
    @TableField(value = "assess_quarter",exist = true)
    private String assessQuarter;
    /**
    * 
    */
    @ApiModelProperty("备注")
    @TableField(value = "remarks",exist = true)
    private String remarks;

    @ApiModelProperty("是否存在未处理完的办结单(0否1是)")
    @TableField(value = "exist_settlement_form",exist = true)
    private String existSettlementForm;

    @ApiModelProperty("是否打印")
    @TableField(value = "printed",exist = true)
    private String printed;
	/**
	 * 实例化
	 */
	public UseWaterPlan() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
