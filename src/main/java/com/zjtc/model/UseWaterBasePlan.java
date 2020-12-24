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
 * @author lianghao
 * @date 2020/12/24
 */
@Data
@TableName(value = "t_w_use_water_base_plan")
@Accessors(chain = true)
@ApiModel(value = "用水基建计划", description = "用水基建计划")
@EqualsAndHashCode(callSuper = true)
public class UseWaterBasePlan extends Model<UseWaterBasePlan> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("当前年基础计划")
  @TableField(value = "cur_year_plan", exist = true)
  private Float curYearPlan;

  @ApiModelProperty(value = "计划年度")
  @TableField(value = "plan_year", exist = true)
  private Integer planYear;

  @ApiModelProperty(value = "第一季度计划")
  @TableField(value = "one_quarter", exist = true)
  private Float  oneQuarter;

  @ApiModelProperty(value = "第二季度计划")
  @TableField(value = "two_quarter", exist = true)
  private Float  twoQuarter;

  @ApiModelProperty(value = "第三季度计划")
  @TableField(value = "three_quarter", exist = true)
  private Float  threeQuarter;

  @ApiModelProperty(value = "第四季度计划")
  @TableField(value = "four_quarter", exist = true)
  private Float  fourQuarter;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "备注")
  @TableField(value = "remarks", exist = true)
  private String remarks;

  @ApiModelProperty("单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty(value = "单位名称")
  @TableField(value = "unit_name", exist = true)
  private String unitName;

  @ApiModelProperty(value = "用水性质")
  @TableField(value = "nature", exist = true)
  private String nature;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;


  @Override
  protected Serializable pkVal() {
    return null;
  }
}