package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterPlanAddMapper;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.service.UseWaterPlanAddService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@Service
public class UseWaterPlanAddServiceImpl extends
    ServiceImpl<UseWaterPlanAddMapper, UseWaterPlanAdd> implements
    UseWaterPlanAddService {

  @Override
  public void updateRemarks(String id, String remarks) {
    this.baseMapper.updateRemarks(id, remarks);
  }

  @Override
  public void updatePlanAdd(List<UseWaterPlanAdd> useWaterPlanAdds) {
    this.baseMapper.updatePlanAdd(useWaterPlanAdds);
  }


  @Override
  public boolean updateStatusOrPrinted(String id, String status, String printed) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    if (StringUtils.isNotBlank(status)) {
      map.put("status", status);
    }
    if (StringUtils.isNotBlank(printed)) {
      map.put("printed", printed);
    }
    if (StringUtils.isNotBlank(status) || StringUtils.isNotBlank(printed)) {
      this.baseMapper.updateStatusOrPrinted(map);
      return true;
    } else {
      return false;
    }
  }


}
