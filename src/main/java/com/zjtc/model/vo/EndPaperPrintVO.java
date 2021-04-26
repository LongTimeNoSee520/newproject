package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjtc.model.FlowProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("办结单打印数据VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class EndPaperPrintVO extends Model<EndPaperPrintVO>{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("节点编码")
    private String nodeCode;

    @ApiModelProperty("单位id")
    private String useWaterUnitId;

    @ApiModelProperty("单位编号")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("水表档案号")
    private String waterMeterCode;

    @ApiModelProperty("办结单类型code")
    private String paperType;

    @ApiModelProperty("办结单类型名称")
    private String paperTypeName;


    @ApiModelProperty("调整年份")
    private Integer planYear;

    @ApiModelProperty("调整季度")
    private String changeQuarter;

    @ApiModelProperty("年计划")
    private Double curYearPlan;

    @ApiModelProperty("第一用水量")
    private Double firstWater;

    @ApiModelProperty("第二用水量")
    private Double secondWater;

    @ApiModelProperty("增加水量")
    private Double addNumber;

    @ApiModelProperty("第一季度计划")
    private Double firstQuarter;

    @ApiModelProperty("第二季度计划")
    private Double secondQuarter;

    @ApiModelProperty("第三季度计划")
    private Double thirdQuarter;

    @ApiModelProperty("第四季度计划")
    private Double fourthQuarter;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("执行时间")
    private Date executeTime;

    @ApiModelProperty("审核流程信息")
    List<FlowProcess> auditMessages;

    @ApiModelProperty("打印时的当季计划")
    private Double quarterNum;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
