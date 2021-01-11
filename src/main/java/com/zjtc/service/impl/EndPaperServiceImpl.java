package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.EndPaperMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.User;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.PlanDailyAdjustmentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 *
 */
@Service
public class EndPaperServiceImpl extends ServiceImpl<EndPaperMapper, EndPaper> implements
    EndPaperService {

  @Autowired
  private PlanDailyAdjustmentService planDailyAdjustmentService;

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();//节点编码
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String unitName = jsonObject.getString("unitName");//单位名称
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Date applyTimeStart = jsonObject.getDate("applyTimeStart");//申请时间起始
    Date applyTimeEnd = jsonObject.getDate("applyTimeEnd");//申请时间截止

    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
    map.put("nodeCode", nodeCode);
    map.put("userId",userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(waterMeterCode)) {
      map.put("waterMeterCode", waterMeterCode);
    }
    if (null != applyTimeStart) {
      map.put("applyTimeStart", applyTimeStart);
    }
    if (null != applyTimeEnd) {
      map.put("applyTimeEnd", applyTimeEnd);
    }

    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    /**查出满足条件的数据*/
    List<Map<String, Object>> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  public void cancelSettlement(List<String> ids) {
    /**根据id查询*/
    List<EndPaper> endPapers = this.selectBatchIds(ids);
   for (EndPaper endPaper:endPapers){
     /**根据单位编号更新是否存在办结单状态为否*/
     planDailyAdjustmentService.updateExistSettlement("0",endPaper.getUnitCode(),endPaper.getNodeCode(),endPaper.getPlanYear());
   }
    /**删除表中数据*/
    this.deleteBatchIds(ids);
  }
}