package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.SmsSendInfo;
import com.zjtc.model.vo.SendListVO;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2020/12/01
 */

public interface SmsSendService extends IService<SmsSendInfo> {

  /**
   * 分页查询所有 同一单位、统一手机号，最新一条数据
   */
  List<SendListVO> queryAll(List<SendListVO> sendListVOS, JSONObject jsonObject);

  /**
   * 查询数据条数
   * @param sendListVOS
   * @param jsonObject
   * @return
   */
  long count(List<SendListVO> sendListVOS, JSONObject jsonObject);
  /**
   * 查询自平计划通知满足条件的数据条数
   */
  int sendInfoNum(List<SendListVO> list, JSONObject json);
  /**
   * 查询自平计划通知满足条件的数据
   */
  List<Map<String,Object>> sendInfoPage(List<SendListVO> list, JSONObject json);
  /**
   * 查询调整结果通知满足条件的数据条数
   */
  int sendResultNum(List<SendListVO> list, JSONObject json);
  /**
   * 查查询调整结果通知满足条件的数据
   */
  List<Map<String,Object>> sendResultPage(List<SendListVO> list, JSONObject json);
}
