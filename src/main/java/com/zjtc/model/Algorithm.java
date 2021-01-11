package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用水计划编制算法表
 *
 * @author yuchen
 */
@ApiModel("用水计划编制算法")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_algorithm")
public class Algorithm extends Model<Algorithm> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("算法类型")
  @TableField(value = "algorithm_type", exist = true)
  private String algorithmType;

  @ApiModelProperty("第一批次号")
  @TableField(value = "first_batch_num", exist = true)
  private String firstBatchNum;

  @ApiModelProperty("第二批次号")
  @TableField(value = "second_batch_num", exist = true)
  private String secondBatchNum;

  @ApiModelProperty("第三批次号")
  @TableField(value = "third_batch_num", exist = true)
  private String thirdBatchNum;

  @ApiModelProperty("新户编码")
  @TableField(value = "new_batch_num", exist = true)
  private String newBatchNum;

  @ApiModelProperty("n8下届")
  @TableField(value = "n8_floot", exist = true)
  private Double n8Floot;

  @ApiModelProperty("n8上届")
  @TableField(value = "n8_up", exist = true)
  private Double n8Up;

  @ApiModelProperty("水价上届")
  @TableField(value = "price_top", exist = true)
  private Double priceTop;

  @ApiModelProperty("水价下届")
  @TableField(value = "price_bottom", exist = true)
  private Double priceBottom;

  @ApiModelProperty("第一年水量（3年倒排，实际是前第3年用水量比重）")
  @TableField(value = "third_year_pro", exist = true)
  private Double thirdYearPro;

  @ApiModelProperty("中间年比重（前第2年用水量比重）")
  @TableField(value = "second_year_pro", exist = true)
  private Double secondYearPro;

  @ApiModelProperty("最近一年比重（用水量比重）")
  @TableField(value = "first_year_pro", exist = true)
  private Double firstYearPro;

  @ApiModelProperty("基础（用水量）比例")
  @TableField(value = "base_pro", exist = true)
  private Double basePro;

  @ApiModelProperty("三年平均（用水量）比例1")
  @TableField(value = "three_avg_pro_1", exist = true)
  private Double threeAvgPro1;

  @ApiModelProperty("三年平均（用水量）比例2")
  @TableField(value = "three_avg_pro_2", exist = true)
  private Double threeAvgPro2;

  @ApiModelProperty("三年平均（用水量）比例3")
  @TableField(value = "three_avg_pro_3", exist = true)
  private Double threeAvgPro3;

  @ApiModelProperty("第一季度（用水量）比例")
  @TableField(value = "first_quarter_pro", exist = true)
  private Double firstQuarterPro;

  @ApiModelProperty("第二季度（用水量）比例")
  @TableField(value = "second_quarter_pro", exist = true)
  private Double secondQuarterPro;

  @ApiModelProperty("第三季度（用水量）比例")
  @TableField(value = "third_quarter_pro", exist = true)
  private Double thirdQuarterPro;

  @ApiModelProperty("第四季度（用水量）比例")
  @TableField(value = "fourth_quarter_pro", exist = true)
  private Double fourthQuarterPro;

  @ApiModelProperty("减扣比例")
  @TableField(value = "reduce_pro", exist = true)
  private Double reducePro;

  @ApiModelProperty("奖励比例")
  @TableField(value = "reward_pro", exist = true)
  private Double rewardPro;

  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true, fill = FieldFill.INSERT)
  private Date createTime;

  @ApiModelProperty("修改时间")
  @TableField(value = "modify_time", exist = true)
  private Date modifyTime;

  /**
   * 实例化
   */
  public Algorithm() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
