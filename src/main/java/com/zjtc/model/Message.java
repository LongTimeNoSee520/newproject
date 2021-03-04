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
@ApiModel(value = "消息表", description = "消息表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_message")
public class Message extends Model<Message> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("消息通知人员id")
  @TableField(value = "msg_person_id", exist = true)
  private String msgPersonId;

  @ApiModelProperty("消息通知人员")
  @TableField(value = "msg_person_name", exist = true)
  private String msgPersonName;

  @ApiModelProperty("消息类型")
  @TableField(value = "msg_type", exist = true)
  private String msgType;

  @ApiModelProperty("消息标题")
  @TableField(value = "msg_title", exist = true)
  private String msgTitle;

  @ApiModelProperty("消息内容")
  @TableField(value = "msg_content", exist = true)
  private String msgContent;

  @ApiModelProperty("消息状态")
  @TableField(value = "msg_status", exist = true)
  private String msgStatus;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty("操作时间")
  @TableField(value = "operation_time", exist = true)
  private Date operationTime;

  @ApiModelProperty("单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  /**
   * 实例化
   */
  public Message() {
    super();
  }

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
