package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description 数据字典
 */
@Data
@TableName(value = "t_dict_item")
@Accessors(chain = true)
@ApiModel(value = "数据字典项", description = "数据字典项")
@EqualsAndHashCode(callSuper = true)
public class DictItem extends Model<DictItem> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;
  @ApiModelProperty("字典id")
  @TableField(value = "dict_id", exist = true)
  private String dictId;

  @ApiModelProperty(value = "数据项名称")
  @TableField(value = "dict_item_name", exist = true)
  private String dictItemName;

  @ApiModelProperty(value = "数据项编码")
  @TableField(value = "dict_item_code", exist = true)
  private String dictItemCode;

  @ApiModelProperty(value = "数据项描述")
  @TableField(value = "dict_item_desc", exist = true)
  private String dictItemDesc;

  @ApiModelProperty(value = "排序")
  @TableField(value = "dict_item_rank", exist = true)
  private int dictItemRank;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
