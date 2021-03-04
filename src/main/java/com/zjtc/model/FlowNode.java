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
 * @date 2021/01/19
 */
@ApiModel(value = "流程节点表", description = "流程节点表")
@TableName("t_flow_node")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FlowNode extends Model<FlowNode> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("流程表id")
  @TableField(value = "flow_id", exist = true)
  private String flowId;

  @ApiModelProperty("流程节点名称")
  @TableField(value = "flow_node_name", exist = true)
  private String flowNodeName;

   @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty("备注")
  @TableField(value = "remark", exist = true)
  private String remark;

  @ApiModelProperty("流程角色id")
  @TableField(value = "flow_node_role_id", exist = true)
  private String flowNodeRoleId;

  @ApiModelProperty("流程角色姓名")
  @TableField(value = "flow_node_role_name", exist = true)
  private String flowNodeRoleName;

  @ApiModelProperty("流程节点编码")
  @TableField(value = "flow_node_code", exist = true)
  private String flowNodeCode;

  @ApiModelProperty("流程排序")
  @TableField(value = "flow_sort", exist = true)
  private int flowSort;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
