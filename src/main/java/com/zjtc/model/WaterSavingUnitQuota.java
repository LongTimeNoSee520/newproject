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
 * t_w_water_saving_unit_quota实体类
 *
 * @author
 */
@ApiModel("节水型单位定量考核指标表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_water_saving_unit_quota")
public class WaterSavingUnitQuota extends Model<WaterSavingUnitQuota> {

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
  @TableField(value = "water_saving_unit_id", exist = true)
  private String waterSavingUnitId;
  /**
   *
   */
  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;
  /**
   *
   */
  @ApiModelProperty("指标")
  @TableField(value = "quota_index", exist = true)
  private String quotaIndex;
  /**
   *
   */
  @ApiModelProperty("考核计算方法")
  @TableField(value = "assess_algorithm", exist = true)
  private String assessAlgorithm;
  /**
   *
   */
  @ApiModelProperty("考核标准")
  @TableField(value = "assess_standard", exist = true)
  private String assessStandard;
  /**
   *
   */
  @ApiModelProperty("标准水平")
  @TableField(value = "standard_level", exist = true)
  private String standardLevel;
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
  @TableField(value = "remarks", exist = true)
  private String remarks;

  /**
   * 实例化
   */
  public WaterSavingUnitQuota() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
