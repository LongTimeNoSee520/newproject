package com.zjtc.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/5/20
 */
@ApiModel("通讯录")
@Data
public class AddressBook {

  @ApiModelProperty(value = "主键", allowableValues = "32", dataType = "String", required = false)
  @TableField(value = "id", exist = false)
  private String id;

  @ApiModelProperty(value = "节点编码", allowableValues = "16", dataType = "String")
  private String nodeCode;

  @ApiModelProperty(value = "父节点id", allowableValues = "32", dataType = "String")
  private String parentId;

  @ApiModelProperty(value = "部门名称", allowableValues = "64", dataType = "String")
  private String orgName;

  @ApiModelProperty(value = "部门和人员的标识", allowableValues = "64", dataType = "String")
  private String tag;

  @ApiModelProperty(value = "收信人名称", allowableValues = "64", dataType = "String")
  private String userName;

  @ApiModelProperty(value = "收信人手机号", allowableValues = "64", dataType = "String")
  private String mobilePhone;

  @ApiModelProperty(value = "是否是子节点(0:否,1:是)", allowableValues = "64", dataType = "String")
  private String son;

  @ApiModelProperty(value="办公地址")
  private String address;

  @ApiModelProperty(value="创建时间")
  private Date createTime;

  @ApiModelProperty(value="备注")
  private String remark;

  @ApiModelProperty(value="部门编码")
  @TableField(value = "unitCode")
  private String unitCode;
}
