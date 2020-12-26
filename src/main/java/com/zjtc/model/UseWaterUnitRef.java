package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description 用水相关单位关联表
 */
@Data
@TableName(value = "t_w_use_water_unit_ref")
@Accessors(chain = true)
@ApiModel(value = "用水相关单位关联表", description = "用水相关单位关联表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterUnitRef extends Model<UseWaterUnitRef> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "父级单位编号")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "单位编号")
  @TableField(value = "use_water_unit_id_ref", exist = true)
  private String useWaterUnitIdRef;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "预留字段1")
  @TableField(value = "exp1", exist = true)
  private String exp1;

  @ApiModelProperty(value = "预留字段2")
  @TableField(value = "exp2", exist = true)
  private String exp2;

  @ApiModelProperty(value = "预留字段3")
  @TableField(value = "exp3", exist = true)
  private String exp3;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
