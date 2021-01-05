package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_use_water_plan实体类
 * 
 * @author 
 *
 */
@ApiModel("")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_plan")
public class UseWaterPlan extends Model<UseWaterPlan>{
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "id",exist = true)
    private String id;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "use_water_unit_id",exist = true)
    private String useWaterUnitId;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "unit_name",exist = true)
    private String unitName;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "algorithm_type",exist = true)
    private String algorithmType;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "water_meter_code",exist = true)
    private String waterMeterCode;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "plan_year",exist = true)
    private Integer planYear;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "base_water_amount",exist = true)
    private Double baseWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "before_last_year_water_amount",exist = true)
    private Double beforeLastYearWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "last_year_water_amount",exist = true)
    private Double lastYearWaterAmount;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "three_year_avg",exist = true)
    private Double threeYearAvg;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "now_price",exist = true)
    private Double nowPrice;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "n8",exist = true)
    private Integer n8;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "minus_pay_status",exist = true)
    private String minusPayStatus;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "balance_test",exist = true)
    private String balanceTest;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "create_type",exist = true)
    private String createType;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "cur_year_base_plan",exist = true)
    private Double curYearBasePlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "cur_year_plan",exist = true)
    private Double curYearPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "next_year_base_start_plan",exist = true)
    private Double nextYearBaseStartPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "next_year_quota_start_plan",exist = true)
    private Double nextYearQuotaStartPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "next_year_base_end_plan",exist = true)
    private Double nextYearBaseEndPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "next_year_quota_end_plan",exist = true)
    private Double nextYearQuotaEndPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "first_quarter_base",exist = true)
    private Double firstQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "second_quarter_base",exist = true)
    private Double secondQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "third_quarter_base",exist = true)
    private Double thirdQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "fourth_quarter_base",exist = true)
    private Double fourthQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "first_quarter_quota",exist = true)
    private Double firstQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "second_quarter_quota",exist = true)
    private Double secondQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "third_quarter_quota",exist = true)
    private Double thirdQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "fourth_quarter_quota",exist = true)
    private Double fourthQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "plan_type",exist = true)
    private String planType;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private java.time.Instant createTime;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "update_user_id",exist = true)
    private String updateUserId;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "added",exist = true)
    private String added;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "reduce_plan",exist = true)
    private Double reducePlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "reward_plan",exist = true)
    private Double rewardPlan;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "assess_quarter",exist = true)
    private String assessQuarter;
    /**
    * 
    */
    @ApiModelProperty("")
    @TableField(value = "remarks",exist = true)
    private String remarks;
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
