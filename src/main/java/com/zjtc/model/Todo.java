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
@ApiModel(value = "待办表", description = "待办表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_todo")
public class Todo extends Model<Todo> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("执行人员id")
  @TableField(value = "execute_person_id", exist = true)
  private String executePersonId;

  @ApiModelProperty("执行人员姓名")
  @TableField(value = "execute_person_name", exist = true)
  private String executePersonName;

  @ApiModelProperty("代办标题")
  @TableField(value = "todo_title", exist = true)
  private String todoTitle;

  @ApiModelProperty("代办内容")
  @TableField(value = "todo_content", exist = true)
  private String todoContent;

  @ApiModelProperty("待办类型码1退减免单2办结单3短信4用水计划超额")
  @TableField(value = "todo_type", exist = true)
  private String todoType;

  @ApiModelProperty("关联业务json数据")
  @TableField(value = "business_json", exist = true)
  private String businessJson;

  @ApiModelProperty("业务表表名")
  @TableField(value = "table_name", exist = true)
  private String tableName;

  @ApiModelProperty("关联业务id")
  @TableField(value = "business_id", exist = true)
  private String businessId;

  @ApiModelProperty("待办发起人")
  @TableField(value = "registrant", exist = true)
  private String registrant;

  @ApiModelProperty("待办发起人id")
  @TableField(value = "registrant_id", exist = true)
  private String registrantId;

  @ApiModelProperty("详情配置文件")
  @TableField(value = "detail_config", exist = true)
  private String detailConfig;

  @ApiModelProperty("代办状态1已办0未办")
  @TableField(value = "status", exist = true)
  private String status;

  @ApiModelProperty("提交操作的nodeCode")
  @TableField(value = "submit_node_code", exist = true)
  private String submitNodeCode;

  @ApiModelProperty("提交操作的所属区县")
  @TableField(value = "submit_node_name", exist = true)
  private String submitNodeName;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("操作时间")
  @TableField(value = "operation_time", exist = true)
  private Date operationTime;

  /**
   * 实例化
   */
  public Todo() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
