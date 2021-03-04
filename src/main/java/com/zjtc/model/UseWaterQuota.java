package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @date 2020/12/23
 */
@Data
@TableName(value = "t_w_use_water_quota")
@Accessors(chain = true)
@ApiModel(value = "用水定额信息关联表", description = "用水定额信息关联表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterQuota extends Model<UseWaterQuota> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "所属行业id")
  @TableField(value = "industry", exist = true)
  private String industry;

  @ApiModelProperty(value = "所属行业名称")
  @TableField(value = "industry_name", exist = false)
  private String industry_name;

  @ApiModelProperty(value = "行业id")
  @TableField(value = "sub_industry", exist = true)
  private String subIndustry;

  @ApiModelProperty(value = "行业名称")
  @TableField(value = "sub_industry_name", exist = true)
  private String subIndustryName;

  @ApiModelProperty(value = "产品id")
  @TableField(value = "product", exist = true)
  private String product;

  @ApiModelProperty(value = "产品id")
  @TableField(value = "product_name", exist = true)
  private String productName;

  @ApiModelProperty(value = "定额单位")
  @TableField(value = "quota_unit", exist = true)
  private String quotaUnit;

  @ApiModelProperty(value = "定额值")
  @TableField(value = "quota_value", exist = true)
  private Float quotaValue;

  @ApiModelProperty(value = "数量")
  @TableField(value = "amount", exist = true)
  private Float amount;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;
  @Override
  protected Serializable pkVal() {
    return null;
  }
}