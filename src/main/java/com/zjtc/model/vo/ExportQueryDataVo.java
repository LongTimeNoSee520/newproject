package com.zjtc.model.vo;

import com.zjtc.model.Bank;
import com.zjtc.model.Contacts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author yuyantian
 * @date 2021/2/1
 * @description
 */
@ApiModel("用水单位导出数据Vo")
@Data
public class ExportQueryDataVo {
  @ApiModelProperty(value = "单位编码，必填")
  private String unitCode;

  @ApiModelProperty(value = "单位名称，必填")
  private String unitName;

  @ApiModelProperty(value = "单位地址")
  private String unitAddress;

  @ApiModelProperty(value = "所属区域名称")
  private String areaCountryName;

  @ApiModelProperty(value = "水表档案号")
  private String waterMeterCode;

  @ApiModelProperty(value = "责任书编号")
  private String responsibilityCode;

  @ApiModelProperty(value = "联系人1")
  private String  contacts1;

  @ApiModelProperty(value = "手机号1")
  private String  mobileNumber1;

  @ApiModelProperty(value = "座机号1")
  private String  phoneNumber1;

  @ApiModelProperty(value = "联系人2")
  private String  contacts2;

  @ApiModelProperty(value = "手机号2")
  private String  mobileNumber2;

  @ApiModelProperty(value = "座机号2")
  private String  phoneNumber2;

  @ApiModelProperty(value = "是否是节水型单位,0:否，1是")
    private String saveUnitType;

  @ApiModelProperty(value = "是否签约")
  private String signed;

  @ApiModelProperty(value = "开户行名称")
  private String bankOfDeposit;

  @ApiModelProperty(value = "银行帐号")
  private String bankAccount;

  @ApiModelProperty(value = "邮寄名称")
  private String zipName;

  @ApiModelProperty(value = "邮寄地址")
  private String zipAddress;

  @ApiModelProperty(value = "开票单位")
  private String invoiceUnitName;

  @ApiModelProperty(value = "类型,取unit_code 5-6 位")
  private String unitCodeType;

  @ApiModelProperty(value = "部门")
  private String department;

  @ApiModelProperty(value = "所属行业名称")
  private String industryName;

  @ApiModelProperty(value = "开户行号/支付号")
  private String peopleBankPaySysNumber;

  @ApiModelProperty(value = "协议号")
  private String agreementNumber;

  @ApiModelProperty(value = "托收单位名称")
  private String entrustUnitName;

  @ApiModelProperty(value = "集中户备注")
  private String focusUserRemark;

  @ApiModelProperty(value = "是否他行(银行代码)，排除银行代码为1，都是他行")
  private String otherBank;

  @ApiModelProperty(value = "相关编号")
  private String useWaterUnitIdRef;

  @ApiModelProperty(value = "异常原因")
  private String abnormalCause;

  @ApiModelProperty(value = "银行信息")
  private List<Bank> bankList;

  @ApiModelProperty(value = "联系人信息")
  private List<Contacts> contactsList;



}
