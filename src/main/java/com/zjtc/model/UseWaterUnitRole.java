package com.zjtc.model;

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
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Data
@TableName(value = "t_w_use_water_unit_role")
@Accessors(chain = true)
@ApiModel(value = "计划用水户权限表", description = "计划用水户权限表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterUnitRole extends Model<UseWaterUnitRole> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "人员id")
  @TableField(value = "person_id", exist = true)
  private String personId;

  @ApiModelProperty(value = "单位批次号")
  @TableField(value = "unit_type_code", exist = true)
  private String unitTypeCode;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "模块类型")
  @TableField(value = "module_type", exist = true)
  private String moduleType;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
