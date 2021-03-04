package com.zjtc.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * @author lianghao
 * @date 2021/01/19
 */
@ApiModel(value = "流程实例表", description = "流程实例表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_flow_example")
public class FlowExample extends Model<FlowExample> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("流程实例标题(用户名+流程名称)")
  @TableField(value = "example_titile", exist = true)
  private String exampleTitile;

  @ApiModelProperty("业务id")
  @TableField(value = "business_id", exist = true)
  private String businessId;

  @ApiModelProperty("创建者")
  @TableField(value = "creator", exist = true)
  private String creator;

  @ApiModelProperty("创建者id")
  @TableField(value = "creator_id", exist = true)
  private String creatorId;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty("流转状态")
  @TableField(value = "flow_status", exist = true)
  private String flowStatus;

//  @ApiModelProperty("流程id")
//  @TableField(value = "flow_id", exist = true)
//  private String flowId;

  /**
   * 实例化
   */
  public FlowExample() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
