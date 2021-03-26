package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("短信发送列表信息VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SendListVO extends Model<SendListVO>{

    @ApiModelProperty("业务id")
    private String id;

    @ApiModelProperty("单位编号")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("电话号码")
    private String mobileNumber;

    @ApiModelProperty("接收人姓名")
    private String receiverName;

    @ApiModelProperty("年份")
    private Integer countYear;

    @ApiModelProperty("季度")
    private Integer countQuarter;

    @ApiModelProperty("超计划水量")
    private Double exceedWater;

    @ApiModelProperty("加价费")
    private Double increaseMoney;


    @ApiModelProperty("状态值名称")
    private String statusName;


	@Override
	protected Serializable pkVal() {
		return null;
	}
	
}
