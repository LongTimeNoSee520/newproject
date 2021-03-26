package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.WaterSavingEfficiencyManage;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yuyantian
 * @date 2021/3/25
 * @description
 */
@Mapper
public interface WaterSavingEfficiencyManageMapper extends
    BaseMapper<WaterSavingEfficiencyManage> {

  long queryListTotal(JSONObject jsonObject);

  List<Map<String,Object>> queryPage(JSONObject jsonObject);

}
