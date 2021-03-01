package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * t_w_water_saving_unit_quota实体类
 *
 * @author
 */
@ApiModel("节水型单位定量考核指标表vo")

public class WaterSavingUnitQuotaVo extends Model<WaterSavingUnitQuotaVo> {

  /**
   *
   */
  @ApiModelProperty("id")
  private String id;
  /**
   *
   */
  @ApiModelProperty("指标")
  private String quotaIndex;
  /**
   *
   */
  @ApiModelProperty("考核计算方法")
  private String assessAlgorithm;
  /**
   *
   */
  @ApiModelProperty("考核标准")
  private String assessStandard;
  /**
   *
   */
  @ApiModelProperty("标准水平")
  private String standardLevel;
  /**
   *
   */
  @ApiModelProperty("企业分数")
  private Double companyScore;
  /**
   *
   */
  @ApiModelProperty("单位分数")
  private Double unitScore;
  /**
   *
   */
  @ApiModelProperty("自查分数")
  private Double checkScore;
  /**
   *
   */
  @ApiModelProperty("实际得分")
  private Double actualScore;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuotaIndex() {
    return quotaIndex;
  }

  public void setQuotaIndex(String quotaIndex) {
    this.quotaIndex = quotaIndex;
  }

  public String getAssessAlgorithm() {
    return assessAlgorithm;
  }

  public void setAssessAlgorithm(String assessAlgorithm) {
    this.assessAlgorithm = assessAlgorithm;
  }

  public String getAssessStandard() {
    return assessStandard;
  }

  public void setAssessStandard(String assessStandard) {
    this.assessStandard = assessStandard;
  }

  public String getStandardLevel() {
    return standardLevel;
  }

  public void setStandardLevel(String standardLevel) {
    this.standardLevel = standardLevel;
  }

  public Double getCompanyScore() {
    return companyScore;
  }

  public void setCompanyScore(Double companyScore) {
    this.companyScore = companyScore;
  }

  public Double getUnitScore() {
    return unitScore;
  }

  public void setUnitScore(Double unitScore) {
    this.unitScore = unitScore;
  }

  public Double getCheckScore() {
    return checkScore;
  }

  public void setCheckScore(Double checkScore) {
    this.checkScore = checkScore;
  }

  public Double getActualScore() {
    return actualScore;
  }

  public void setActualScore(Double actualScore) {
    this.actualScore = actualScore;
  }

  /**
   * 实例化
   */
  public WaterSavingUnitQuotaVo() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
