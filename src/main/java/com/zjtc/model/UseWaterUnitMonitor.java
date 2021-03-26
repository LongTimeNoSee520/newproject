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
 * @author lianghao
 * @date 2021/01/15
 */
@ApiModel(value ="用水单位监控表", description = "用水单位监控表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_use_water_unit_monitor")
public class UseWaterUnitMonitor extends Model<UseWaterUnitMonitor>{

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
   
    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;
   
    @ApiModelProperty("用水单位id")
    @TableField(value = "use_water_unit_id",exist = true)
    private String useWaterUnitId;
   
    @ApiModelProperty("单位编号")
    @TableField(value = "unit_code",exist = true)
    private String unitCode;
   
    @ApiModelProperty("单位名称")
    @TableField(value = "unit_name",exist = true)
    private String unitName;
   
    @ApiModelProperty("单位地址")
    @TableField(value = "unit_address",exist = true)
    private String unitAddress;

    @ApiModelProperty("所属行业id")
    @TableField(value = "industry",exist = true)
    private String industry;

    @ApiModelProperty("所属行业名称")
    @TableField(value = "industry_name",exist = true)
    private String industryName;
   
    @ApiModelProperty("年份")
    @TableField(value = "year",exist = true)
    private Integer year;
   
    @ApiModelProperty("监控类型")
    @TableField(value = "monitor_type",exist = true)
    private String monitorType;
   
    @ApiModelProperty("是否逻辑删除，1是0否")
    @TableField(value = "deleted",exist = true)
    private String deleted;
   
    @ApiModelProperty("创建人id")
    @TableField(value = "create_person_id",exist = true)
    private String createPersonId;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("删除时间")
    @TableField(value = "delete_time",exist = true)
    private Date deleteTime;
   
    @ApiModelProperty("用户类型(查看时权限)")
    @TableField(value = "unit_code_type",exist = true)
    private String unitCodeType;
	/**
	 * 实例化
	 */
	public UseWaterUnitMonitor() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
