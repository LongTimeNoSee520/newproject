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
 * @author lianghao
 * @date 2021/01/19
 */
@ApiModel(value = "流程节点线记录表", description = "流程节点线记录表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_flow_node_line_info")
public class FlowNodeLineInfo extends Model<FlowNodeLineInfo>{

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("流程节点表id")
  @TableField(value = "flow_node_id", exist = true)
  private String flowNodeId;

  @ApiModelProperty("操作类型(通过、不通过)")
  @TableField(value = "operate_type", exist = true)
  private String operateType;

  @ApiModelProperty("下一环节流程节点id")
  @TableField(value = "next_node_id", exist = true)
  private String nextNodeId;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty("排序")
  @TableField(value = "sort", exist = true)
  private Integer sort;
	/**
	 * 实例化
	 */
	public FlowNodeLineInfo() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
