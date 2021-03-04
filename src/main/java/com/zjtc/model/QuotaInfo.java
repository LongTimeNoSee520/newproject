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
@TableName(value = "t_w_quota_info")
@Accessors(chain = true)
@ApiModel(value = "定额信息维护表", description = "定额信息维护表")
@EqualsAndHashCode(callSuper = true)
public class QuotaInfo extends Model<QuotaInfo> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("上级行业id")
  @TableField(value = "parent_id", exist = true)
  private String parentId;

  @ApiModelProperty(value = "行业名称")
  @TableField(value = "industry_name", exist = true)
  private String industryName;

  @ApiModelProperty(value = "行业/定额编码")
  @TableField(value = "industry_code", exist = true)
  private String industryCode;

  @ApiModelProperty(value = "定额单位")
  @TableField(value = "quota_unit", exist = true)
  private String quotaUnit;

  @ApiModelProperty(value = "定额标准单位值")
  @TableField(value = "quota_value", exist = true)
  private Float quotaValue;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "删除时间")
  @TableField(value = "delete_time", exist = true)
  private Date deleteTime;

  @ApiModelProperty(value = "备注")
  @TableField(value = "remark", exist = true)
  private String remark;

  @ApiModelProperty(value ="定额单位先进值")
  @TableField(value = "advance_value", exist = true)
  private Float advanceValue;

  @ApiModelProperty(value = "定额单位通用值")
  @TableField(value = "common_value", exist = true)
  private Float commonValue;

  @ApiModelProperty(value = "综合利用率")
  @TableField(value = "quota_rate", exist = true)
  private Float quotaRate;

  @ApiModelProperty(value = "时间参数，标准单位为天：365，标准单位为年：1")
  @TableField(value = "time_param", exist = true)
  private int timeParam;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}