package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * t_w_water_saving_unit_base实体类
 * 
 * @author 
 *
 */
@ApiModel("节水型单位基础管理考核标准表vo")
public class WaterSavingUnitBaseVo extends Model<WaterSavingUnitBaseVo>{
    /**
    * 
    */
    @ApiModelProperty("id")
    private String id;
    /**
    * 
    */
    @ApiModelProperty("基础管理考核内容")
    private String contents;
    /**
    * 
    */
    @ApiModelProperty("考核方法")
    private String assessMethod;
    /**
    * 
    */
    @ApiModelProperty("考核标准")
    private String assessStandard;
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

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public String getAssessMethod() {
    return assessMethod;
  }

  public void setAssessMethod(String assessMethod) {
    this.assessMethod = assessMethod;
  }

  public String getAssessStandard() {
    return assessStandard;
  }

  public void setAssessStandard(String assessStandard) {
    this.assessStandard = assessStandard;
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
	public WaterSavingUnitBaseVo() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
