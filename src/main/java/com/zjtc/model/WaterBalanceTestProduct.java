package com.zjtc.model;

import com.baomidou.mybatisplus.annotations.TableId;
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
import lombok.experimental.Accessors;
import java.io.Serializable;
/**
 * @author lianghao
 * @date 2021/02/25
 */
@ApiModel("水平衡测试产品表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_balance_test_product")
public class WaterBalanceTestProduct extends Model<WaterBalanceTestProduct>{

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;

    @ApiModelProperty("水平衡测试表id")
    @TableField(value = "balance_test_id",exist = true)
    private String balanceTestId;

    @ApiModelProperty("产品名称")
    @TableField(value = "product_name",exist = true)
    private String productName;

    @ApiModelProperty("产品编码")
    @TableField(value = "product_code",exist = true)
    private String productCode;

    @ApiModelProperty("单耗比对定额值")
    @TableField(value = "diff_to_quota",exist = true)
    private Double diffToQuota;

    @ApiModelProperty("人均用水量")
    @TableField(value = "per_use_amount",exist = true)
    private Double perUseAmount;

    @ApiModelProperty("年计划用水量")
    @TableField(value = "year_plan",exist = true)
    private Double yearPlan;

    @ApiModelProperty("逻辑删除，1是0否")
    @TableField(value = "deleted",exist = true)
    private String deleted;

    @ApiModelProperty("删除时间")
    @TableField(value = "delete_time",exist = true)
    private Date deleteTime;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true)
    private Date createTime;
	/**
	 * 实例化
	 */
	public WaterBalanceTestProduct() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
