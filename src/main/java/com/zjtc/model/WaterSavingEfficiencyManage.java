package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_water_saving_unit实体类
 * 
 * @author 
 *
 */
@ApiModel("节水效率评估管理表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_saving_efficiency_manage")
public class WaterSavingEfficiencyManage extends Model<WaterSavingEfficiencyManage>{
    /**
    *
    */
    @ApiModelProperty("id")
    @TableId(value = "id",type = IdType.UUID)
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
    @ApiModelProperty("分类")
    @TableField(value = "type",exist = true)
    private String type;
    /**
    *
    */
    @ApiModelProperty("评估指标")
    @TableField(value = "evaluation_index",exist = true)
    private String evaluationIndex;
    /**
    *
    */
    @ApiModelProperty("计算公式")
    @TableField(value = "calculation_formula",exist = true)
    private String calculationFormula;
    /**
    *
    */
    @ApiModelProperty("实测值")
    @TableField(value = "actual_value",exist = true)
    private Double actualValue;
    /**
    *
    */
    @ApiModelProperty("省市定额")
    @TableField(value = "PC_quota",exist = true)
    private Double PCQuota;
    /**
    *
    */
    @ApiModelProperty("行业定额")
    @TableField(value = "industry_quota",exist = true)
    private Double  industryQuota;
    /**
    *
    */
    @ApiModelProperty("国际先进值")
    @TableField(value = "inter_advanced_value",exist = true)
    private Double interAdvancedValue;
    /**
    *
    */
    @ApiModelProperty("水平分析")
    @TableField(value = "level_analysis",exist = true)
    private String levelAnalysis;

    /**
     *
     */
    @ApiModelProperty("是否删除")
    @TableField(value = "deleted",exist = true)
    private String deleted;
    /**
     *
     */
    @ApiModelProperty("水平衡测试id")
    @TableField(value = "water_balance_test_id",exist = true)
    private String waterBalanceTestId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
