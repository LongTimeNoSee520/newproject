package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("审核相关附件VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class FileVO extends Model<FileVO>{

    @ApiModelProperty("附件表id")
    private String id;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("附件路径")
    private String filePath;

    @ApiModelProperty("是否删除")
    private String deleted;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
