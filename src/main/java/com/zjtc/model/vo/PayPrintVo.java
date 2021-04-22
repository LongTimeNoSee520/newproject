package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("办结单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayPrintVo extends Model<PayPrintVo> {


  @ApiModelProperty("单位编号")
  private String unitCode;

  @ApiModelProperty("单位名称")
  private String unitName;

  @ApiModelProperty("水价")
  private Double price;

  @ApiModelProperty("年计划")
  private Double yearPlan;

  @ApiModelProperty("季计划")
  private Double quarterPlan;

  @ApiModelProperty("基建计划")
  private Double basePlan;

  @ApiModelProperty("水量1")
  private Double waterNum1;

  @ApiModelProperty("水量2")
  private Double waterNum2;

  @ApiModelProperty("水量3")
  private Double waterNum3;

  @ApiModelProperty("总水量")
  private Double waterNumAmount;

  @ApiModelProperty("超用水量")
  private Double exceedWater;

  @ApiModelProperty("比例")
  private Double payRatio;

  @ApiModelProperty("倍数")
  private Double multiple;

  @ApiModelProperty("应收金额")
  private Double amountReceivable;

  @ApiModelProperty("备注")
  private String remarks;

  @ApiModelProperty("是否托收")
  private String isSigning;

  @ApiModelProperty("是否签责")
  private String responsibilityCode;

  @ApiModelProperty("开户行")
  private String bankOfDeposit;

  @ApiModelProperty("银行账号")
  private String bankAccount;


  @Override
  protected Serializable pkVal() {
    return null;
  }

}
