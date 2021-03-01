package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.zjtc.model.WaterBalanceTest;
import com.zjtc.model.WaterBalanceTestProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lianghao
 * @date 2021/02/25
 */
@ApiModel("水平衡测试VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class WaterBalanceTestVO extends Model<WaterBalanceTestVO>{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("节点编码")
    private String nodeCode;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位地址")
    private String unitAddress;

    @ApiModelProperty("所属行业名称")
    private String industryName;

    @ApiModelProperty("所属行业编码")
    private String industryCode;

    @ApiModelProperty("单位占地面积")
    private Double coverSpace;

    @ApiModelProperty("单位建筑面积")
    private Double floorSpace;

    @ApiModelProperty("单位用水人数")
    private Integer usePeopleNum;

    @ApiModelProperty("单位年用水量")
    private Double yearAmount;

    @ApiModelProperty("上次测试时间")
    private Date lastTestTime;

    @ApiModelProperty("合格证编号")
    private String certificateNo;

    @ApiModelProperty("管网漏损率")
    private Double leakageRate;

    @ApiModelProperty("逻辑删除，1是0否")
    private String deleted;

    @ApiModelProperty("删除时间")
    private Date deleteTime;

    @ApiModelProperty("创建人id")
    private String createPersonId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("主要产品用水单耗情况列表")
    private List<WaterBalanceTestProduct> products;

    @ApiModelProperty("附件列表")
    private List<FileVO> fileList;


    @Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
