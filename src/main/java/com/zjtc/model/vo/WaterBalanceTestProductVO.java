package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author lianghao
 * @date 2021/02/25
 */
@ApiModel("水平衡测试产品表")
@Data
@EqualsAndHashCode(callSuper = true)
public class WaterBalanceTestProductVO extends Model<WaterBalanceTestProductVO>{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("节点编码")
    private String nodeCode;

    @ApiModelProperty("水平衡测试表id")
    private String balanceTestId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("单耗比对定额值")
    private Double diffToQuota;

    @ApiModelProperty("人均用水量")
    private Double perUseAmount;

    @ApiModelProperty("年计划用水量")
    private Double yearPlan;

    @ApiModelProperty("逻辑删除，1是0否")
    private String deleted;

    @ApiModelProperty("删除时间")
    private Date deleteTime;

    @ApiModelProperty("创建时间")
    private Date createTime;
	/**
	 * 实例化
	 */
	public WaterBalanceTestProductVO() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
