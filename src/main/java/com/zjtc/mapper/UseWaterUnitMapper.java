package com.zjtc.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.model.vo.UseWaterUnitVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import springfox.documentation.spring.web.json.Json;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Mapper
public interface UseWaterUnitMapper extends BaseMapper<UseWaterUnit> {

  /**
   * 查询当前节点编码、当前批次,节点编码后三位最大值
   */
  String maxUnitCode(@Param("unitCode") String unitCode,
      @Param("id") String id, @Param("nodeCode") String nodeCode);

  /**
   * 分页
   */
  List<Map<String, Object>> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   */
  long queryListTotal(JSONObject jsonObject);

  /**
   * 详情查询
   * @param jsonObject 单位id
   * @return
   */
  UseWaterUnitVo selectById(JSONObject jsonObject);

  UseWaterUnitRefVo queryUnitRef(@Param("ids") List<String > ids, @Param("nodeCode") String nodeCode,@Param("userId")String UserId);

}
