package com.zjtc.mapper.waterSys;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.SmsSendInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信发送
 * @author lianghao
 * @date 2020/12/01
 */
@Mapper
public interface SmsSendMapper extends BaseMapper<SmsSendInfo> {


}