package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_w_water_saving_unit_base实体类
 *
 * @author
 */
@ApiModel("节水型单位基础管理考核标准表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_saving_unit_base")
public class WaterSavingUnitBase extends Model<WaterSavingUnitBase> {

  /**
   *
   */
  @ApiModelProperty("id")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  /**
   *
   */
  @ApiModelProperty("id")
  @TableField(value = "water_saving_unit_id",exist = true)
  private String waterSavingUnitId;

  /**
   *
   */
  @ApiModelProperty("节点编码")
  @TableField(value = "node_code",exist = true)
  private String nodeCode;
  /**
   *
   */
  @ApiModelProperty("基础管理考核内容")
  @TableField(value = "contents", exist = true)
  private String contents;
  /**
   *
   */
  @ApiModelProperty("考核方法")
  @TableField(value = "assess_method", exist = true)
  private String assessMethod;
  /**
   *
   */
  @ApiModelProperty("考核标准")
  @TableField(value = "assess_standard", exist = true)
  private String assessStandard;
  /**
   *
   */
  @ApiModelProperty("企业分数")
  @TableField(value = "company_score", exist = true)
  private Double companyScore;
  /**
   *
   */
  @ApiModelProperty("单位分数")
  @TableField(value = "unit_score", exist = true)
  private Double unitScore;
  /**
   *
   */
  @ApiModelProperty("自查分数")
  @TableField(value = "check_score", exist = true)
  private Double checkScore;
  /**
   *
   */
  @ApiModelProperty("实际得分")
  @TableField(value = "actual_score", exist = true)
  private Double actualScore;
  /**
   *
   */
  @ApiModelProperty("是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty("备注")
  @TableField(value = "remarks",exist = true)
  private String remarks;

  /**
   * 实例化
   */
  public WaterSavingUnitBase() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
