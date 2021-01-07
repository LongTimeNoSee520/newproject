package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterPlanAddMapper;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.service.UseWaterPlanAddService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode,String loginId) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);
//    页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");
//    单位名称
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName");
    }
//    用户类型(截取的是3-4位)
    String userType = "";
    if (null != jsonObject.getString("userType")) {
      userType = jsonObject.getString("userType");
    }
    //    是否审核(0:未审核,1:已审核)
    String auditStatus = "";
    if (null != jsonObject.getString("auditStatus")) {
      auditStatus = jsonObject.getString("auditStatus");
    }
    //    是否执行(0:未执行,1:已执行)
    String executed = "";
    if (null != jsonObject.getString("executed")) {
      executed = jsonObject.getString("executed");
    }
//    总条数
    Integer total = this.baseMapper
        .selectCount(unitName, userType, executed, nodeCode, auditStatus,loginId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<UseWaterPlanAdd> useWaterPlanAdds = this.baseMapper
        .UseWaterPlanAdd(currPage, pageSize, unitName, userType,
            executed, nodeCode, auditStatus,loginId);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", useWaterPlanAdds);
    response.setCode(200);
    response.setData(map);
    return response;
  }
}
