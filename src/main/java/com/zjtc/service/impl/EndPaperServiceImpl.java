package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.EndPaperMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.User;
import com.zjtc.model.vo.EndPaperVO;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.UseWaterPlanAddService;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
 */
@Service
public class EndPaperServiceImpl extends ServiceImpl<EndPaperMapper, EndPaper> implements
    EndPaperService {

  @Autowired
  private PlanDailyAdjustmentService planDailyAdjustmentService;

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();//节点编码
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String unitName = jsonObject.getString("unitName");//单位名称
    String executed = jsonObject.getString("executed");//是否已执行
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Date applyTimeStart = jsonObject.getDate("applyTimeStart");//申请时间起始
    Date applyTimeEnd = jsonObject.getDate("applyTimeEnd");//申请时间截止

    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
    map.put("nodeCode", nodeCode);
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(executed)) {
      map.put("executed", executed);
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
    List<EndPaperVO> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  public void cancelSettlement(List<String> ids) {
    /**根据id查询*/
    List<EndPaper> endPapers = this.selectBatchIds(ids);
    for (EndPaper endPaper : endPapers) {
      /**根据单位编号更新是否存在办结单状态为否*/
      planDailyAdjustmentService
          .updateExistSettlement("0", endPaper.getUnitCode(), endPaper.getNodeCode(),
              endPaper.getPlanYear());
    }
    /**删除表中数据*/
    this.deleteBatchIds(ids);
  }

  @Override
  public void examineSettlement(User user, JSONObject jsonObject) {
    String id = jsonObject.getString("id");
//    Double firstQuarter =jsonObject.getDouble("firstQuarter");
//    Double secondQuarter = jsonObject.getDouble("secondQuarter");
//    Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
//    Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
    Double firstWater = jsonObject.getDouble("firstWater");
    Double secondWater = jsonObject.getDouble("secondWater");
    String addWay = jsonObject.getString("addWay");//加计划的方式：1-平均，2-最高
    Integer quarter = jsonObject.getInteger("quarter");
    Boolean year = jsonObject.getBoolean("year"); //是否在年计划上增加
    Double addNumber = jsonObject.getDouble("addNumber");
    String opinions = jsonObject.getString("opinions");//意见

    Map<String, Object> map = new HashMap<>();
    if (null != year && year) {
      /**选择了年计划*/
      map.put("algorithmRules", "1");
    }
    map.put("id", id);
    map.put("firstWater", firstWater);
    map.put("secondWater", secondWater);
    map.put("addWay", addWay);
    map.put("quarter", quarter.toString());
    map.put("addNumber", addNumber);
    /**更新数据*/
    this.baseMapper.update(map);

    /**TODO 审核流程信息新增*/

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse executeSettlement(User user, JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String id = jsonObject.getString("id");
    Double firstQuarter = (null ==jsonObject.getDouble("firstQuarter")? 0:jsonObject.getDouble("firstQuarter"));
    Double secondQuarter = (null == jsonObject.getDouble("secondQuarter")? 0:jsonObject.getDouble("secondQuarter"));
    Double thirdQuarter = (null == jsonObject.getDouble("thirdQuarter")? 0:jsonObject.getDouble("thirdQuarter")) ;
    Double fourthQuarter = (null ==jsonObject.getDouble("fourthQuarter")? 0:jsonObject.getDouble("fourthQuarter"));
    Double curYearPlan = jsonObject.getDouble("curYearPlan");

    if (curYearPlan != firstQuarter + secondQuarter + thirdQuarter + fourthQuarter) {
      response.recordError("4个季度水量和不等于年计划");
      return response;
    }
    /**查询办结单信息*/
    EndPaper endPaper = this.selectById(id);
    /**查询计划表信息*/
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", endPaper.getNodeCode());
    wrapper.eq("unit_code", endPaper.getUnitCode());
    wrapper.eq("plan_year", endPaper.getPlanYear());
    UseWaterPlan useWaterPlan = planDailyAdjustmentService.selectOne(wrapper);
    /**计划调整表信息*/
    UseWaterPlanAdd useWaterPlanAdd = new UseWaterPlanAdd();
    useWaterPlanAdd.setCreateTime(new Date());
    useWaterPlanAdd.setCreaterId(user.getId());
    useWaterPlanAdd.setCreater(user.getUsername());
    useWaterPlanAdd.setPlanYear(endPaper.getPlanYear());
    useWaterPlanAdd.setNodeCode(endPaper.getNodeCode());
    useWaterPlanAdd.setUnitCode(endPaper.getUnitCode());
    useWaterPlanAdd.setUnitName(endPaper.getUnitName());
    useWaterPlanAdd.setUseWaterUnitId(endPaper.getUseWaterUnitId());
    useWaterPlanAdd.setWaterMeterCode(endPaper.getWaterMeterCode());
    useWaterPlanAdd.setPrinted("0");
    useWaterPlanAdd.setStatus("3");//已审核，已累加
    useWaterPlanAdd.setPlanType(endPaper.getPaperType());

    if ("1".equals(endPaper.getPaperType())) {//增加计划
      useWaterPlan.setCurYearPlan(useWaterPlan.getCurYearPlan() + curYearPlan);
      useWaterPlan.setFirstQuarter(useWaterPlan.getFirstQuarter() + firstQuarter);
      useWaterPlan.setSecondQuarter(useWaterPlan.getSecondQuarter() + secondQuarter);
      useWaterPlan.setThirdQuarter(useWaterPlan.getThirdQuarter() + thirdQuarter);
      useWaterPlan.setFourthQuarter(useWaterPlan.getFourthQuarter() + fourthQuarter);
      /**调整表“增加计划”的年计划、4个季度数据*/
      useWaterPlanAdd.setFirstQuarter(firstQuarter);
      useWaterPlanAdd.setSecondQuarter(secondQuarter);
      useWaterPlanAdd.setThirdQuarter(thirdQuarter);
      useWaterPlanAdd.setFourthQuarter(fourthQuarter);
      useWaterPlanAdd.setCurYearPlan(curYearPlan);
      /**调整结果*/
      endPaper.setResult(
          "增加计划" + endPaper.getAddNumber() + "m3(" + endPaper.getChangeQuarter() + "季度)");
    } else {//调整计划、自来水、专供水等(4个季度间调整)
      /**调整表“计划调整/自来水/专供水/..”的年计划、4个季度数据(相对于计划表数据的改变量)*/
      useWaterPlanAdd.setCurYearPlan(0d);//double
      useWaterPlanAdd.setFirstQuarter(firstQuarter - useWaterPlan.getFirstQuarter());
      useWaterPlanAdd.setSecondQuarter(secondQuarter - useWaterPlan.getSecondQuarter());
      useWaterPlanAdd.setThirdQuarter(thirdQuarter - useWaterPlan.getThirdQuarter());
      useWaterPlanAdd.setFourthQuarter(fourthQuarter - useWaterPlan.getFourthQuarter());
      /**修改后计划表4个季度水量*/
      useWaterPlan.setFirstQuarter(firstQuarter);
      useWaterPlan.setSecondQuarter(secondQuarter);
      useWaterPlan.setThirdQuarter(thirdQuarter);
      useWaterPlan.setFourthQuarter(fourthQuarter);
      /**调整结果*/
      endPaper.setResult(
          "各季度依次变化:" + useWaterPlanAdd.getFirstQuarter().toString() + ";" + useWaterPlanAdd.getSecondQuarter().toString()
              + ";" + useWaterPlanAdd.getThirdQuarter().toString() + ";" + useWaterPlanAdd.getFourthQuarter().toString()
              + "。");
    }
    /**更新计划表数据*/
    useWaterPlan.setUpdateUserId(user.getId());
    useWaterPlan.setUpdateTime(new Date());
    useWaterPlan.setExistSettlementForm("0");//执行办结单后将是否存在未完成的办结单状态设置为否
    planDailyAdjustmentService.updateById(useWaterPlan);
    /**调整表新增*/
    useWaterPlanAddService.insert(useWaterPlanAdd);
    /**更新办结单信息*/
    endPaper.setExecuted("1");//已执行
    endPaper.setExecutorId(user.getId());
    endPaper.setExecutor(user.getUsername());
    endPaper.setExecuteTime(new Date());
    endPaper.setFirstQuarter(firstQuarter);
    endPaper.setSecondQuarter(secondQuarter);
    endPaper.setThirdQuarter(thirdQuarter);
    endPaper.setFourthQuarter(fourthQuarter);
    this.updateById(endPaper);
    return response;
  }
}