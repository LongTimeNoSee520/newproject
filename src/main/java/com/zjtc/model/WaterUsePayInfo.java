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
 * t_w_water_use_pay_info实体类
 * 
 * @author 
 *
 */
@ApiModel("水使用量季度缴费表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_use_pay_info")
public class WaterUsePayInfo extends Model<WaterUsePayInfo>{
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
    @ApiModelProperty("付款方式，默认0，2现金,3转账")
    @TableField(value = "pay_type",exist = true)
    private String payType;
    /**
    * 
    */
    @ApiModelProperty("发票号")
    @TableField(value = "invoice_num",exist = true)
    private String invoiceNum;

    @ApiModelProperty("发票id")
    @TableField(value = "invoice_id",exist =false)
    private String invoiceId;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("开票日期")
    @TableField(value = "invoice_print_time",exist = true)
    private Date invoicePrintTime;
    /**
    * 
    */
    @ApiModelProperty("缴费季度")
    @TableField(value = "count_quarter",exist = true)
    private String countQuarter;
    /**
    * 
    */
    @ApiModelProperty("缴费年度")
    @TableField(value = "count_year",exist = true)
    private Integer countYear;
    /**
    * 年计划=年基建计划+年用水计划
    */
    @ApiModelProperty("年计划")
    @TableField(value = "year_plan",exist = true)
    private Double yearPlan;
    /**
    * 
    */
    @ApiModelProperty("季计划")
    @TableField(value = "quarter_plan",exist = true)
    private Double quarterPlan;
    /**
    *一期没有这个字段，分页带出来的，这里也不存
    */
    @ApiModelProperty("基建计划")
    @TableField(value = "base_plan",exist = true)
    private Double basePlan;
    /**
    * 
    */
    @ApiModelProperty("水量1")
    @TableField(value = "water_num1",exist = true)
    private Double waterNum1;
    /**
    * 
    */
    @ApiModelProperty("水量2")
    @TableField(value = "water_num2",exist = true)
    private Double waterNum2;
    /**
    * 
    */
    @ApiModelProperty("水量3")
    @TableField(value = "water_num3",exist = true)
    private Double waterNum3;
    /**
    * 
    */
    @ApiModelProperty("总水量(使用)")
    @TableField(value = "water_num_amount",exist = true)
    private Double waterNumAmount;
    /**
    * 
    */
    @ApiModelProperty("收费比例")
    @TableField(value = "pay_ratio",exist = true)
    private Double payRatio;
    /**
    * 
    */
    @ApiModelProperty("水价")
    @TableField(value = "price",exist = true)
    private Float price;
    /**
    * 
    */
    @ApiModelProperty("超计划水量")
    @TableField(value = "exceed_water",exist = true)
    private Double exceedWater;
    /**
    * 
    */
    @ApiModelProperty("应收倍数")
    @TableField(value = "multiple",exist = true)
    private Double multiple;
    /**
    * 
    */
    @ApiModelProperty("实收倍数")
    @TableField(value = "actual_multiple",exist = true)
    private Double actualMultiple;
    /**
    * 
    */
    @ApiModelProperty("应收金额")
    @TableField(value = "amount_receivable",exist = true)
    private Double amountReceivable;
    /**
    * 
    */
    @ApiModelProperty("实收金额")
    @TableField(value = "actual_amount",exist = true)
    private Double actualAmount;
    /**
    * 页面勾选了托收已缴费后，缴费状态改为1
     * 勾选现金、转账复核后，改为状态改为5，反之0
    */
    @ApiModelProperty("缴费状态,// 0未缴费 1托收已缴费  5非托收已缴费")
    @TableField(value = "pay_status",exist = true)
    private String payStatus;
    /**
    * 
    */
    @ApiModelProperty("是否打印,0否，1是")
    @TableField(value = "printed",exist = true)
    private String printed;
    /**
    * 
    */
    @ApiModelProperty("是否预警")
    @TableField(value = "warned",exist = true)
    private String warned;
    /**
    * 
    */
    @ApiModelProperty("备注")
    @TableField(value = "remarks",exist = true)
    private String remarks;
    /**
    * 
    */
    @ApiModelProperty("审核状态")
    @TableField(value = "audit_status",exist = true)
    private String auditStatus;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("审核时间")
    @TableField(value = "audit_time",exist = true)
    private Date auditTime;
    /**
    * 
    */
    @ApiModelProperty("审核人")
    @TableField(value = "audit_person",exist = true)
    private String auditPerson;
    /**
    * 
    */
    @ApiModelProperty("审核人id")
    @TableField(value = "audit_person_id",exist = true)
    private String auditPersonId;
    /**
    * 
    */
    @ApiModelProperty("是否修改过实收，0否，1是")
    @TableField(value = "edited_actual",exist = true)
    private String editedActual;
    /**
     *
     */
    @ApiModelProperty("加价费")
    @TableField(value = "increase_money", exist = true)
    private Double increaseMoney;
    /**
    * 
    */
    @ApiModelProperty("是否需要托收，0否，1是")
    @TableField(value = "entrusted",exist = true)
    private String entrusted;
    /**
    * 
    */
    @ApiModelProperty("现金财务复核，0否，1是")
    @TableField(value = "cash_check",exist = true)
    private String cashCheck;
    /**
    * 
    */
    @ApiModelProperty("转账财务复核，0否 ，1是")
    @TableField(value = "transfer_check",exist = true)
    private String transferCheck;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;



    @ApiModelProperty("缴费时间")
    @TableField(exist = false)
    private String countDate;

    @ApiModelProperty("是否托收")
    @TableField(exist = false)
    private String isSigning;

    @ApiModelProperty("银行代码")
    @TableField(exist = false)
    private String otherBank;

	/**
	 * 实例化
	 */
	public WaterUsePayInfo() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
