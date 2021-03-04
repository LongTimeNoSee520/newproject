package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_w_water_use_pay_info实体类
 * 
 * @author 
 *
 */
@ApiModel("水使用量季度缴费表Vo")
@Data
public class WaterUsePayInfoVo extends Model<WaterUsePayInfoVo>{
    /**
    * 
    */
    @ApiModelProperty("id")
    private String id;
    /**
    * 
    */
    @ApiModelProperty("单位id")
    private String useWaterUnitId;
    /**
    * 
    */
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
    * 
    */
    @ApiModelProperty("单位编号")
    private String unitCode;
    /**
    * 
    */
    @ApiModelProperty("付款方式，默认0，2现金,3转账")
    private String payType;
    /**
    * 
    */
    @ApiModelProperty("发票号")
    private String invoiceNum;

    @ApiModelProperty("发票id")
    private String invoiceId;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开票日期")
    private Date invoicePrintTime;
    /**
    * 
    */
    @ApiModelProperty("缴费季度")
    private String countQuarter;
    /**
    * 
    */
    @ApiModelProperty("缴费年度")
    private Integer countYear;
    /**
    * 年计划=年基建计划+年用水计划
    */
    @ApiModelProperty("年计划")
    private Double yearPlan;
    /**
    * 
    */
    @ApiModelProperty("季计划")
    private Double quarterPlan;
    /**
    *一期没有这个字段，分页带出来的，这里也不存
    */
    @ApiModelProperty("基建计划")
    private Double basePlan;
    /**
    * 
    */
    @ApiModelProperty("水量1")
    private Double waterNum1;
    /**
    * 
    */
    @ApiModelProperty("水量2")
    private Double waterNum2;
    /**
    * 
    */
    @ApiModelProperty("水量3")
    private Double waterNum3;
    /**
    * 
    */
    @ApiModelProperty("总水量(使用)")
    private Double waterNumAmount;
    /**
    * 
    */
    @ApiModelProperty("收费比例")
    private Double payRatio;
    /**
    * 
    */
    @ApiModelProperty("水价")
    private Float price;
    /**
    * 
    */
    @ApiModelProperty("超计划水量")
    private Double exceedWater;
    /**
    * 
    */
    @ApiModelProperty("应收倍数")
    private Double multiple;
    /**
    * 
    */
    @ApiModelProperty("实收倍数")
    private Double actualMultiple;
    /**
    * 
    */
    @ApiModelProperty("应收金额")
    private Double amountReceivable;
    /**
    * 
    */
    @ApiModelProperty("实收金额")
    private Double actualAmount;
    /**
    * 页面勾选了托收已缴费后，缴费状态改为1
     * 勾选现金、转账复核后，改为状态改为5，反之0
    */
    @ApiModelProperty("缴费状态,// 0未缴费 1托收已缴费  5非托收已缴费")
    private String payStatus;

    @ApiModelProperty("")
    private String payStatusName;
    /**
    * 
    */
    @ApiModelProperty("是否打印,0否，1是")
    private String printed;
    /**
    * 
    */
    @ApiModelProperty("是否预警")
    private String warned;
    /**
    * 
    */
    @ApiModelProperty("备注")
    private String remarks;
    /**
    * 
    */
    @ApiModelProperty("审核人")
    private String auditPerson;
    /**
    * 
    */
    @ApiModelProperty("审核人id")
    private String auditPersonId;
    /**
    * 
    */
    @ApiModelProperty("是否修改过实收，0否，1是")
    private String editedActual;
    /**
     *
     */
    @ApiModelProperty("加价费")
    private String increaseMoney;
    /**
    * 
    */
    @ApiModelProperty("是否需要托收，0否，1是")
    private String entrusted;
    /**
    * 
    */
    @ApiModelProperty("现金财务复核，0否，1是")
    private String cashCheck;
    /**
    * 
    */
    @ApiModelProperty("转账财务复核，0否 ，1是")
    private String transferCheck;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("银行代码")
    private String otherBank;

    @ApiModelProperty("责任书")
    private String responsibilityCode;

    @ApiModelProperty("水表档案号")
    private String waterMeterCode;

    @ApiModelProperty("开户行名称")
    private String bankOfDeposit;

    @ApiModelProperty("银行账户")
    private String bankAccount;

    @ApiModelProperty("基建季计划")
    private String baseQuarterPlan;

    @ApiModelProperty("是否签约")
    private String signed;
    /**
	 * 实例化
	 */
	public WaterUsePayInfoVo() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
