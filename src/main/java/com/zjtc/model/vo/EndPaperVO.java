package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel("办结单")
@Data
@EqualsAndHashCode(callSuper = true)
public class EndPaperVO extends Model<EndPaperVO>{

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

    @ApiModelProperty("数据来源")
    private String dataSources;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("经办人（创建人id）")
    private String createrId;

    @ApiModelProperty("经办人（创建人名称）")
    private String createrName;

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

    @ApiModelProperty("加计划的方式：1-平均，2-最高")
    private String addWay;

//    @ApiModelProperty("是否在年计划上增加")
//    private String year;

    @ApiModelProperty("扣加价")
    private String minusPayStatus;

    @ApiModelProperty("水平衡测试(是否扣/奖水平衡)")
    private String balanceTest;

    @ApiModelProperty("创建类型(是否扣/奖创建)")
    private String createType;

    @ApiModelProperty("第一季度计划")
    private Double firstQuarter;

    @ApiModelProperty("第二季度计划")
    private Double secondQuarter;

    @ApiModelProperty("第三季度计划")
    private Double thirdQuarter;

    @ApiModelProperty("第四季度计划")
    private Double fourthQuarter;

    @ApiModelProperty("是否确认")
    private String confirmed;

//    @ApiModelProperty("确认时间")
//    private Date confirmTime;

    @ApiModelProperty("是否审核")
    private String auditStatus;

    @ApiModelProperty("是否执行")
    private String executed;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("执行时间")
    private Date executeTime;

    @ApiModelProperty("执行人")
    private String executor;

    @ApiModelProperty("执行人id")
    private String executorId;

    @ApiModelProperty("算法规则(是否在年计划上增加)")
    private String algorithmRules;

    @ApiModelProperty("处理结果")
    private String result;
    @ApiModelProperty("审核下一流程id")
    private String nextNodeId;

    @ApiModelProperty("审批申请附件(调整申请表等)信息列表")
    List<FileVO> auditFiles;

    @ApiModelProperty("近2月水量凭证附件(水费发票)信息列表")
    List<FileVO> waterProofFiles;

    @ApiModelProperty("其他证明材料信息列表")
    List<FileVO> otherFiles;

    @ApiModelProperty("预览路径")
    private String preViewRealPath;

//    @ApiModelProperty("未缴费情况")
//    List<Map<String,Object>> unpaidList;

    @ApiModelProperty("审核流程信息")
    List<Map<String,Object>> auditMessages;

    @ApiModelProperty("能否执行")
    private boolean canExecute;

    @ApiModelProperty("是否需要审核")
    private boolean needAudit;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
