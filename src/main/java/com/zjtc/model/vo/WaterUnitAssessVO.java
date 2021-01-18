package com.zjtc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/18
 */
@ApiModel("用水单位考核VO")
@Data
public class WaterUnitAssessVO {

  @ApiModelProperty("主键")
  private String id;

  @ApiModelProperty("节点编码")
  private String nodeCode;

  @ApiModelProperty("单位编码")
  private String unitCode;

  @ApiModelProperty("单位名称")
  private String unitName;

  @ApiModelProperty("年度")
  private Integer planYear;

  @ApiModelProperty("本年计划")
  private Double curYearPlan;

  @ApiModelProperty("年实际水量")
  private Double yearPracticalWater;

  @ApiModelProperty("水表档案号")
  private String waterMeterCode;

  @ApiModelProperty("第一季度基建计划水量")
  private Double oneQuarter;

  @ApiModelProperty("第二季度基建计划")
  private Double twoQuarter;

  @ApiModelProperty("第三季度基建计划")
  private Double threeQuarter;

  @ApiModelProperty("第四季度基建计划")
  private Double fourQuarter;

  @ApiModelProperty("第一季度计划水量")
  private Double oneFirstQuarter;

  @ApiModelProperty("第二季度计划水量")
  private Double secondQuarter;

  @ApiModelProperty("第三季度计划水量")
  private Double thirdQuarter;

  @ApiModelProperty("第四季度计划水量")
  private Double fourthQuarter;

  @ApiModelProperty("第一季度实际水量")
  private Double onePracticalWater;

  @ApiModelProperty("第二季度实际水量")
  private Double twoPracticalWater;

  @ApiModelProperty("第三季度实际水量")
  private Double threePracticalWater;

  @ApiModelProperty("第四季度实际水量")
  private Double fourPracticalWater;

  @ApiModelProperty("第一季度超节量")
  private Double oneExceedWater;

  @ApiModelProperty("第二季度超节量")
  private Double twoExceedWater;

  @ApiModelProperty("第三季度超节量")
  private Double threeExceedWater;

  @ApiModelProperty("第四季度超节量")
  private Double fourExceedWater;

  @ApiModelProperty("第一季度回收情况")
  private String oneStatus;

  @ApiModelProperty("第二季度回收情况")
  private String twoStatus;

  @ApiModelProperty("第三季度回收情况")
  private String threeStatus;

  @ApiModelProperty("第四季度回收情况")
  private String fourStatus;






}
