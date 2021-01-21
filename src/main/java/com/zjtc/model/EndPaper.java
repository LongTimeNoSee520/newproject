package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_end_paper实体类
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 *
 */
@ApiModel("办结单")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_end_paper")
public class  EndPaper extends Model<EndPaper>{
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
    @ApiModelProperty("水表档案号")
    @TableField(value = "water_meter_code",exist = true)
    private String waterMeterCode;
    /**
    * 
    */
    @ApiModelProperty("办结单类型")
    @TableField(value = "paper_type",exist = true)
    private String paperType;
    /**
    * 
    */
    @ApiModelProperty("数据来源")
    @TableField(value = "data_sources",exist = true)
    private String dataSources;
    /**
    * 
    */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("经办人（创建人id）")
    @TableField(value = "creater_id",exist = true)
    private String createrId;
    /**
    * 
    */
    @ApiModelProperty("调整年份")
    @TableField(value = "plan_year",exist = true)
    private Integer planYear;
    /**
    * 
    */
    @ApiModelProperty("调整季度")
    @TableField(value = "change_quarter",exist = true)
    private String changeQuarter;
    /**
    * 
    */
    @ApiModelProperty("年计划")
    @TableField(value = "cur_year_plan",exist = true)
    private Double curYearPlan;
    /**
    * 
    */
    @ApiModelProperty("第一用水量")
    @TableField(value = "first_water",exist = true)
    private Double firstWater;
    /**
    * 
    */
    @ApiModelProperty("第二用水量")
    @TableField(value = "second_water",exist = true)
    private Double secondWater;
    /**
    * 
    */
    @ApiModelProperty("增加水量")
    @TableField(value = "add_number",exist = true)
    private Double addNumber;
    /**
    * 
    */
    @ApiModelProperty("加计划的方式：1-平均，2-最高")
    @TableField(value = "add_way",exist = true)
    private String addWay;
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

    @ApiModelProperty("创建类型(是否扣/奖创建)")
    @TableField(value = "create_type",exist = true)
    private String createType;
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
    @ApiModelProperty("是否确认")
    @TableField(value = "confirmed",exist = true)
    private String confirmed;
    /**
    * 
    */
    @ApiModelProperty("确认时间")
    @TableField(value = "confirm_time",exist = true)
    private Date confirmTime;
    /**
    * 
    */
    @ApiModelProperty("是否审核")
    @TableField(value = "audit_status",exist = true)
    private String auditStatus;
    /**
    * 
    */
    @ApiModelProperty("是否执行")
    @TableField(value = "executed",exist = true)
    private String executed;
    /**
    * 
    */
    @ApiModelProperty("执行时间")
    @TableField(value = "execute_time",exist = true)
    private Date executeTime;
    /**
    * 
    */
    @ApiModelProperty("执行人")
    @TableField(value = "executor",exist = true)
    private String executor;
    /**
    * 
    */
    @ApiModelProperty("执行人id")
    @TableField(value = "executor_id",exist = true)
    private String executorId;
    /**
    * 
    */
    @ApiModelProperty("审批申请附件id")
    @TableField(value = "audit_file_id",exist = true)
    private String auditFileId;
    /**
    * 
    */
    @ApiModelProperty("近2月水量凭证附件id")
    @TableField(value = "water_proof_file_id",exist = true)
    private String waterProofFileId;
    /**
    * 
    */
    @ApiModelProperty("其他证明材料")
    @TableField(value = "other_file_id",exist = true)
    private String otherFileId;
    /**
    * 
    */
    @ApiModelProperty("算法规则(是否在年计划上增加)")
    @TableField(value = "algorithm_rules",exist = true)
    private String algorithmRules;

    @ApiModelProperty("经办人名字")
    @TableField(value = "creater_name",exist = true)
    private String createrName;

    @ApiModelProperty("处理结果")
    @TableField(value = "result",exist = true)
    private String result;

    @ApiModelProperty("计划调整微信表Id")
    @TableField(value = "water_plan_WX_id",exist = true)
    private String waterPlanWXId;

    @ApiModelProperty("审核流程下一环节id")
    @TableField(value = "next_node_id",exist = true)
    private String nextNodeId;

	/**
	 * 实例化
	 */
	public EndPaper() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
