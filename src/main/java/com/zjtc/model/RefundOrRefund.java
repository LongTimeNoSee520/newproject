package com.zjtc.model;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
/**
 * t_w_refund_or_refund实体类
 * 
 * @author 
 *
 */
@ApiModel("退减免单")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_refund_or_refund")
public class RefundOrRefund extends Model<RefundOrRefund>{
    /**
    * 
    */
    @ApiModelProperty("id")
    @TableId(value = "id",type=IdType.UUID)
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
    @ApiModelProperty("单位id")
    @TableField(value = "use_water_unit_id",exist = true)
    private String useWaterUnitId;
    /**
    * 
    */
    @ApiModelProperty("单位名称")
    @TableField(value = "unit_name",exist = true)
    private String unitName;
    /**
    * 
    */
    @ApiModelProperty("单位编号")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;
    /**
    * 
    */
    @ApiModelProperty("水表档案号")
    @TableField(value = "water_meter_code",exist = true)
    private String waterMeterCode;
    /**
    * 
    */
    @ApiModelProperty("缴费id")
    @TableField(value = "pay_id",exist = true)
    private String payId;
    /**
    * 
    */
    @ApiModelProperty("单据类型")
    @TableField(value = "type",exist = true)
    private String type;
    /**
    * 
    */
    @ApiModelProperty("退款/减免季度")
    @TableField(value = "quarter",exist = true)
    private String quarter;
    /**
    * 
    */
    @ApiModelProperty("退款/减免年度")
    @TableField(value = "year",exist = true)
    private Integer year;
    /**
    * 
    */
    @ApiModelProperty("实收金额")
    @TableField(value = "actual_amount",exist = true)
    private Double actualAmount;
    /**
    * 
    */
    @ApiModelProperty("退款/减免金额")
    @TableField(value = "money",exist = true)
    private Double money;
    /**
    * 
    */
    @ApiModelProperty("退款/减免原因")
    @TableField(value = "reason",exist = true)
    private String reason;
    /**
    * 
    */
    @ApiModelProperty("经办人")
    @TableField(value = "drawer",exist = true)
    private String drawer;
    /**
    * 
    */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private java.time.Instant createTime;
    /**
    * 
    */
    @ApiModelProperty("审核人")
    @TableField(value = "audit_person",exist = true)
    private String auditPerson;
    /**
    * 
    */
    @ApiModelProperty("审核时间")
    @TableField(value = "audit_time",exist = true)
    private java.time.Instant auditTime;
    /**
    * 
    */
    @ApiModelProperty("状态,0:初始状态")
    @TableField(value = "status",exist = true)
    private String status;
	/**
	 * 实例化
	 */
	public RefundOrRefund() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
