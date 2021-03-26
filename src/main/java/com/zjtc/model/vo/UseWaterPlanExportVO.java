package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author lianghao
 * @date 2021/01/04
 */
@ApiModel("用水计划表")
@Data
@EqualsAndHashCode(callSuper = true)
public class UseWaterPlanExportVO extends Model<UseWaterPlanExportVO>{


    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位编号")
    private String unitCode;

    @ApiModelProperty("水表档案号")
    private String waterMeterCode;

    @ApiModelProperty("编制年度")
    private Integer planYear;

    @ApiModelProperty("本年计划（当前年计划）")
    private Double curYearPlan;

    @ApiModelProperty("第一季度计划")
    private Double firstQuarter;

    @ApiModelProperty("第二季度计划")
    private Double secondQuarter;

    @ApiModelProperty("第三季度计划")
    private Double thirdQuarter;

    @ApiModelProperty("第四季度计划")
    private Double fourthQuarter;

    @ApiModelProperty("计划类型")
    private String planType;

    @ApiModelProperty("调整时间")
    private String updateTime;

    @ApiModelProperty("备注")
    private String remarks;

	/**
	 * 实例化
	 */
	public UseWaterPlanExportVO() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return null;
	}
	
}
