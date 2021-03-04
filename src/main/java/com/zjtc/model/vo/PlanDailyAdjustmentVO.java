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

@ApiModel("日常调整VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanDailyAdjustmentVO extends Model<PlanDailyAdjustmentVO>{

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("节点编码")
    private String nodeCode;

    @ApiModelProperty("单位id")
    private String useWaterUnitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位编号")
    private String unitCode;

    @ApiModelProperty("水表档案号")
    private String waterMeterCode;

    @ApiModelProperty("编制年度(计划年度)")
    private Integer planYear;

    @ApiModelProperty("扣加价")
    private String minusPayStatus;

    @ApiModelProperty("水平衡测试")
    private String balanceTest;

    @ApiModelProperty("创建")
    private String createType;

    @ApiModelProperty("本年计划（当前年计划）")
    private Double curYearPlan;

    @ApiModelProperty("第一季度计划(基础)")
    private Double firstQuarter;

    @ApiModelProperty("第二季度计划（基础）")
    private Double secondQuarter;

    @ApiModelProperty("第三季度计划（基础）")
    private Double thirdQuarter;

    @ApiModelProperty("第四季度计划（基础）")
    private Double fourthQuarter;

    @ApiModelProperty("计划类型")
    private String planType;

    @ApiModelProperty("调整时间")
    private Date updateTime;

    @ApiModelProperty("是否打印")
    private String printed;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("计划调整列表")
    List<Map<String,Object>> adjustList;

//    @ApiModelProperty("审批申请附件信息列表")
//    List<Map<String,Object>> auditFiles;
//
//    @ApiModelProperty("近2月水量凭证附件信息列表")
//    List<Map<String,Object>> waterProofFiles;
//
//    @ApiModelProperty("其他证明材料信息列表")
//    List<Map<String,Object>> otherFiles;

    @ApiModelProperty("未缴费信息列表")
    List<Map<String,Object>> unpaidList;

    @Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
