package com.zjtc.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("部门树VO")
@Data
public class OrgTreeVO extends Model<OrgTreeVO> {

  @ApiModelProperty(value = "主键", allowableValues = "32", dataType = "String", required = false)
  @TableField(value = "id", exist = false)
  private String id;

  @ApiModelProperty(value = "节点编码", allowableValues = "16", dataType = "String")
  private String nodeCode;

  @ApiModelProperty(value = "父节点id", allowableValues = "32", dataType = "String")
  private String parentId;

  @ApiModelProperty(value = "部门排序", dataType = "int")
  private int orgRank;

  @ApiModelProperty(value = "部门名称", allowableValues = "64", dataType = "String")
  private String orgName;

  @ApiModelProperty(value = "联系电话", allowableValues = "64", dataType = "String")
  private String dutyTel;

  @ApiModelProperty(value = "部门和人员的标识", allowableValues = "64", dataType = "String")
  private String tag;

  @ApiModelProperty(value = "所属部门", allowableValues = "64", dataType = "String")
  private String personOrg;

  @ApiModelProperty(value = "职位", allowableValues = "64", dataType = "String")
  private String position;

  @ApiModelProperty(value = "收信人名称", allowableValues = "64", dataType = "String")
  private String userName;

  @ApiModelProperty(value = "收信人手机号", allowableValues = "64", dataType = "String")
  private String mobilePhone;

  @ApiModelProperty(value = "父级名称", allowableValues = "64", dataType = "String")
  private String parentName;

  @ApiModelProperty(value = "是否是子节点(0:否,1:是)", allowableValues = "64", dataType = "String")
  private String son;

  @ApiModelProperty(value="办公地址")
  private String address;

  @ApiModelProperty(value="办公电话")
  private String officeTel;

  @ApiModelProperty(value="传真")
  private String fax;

  @ApiModelProperty(value="联系人")
  private  String contact;

  @ApiModelProperty(value="创建时间")
  private Date createTime;

  @ApiModelProperty(value="修改时间")
  private Date modifiedTime;

  @ApiModelProperty(value="备注")
  private String remark;

  @ApiModelProperty(value="部门编码")
  @TableField(value = "unitCode")
  private String unitCode;


  @ApiModelProperty(value = "节水业务-加价费管理-开票人id")
  @TableField(value = "drawerId", exist = false)
  private String drawerId;

  @ApiModelProperty(value = "节水业务-加价费管理-开票人名称")
  @TableField(value = "drawer", exist = false)
  private String drawer;

  @ApiModelProperty(value="子集")
  private List<OrgTreeVO> orgTreeVOS;



  @Override
  protected Serializable pkVal() {
    return null;
  }
}
