package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.model.vo.WaterUsePayInfoVo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * WaterUsePayInfo的Dao接口
 *
 * @author
 */
@Mapper
public interface WaterUsePayInfoMapper extends BaseMapper<WaterUsePayInfo> {

  /**
   * 分页
   */
  List<WaterUsePayInfoVo> queryPage(JSONObject jsonObject);

  /**
   * 数据总条数
   */
  Map<String, Object> queryListTotal(JSONObject jsonObject);

  /**
   * 根据条件删除之前的加价数据
   */
  boolean deleteByParam(JSONObject jsonObject);

  /**
   * 初始化满足条件的缴费数据
   */
  List<WaterUsePayInfo> initPayInfo(JSONObject jsonObject);

  /**
   * @param id 缴费记录id
   * @param money 退减免金额
   */
  boolean updateMoney(@Param("id") String id, @Param("money") double money);

  boolean updateActualAmount(@Param("id") String id, @Param("actualAmount") double actualAmount);

  List<Map<String, Object>> findPayBefor(@Param("unitId") String unitId);

  List<Map<String, Object>> ThreePayMess(@Param("year") Integer year,
      @Param("unitId") String unitId);

  List<SendListVO> selectPayNotice(JSONObject jsonObject);

  long selectPayNoticeCount(JSONObject jsonObject);

  List<Map<String, Object>> selectUser(JSONObject jsonObject);

  List<Map<String, Object>> exportPayInfo(JSONObject jsonObject);

  List<Map<String, Object>> exportBankInfo(JSONObject jsonObject);

  List<Map<String, Object>> exportOtherBankInfo(JSONObject jsonObject);

  List<Map<String, Object>> exportQueryData(JSONObject jsonObject);



}