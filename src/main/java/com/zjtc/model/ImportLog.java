package com.zjtc.model;

import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

@ApiModel("导入日志")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_w_import_log")
public class ImportLog extends Model<ImportLog>{

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;

    @ApiModelProperty("导入文件名称")
    @TableField(value = "import_file_name",exist = true)
    private String importFileName;

    @ApiModelProperty("导入时间")
    @TableField(value = "import_time",exist = true)
    private Date importTime;

    @ApiModelProperty("导入状态")
    @TableField(value = "import_status",exist = true)
    private String importStatus;

    @ApiModelProperty("导入详情")
    @TableField(value = "import_detail",exist = true)
    private String importDetail;

    @ApiModelProperty("相关附件")
    @TableField(value = "files",exist = false)
    private List<Map<String,Object>> files;

    @ApiModelProperty("预览路径")
    private String preViewRealPath;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
