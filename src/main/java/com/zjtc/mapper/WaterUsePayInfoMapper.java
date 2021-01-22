package com.zjtc.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterUsePayInfo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.OBJ_ADAPTER;
import springfox.documentation.spring.web.json.Json;

/**
 * WaterUsePayInfo的Dao接口
 * 
 * @author 
 *
 */
@Mapper
public interface WaterUsePayInfoMapper extends BaseMapper<WaterUsePayInfo> {

  /**
   * 分页
   * @param jsonObject
   * @return
   */
  List<Map<String ,Object>> queryPage(JSONObject jsonObject);

  /**
   * 数据总条数
   * @param jsonObject
   * @return
   */
  Map<String,Object> queryListTotal(JSONObject jsonObject);

  /**
   * 根据条件删除之前的加价数据
   * @param jsonObject
   * @return
   */
  boolean deleteByParam(JSONObject jsonObject);

  /**
   * 初始化满足条件的缴费数据
   * @param jsonObject
   * @return
   */
   List<WaterUsePayInfo> initPayInfo(JSONObject jsonObject);

  /**
   *
   * @param id 缴费记录id
   * @param money 退减免金额
   * @return
   */
   boolean updateMoney(String id ,double money);

  List<Map<String, Object>> findPayBefor(@Param("unitId") String unitId);

  List<Map<String, Object>> ThreePayMess(@Param("year") Integer year,@Param("unitId") String unitId);
}