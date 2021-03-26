package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author lianghao
 * @date 2021/02/01
 */
@ApiModel("用水单位监控导出VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class UseWaterMonitorExportVO extends Model<UseWaterMonitorExportVO> {

  @ApiModelProperty("序号")
  private Integer rowNumber;

  @ApiModelProperty("单位编号")
  private String unitCode;

  @ApiModelProperty("单位名称")
  private String unitName;

  @ApiModelProperty("单位地址")
  private String unitAddress;

  @ApiModelProperty("行业名称")
  private String industryName;;

  @ApiModelProperty("年计划")
  private Double curYearPlan;

  @ApiModelProperty("年实际使用")
  private Double curYearTotal;

  @ApiModelProperty("年节超率")
  private String curYearRate;

  @ApiModelProperty("第一季度计划")
  private Double firstQuarterPlan;

  @ApiModelProperty("第一季度实际使用")
  private Double firstQuarterTotal;

  @ApiModelProperty("第一季度节超率")
  private String firstQuarterRate;

  @ApiModelProperty("第二季度计划")
  private Double secondQuarterPlan;

  @ApiModelProperty("第二季度实际使用")
  private Double secondQuarterTotal;

  @ApiModelProperty("第二季度节超率")
  private String secondQuarterRate;

  @ApiModelProperty("第三季度计划")
  private Double thirdQuarterPlan;

  @ApiModelProperty("第三季度实际使用")
  private Double thirdQuarterTotal;

  @ApiModelProperty("第三季度节超率")
  private String thirdQuarterRate;

  @ApiModelProperty("第四季度计划")
  private Double fourthQuarterPlan;

  @ApiModelProperty("第四季度实际使用")
  private Double fourthQuarterTotal;

  @ApiModelProperty("第四季度节超率")
  private String fourthQuarterRate;

  @ApiModelProperty("一月实际使用")
  private Double januaryCount;

  @ApiModelProperty("二月实际使用")
  private Double februaryCount;

  @ApiModelProperty("三月实际使用")
  private Double marchCount;

  @ApiModelProperty("四月实际使用")
  private Double aprilCount;

  @ApiModelProperty("五月实际使用")
  private Double mayCount;

  @ApiModelProperty("六月实际使用")
  private Double juneCount;

  @ApiModelProperty("七月实际使用")
  private Double julyCount;

  @ApiModelProperty("八月实际使用")
  private Double augustCount;

  @ApiModelProperty("九月实际使用")
  private Double septemberCount;

  @ApiModelProperty("十月实际使用")
  private Double octoberCount;

  @ApiModelProperty("十一月实际使用")
  private Double novemberCount;

  @ApiModelProperty("十二月实际使用")
  private Double decemberCount;


  /**
   * 实例化
   */
  public UseWaterMonitorExportVO() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

}
