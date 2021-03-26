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
@TableName(value = "t_w_use_water_unit_modify")
@Accessors(chain = true)
@ApiModel(value = "用水单位名称修改日志", description = "用水单位名称修改日志")
@EqualsAndHashCode(callSuper = true)
public class UseWaterUnitModify extends Model<UseWaterUnitModify> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "修改前名称")
  @TableField(value = "before_name", exist = true)
  private String beforeName;

  @ApiModelProperty(value = "修改后名称")
  @TableField(value = "after_name", exist = true)
  private String afterName;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "修改时间")
  @TableField(value = "modify_time", exist = true)
  private Date modifyTime;

  @ApiModelProperty(value = "修改人")
  @TableField(value = "modify_person", exist = true)
  private String modifyPerson;

  @ApiModelProperty(value = "修改人id")
  @TableField(value = "modify_person_id", exist = true)
  private String modifyPersonId;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
