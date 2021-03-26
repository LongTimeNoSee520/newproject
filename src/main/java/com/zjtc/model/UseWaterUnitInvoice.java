package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_use_water_unit_invoice实体类
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@ApiModel("计划用水户发票表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_unit_invoice")
public class UseWaterUnitInvoice extends Model<UseWaterUnitInvoice>{
    /**
    * 
    */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
    * 
    */
    @ApiModelProperty("缴费记录id")
    @TableField(value = "pay_info_id",exist = true)
    private String payInfoId;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("开票日期")
    @TableField(value = "invoice_date",exist = true)
    private Date invoiceDate;
    /**
    * 
    */
    @ApiModelProperty("发票号")
    @TableField(value = "invoice_number",exist = true)
    private String invoiceNumber;
    /**
    * 
    */
    @ApiModelProperty("发票金额")
    @TableField(value = "invoice_money",exist = true)
    private Double invoiceMoney;
    /**
    * 
    */
    @ApiModelProperty("发票单位名称")
    @TableField(value = "invoice_unit_name",exist = true)
    private String invoiceUnitName;
    /**
    * 
    */
    @ApiModelProperty("是否作废")
    @TableField(value = "enabled",exist = true)
    private String enabled;
    /**
    * 
    */
    @ApiModelProperty("经手人")
    @TableField(value = "drawer",exist = true)
    private String drawer;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("发票类型,0污水处理费1水资源费")
    @TableField(value = "invoice_type",exist = true)
    private String invoiceType;
    /**
    * 
    */
    @ApiModelProperty("发票单位编号")
    @TableField(value = "invoice_unit_code",exist = true)
    private String invoiceUnitCode;
    /**
    * 
    */
    @ApiModelProperty("是否打印")
    @TableField(value = "printed",exist = true)
    private String printed;
    /**
    * 
    */
    @ApiModelProperty("所属人员id")
    @TableField(value = "person",exist = true)
    private String person;
    /**
    * 
    */
    @ApiModelProperty("是否领取")
    @TableField(value = "received",exist = true)
    private String received;
    /**
    * 
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("领取时间")
    @TableField(value = "receive_time",exist = true)
    private Date receiveTime;
    /**
    * 
    */
    @ApiModelProperty("备注")
    @TableField(value = "remarks",exist = true)
    private String remarks;
    /**
    * 
    */
    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;

  @ApiModelProperty("是否删除")
  @TableField(value = "deleted",exist = true)
  private String deleted;

  @ApiModelProperty("序号")
  @TableField(value = "rownumber",exist = false)
  private String rownumber;

  @ApiModelProperty("单位编码")
  @TableField(value = "unitCode",exist = false)
  private  String unitCode;

  @ApiModelProperty("统计季度")
  @TableField(value = "countQuarter",exist = false)
  private  String countQuarter;

  @ApiModelProperty("统计年份")
  @TableField(value = "countYear",exist = false)
  private  Integer countYear;

  @ApiModelProperty("实际金额")
  @TableField(value = "actualAmount",exist = false)
  private  double actualAmount;


  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("开票日期")
  @TableField(value = "invoiceTime",exist = false)
  private String invoiceTime;
	/**
	 * 实例化
	 */
	public UseWaterUnitInvoice() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
