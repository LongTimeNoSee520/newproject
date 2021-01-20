package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.FlowNodeLine;
import com.zjtc.model.FlowNodeLineInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface FlowNodeLineMapper extends BaseMapper<FlowNodeLine> {

  List<FlowNodeLineInfo> selectLineInfo(@Param("flowCode") String flowCode, @Param("nodeCode") String nodeCode);
}