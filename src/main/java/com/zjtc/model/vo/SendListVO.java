package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("短信发送列表信息VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SendListVO extends Model<SendListVO>{

    @ApiModelProperty("单位编号")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("电话号码")
    private String mobileNumber;

    @ApiModelProperty("接收人姓名")
    private String contacts;

    @ApiModelProperty("年份")
    private Integer countYear;

    @ApiModelProperty("季度")
    private Integer countQuarter;

    @ApiModelProperty("超计划水量")
    private Double exceedWater;

    @ApiModelProperty("加价费")
    private Double increaseMoney;




	@Override
	protected Serializable pkVal() {
		return null;
	}
	
}
