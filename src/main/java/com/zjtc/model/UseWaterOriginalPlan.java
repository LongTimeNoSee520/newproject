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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_use_water_original_plan实体类
 * 
 * @author 
 *
 */
@ApiModel("用水计划原始表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_original_plan")
public class UseWaterOriginalPlan extends Model<UseWaterOriginalPlan>{
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
    @ApiModelProperty("单位编码")
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
    @ApiModelProperty("扣加价,0否，1是")
    @TableField(value = "minus_pay_status",exist = true)
    private String minusPayStatus;
    /**
    * 
    */
    @ApiModelProperty("水平衡测试,0默认值,1奖，2罚")
    @TableField(value = "balance_test",exist = true)
    private String balanceTest;
    /**
    * 
    */
    @ApiModelProperty("创建,0默认值,1奖，2罚")
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
    @ApiModelProperty("下年初始计划（基础）")
    @TableField(value = "next_year_base_start_plan",exist = true)
    private Double nextYearBaseStartPlan;
    /**
    * 
    */
    @ApiModelProperty("下年初始计划（定额）")
    @TableField(value = "next_year_quota_start_plan",exist = true)
    private Double nextYearQuotaStartPlan;
    /**
    * 
    */
    @ApiModelProperty("下年终计划（基础）")
    @TableField(value = "next_year_base_end_plan",exist = true)
    private Double nextYearBaseEndPlan;
    /**
    * 
    */
    @ApiModelProperty("下年终计划（定额）")
    @TableField(value = "next_year_quota_end_plan",exist = true)
    private Double nextYearQuotaEndPlan;
    /**
    * 
    */
    @ApiModelProperty("第一季度计划(基础)")
    @TableField(value = "first_quarter_base",exist = true)
    private Double firstQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("第二季度计划(基础)")
    @TableField(value = "second_quarter_base",exist = true)
    private Double secondQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("第三季度计划(基础)")
    @TableField(value = "third_quarter_base",exist = true)
    private Double thirdQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("第四季度计划(基础)")
    @TableField(value = "fourth_quarter_base",exist = true)
    private Double fourthQuarterBase;
    /**
    * 
    */
    @ApiModelProperty("第一季度计划（定额）")
    @TableField(value = "first_quarter_quota",exist = true)
    private Double firstQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("第二季度计划（定额）")
    @TableField(value = "second_quarter_quota",exist = true)
    private Double secondQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("第三季度计划（定额）")
    @TableField(value = "third_quarter_quota",exist = true)
    private Double thirdQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("第四季度计划（定额）")
    @TableField(value = "fourth_quarter_quota",exist = true)
    private Double fourthQuarterQuota;
    /**
    * 
    */
    @ApiModelProperty("是否打印")
    @TableField(value = "printed",exist = true)
    private String printed;
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
    @ApiModelProperty("开始考核季度")
    @TableField(value = "assess_quarter",exist = true)
    private Integer assessQuarter;
    /**
    * 
    */
    @ApiModelProperty("是否编制")
    @TableField(value = "planed",exist = true)
    private String planed;
    /**
     *
     */
    @ApiModelProperty("是否新增(户)")
    @TableField(value = "added",exist = true)
    private String added;
    /**
     *
     */
    @ApiModelProperty("基础计划大于，定额计划的标识")
    @TableField(value = "sign",exist = true)
    private String sign;
	/**
	 * 实例化
	 */
	public UseWaterOriginalPlan() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
