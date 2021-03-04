package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * t_w_water_saving_unit实体类
 * 
 * @author 
 *
 */
@ApiModel("节水型单位管理表Vo")
public class WaterSavingUnitVo extends Model<WaterSavingUnitVo>{
    /**
    *
    */
    @ApiModelProperty("id")
    private String id;
    /**
    *
    */
    @ApiModelProperty("单位编号")
    private String unitCode;
    /**
    *
    */
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
    *
    */
    @ApiModelProperty("地址")
    private String address;
    /**
    *
    */
    @ApiModelProperty("法人")
    private String legalRepresentative;
    /**
    *
    */
    @ApiModelProperty("电话号码")
    private String phoneNumber;
    /**
    *
    */
    @ApiModelProperty("归口部门")
    private String centralizedDepartment;
    /**
    *
    */
    @ApiModelProperty("复查时间")
    private Date reviewTime;
    /**
    *
    */
    @ApiModelProperty("复查得分")
    private Double reviewScore;
    /**
    *
    */
    @ApiModelProperty("邮编")
    private String zipCode;
    /**
    *
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    *
    */
    @ApiModelProperty("创建得分")
    private Double createScore;
    /**
    *
    */
    @ApiModelProperty("工业增加值(万元)")
    private Double industrialAdded;
    /**
    *
    */
    @ApiModelProperty("总取水量(m3)")
    private Double totalWaterQuantity;
    /**
    *
    */
    @ApiModelProperty("万元工业增加值取水量(m3/万元)")
    private Double industrialAddedWater;
    /**
    *
    */
    @ApiModelProperty("装表率（%）")
    private Double zbRate;
    /**
    *
    */
    @ApiModelProperty("重复利用率（%）")
    private Double reuseRate;
    /**
    *
    */
    @ApiModelProperty("漏失率（%）")
    private Double leakageRale;
    /**
    *
    */
    @ApiModelProperty("备注")
    private String remarks;
    /**
     *
     */
    @ApiModelProperty("定量标准vo集合")
    @TableField(exist = false)
    private List<WaterSavingUnitQuotaVo> waterSavingUnitQuotaVoList;

    /**
     *
     */
    @ApiModelProperty("基础标准vo集合")
    @TableField(exist = false)
    private List<WaterSavingUnitBaseVo> waterSavingUnitBaseVoList;

	/**
	 * 实例化
	 */
	public WaterSavingUnitVo() {
		super();
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCentralizedDepartment() {
        return centralizedDepartment;
    }

    public void setCentralizedDepartment(String centralizedDepartment) {
        this.centralizedDepartment = centralizedDepartment;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(Double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getCreateScore() {
        return createScore;
    }

    public void setCreateScore(Double createScore) {
        this.createScore = createScore;
    }

    public Double getIndustrialAdded() {
        return industrialAdded;
    }

    public void setIndustrialAdded(Double industrialAdded) {
        this.industrialAdded = industrialAdded;
    }

    public Double getTotalWaterQuantity() {
        return totalWaterQuantity;
    }

    public void setTotalWaterQuantity(Double totalWaterQuantity) {
        this.totalWaterQuantity = totalWaterQuantity;
    }

    public Double getIndustrialAddedWater() {
        return industrialAddedWater;
    }

    public void setIndustrialAddedWater(Double industrialAddedWater) {
        this.industrialAddedWater = industrialAddedWater;
    }

    public Double getZbRate() {
        return zbRate;
    }

    public void setZbRate(Double zbRate) {
        this.zbRate = zbRate;
    }

    public Double getReuseRate() {
        return reuseRate;
    }

    public void setReuseRate(Double reuseRate) {
        this.reuseRate = reuseRate;
    }

    public Double getLeakageRale() {
        return leakageRale;
    }

    public void setLeakageRale(Double leakageRale) {
        this.leakageRale = leakageRale;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<WaterSavingUnitQuotaVo> getWaterSavingUnitQuotaVoList() {
        return waterSavingUnitQuotaVoList;
    }

    public void setWaterSavingUnitQuotaVoList(
        List<WaterSavingUnitQuotaVo> waterSavingUnitQuotaVoList) {
        this.waterSavingUnitQuotaVoList = waterSavingUnitQuotaVoList;
    }

    public List<WaterSavingUnitBaseVo> getWaterSavingUnitBaseVoList() {
        return waterSavingUnitBaseVoList;
    }

    public void setWaterSavingUnitBaseVoList(
        List<WaterSavingUnitBaseVo> waterSavingUnitBaseVoList) {
        this.waterSavingUnitBaseVoList = waterSavingUnitBaseVoList;
    }

    @Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
