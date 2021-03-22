package com.zjtc.mapper.waterSys;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.SmsSendInfo;
import com.zjtc.model.vo.SendListVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短信发送
 *
 * @author lianghao
 * @date 2020/12/01
 */
@Mapper
public interface SmsSendMapper extends BaseMapper<SmsSendInfo> {

  List<SendListVO> queryAll(@Param("list")List<SendListVO> sendListVOS,@Param("json") JSONObject jsonObject);

  long count(@Param("list")List<SendListVO> sendListVOS,@Param("json") JSONObject jsonObject);

  int sendInfoNum(@Param("list")List<SendListVO> list,@Param("json") JSONObject json);

  List<Map<String,Object>> sendInfoPage(@Param("list")List<SendListVO> list, @Param("json")JSONObject json);

  int sendResultNum(@Param("list")List<SendListVO> list, @Param("json")JSONObject json);

  List<Map<String,Object>> sendResultPage(@Param("list")List<SendListVO> list, @Param("json")JSONObject json);
}