package com.zjtc.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author lianghao
 * @date 2021/02/25
 */
@ApiModel("水平衡测试")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_balance_test")
public class WaterBalanceTest extends Model<WaterBalanceTest>{

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;

    @ApiModelProperty("单位编码")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;

    @ApiModelProperty("单位名称")
    @TableField(value = "unit_name",exist = true)
    private String unitName;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @ApiModelProperty("单位地址")
    @TableField(value = "unit_address",exist = true)
    private String unitAddress;

    @ApiModelProperty("所属行业名称")
    @TableField(value = "industry_name",exist = true)
    private String industryName;

    @ApiModelProperty("所属行业编码")
    @TableField(value = "industry_code",exist = true)
    private String industryCode;

    @ApiModelProperty("单位占地面积")
    @TableField(value = "cover_space",exist = true)
    private Double coverSpace;

    @ApiModelProperty("单位建筑面积")
    @TableField(value = "floor_space",exist = true)
    private Double floorSpace;

    @ApiModelProperty("单位用水人数")
    @TableField(value = "use_people_num",exist = true)
    private Integer usePeopleNum;

    @ApiModelProperty("单位年用水量")
    @TableField(value = "year_amount",exist = true)
    private Double yearAmount;

    @ApiModelProperty("上次测试时间")
    @TableField(value = "last_test_time",exist = true)
    private Date lastTestTime;

    @ApiModelProperty("合格证编号")
    @TableField(value = "certificate_no",exist = true)
    private String certificateNo;

    @ApiModelProperty("管网漏损率")
    @TableField(value = "leakage_rate",exist = true)
    private Double leakageRate;

    @ApiModelProperty("逻辑删除，1是0否")
    @TableField(value = "deleted",exist = true)
    private String deleted;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("删除时间")
    @TableField(value = "delete_time",exist = true)
    private Date deleteTime;

    @ApiModelProperty("创建人id")
    @TableField(value = "create_person_id",exist = true)
    private String createPersonId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;
	/**
	 * 实例化
	 */
	public WaterBalanceTest() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
