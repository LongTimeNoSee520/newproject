package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_water_saving_unit实体类
 * 
 * @author 
 *
 */
@ApiModel("节水型单位管理表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_saving_unit")
public class WaterSavingUnit extends Model<WaterSavingUnit>{
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
    @ApiModelProperty("单位编号")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;
    /**
    * 
    */
    @ApiModelProperty("单位名称")
    @TableField(value = "unit_name",exist = true)
    private String unitName;
    /**
    * 
    */
    @ApiModelProperty("地址")
    @TableField(value = "address",exist = true)
    private String address;
    /**
    * 
    */
    @ApiModelProperty("法人")
    @TableField(value = "legal_representative",exist = true)
    private String legalRepresentative;
    /**
    * 
    */
    @ApiModelProperty("电话号码")
    @TableField(value = "phone_number",exist = true)
    private String phoneNumber;
    /**
    * 
    */
    @ApiModelProperty("归口部门")
    @TableField(value = "centralized_department",exist = true)
    private String centralizedDepartment;
    /**
    * 
    */
    @ApiModelProperty("复查时间")
    @TableField(value = "review_time",exist = true)
    private Date reviewTime;
    /**
    * 
    */
    @ApiModelProperty("复查得分")
    @TableField(value = "review_score",exist = true)
    private Double reviewScore;
    /**
    * 
    */
    @ApiModelProperty("邮编")
    @TableField(value = "zip_code",exist = true)
    private String zipCode;
    /**
    * 
    */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true)
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("创建得分")
    @TableField(value = "create_score",exist = true)
    private Double createScore;
    /**
    * 
    */
    @ApiModelProperty("工业增加值(万元)")
    @TableField(value = "industrial_added",exist = true)
    private Double industrialAdded;
    /**
    * 
    */
    @ApiModelProperty("总取水量(m3)")
    @TableField(value = "total_water_quantity",exist = true)
    private Double totalWaterQuantity;
    /**
    * 
    */
    @ApiModelProperty("万元工业增加值取水量(m3/万元)")
    @TableField(value = "industrial_added_water",exist = true)
    private Double industrialAddedWater;
    /**
    * 
    */
    @ApiModelProperty("装表率（%）")
    @TableField(value = "zb_rate",exist = true)
    private Double zbRate;
    /**
    * 
    */
    @ApiModelProperty("重复利用率（%）")
    @TableField(value = "reuse_rate",exist = true)
    private Double reuseRate;
    /**
    * 
    */
    @ApiModelProperty("漏失率（%）")
    @TableField(value = "leakage_rale",exist = true)
    private Double leakageRale;
    /**
    * 
    */
    @ApiModelProperty("备注")
    @TableField(value = "remarks",exist = true)
    private String remarks;
    /**
     *
     */
    @ApiModelProperty("是否删除")
    @TableField(value = "deleted",exist = true)
    private String deleted;
    /**
     *
     */
    @ApiModelProperty("附件集合")
    @TableField(exist = false)
    private List<File> sysFiles;

    /**
     *
     */
    @ApiModelProperty("定量标准集合")
    @TableField(exist = false)
    private List<WaterSavingUnitQuota> waterSavingUnitQuotaList;

    /**
     *
     */
    @ApiModelProperty("基础标准集合")
    @TableField(exist = false)
    private List<WaterSavingUnitBase> waterSavingUnitBaseList;
	/**
	 * 实例化
	 */
	public WaterSavingUnit() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
